package com.example.ra.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTopic, tvTotal;
    ListView lvList;
    Button btnAdd;
    ArrayList topicList;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        tvTopic = (TextView) findViewById(R.id.tv_topic);
        tvTotal = (TextView) findViewById(R.id.tv_total);
        lvList = (ListView) findViewById(R.id.lv_list);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        Intent intent = getIntent();
        topic = intent.getStringExtra("topic");
        tvTopic.setText(topic);
        retrieveData();


        //Ensure that the app doesn't freak out when no data is available
        if (topicList == null) {
            topicList = new ArrayList();
            topicList.add("");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicList);
            lvList.setAdapter(arrayAdapter);
        }


    }

    /**
     * Retrieve and show the appropriate data
     */
    public void retrieveData() {
        //This happens only after the function fbManager.get[TOPIC] is executed;
        final FirebaseManager fbManager = FirebaseManager.getInstance(this);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.arg1 == FirebaseManager.DONE_GRADES) {
                    topicList = FirebaseManager.grades;
                    GradeAdapter gradeAdapter = new GradeAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(gradeAdapter);
                } else if (msg.arg1 == FirebaseManager.DONE_EXAMS) {
                    topicList = FirebaseManager.exams;
                    ExamAdapter examAdapter = new ExamAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(examAdapter);
                } else if (msg.arg1 == FirebaseManager.DONE_ABSENCE) {
                    topicList = FirebaseManager.absence;
                    AbsenceAdapter absenceAdapter = new AbsenceAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(absenceAdapter);
                } else if (msg.arg1 == FirebaseManager.DONE_SUBJECTS) {
                    topicList = FirebaseManager.subjects;
                    SubjectAdapter subjectAdapter = new SubjectAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(subjectAdapter);
                }
                return true;
            }
        });

        switch (topic) {
            case "Grades":
                fbManager.getGrades(handler);
                break;
            case "Exams":
                fbManager.getExams(handler);
                break;
            case "Absence":
                fbManager.getAbsence(handler);
                break;
            case "Subjects":
                fbManager.getSubjectsAndGrades(handler);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            Intent intent = new Intent(ListActivity.this, AddActivity.class);
            intent.putExtra("topic", topic);
            startActivity(intent);
        }
    }

}
