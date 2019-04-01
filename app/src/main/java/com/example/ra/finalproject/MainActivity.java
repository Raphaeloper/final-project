package com.example.ra.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final int UPDATE_INFO_REQUEST = 1234;
    Button btnLogout, btnGrades, btnAbsence, btnExams;
    TextView tvName, tvClass, tvSchool, tvAvg, tvAbsTotal, tvExamTotal;
    DatabaseReference userReference;
    String uid;
    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else
                Toast.makeText(MainActivity.this, "Not logged out", Toast.LENGTH_SHORT).show();
        }
    };


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

        btnLogout.setOnClickListener(this);
        btnGrades.setOnClickListener(this);
        btnAbsence.setOnClickListener(this);
        btnExams.setOnClickListener(this);

        uid = FirebaseManager.auth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("users/" + uid);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student s = dataSnapshot.getValue(Student.class);
                if (s == null) {                                                                             // If user is new, make him update his info
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivityForResult(intent, UPDATE_INFO_REQUEST);
                } else {                                                                                    // If user exists, display his info on-screen
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Student s = dataSnapshot.getValue(Student.class);
                            tvName.setText(s.getName());
                            tvClass.setText(s.getClassroom());
                            tvSchool.setText(s.getSchool());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseManager.auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null)
            FirebaseManager.auth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UPDATE_INFO_REQUEST) {
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Student s = dataSnapshot.getValue(Student.class);
                    tvName.setText(s.getName());
                    tvClass.setText(s.getClassroom());
                    tvSchool.setText(s.getSchool());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
        if (v == btnLogout)
            FirebaseManager.auth.signOut();
        else if (v == btnGrades) {
            intent.putExtra("topic", "Grades");
            startActivity(intent);
        } else if (v == btnAbsence) {
            intent.putExtra("topic", "Absence");
            startActivity(intent);
        } else if (v == btnExams) {
            intent.putExtra("topic", "Exams");
            startActivity(intent);
        }
    }
}
