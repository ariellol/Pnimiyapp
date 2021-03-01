package com.lipstudio.pnimiyapp;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AttendanceAdapter extends ArrayAdapter<AttendanceSheet>{

    Context context;
    ArrayList<AttendanceSheet> attendanceSheets;
    public AttendanceAdapter(@NonNull Context context, int resource, ArrayList<AttendanceSheet> sheets) {
        super(context, resource,sheets);
        this.context = context;
        this.attendanceSheets = sheets;
        Log.e("infoAdapter","aaaa");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_attendance_details,parent,false);

        TextView title = convertView.findViewById(R.id.custom_attendance_title);
        TextView date = convertView.findViewById(R.id.custom_attendance_date);
        TextView day = convertView.findViewById(R.id.custom_attendance_day);
        TextView hour = convertView.findViewById(R.id.custom_attendance_hour);
        AttendanceSheet attendanceSheet = this.attendanceSheets.get(position);

        title.setText(attendanceSheet.getTitle());
        date.setText(attendanceSheet.getCurrentSheetDate());
        day.setText(attendanceSheet.getDayOfWeek());
        hour.setText(attendanceSheet.getCurrentHour());

        return convertView;
    }

}
