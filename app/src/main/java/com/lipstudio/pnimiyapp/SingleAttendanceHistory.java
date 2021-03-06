package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class SingleAttendanceHistory extends Fragment {

    TextView attendanceTitle;
    TextView attendanceDate;
    TextView attendanceDay;
    TextView attendanceHour;
    ArrayList<Attendance> attendances;
    Context context;
    ArrayList<UserStudent> students;
    ArrayList<User> users;
    ListView attendanceList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_attendance_history,container,false);
        context = getActivity();

        UserHelper userHelper = new UserHelper(context);
        userHelper.open();
        users = userHelper.getAllUsers();
        students = new ArrayList<>();
        for (int i = 0; i<users.size(); i++){
            if(users.get(i) instanceof UserStudent){
                students.add((UserStudent) users.get(i));
                Log.e("singleHistory",students.get(i).getFirstName()+ " " + students.get(i).getLastName());
            }
        }

        AttendanceSheet attendanceSheet = (AttendanceSheet)getArguments().getSerializable("attendanceSheet");

        for (int i = 0; i<attendanceSheet.getAttendances().size(); i++){
            Log.e("attendance",attendanceSheet.getAttendances().get(i).toString());
            Log.e("attendance",attendanceSheet.getAttendances().size()+"");
        }
        attendanceTitle = view.findViewById(R.id.single_attendance_title);
        attendanceTitle.setText(attendanceSheet.getTitle());
        attendanceDate = view.findViewById(R.id.single_attendance_date);
        attendanceDate.setText(attendanceSheet.getCurrentSheetDate());
        attendanceDay = view.findViewById(R.id.single_attendance_day);
        attendanceDay.setText(attendanceSheet.getDayOfWeek());
        attendanceHour = view.findViewById(R.id.single_attendance_hour);
        attendanceHour.setText(attendanceSheet.getCurrentHour());

        attendances = attendanceSheet.getAttendances();

        attendanceList = view.findViewById(R.id.attendanceList);
        SingleAttendanceAdapter adapter = new SingleAttendanceAdapter(context,0,attendances,students);
        attendanceList.setAdapter(adapter);

        return view;
    }
}
