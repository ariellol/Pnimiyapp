package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AttendanceHistoryFragment extends Fragment{

    ArrayList<AttendanceSheet> attendanceSheets;
    ListView attendanceListView;
    AttendanceAdapter sheetAdapter;
    boolean deleteMode = false;
    int deleteNum = 0;
    BottomNavigationView navigationView;
    ArrayList<AttendanceSheet> deleteAttendanceSheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_history_fragment, container, false);

        navigationView = view.findViewById(R.id.bottomNavigation);
        navigationView.setItemIconTintList(null);
        navigationView.setSelectedItemId(0);
        deleteAttendanceSheet = new ArrayList<>();

        final Context context = getActivity();
        attendanceSheets = new ArrayList<>();
        AttendanceSheetHelper sheetHelper = new AttendanceSheetHelper(context);
        sheetHelper.open();
        attendanceSheets = sheetHelper.getAllAttendanceSheets();
        sheetHelper.close();

        attendanceListView = view.findViewById(R.id.allAttendances);
        sheetAdapter = new AttendanceAdapter(context, 0, attendanceSheets);
        attendanceListView.setAdapter(sheetAdapter);

        attendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (deleteMode) {
                    if (view.getTag() == null) {
                        view.setTag("deleteMode");
                        view.setBackgroundColor(getResources().getColor(R.color.superLightCyan));
                        deleteNum++;
                        deleteAttendanceSheet.add(attendanceSheets.get(position));
                    } else {
                        view.setBackground(null);
                        view.setTag(null);
                        deleteNum--;
                        deleteAttendanceSheet.remove(attendanceSheets.get(position));
                        if (deleteNum == 0) {
                            deleteMode = false;
                            navigationView.setVisibility(View.GONE);
                        }
                    }
                } else if (!deleteMode) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("attendanceSheet", attendanceSheets.get(position));
                    SingleAttendanceHistory singleAttendanceHistory = new SingleAttendanceHistory();
                    singleAttendanceHistory.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, singleAttendanceHistory)
                            .addToBackStack(null).commit();
                }
            }
        });

        attendanceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                navigationView.setVisibility(View.VISIBLE);
                deleteMode = true;

                if (view.getTag() != null) {
                    view.setBackground(null);
                    view.setTag(null);
                    deleteNum--;
                    deleteAttendanceSheet.remove(attendanceSheets.get(position));
                    if (deleteNum == 0) {
                        deleteMode = false;
                        navigationView.setVisibility(View.GONE);
                    }
                } else {
                    deleteAttendanceSheet.add(attendanceSheets.get(position));
                    view.setTag("deleteMode");
                    deleteNum++;
                    view.setBackgroundColor(getResources().getColor(R.color.superLightCyan));
                }
                return true;
            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.deleteAttendances){
                    AttendanceSheetHelper helper = new AttendanceSheetHelper(context);
                    helper.open();
                    helper.deleteAttendanceSheets(deleteAttendanceSheet);
                    helper.close();

                    for(int i = 0; i<deleteAttendanceSheet.size(); i++){
                        for (int j = 0; j<attendanceSheets.size(); j++){
                            if(deleteAttendanceSheet.get(i).getSheetId() == attendanceSheets.get(j).getSheetId()){
                                attendanceSheets.remove(j);
                                break;
                            }
                        }
                    }
                    sheetAdapter.notifyDataSetChanged();
                    deleteAttendanceSheet.clear();
                }

                else{
                    for (int i = 0; i<attendanceSheets.size(); i++){
                        View sheet = getViewByPosition(i,attendanceListView);
                        sheet.setBackground(null);
                        sheet.setTag(null);
                    }
                    sheetAdapter.notifyDataSetChanged();
                    deleteAttendanceSheet.clear();
                }
                navigationView.setVisibility(View.GONE);
                deleteMode = false;
                deleteNum = 0;
                return true;
            }
        });
        return view;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}
