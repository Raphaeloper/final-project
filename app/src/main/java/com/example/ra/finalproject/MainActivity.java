package com.example.ra.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnLogout, btnGrades, btnAbsence, btnExams;
    TextView tvName, tvClass, tvSchool, tvAvg, tvAbsTotal, tvExamTotal;
    ImageView ivProfile;
    DatabaseReference databaseReference;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnGrades = (Button) findViewById(R.id.btn_grades);
        btnAbsence = (Button) findViewById(R.id.btn_absence);
        btnExams = (Button) findViewById(R.id.btn_exams);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvClass = (TextView) findViewById(R.id.tv_class);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvAvg = (TextView) findViewById(R.id.tv_avg);
        tvAbsTotal = (TextView) findViewById(R.id.tv_abs_total);
        tvExamTotal = (TextView) findViewById(R.id.tv_exam_total);
        ivProfile = (ImageView) findViewById(R.id.iv_profile);

        uid = FirebaseManager.auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference(uid);




    }
}
