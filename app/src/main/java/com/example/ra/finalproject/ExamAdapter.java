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

import java.util.Calendar;
import java.util.List;

public class ExamAdapter extends ArrayAdapter {
    Context context;
    List<Exam> obj;

    public ExamAdapter(Context context, int resource, List<Exam> obj) {
        super(context, resource, obj);
        this.context = context;
        this.obj = obj;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.c_layout_ex, parent, false);

        TextView tvTitleEx = (TextView) view.findViewById(R.id.tv_title_ex);
        TextView tvSubEx = (TextView) view.findViewById(R.id.tv_sub_ex);
        TextView tvWeightEx = (TextView) view.findViewById(R.id.tv_weight_ex);
        TextView tvDateEx = (TextView) view.findViewById(R.id.tv_date_ex);
        Exam t = obj.get(position);

        tvTitleEx.setText(t.getTitle());
        tvSubEx.setText(t.getSubject());
        tvWeightEx.setText(t.getWeight() + "%");
        tvDateEx.setText(getDate(t.getDate()));
        return view;
    }

    String getDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }
}
