package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminPageFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_page, container, false);

        view.findViewById(R.id.notification_title).setOnFocusChangeListener(this);
        view.findViewById(R.id.notification_content).setOnFocusChangeListener(this);
        view.findViewById(R.id.changeUserBtn).setOnClickListener(this);
        view.findViewById(R.id.attendanceHistory).setOnClickListener(this);
        Button addUserBtn = view.findViewById(R.id.addUserBtn);
        addUserBtn.setOnClickListener(this);
        Button addAttendanceSheet = view.findViewById(R.id.addAttendanceBtn);
        addAttendanceSheet.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addUserBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUserFragment())
                        .addToBackStack(null).commit();
                break;

            case R.id.changeUserBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListChangeUser())
                        .addToBackStack(null).commit();
                break;

            case R.id.attendanceHistory:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceHistoryFragment())
                        .addToBackStack(null).commit();
                break;

            case R.id.addAttendanceBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment())
                        .addToBackStack(null).commit();
                break;

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }
}


