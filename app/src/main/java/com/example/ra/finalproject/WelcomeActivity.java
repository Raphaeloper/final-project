package com.example.ra.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etClass, etSchool;
    Button btnConfirm, btnCancel;
    boolean blankSheet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        etName = (EditText) findViewById(R.id.et_name);
        etClass = (EditText) findViewById(R.id.et_class);
        etSchool = (EditText) findViewById(R.id.et_school);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setActivated(true);
        Intent intent = getIntent();
        if (intent.getStringExtra("name") != null) {
            String name, stClass, school;
            name = intent.getStringExtra("name");
            stClass = intent.getStringExtra("class");
            school = intent.getStringExtra("school");
            etName.setText(name);
            etClass.setText(stClass);
            etSchool.setText(school);
            blankSheet = false;
        }
        if (blankSheet) {
            btnCancel.setVisibility(View.INVISIBLE);
            btnCancel.setActivated(false);
        }
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            if (legit()) {
                String name, stClass, school;
                name = etName.getText().toString();
                stClass = etClass.getText().toString();
                school = etSchool.getText().toString();
                String uid = FirebaseManager.auth.getUid();
                Student student = new Student(uid, name, stClass, school);
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef.child("users").child(uid).setValue(student);
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        } else if (v == btnCancel) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public boolean legit() {
        return etName.getText().length() != 0 && etClass.getText().length() != 0 && etSchool.getText().length() != 0;
    }
}
