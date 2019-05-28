package com.example.ra.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    TextView tvTopic, tvTotal;
    ListView lvList;
    Button btnAdd;
    ArrayList topicList;
    String topic;
    GradeAdapter gradeAdapter;
    ExamAdapter examAdapter;
    AbsenceAdapter absenceAdapter;
    SubjectAdapter subjectAdapter;


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
        lvList.setVisibility(View.INVISIBLE);
        lvList.setActivated(false);
        retrieveData();


        //Ensure that the app doesn't freak out when no data is available
        if (topicList == null) {
            topicList = new ArrayList();
            topicList.add("");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicList);
            lvList.setAdapter(arrayAdapter);
            if (topic.equals("Grades"))
                tvTotal.setText("Average: 0");
            else
                tvTotal.setText("Total: 0");
        } else {
            lvList.setVisibility(View.VISIBLE);
            lvList.setActivated(true);
            if (topic.equals("Grades"))
                tvTotal.setText("Average: " + getAverage());
            else
                tvTotal.setText("Total: " + topicList.size());
        }
        lvList.setOnItemLongClickListener(this);

    }

    /**
     * Retrieve and show the appropriate data
     */
    public void retrieveData() {
        //This happens only after the function fbManager.get[TOPIC] is executed;
        final FirebaseManager fbManager = FirebaseManager.getInstance(this);
        final ProgressDialog progressDialog = fbManager.buildProgressDialog();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.arg1 == FirebaseManager.DONE_GRADES) {
                    topicList = FirebaseManager.grades;
                    gradeAdapter = new GradeAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(gradeAdapter);
                } else if (msg.arg1 == FirebaseManager.DONE_EXAMS) {
                    topicList = FirebaseManager.exams;
                    examAdapter = new ExamAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(examAdapter);
                } else if (msg.arg1 == FirebaseManager.DONE_ABSENCE) {
                    topicList = FirebaseManager.absence;
                    absenceAdapter = new AbsenceAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(absenceAdapter);
                } else if (msg.arg1 == FirebaseManager.DONE_SUBJECTS) {
                    topicList = FirebaseManager.subjects;
                    subjectAdapter = new SubjectAdapter(ListActivity.this, 0, topicList);
                    lvList.setAdapter(subjectAdapter);
                }
                if (topic.equals("Grades"))
                    tvTotal.setText("Average: " + getAverage());
                else
                    tvTotal.setText("Total: " + topicList.size());
                lvList.setVisibility(View.VISIBLE);
                lvList.setActivated(true);
                progressDialog.dismiss();
                return true;
            }
        });
        progressDialog.show();
        switch (topic) {
            case "Grades":
                fbManager.getGrades(handler);
                break;
            case "Exams":
                fbManager.getExams(handler);
                break;
            case "Absence":
                fbManager.getAbsence(handler);
                break;
            case "Subjects":
                fbManager.getSubjectsAndGrades(handler);
                break;
        }
    }

    String getAverage() {
        double sum = 0;
        int weightSum = 0;
        String average = "";
        ArrayList<Grade> grades = topicList;
        for (Grade grade : grades) {
            sum += grade.getNum() * grade.getWeight();
            weightSum += grade.getWeight();
        }
        average = (sum / weightSum) + "";
        average = average.substring(0, average.indexOf(".") + 2);
        return average;
    }


    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            Intent intent = new Intent(ListActivity.this, AddActivity.class);
            intent.putExtra("topic", topic);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = (Item) parent.getItemAtPosition(position);
        buildAndShowDialog(item.getId());
        return true;
    }

    public void buildAndShowDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this?");
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogListener(this, id));
        builder.setPositiveButton("Yes", new DialogListener(this, id));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
        if (!topicList.contains("")) {
            switch (topic) {
                case "Grades":
                    gradeAdapter.notifyDataSetChanged();
                    break;
                case "Exams":
                    examAdapter.notifyDataSetChanged();
                    break;
                case "Absence":
                    absenceAdapter.notifyDataSetChanged();
                    break;
                case "Subjects":
                    subjectAdapter.notifyDataSetChanged();
                    break;
            }
        }
        if (topicList == null || topicList.size() == 0) {
            topicList = new ArrayList();
            topicList.add("");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topicList);
            lvList.setAdapter(arrayAdapter);
        }
    }

    public class DialogListener implements DialogInterface.OnClickListener {
        Context context;
        String itemId;

        public DialogListener(Context context, String itemId) {
            this.context = context;
            this.itemId = itemId;
        }


        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == AlertDialog.BUTTON_POSITIVE) {
                FirebaseManager fbManager = FirebaseManager.getInstance(ListActivity.this);
                switch (topic) {
                    case "Grades":
                        fbManager.gradeRef.child(itemId).removeValue();
                        break;
                    case "Exams":
                        fbManager.examRef.child(itemId).removeValue();
                        break;
                    case "Absence":
                        fbManager.absenceRef.child(itemId).removeValue();
                        break;
                    case "Subjects":
                        fbManager.subjectRef.child(itemId).removeValue();
                        break;
                }
                retrieveData();
                switch (topic) {
                    case "Grades":
                        gradeAdapter.notifyDataSetChanged();
                        break;
                    case "Exams":
                        examAdapter.notifyDataSetChanged();
                        break;
                    case "Absence":
                        absenceAdapter.notifyDataSetChanged();
                        break;
                    case "Subjects":
                        subjectAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }
}
