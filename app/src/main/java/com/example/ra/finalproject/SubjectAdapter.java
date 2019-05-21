package com.example.ra.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends ArrayAdapter<Subject> {
    Context context;
    List<Subject> obj;
    double average = 0;

    public SubjectAdapter(Context context, int resource, List<Subject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.obj = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.c_layout_sb, parent, false);
        TextView tvSubSb = (TextView) view.findViewById(R.id.tv_sub_sb);
        TextView tvAvgSb = (TextView) view.findViewById(R.id.tv_avg_sb);
        Subject t = obj.get(position);
        tvSubSb.setText(t.getName());
        getAvg(t);
        String avg = average + "";
        avg = avg.substring(0, avg.indexOf(".") + 2);
        tvAvgSb.setText("Average " + avg);
        return view;
    }

    void getAvg(final Subject t) {
        double sum = 0;
        int weightSum = 0;
        ArrayList<Grade> grades = FirebaseManager.grades;
        for (Grade grade : grades) {
            if (grade.getSubject().equals(t.getName())) {
                sum += grade.getNum() * grade.getWeight();
                weightSum += grade.getWeight();
            }
        }
        if (sum != 0)
            average = sum / weightSum;
        else
            average = 0;

    }


}
