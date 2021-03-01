package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.lipstudio.pnimiyapp.R.color.primaryColorGreen;

public class SingleAttendanceAdapter extends ArrayAdapter<Attendance>{

    Context context;
    ArrayList<Attendance> attendances;
    ArrayList<UserStudent> students;
    public SingleAttendanceAdapter(@NonNull Context context, int resource,ArrayList<Attendance> attendances, ArrayList<UserStudent> students){
        super(context, resource,attendances);
        this.context = context;
        this.attendances = attendances;
        this.students = students;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.attendance_show_custom_layout,parent,false);

        TextView fullName = convertView.findViewById(R.id.fullName);
        TextView isAttendance = convertView.findViewById(R.id.isAttendance);
        Attendance attendance = attendances.get(position);

        for (int i = 0; i < attendances.size(); i++) {
            if(attendance.getUserId() == students.get(i).getId())
                fullName.setText(students.get(i).getFirstName()+ " " +students.get(i).getLastName());
        }
        if(attendance.getAttendanceCode() ==1){
            isAttendance.setText("נכח/ה");
            isAttendance.setTextColor(context.getResources().getColor(primaryColorGreen));
        }
        else{
            isAttendance.setText("לא נכח/ה");
            isAttendance.setTextColor(context.getResources().getColor(R.color.red));
        }
        return convertView;
    }
}
