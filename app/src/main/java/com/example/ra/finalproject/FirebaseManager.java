package com.example.ra.finalproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseManager {
    //const for handler
    static final int DONE_GRADES = 5555, DONE_EXAMS = 6666, DONE_ABSENCE = 7777, DONE_SUBJECTS = 8888, DONE_PIC = 9999;
    //topic lists
    public static ArrayList<Grade> grades;
    public static ArrayList<Exam> exams;
    public static ArrayList<Absence> absence;
    public static ArrayList<Subject> subjects;
    //user
    static FirebaseAuth auth;
    //singleton
    private static FirebaseManager instance = null;
    //for toasts, progress dialogs, etc.
    private static Context context;
    //firebase-related stuff
    DatabaseReference baseDataRef, gradeRef, examRef, absenceRef, subjectRef;
    StorageReference baseStorageRef, picStorageRef;
    //verification for grades and subjects - need to be retrieved together for the subject list
    boolean gotGrades, gotSubjects;

    //initiation
    private FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        if (auth.getUid() != null) {
            baseDataRef = FirebaseDatabase.getInstance().getReference();
            gradeRef = baseDataRef.child("grades").child(auth.getUid());
            examRef = baseDataRef.child("exams").child(auth.getUid());
            absenceRef = baseDataRef.child("absence").child(auth.getUid());
            subjectRef = baseDataRef.child("subjects").child(auth.getUid());
            baseStorageRef = FirebaseStorage.getInstance().getReference();
            picStorageRef = baseStorageRef.child("images/" + auth.getUid() + ".png");
            grades = new ArrayList<Grade>();
            exams = new ArrayList<Exam>();
            absence = new ArrayList<Absence>();
            subjects = new ArrayList<Subject>();
            gotGrades = false;
            gotSubjects = false;
        }
    }

    static FirebaseManager getInstance(Context context) {
        FirebaseManager.context = context;
        if (instance == null)
            instance = new FirebaseManager();
        return instance;
    }

    void logout() {
        auth.signOut();
        instance = null;
    }

    void signUp(String email, String pass) {
        final ProgressDialog progressDialog = buildProgressDialog();
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Successful sign up", Toast.LENGTH_SHORT).show();
                    instance = new FirebaseManager();
                } else {
                    Toast.makeText(context, "Failed to sign up", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    void login(String email, String pass) {
        final ProgressDialog progressDialog = buildProgressDialog();
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Successful login", Toast.LENGTH_SHORT).show();
                    instance = new FirebaseManager();
                } else {
                    Toast.makeText(context, "Failed to login, try again or sign up", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
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
                gotSubjects = false;
                for (DataSnapshot data : dataSnapshot.getChildren())
                    subjects.add(data.getValue(Subject.class));
                Message message = new Message();
                message.arg1 = DONE_SUBJECTS;
                gotSubjects = true;
                if (gotGrades) {
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
                gotGrades = false;
                for (DataSnapshot data : dataSnapshot.getChildren())
                    grades.add(data.getValue(Grade.class));
                Message message = new Message();
                message.arg1 = DONE_SUBJECTS;
                gotGrades = true;
                if (gotSubjects) {
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void uploadImage(Uri filePath, final Handler handler) {
        if (filePath != null) {
            picStorageRef = baseStorageRef.child("images/" + auth.getUid() + ".png");
            final Message message = new Message();
            picStorageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            message.arg1 = DONE_PIC;
                            handler.sendMessage(message);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to save the picture", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public ProgressDialog buildProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Data is being transferred");
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
