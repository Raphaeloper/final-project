package com.example.ra.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseManager manager;
    EditText etEmail, etPass;
    Button btnLogin, btnSignUp;
    //allows the user to skip login if he's already logged in
    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = FirebaseManager.getInstance(this);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPass = (EditText) findViewById(R.id.et_pass);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] arr = {0, 200, 0, 200, 0, 200};
        vibrator.vibrate(arr, -1);
    }

    /**
     * Check if the input is valid
     *
     * @param email The text that is typed in the email box
     * @param pass  The text that is typed in the password box
     * @return Input is valid
     */
    public boolean legit(String email, String pass) {
        return email.contains("@") && email.contains(".") && pass.length() > 7;
    }

    @Override
    public void onClick(View v) {
        if (etEmail.getText().length() != 0 && etPass.getText().length() != 0) {
            String email = etEmail.getText().toString();
            String pass = etPass.getText().toString();
            if (legit(email, pass)) {
                if (v == btnLogin)
                    manager.login(email, pass);
                else if (v == btnSignUp)
                    manager.signUp(email, pass);
            } else {
                vibrate();
                Toast.makeText(LoginActivity.this, "Email must be valid, Password must contain 8 or more characters",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            vibrate();
            Toast.makeText(LoginActivity.this, "All fields must be filled out in order to continue", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseManager.auth.addAuthStateListener(authStateListener);
    }

    //prevents the waste of resources
    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null)
            FirebaseManager.auth.removeAuthStateListener(authStateListener);
    }
}
