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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class AdminPageFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    TextView toolbarTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_page, container, false);

        view.findViewById(R.id.notification_title).setOnFocusChangeListener(this);
        view.findViewById(R.id.notification_content).setOnFocusChangeListener(this);
        view.findViewById(R.id.changeUserBtn).setOnClickListener(this);
        view.findViewById(R.id.attendanceHistory).setOnClickListener(this);
        view.findViewById(R.id.addUserBtn).setOnClickListener(this);
        view.findViewById(R.id.addAttendanceBtn).setOnClickListener(this);

        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);

        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.addUserBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUserFragment())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("הוספת משתמש");
                break;

            case R.id.changeUserBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListChangeUser())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("שנה הגדרות משתמש");
                break;

            case R.id.attendanceHistory:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceHistoryFragment())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("היסטוריית טפסי נוכחות");
                break;

            case R.id.addAttendanceBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("הוספת טופס נוכחות");
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


