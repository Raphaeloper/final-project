package com.example.ra.finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    TextView tvDate, tvWeight, tvWeightAdd, tvMark, tvMarkAdd;
    EditText etTitle, etSub;
    CheckBox cbApprovedAdd;
    SeekBar sbWeight, sbMark;
    Button btnConfirmAdd, btnCancelAdd;
    String topic, directory;
    DatabaseReference dbRef, topicRef;
    int yearC, monthC, dayC;
    Calendar calendar;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        topic = intent.getStringExtra("topic");

        etTitle = (EditText) findViewById(R.id.et_title);
        etSub = (EditText) findViewById(R.id.et_sub);
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

        tvDate.setOnClickListener(this);
        btnConfirmAdd.setOnClickListener(this);
        btnCancelAdd.setOnClickListener(this);
        sbWeight.setOnSeekBarChangeListener(this);
        sbMark.setOnSeekBarChangeListener(this);

        hideAll();

        setMode(topic);
        dbRef = FirebaseDatabase.getInstance().getReference().child(directory).child(FirebaseManager.auth.getUid());
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
        disableView(etSub);
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
                enableView(etSub);
                enableView(tvDate);
                enableView(cbApprovedAdd);
                directory = "absence";
                break;
            case "Exams":
                enableView(etTitle);
                enableView(etSub);
                enableView(tvDate);
                enableView(tvWeight);
                enableView(sbWeight);
                enableView(tvWeightAdd);
                directory = "exams";
                break;
            case "Grades":
                enableView(etTitle);
                enableView(etSub);
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

    @Override
    public void onClick(View v) {
        if (v == tvDate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, yearC, monthC - 1, dayC);
            datePickerDialog.show();
        } else if (v == btnConfirmAdd) {
            if (checkAll()) {
                switch (topic) {
                    case "Absence":
                        Absence a = new Absence(etSub.getText().toString(), calendar.getTimeInMillis(), cbApprovedAdd.isChecked());
                        topicRef = dbRef.push();
                        a.setAid(topicRef.getKey());
                        topicRef.setValue(a);
                        break;
                    case "Exams":
                        Exam e = new Exam(etTitle.getText().toString(), etSub.getText().toString(), calendar.getTimeInMillis(), sbWeight.getProgress());
                        topicRef = dbRef.push();
                        e.setEid(topicRef.getKey());
                        topicRef.setValue(e);
                        break;
                    case "Grades":
                        Grade g = new Grade(etTitle.getText().toString(), etSub.getText().toString(), calendar.getTimeInMillis(), sbWeight.getProgress(), sbMark.getProgress());
                        topicRef = dbRef.push();
                        g.setGid(topicRef.getKey());
                        topicRef.setValue(g);
                        break;
                    case "Subjects":
                        Subject s = new Subject(etTitle.getText().toString());
                        topicRef = dbRef.push();
                        s.setSid(topicRef.getKey());
                        topicRef.setValue(s);
                        break;
                }
                finish();
            } else {
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
                (etSub.isActivated() && etSub.getText().length() == 0) ||
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

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
