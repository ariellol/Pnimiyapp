package com.lipstudio.pnimiyapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class ChangeUserSettings extends Fragment implements View.OnFocusChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Context context;
    UserStudent student;
    UserStudent newStudent;
    EditText fnameTv;
    EditText lnameTv;
    EditText passwordTv;
    Switch editContentSw;
    Switch editScheduleSw;
    ArrayList<String> newGroups;
    RadioButton[] grades;
    TextView deleteUserTv;
    Dialog deleteUserDialog;
    TextView deleteName;
    Button deleteUser;
    Button cancel;
    UserHelper helper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_settings_page,container,false);
        student = (UserStudent) getArguments().getSerializable("student");
        context = getActivity();
        TextView settingsTitle = view.findViewById(R.id.changeSettingsTitle);
        TextView permissionTitle = view.findViewById(R.id.changePermissionTitle);
        settingsTitle.setText("שינוי הגדרות ל" + student.getFirstName() + " " + student.getLastName());
        permissionTitle.setText("שינוי הרשאות ל" + student.getFirstName() + " " + student.getLastName());

        helper = new UserHelper(context);
        deleteUserDialog = new Dialog(context);
        deleteUserDialog.setContentView(R.layout.delete_user_dialog);
        deleteName = deleteUserDialog.findViewById(R.id.deleteName);
        deleteUser = deleteUserDialog.findViewById(R.id.deleteUserFr);
        cancel = deleteUserDialog.findViewById(R.id.cancelDelete);

        deleteUserTv = view.findViewById(R.id.deleteUser);
        deleteUserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteName.setText("האם אתה בטוח שאתה רוצה למחוק את " + student.getFirstName() + " " + student.getLastName());
                deleteUserDialog.show();
                deleteUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.open();
                        helper.deleteUser(student);
                        helper.close();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AdminPageFragment())
                                .addToBackStack(null).commit();
                        Toast.makeText(context, "המשתמש נמחק בהצלחה.", Toast.LENGTH_SHORT).show();
                        deleteUserDialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteUserDialog.dismiss();
                    }
                });
            }
        });
        fnameTv = view.findViewById(R.id.fnameSettnings);
        fnameTv.setOnFocusChangeListener(this);
        fnameTv.setText(student.getFirstName());
        lnameTv = view.findViewById(R.id.lnameSettings);
        lnameTv.setOnFocusChangeListener(this);
        lnameTv.setText(student.getLastName());
        passwordTv = view.findViewById(R.id.passwordSettings);
        passwordTv.setOnFocusChangeListener(this);
        passwordTv.setText(student.getPassword());

        Button updateUser = view.findViewById(R.id.updateUser);
        updateUser.setOnClickListener(this);

        grades = new RadioButton[6];
        grades[0] = view.findViewById(R.id.ז);
        grades[1] = view.findViewById(R.id.ח);
        grades[2] = view.findViewById(R.id.ט);
        grades[3] = view.findViewById(R.id.י);
        grades[4] = view.findViewById(R.id.יא);
        grades[5] = view.findViewById(R.id.יב);
        for (int i = 0; i < grades.length; i++) {
            grades[i].setOnCheckedChangeListener(this);
            grades[i].setTag(i);
        }

        if (student.getGroup().get(0).contains("ז"))
            grades[0].setChecked(true);

        else if (student.getGroup().get(0).contains("ח"))
            grades[1].setChecked(true);


        else if(student.getGroup().get(0).contains("ט"))
            grades[2].setChecked(true);

        else if(student.getGroup().get(0).contains("יא"))
            grades[4].setChecked(true);

        else if(student.getGroup().get(0).contains("יב"))
            grades[5].setChecked(true);

        else
            grades[3].setChecked(true);


        newGroups = new ArrayList<>();

        editContentSw = view.findViewById(R.id.editContent);
        editScheduleSw = view.findViewById(R.id.editSchedule);
        if (student.isEditContent()){
            editContentSw.setChecked(true);
        }
        if (student.isEditSchedule()){
            editScheduleSw.setChecked(true);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        for (int i = 0; i < 6 ; i++) {
            if(grades[i].isChecked())
                newGroups.add(grades[i].getText().toString());
        }
        newStudent = new UserStudent(fnameTv.getText().toString(),lnameTv.getText().toString(),student.getId(),
                    passwordTv.getText().toString(),newGroups,editContentSw.isChecked(),editScheduleSw.isChecked());

        helper.open();
        helper.updateUser(newStudent);
        helper.close();
        Toast.makeText(context, "המשתמש עודכן בהצלחה!", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AdminPageFragment())
                .addToBackStack(null).commit();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int index = (int)buttonView.getTag();
        if(isChecked){
            grades[index].setChecked(true);
            for (int i =0; i<6; i++) {
                if (i != index)
                    grades[i].setChecked(false);
            }
        }
    }
}
