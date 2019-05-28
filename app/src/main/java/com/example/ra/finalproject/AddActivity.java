package com.example.ra.finalproject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    TextView tvDate, tvWeight, tvWeightAdd, tvMark, tvMarkAdd;
    EditText etTitle;
    Spinner spSub;
    CheckBox cbApprovedAdd;
    SeekBar sbWeight, sbMark;
    Button btnConfirmAdd, btnCancelAdd;
    String topic, directory, subject;
    DatabaseReference dbRef, topicRef;
    int yearC, monthC, dayC;
    Calendar calendar;
    ArrayList<String> subjects;
    ArrayAdapter stringAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        topic = intent.getStringExtra("topic");

        etTitle = (EditText) findViewById(R.id.et_title);
        spSub = (Spinner) findViewById(R.id.sp_sub);
        tvDate = (TextView) findViewById(R.id.tv_date);
        cbApprovedAdd = (CheckBox) findViewById(R.id.cb_approved_add);
        tvWeight = (TextView) findViewById(R.id.tv_weight);
        sbWeight = (SeekBar) findViewById(R.id.sb_weight);
        tvWeightAdd = (TextView) findViewById(R.id.tv_weight_add);
        tvMark = (TextView) findViewById(R.id.tv_mark);
        sbMark = (SeekBar) findViewById(R.id.sb_mark);
        tvMarkAdd = (TextView) findViewById(R.id.tv_mark_add);
        btnConfirmAdd = (Button) findViewById(R.id.btn_confirm_add);
        btnCancelAdd = (Button) findViewById(R.id.btn_cancel_add);
        calendar = Calendar.getInstance();
        yearC = calendar.get(Calendar.YEAR);
        monthC = calendar.get(Calendar.MONTH) + 1;
        dayC = calendar.get(Calendar.DAY_OF_MONTH);
        subject = "";

        tvDate.setOnClickListener(this);
        btnConfirmAdd.setOnClickListener(this);
        btnCancelAdd.setOnClickListener(this);
        sbWeight.setOnSeekBarChangeListener(this);
        sbMark.setOnSeekBarChangeListener(this);

        hideAll();

        setMode(topic);
        dbRef = FirebaseDatabase.getInstance().getReference().child(directory).child(FirebaseManager.auth.getUid());

        getSubjects();
        if (subjects != null) {
            stringAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjects);
        }
    }

    public void getSubjects() {
        subjects = new ArrayList<String>();
        if (spSub.isActivated()) {
            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.arg1 == FirebaseManager.DONE_SUBJECTS) {
                        ArrayList<Subject> alSub = FirebaseManager.subjects;
                        for (Subject subject : alSub)
                            subjects.add(subject.getName());
                    }

                    spSub.setAdapter(stringAdapter);
                    spSub.setOnItemSelectedListener(AddActivity.this);
                    return true;
                }
            });
            FirebaseManager.getInstance(this).getSubjects(handler);

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subject = parent.getItemAtPosition(position).toString();
    }

    /**
     * Make a certain view not interactive
     * @param v The view that needs to be disabled
     */
    public void disableView(View v) {
        v.setActivated(false);
        v.setVisibility(View.INVISIBLE);
    }

    /**
     * Clear the screen for easier screen setup
     */
    public void hideAll() {
        disableView(etTitle);
        disableView(spSub);
        disableView(tvDate);
        disableView(cbApprovedAdd);
        disableView(tvWeight);
        disableView(sbWeight);
        disableView(tvWeightAdd);
        disableView(tvMark);
        disableView(sbMark);
        disableView(tvMarkAdd);
    }

    /**
     * Make a certain view interactive
     * @param v The view that needs to be enabled
     */
    public void enableView(View v) {
        v.setActivated(true);
        v.setVisibility(View.VISIBLE);
    }

    /**
     * Set up the screen in a way that shows only the relevant views for that topic
     * @param topic The topic that the screen needs to show
     */
    public void setMode(String topic) {
        switch (topic) {
            case "Absence":
                enableView(spSub);
                enableView(tvDate);
                enableView(cbApprovedAdd);
                directory = "absence";
                break;
            case "Exams":
                enableView(etTitle);
                enableView(spSub);
                enableView(tvDate);
                enableView(tvWeight);
                enableView(sbWeight);
                enableView(tvWeightAdd);
                directory = "exams";
                break;
            case "Grades":
                enableView(etTitle);
                enableView(spSub);
                enableView(tvDate);
                enableView(tvWeight);
                enableView(sbWeight);
                enableView(tvWeightAdd);
                enableView(tvMark);
                enableView(sbMark);
                enableView(tvMarkAdd);
                directory = "grades";
                break;
            case "Subjects":
                enableView(etTitle);
                etTitle.setHint("Subject");
                directory = "subjects";
                break;
        }
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yearC = year;
            monthC = month + 1;
            dayC = dayOfMonth;
            calendar.set(yearC, monthC - 1, dayC);
            tvDate.setText(dayC + "/" + monthC + "/" + yearC);
        }
    };

    @Override
    public void onClick(View v) {
        if (v == tvDate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, yearC, monthC - 1, dayC);
            datePickerDialog.show();
        } else if (v == btnConfirmAdd) {
            if (checkAll()) {
                ProgressDialog progressDialog = FirebaseManager.getInstance(AddActivity.this).buildProgressDialog();
                progressDialog.show();
                switch (topic) {
                    case "Absence":
                        Absence a = new Absence(subject, calendar.getTimeInMillis(), cbApprovedAdd.isChecked());
                        topicRef = dbRef.push();
                        a.setId(topicRef.getKey());
                        topicRef.setValue(a);
                        break;
                    case "Exams":
                        Exam e = new Exam(etTitle.getText().toString(), subject, calendar.getTimeInMillis(), sbWeight.getProgress());
                        topicRef = dbRef.push();
                        e.setId(topicRef.getKey());
                        topicRef.setValue(e);
                        break;
                    case "Grades":
                        Grade g = new Grade(etTitle.getText().toString(), subject, calendar.getTimeInMillis(), sbWeight.getProgress(), sbMark.getProgress());
                        topicRef = dbRef.push();
                        g.setId(topicRef.getKey());
                        topicRef.setValue(g);
                        break;
                    case "Subjects":
                        Subject s = new Subject(etTitle.getText().toString());
                        topicRef = dbRef.push();
                        s.setId(topicRef.getKey());
                        topicRef.setValue(s);
                        break;
                }
                progressDialog.dismiss();
                finish();
            } else {
                vibrate();
                Toast.makeText(this, "Either one of the fields is empty or data is illogical", Toast.LENGTH_LONG).show();
            }
        } else if (v == btnCancelAdd) {
            finish();
        }
    }
    /**
     * Check if the data input was valid
     * @return Data input is valid
     */
    public boolean checkAll() {
        return !((etTitle.isActivated() && etTitle.getText().length() == 0) ||
                (spSub.isActivated() && subject.length() == 0) ||
                (tvDate.isActivated() && tvDate.getText().length() == 4) ||
                (sbWeight.isActivated() && (sbWeight.getProgress() == 0 || sbWeight.getProgress() == 100)));
    }

    /**
     * Modify the appropriate TextView when the value on the SeekBar is modified
     * @param seekBar The SeekBar whose value is modified
     * @param progress Current progress
     * @param fromUser Was the progress changed by the user?
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbWeight)
            tvWeightAdd.setText(progress + "%");
        else if (seekBar == sbMark)
            tvMarkAdd.setText(progress + "%");
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] arr = {0, 200, 0, 200, 0, 200};
        vibrator.vibrate(arr, -1);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
