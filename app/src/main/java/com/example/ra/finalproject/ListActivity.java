package com.example.ra.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
                retriveData();
                AbsenceAdapter absenceAdapter = new AbsenceAdapter(this, 0, topicList);
                lvList.setAdapter(absenceAdapter);
                break;
            case "Exams":
                dbRef = dbRef.child("exams");
                break;
            case "Grades":
                dbRef = dbRef.child("grades");
                break;
        }


    }

    public void retriveData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("this", "Got here");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
