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

public class GradeAdapter extends ArrayAdapter {
    Context context;
    List<Grade> obj;

    public GradeAdapter(Context context, int resource, List<Grade> obj) {
        super(context, resource, obj);
        this.context = context;
        this.obj = obj;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.c_layout_gr, parent, false);
        TextView tvTitleGr = (TextView) view.findViewById(R.id.tv_title_gr);
        TextView tvSubGr = (TextView) view.findViewById(R.id.tv_sub_gr);
        TextView tvWeightGr = (TextView) view.findViewById(R.id.tv_weight_gr);
        TextView tvMarkGr = (TextView) view.findViewById(R.id.tv_mark_gr);
        TextView tvDateGr = (TextView) view.findViewById(R.id.tv_date_gr);
        Grade t = obj.get(position);
        tvTitleGr.setText(t.getTitle());
        tvSubGr.setText(t.getSubject());
        tvWeightGr.setText(t.getWeight() + "%");
        tvMarkGr.setText(t.getNum()+"");
        tvDateGr.setText(getDate(t.getDate()));
        return view;
    }

    String getDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }
}
