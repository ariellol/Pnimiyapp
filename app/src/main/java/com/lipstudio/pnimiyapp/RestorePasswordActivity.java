package com.lipstudio.pnimiyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RestorePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText newPassword;
    EditText newRePassword;
    Button setNewPassword;
    long userId;
    UserHelper userHelper;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        userId = getIntent().getExtras().getLong("userId");
        Log.e("userID",userId+"");
        newPassword = findViewById(R.id.newPassword);
        newRePassword = findViewById(R.id.newRePassword);
        setNewPassword = findViewById(R.id.setNewPassword);
        setNewPassword.setOnClickListener(this);
        userHelper = new UserHelper(this);
        userHelper.open();
        user = userHelper.getUserById(userId);
        userHelper.close();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setNewPassword){
            if (user == null){
                Toast.makeText(this, "משהו לא תקין כעת, נסה שוב", Toast.LENGTH_SHORT).show();
            }

            else {

                if (newPassword.getText().toString().isEmpty() || newRePassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "חובה למלא את שתי השדות", Toast.LENGTH_SHORT).show();
                } else if (!newRePassword.getText().toString().equals(newPassword.getText().toString())) {
                    Toast.makeText(this, "השדות אינם תואמים", Toast.LENGTH_SHORT).show();

                } else {
                    User newPasswordUser;
                    if (user instanceof UserEducator) {
                        newPasswordUser = new UserEducator(user.getFirstName(), user.getLastName(), user.getId()
                                , newPassword.getText().toString(), user.getGroup(), user.getPhoneNumber());
                    } else if (user instanceof UserShinshin) {
                        newPasswordUser = new UserShinshin(user.getFirstName(), user.getLastName(), user.getId()
                                , newPassword.getText().toString(), user.getGroup(), user.getPhoneNumber());
                    } else {
                        newPasswordUser = new UserStudent(user.getFirstName(), user.getLastName(), user.getId()
                                , newPassword.getText().toString(), user.getGroup(), user.getPhoneNumber());
                    }
                    userHelper.open();
                    userHelper.updateUser(newPasswordUser);
                    userHelper.close();
                    startActivity(new Intent(this, Home.class));
                }
            }
        }
    }
}