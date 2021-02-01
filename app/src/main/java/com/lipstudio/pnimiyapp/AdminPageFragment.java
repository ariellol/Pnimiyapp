package com.lipstudio.pnimiyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminPageFragment extends Fragment implements View.OnClickListener{


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_page,container,false);
        Button addUserBtn = view.findViewById(R.id.addUserBtn);
        addUserBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(),AddUserActivity.class));
    }
}
