package com.example.ra.finalproject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseManager {
    static final int DONE_GRADES = 5555, DONE_EXAMS = 6666, DONE_ABSENCE = 7777, DONE_SUBJECTS = 8888;
    public static ArrayList<Grade> grades;
    public static ArrayList<Exam> exams;
    public static ArrayList<Absence> absence;
    public static ArrayList<Subject> subjects;
    static FirebaseAuth auth;
    private static FirebaseManager instance = null;
    private static Context context;
    public DatabaseReference baseRef, gradeRef, examRef, absenceRef, subjectRef;

    private FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        if (auth.getUid() != null) {
            baseRef = FirebaseDatabase.getInstance().getReference();
            gradeRef = baseRef.child("grades").child(auth.getUid());
            examRef = baseRef.child("exams").child(auth.getUid());
            absenceRef = baseRef.child("absence").child(auth.getUid());
            subjectRef = baseRef.child("subjects").child(auth.getUid());
            grades = new ArrayList<Grade>();
            exams = new ArrayList<Exam>();
            absence = new ArrayList<Absence>();
            subjects = new ArrayList<Subject>();
        }
    }

    static FirebaseManager getInstance(Context context) {
        FirebaseManager.context = context;
        if (instance == null || grades == null)
            instance = new FirebaseManager();
        return instance;
    }

    void logout() {
        auth.signOut();
        instance = null;
    }
    void signUp(String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Successful sign up", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to sign up", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Successful login", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to login, try again or sign up", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getGrades(final Handler handler) {
        gradeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                grades.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    grades.add(data.getValue(Grade.class));
                Message message = new Message();
                message.arg1 = DONE_GRADES;
                handler.sendMessage(message);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getExams(final Handler handler) {
        examRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exams.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    exams.add(data.getValue(Exam.class));
                Message message = new Message();
                message.arg1 = DONE_EXAMS;
                handler.sendMessage(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getAbsence(final Handler handler) {
        absenceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                absence.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    absence.add(data.getValue(Absence.class));
                Message message = new Message();
                message.arg1 = DONE_ABSENCE;
                handler.sendMessage(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getSubjects(final Handler handler) {
        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    subjects.add(data.getValue(Subject.class));
                Message message = new Message();
                message.arg1 = DONE_SUBJECTS;
                handler.sendMessage(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getSubjectsAndGrades(final Handler handler) {
        subjects.clear();
        subjectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    subjects.add(data.getValue(Subject.class));
                Message message = new Message();
                message.arg1 = DONE_SUBJECTS;
                if (grades.size() != 0) {
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        grades.clear();
        gradeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                grades.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    grades.add(data.getValue(Grade.class));
                Message message = new Message();
                message.arg1 = DONE_SUBJECTS;
                if (subjects.size() != 0) {
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void removeValue(DatabaseReference dbRef) {
        dbRef.removeValue();
    }
}
