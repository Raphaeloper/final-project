package com.example.ra.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    TextView tvWeight, tvWeightAdd, tvMark, tvMarkAdd;
    EditText etTitle, etSub, etDate;
    CheckBox cbApprovedAdd;
    SeekBar sbWeight, sbMark;
    Button btnConfirmAdd, btnCancelAdd;
    String topic, directory;
    DatabaseReference dbRef, topicRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        topic = intent.getStringExtra("topic");

        etTitle = (EditText) findViewById(R.id.et_title);
        etSub = (EditText) findViewById(R.id.et_sub);
        etDate = (EditText) findViewById(R.id.et_date);
        cbApprovedAdd = (CheckBox) findViewById(R.id.cb_approved_add);
        tvWeight = (TextView) findViewById(R.id.tv_weight);
        sbWeight = (SeekBar) findViewById(R.id.sb_weight);
        tvWeightAdd = (TextView) findViewById(R.id.tv_weight_add);
        tvMark = (TextView) findViewById(R.id.tv_mark);
        sbMark = (SeekBar) findViewById(R.id.sb_mark);
        tvMarkAdd = (TextView) findViewById(R.id.tv_mark_add);
        btnConfirmAdd = (Button) findViewById(R.id.btn_confirm_add);
        btnCancelAdd = (Button) findViewById(R.id.btn_cancel_add);

        btnConfirmAdd.setOnClickListener(this);
        btnCancelAdd.setOnClickListener(this);
        sbWeight.setOnSeekBarChangeListener(this);
        sbMark.setOnSeekBarChangeListener(this);

        hideAll(); //cleans the screen for easier customization

        setMode(topic); //customizes the screen for the selected topic
        dbRef = FirebaseDatabase.getInstance().getReference().child(directory).child(FirebaseManager.auth.getUid());


    }

    public void hideAll() {
        disableView(etTitle);
        disableView(etSub);
        disableView(etDate);
        disableView(cbApprovedAdd);
        disableView(tvWeight);
        disableView(sbWeight);
        disableView(tvWeightAdd);
        disableView(tvMark);
        disableView(sbMark);
        disableView(tvMarkAdd);
    }

    public void disableView(View v) {
        v.setActivated(false);
        v.setVisibility(View.INVISIBLE);
    }

    public void setMode(String topic) {
        switch (topic) {
            case "Absence":
                enableView(etSub);
                enableView(etDate);
                enableView(cbApprovedAdd);
                directory = "absence";
                break;
            case "Exams":
                enableView(etTitle);
                enableView(etSub);
                enableView(etDate);
                enableView(tvWeight);
                enableView(sbWeight);
                enableView(tvWeightAdd);
                directory = "exams";
                break;
            case "Grades":
                enableView(etTitle);
                enableView(etSub);
                enableView(etDate);
                enableView(tvWeight);
                enableView(sbWeight);
                enableView(tvWeightAdd);
                enableView(tvMark);
                enableView(sbMark);
                enableView(tvMarkAdd);
                directory = "grades";
                break;
        }
    }

    public void enableView(View v) {
        v.setActivated(true);
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == sbWeight)
            tvWeightAdd.setText(progress + "%");
        else if (seekBar == sbMark)
            tvMarkAdd.setText(progress + "%");
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirmAdd) {
            if (checkAll()) {
                switch (topic) {
                    case "Absence":
                        Absence a = new Absence(etSub.getText().toString(), etDate.getText().toString(), cbApprovedAdd.isChecked());
                        topicRef = dbRef.push();
                        a.setAid(topicRef.getKey());
                        topicRef.setValue(a);
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

    public boolean checkAll() {
        return !((etTitle.isActivated() && etTitle.getText().length() == 0) ||
                (etSub.isActivated() && etSub.getText().length() == 0) ||
                (etDate.isActivated() && etDate.getText().length() == 0) ||
                (sbWeight.isActivated() && (sbWeight.getProgress() == 0 || sbWeight.getProgress() == 100)));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
