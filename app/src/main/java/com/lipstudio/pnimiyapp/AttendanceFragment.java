package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;



public class AttendanceFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    ArrayList<User> users;
    ArrayList<UserStudent> students;
    UserHelper userHelper;
    Context context;
    LinearLayout tab1;
    LinearLayout tab2;
    LinearLayout tab3;
    LinearLayout tab4;
    LinearLayout tab5;
    LinearLayout tab6;
    LinearLayout.LayoutParams tabsParams;
    EditText titleEt;
    TextView date;
    TextView day;
    TextView hour;
    EditText search;
    Button addAttendanceSheet;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_sheet,container,false);
        context = getActivity();
        userHelper = new UserHelper(context);
        userHelper.open();
        users = userHelper.getAllUsers();
        userHelper.close();

        titleEt = view.findViewById(R.id.attendanceTitle);
        date = view.findViewById(R.id.date);
        day = view.findViewById(R.id.day);
        hour = view.findViewById(R.id.hour);
        addAttendanceSheet = view.findViewById(R.id.createAttendance);
        addAttendanceSheet.setOnClickListener(this);

        for (int i =0; i <users.size(); i++){
            Log.e("user Details",users.get(i).toString());
        }
        view.findViewById(R.id.ז).setOnClickListener(this);
        view.findViewById(R.id.ח).setOnClickListener(this);
        view.findViewById(R.id.ט).setOnClickListener(this);
        view.findViewById(R.id.י).setOnClickListener(this);
        view.findViewById(R.id.יא).setOnClickListener(this);
        view.findViewById(R.id.יב).setOnClickListener(this);
        view.findViewById(R.id.ז).setTag("notExpended");
        view.findViewById(R.id.ח).setTag("notExpended");
        view.findViewById(R.id.ט).setTag("notExpended");
        view.findViewById(R.id.י).setTag("notExpended");
        view.findViewById(R.id.יא).setTag("notExpended");
        view.findViewById(R.id.יב).setTag("notExpended");

        students = new ArrayList<>();
        tabsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,dpToPx(50));
        tabsParams.setMargins(0,0,0,15);
        tab1 = view.findViewById(R.id.tab1);
        tab2 = view.findViewById(R.id.tab2);
        tab3 = view.findViewById(R.id.tab3);
        tab4 = view.findViewById(R.id.tab4);
        tab5 = view.findViewById(R.id.tab5);
        tab6 = view.findViewById(R.id.tab6);
        tab1.setLayoutParams(tabsParams);
        tab2.setLayoutParams(tabsParams);
        tab3.setLayoutParams(tabsParams);
        tab4.setLayoutParams(tabsParams);
        tab5.setLayoutParams(tabsParams);
        tab6.setLayoutParams(tabsParams);
        orderUsers();

        AttendanceSheet attendanceSheet = new AttendanceSheet();
        ArrayList<Attendance> attendances = new ArrayList<>();
        date.setText(attendanceSheet.getCurrentSheetDate());
        day.setText(attendanceSheet.getDayOfWeek());
        hour.setText(attendanceSheet.getCurrentHour());
        search = view.findViewById(R.id.searchEditText);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.attendance_sheet_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        search.setVisibility(View.VISIBLE);
        search.requestFocus();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }


    @Override
    public void onClick(View v) {

        if (v.getTag().equals("notExpended")) {
            v.setBackgroundResource(R.drawable.expnded_tab);
            v.setTag("expended");
            ((TextView) v).setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams newTablayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            newTablayoutParams.setMargins(0,0,0,15);
            switch (v.getId()) {
                case R.id.ז:
                    tab1.setLayoutParams(newTablayoutParams);
                    break;
                case R.id.ח:
                    tab2.setLayoutParams(newTablayoutParams);
                    break;
                case R.id.ט:
                    tab3.setLayoutParams(newTablayoutParams);
                    break;
                case R.id.י:
                    tab4.setLayoutParams(newTablayoutParams);
                    break;
                case R.id.יא:
                    tab5.setLayoutParams(newTablayoutParams);
                    break;
                case R.id.יב:
                    tab6.setLayoutParams(newTablayoutParams);
                    break;
            }
        } else if(v.getTag().equals("expended")){
            v.setBackgroundResource(R.drawable.expend_tab);
            v.setTag("notExpended");
            ((TextView) v).setTextColor(getResources().getColor(R.color.grey));
            switch (v.getId()) {
                case R.id.ז:
                    tab1.setLayoutParams(tabsParams);
                    break;
                case R.id.ח:
                    tab2.setLayoutParams(tabsParams);
                    break;
                case R.id.ט:
                    tab3.setLayoutParams(tabsParams);
                    break;
                case R.id.י:
                    tab4.setLayoutParams(tabsParams);
                    break;
                case R.id.יא:
                    tab5.setLayoutParams(tabsParams);
                    break;
                case R.id.יב:
                    tab6.setLayoutParams(tabsParams);
                    break;
            }
        }
        else if(v.getId() == R.id.createAttendance){

        }
    }

    private void orderUsers(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i = 0; i <users.size(); i++){
            if(users.get(i) instanceof UserStudent) {

                LinearLayout userCheckBoxLayout = new LinearLayout(context);
                userCheckBoxLayout.setLayoutParams(layoutParams);
                CheckBox checkBox = new CheckBox(context);
                checkBox.setLayoutParams(layoutParams);

                students.add((UserStudent) users.get(i));
                TextView userName = new TextView(context);
                userName.setLayoutParams(layoutParams);
                userName.setTextSize(18);
                userName.setText(users.get(i).getFirstName() + " " + users.get(i).getLastName());
                Log.e("userName",userName.getText().toString());
                checkBox.setTag(users.get(i).getId());
                userCheckBoxLayout.addView(userName);
                userCheckBoxLayout.addView(checkBox);
                if (users.get(i).getGroup().get(0).contains("ז"))
                    tab1.addView(userCheckBoxLayout);

                else if(users.get(i).getGroup().get(0).contains("ח"))
                    tab2.addView(userCheckBoxLayout);

                else if(users.get(i).getGroup().get(0).contains("ט"))
                    tab3.addView(userCheckBoxLayout);

                else if(users.get(i).getGroup().get(0).contains("יא"))
                    tab5.addView(userCheckBoxLayout);

                else if(users.get(i).getGroup().get(0).contains("יב"))
                    tab6.addView(userCheckBoxLayout);

                else
                    tab4.addView(userCheckBoxLayout);
            }
        }
    }

    private int dpToPx(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }


}
