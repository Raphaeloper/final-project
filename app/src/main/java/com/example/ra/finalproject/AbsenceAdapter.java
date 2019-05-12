package com.example.ra.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class AbsenceAdapter extends ArrayAdapter<Absence> {
    Context context;
    List<Absence> obj;

    public AbsenceAdapter(Context context, int resource, List<Absence> obj) {
        super(context, resource, obj);
        this.context = context;
        this.obj = obj;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.c_layout_ab, parent, false);
        TextView tvSubAb = (TextView) view.findViewById(R.id.tv_sub_ab);
        TextView tvDateAb = (TextView) view.findViewById(R.id.tv_date_ab);
        CheckBox cbApproved = (CheckBox) view.findViewById(R.id.cb_approved);
        Absence t = obj.get(position);
        tvSubAb.setText(t.getSubject());
        tvDateAb.setText(getDate(t.getDate()));
        cbApproved.setChecked(t.isApproved());
        return view;
    }

    String getDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }
}
