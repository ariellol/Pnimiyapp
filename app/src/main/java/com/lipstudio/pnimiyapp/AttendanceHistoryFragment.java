package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AttendanceHistoryFragment extends Fragment {

    ArrayList<AttendanceSheet> attendanceSheets;
    ListView attendanceListView;
    AttendanceAdapter sheetAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_history_fragment,container,false);

        final Context context = getActivity();
        attendanceSheets = new ArrayList<>();
        AttendanceSheetHelper sheetHelper = new AttendanceSheetHelper(context);
        sheetHelper.open();
        attendanceSheets = sheetHelper.getAllAttendanceSheets();
        sheetHelper.close();
        for (int i = 0; i < attendanceSheets.size(); i++) {
            Log.e("attendanceSize",attendanceSheets.get(i).getAttendances().size()+"");

        }
        attendanceListView = view.findViewById(R.id.allAttendances);
        sheetAdapter = new AttendanceAdapter(context,0,attendanceSheets);
        attendanceListView.setAdapter(sheetAdapter);

        attendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable("attendanceSheet",attendanceSheets.get(position));
                SingleAttendanceHistory singleAttendanceHistory = new SingleAttendanceHistory();
                singleAttendanceHistory.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, singleAttendanceHistory)
                        .addToBackStack(null).commit();
            }
        });
        return view;
    }
}
