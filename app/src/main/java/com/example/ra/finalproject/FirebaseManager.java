package com.example.ra.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseManager {
    static final FirebaseAuth auth = FirebaseAuth.getInstance();

    private Context context;

    FirebaseManager(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
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
}
