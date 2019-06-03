package com.example.ra.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final int UPDATE_INFO_REQUEST = 1234, FIN_PIC_DOWNLOAD = 5678;
    Button btnGrades, btnAbsence, btnExams, btnSubjects;
    TextView tvName, tvClass, tvSchool, tvAvg, tvAbsTotal, tvExamTotal;
    ImageView ivProfile;
    DatabaseReference userReference;
    String uid;
    Handler handler;
    Bitmap bitmap;
    boolean newUser = false;
    //if the user logs out, he's redirected to the login screen
    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGrades = (Button) findViewById(R.id.btn_grades);
        btnAbsence = (Button) findViewById(R.id.btn_absence);
        btnExams = (Button) findViewById(R.id.btn_exams);
        btnSubjects = (Button) findViewById(R.id.btn_subjects);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvClass = (TextView) findViewById(R.id.tv_class);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvAvg = (TextView) findViewById(R.id.tv_avg);
        tvAbsTotal = (TextView) findViewById(R.id.tv_abs_total);
        tvExamTotal = (TextView) findViewById(R.id.tv_exam_total);
        ivProfile = (ImageView) findViewById(R.id.iv_profile);

        btnGrades.setOnClickListener(this);
        btnAbsence.setOnClickListener(this);
        btnExams.setOnClickListener(this);
        btnSubjects.setOnClickListener(this);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.arg1 == FIN_PIC_DOWNLOAD) {
                    ivProfile.setImageBitmap(bitmap);
                }
                return true;
            }
        });

        uid = FirebaseManager.auth.getUid();
        userReference = FirebaseDatabase.getInstance().getReference("users/" + uid);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ProgressDialog progressDialog = FirebaseManager.getInstance(MainActivity.this).buildProgressDialog();
                progressDialog.show();
                Student s = dataSnapshot.getValue(Student.class);
                // If user is new, make him update his info
                if (s == null) {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    progressDialog.dismiss();
                    startActivityForResult(intent, UPDATE_INFO_REQUEST);
                    newUser = true;
                }
                // If user exists, display his info on-screen
                else {
                    tvName.setText(s.getName());
                    tvClass.setText(s.getClassroom());
                    tvSchool.setText(s.getSchool());
                    if (!newUser) {
                        final long ONE_MEGABYTE = 1024 * 1024;
                        FirebaseManager.getInstance(MainActivity.this).picStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                if (bitmap != null) {
                                    Message message = new Message();
                                    message.arg1 = FIN_PIC_DOWNLOAD;
                                    handler.sendMessage(message);
                                    progressDialog.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed: " + e, Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    } else
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        if (v == btnGrades) {
            intent.putExtra("topic", "Grades");
            startActivity(intent);
        } else if (v == btnAbsence) {
            intent.putExtra("topic", "Absence");
            startActivity(intent);
        } else if (v == btnExams) {
            intent.putExtra("topic", "Exams");
            startActivity(intent);
        } else if (v == btnSubjects) {
            intent.putExtra("topic", "Subjects");
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UPDATE_INFO_REQUEST) {
            final ProgressDialog progressDialog = FirebaseManager.getInstance(MainActivity.this).buildProgressDialog();
            progressDialog.show();
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Student s = dataSnapshot.getValue(Student.class);
                    tvName.setText(s.getName());
                    tvClass.setText(s.getClassroom());
                    tvSchool.setText(s.getSchool());
                    final long ONE_MEGABYTE = 1024 * 1024;
                    FirebaseManager.getInstance(MainActivity.this).picStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            if (bitmap != null) {
                                Message message = new Message();
                                message.arg1 = FIN_PIC_DOWNLOAD;
                                handler.sendMessage(message);
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed: " + e, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_disconect:
                FirebaseManager.getInstance(MainActivity.this).logout();
        }
        return true;
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
