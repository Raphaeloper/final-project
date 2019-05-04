package com.example.ra.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTopic, tvTotal;
    ListView lvList;
    Button btnAdd;
    ArrayList topicList;
    String topic;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

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
        switch (topic) {
            case "Absence":
                dbRef = dbRef.child("absence").child(FirebaseManager.auth.getUid());
                retrieveData();
                break;
            case "Exams":
                dbRef = dbRef.child("exams").child(FirebaseManager.auth.getUid());
                retrieveData();
                break;
            case "Grades":
                dbRef = dbRef.child("grades").child(FirebaseManager.auth.getUid());
                retrieveData();
                break;
        }

        if (topicList == null) {
            topicList = new ArrayList();
            topicList.add("No data");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicList);
            lvList.setAdapter(arrayAdapter);
        }


    }

    public void retrieveData() {                      //will return false if no data was retrieved
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topicList = new ArrayList();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    switch (topic) {
                        case "Absence":
                            Absence a = data.getValue(Absence.class);
                            topicList.add(a);
                            break;
                        case "Exams":
                            Exam e = data.getValue(Exam.class);
                            topicList.add(e);
                            break;
                        case "Grades":
                            Grade g = data.getValue(Grade.class);
                            topicList.add(g);
                            break;
                    }
                }
                setList(topic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void setList(String topic) {
        if (topicList.size() != 0 && !topicList.get(0).getClass().equals(String.class)) {
            switch (topic) {
                case "Absence":
                    AbsenceAdapter absenceAdapter = new AbsenceAdapter(this, 0, topicList);
                    lvList.setAdapter(absenceAdapter);
                    break;
                case "Exams":
                    ExamAdapter examAdapter = new ExamAdapter(this, 0, topicList);
                    lvList.setAdapter(examAdapter);
                    break;
                case "Grades":
                    GradeAdapter gradeAdapter = new GradeAdapter(this, 0, topicList);
                    lvList.setAdapter(gradeAdapter);
                    break;
            }
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
