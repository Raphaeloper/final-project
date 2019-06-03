package com.example.ra.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    final int TAKE_PIC = 1234, PICK_PIC = 7689;
    EditText etName, etClass, etSchool;
    ImageView ivPreview;
    Button btnTakePic, btnBrowse, btnConfirm, btnCancel;
    Uri filePath;
    Bitmap bitmap;
    boolean blankSheet = true, pic = false;
    Handler handler;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        etName = (EditText) findViewById(R.id.et_name);
        etClass = (EditText) findViewById(R.id.et_class);
        etSchool = (EditText) findViewById(R.id.et_school);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
        btnTakePic = (Button) findViewById(R.id.btn_take_pic);
        btnBrowse = (Button) findViewById(R.id.btn_browse);
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
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.arg1 == FirebaseManager.DONE_PIC) {
                    setResult(RESULT_OK);
                    progressDialog.dismiss();
                    finish();
                }
                return true;
            }
        });
        //If the user is new, force him to submit info in order to continue
        if (blankSheet) {
            btnCancel.setVisibility(View.INVISIBLE);
            btnCancel.setActivated(false);
        }
        btnTakePic.setOnClickListener(this);
        btnBrowse.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] arr = {0, 200, 0, 200, 0, 200};
        vibrator.vibrate(arr, -1);
    }

    @Override
    public void onClick(View v) {
        if (v == btnTakePic) {
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PIC);
        } else if (v == btnBrowse) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String path = directory.getPath();
            Uri data = Uri.parse(path);
            intent.setDataAndType(data, "image/*");
            startActivityForResult(intent, PICK_PIC);
        } else if (v == btnConfirm) {
            if (legit() && pic) {
                FirebaseManager firebaseManager = FirebaseManager.getInstance(this);
                progressDialog = firebaseManager.buildProgressDialog();
                progressDialog.show();
                firebaseManager.uploadImage(filePath, handler);
                String name, stClass, school;
                name = etName.getText().toString();
                stClass = etClass.getText().toString();
                school = etSchool.getText().toString();
                String uid = FirebaseManager.auth.getUid();
                Student student = new Student(uid, name, stClass, school);
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef.child("users").child(uid).setValue(student);
            } else {
                vibrate();
                Toast.makeText(this, "All fields must be filled and a picture must be selected/taken", Toast.LENGTH_LONG).show();
            }
        } else if (v == btnCancel) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PIC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String[] FILE = {MediaStore.Images.Media.DATA};
            //ContentProvider
            Cursor cursor = getContentResolver().query(filePath, FILE, null, null, null);
            cursor.moveToFirst();
            BitmapFactory.Options options = new BitmapFactory.Options();
            int columnIndex = cursor.getColumnIndex(FILE[0]);
            String imageDecode = cursor.getString(columnIndex);
            cursor.close();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imageDecode, options);
            ivPreview.setImageBitmap(bitmap);
            pic = true;
        } else if (requestCode == TAKE_PIC && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ivPreview.setImageBitmap(bitmap);
            String uid = FirebaseManager.auth.getUid();
            saveBItmap(bitmap, uid);
            File tFile = new File(getFilesDir() + "/" + uid);
            filePath = Uri.fromFile(tFile);
            pic = true;
        }
    }

    void saveBItmap(Bitmap bitmap, String fileName) {
        ByteArrayOutputStream byteAOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteAOS);
        FileOutputStream fileOS = null;
        try {
            fileOS = openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fileOS.write(byteAOS.toByteArray());
            fileOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the data input is valid
     *
     * @return Data input is valid
     */
    public boolean legit() {
        return etName.getText().length() != 0 && etClass.getText().length() != 0 && etSchool.getText().length() != 0;
    }
}
