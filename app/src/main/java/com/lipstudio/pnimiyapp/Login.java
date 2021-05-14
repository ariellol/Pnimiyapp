package com.lipstudio.pnimiyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.chooser.ChooserTargetService;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;
import java.util.Set;
public class Login extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

    EditText loginId;
    EditText loginPassword;
    ImageView showPassword;
    Button loginButton;
    UserHelper userHelper;
    SharedPreferences userSp;
    TextView forgotPassword;
    Context context;
    public static final int INVISIBLE_INPUT = InputType.TYPE_CLASS_TEXT+InputType.TYPE_TEXT_VARIATION_PASSWORD;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

        context = this;

        loginId = findViewById(R.id.login_id);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnClickListener(this);
        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(this);
        userHelper = new UserHelper(context);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.forgot_password){
            startActivity(new Intent(this, SendCodeRestorePasswordActivity.class));
        }

        else if (v.getId() == R.id.login_button){
            if(loginId.getText().toString().equals("") || loginPassword.getText().toString().equals("")){
                Toast.makeText(this, "תעודת הזהות או הסיסמה אינם תקינים", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                String password = loginPassword.getText().toString();
                long id = Long.parseLong(loginId.getText().toString());
                userHelper.open();
                User user = userHelper.loginUser(id,password);
                userHelper.close();
                if (user == null){
                    Toast.makeText(this, "תעודת הזהות או הסיסמה אינם תקינים", Toast.LENGTH_SHORT).show();
                    return;
                }

                    userSp = this.getSharedPreferences("userSharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor userSpEditor = userSp.edit();
                    userSpEditor.putLong("userId",id);
                    userSpEditor.putString("userName", user.getFirstName() + " " + user.getLastName());
                    userSpEditor.putString("userPassword",password);
                    if (user instanceof UserEducator) {
                        userSpEditor.putBoolean("userEducator", true);
                        userSpEditor.putBoolean("editContent", true);
                        userSpEditor.putBoolean("editSchedule", true);
                        userSpEditor.putStringSet("groups",new HashSet<>(((UserEducator) user).getGroup()));
                    }
                    else if (user instanceof UserShinshin){
                        userSpEditor.putBoolean("editContent", true);
                        userSpEditor.putBoolean("editSchedule", true);
                        userSpEditor.putString("group", user.getGroup().get(0));
                    }

                    else {
                        userSpEditor.putBoolean("userEducator", false);
                        userSpEditor.putBoolean("editContent", ((UserStudent) user).isEditContent());
                        userSpEditor.putBoolean("editSchedule", ((UserStudent) user).isEditSchedule());
                        userSpEditor.putString("group", user.getGroup().get(0));
                    }
                    userSpEditor.putBoolean("init",true);
                    userSpEditor.apply();
                Intent HomeIntent = new Intent(this, Home.class);
                startActivity(HomeIntent);
            }
        }
        else if(v.getId() == R.id.showPassword){
                if(loginPassword.getInputType() == INVISIBLE_INPUT) {
                    loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassword.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                }
                else {
                    loginPassword.setInputType(INVISIBLE_INPUT);
                    showPassword.setImageResource(R.drawable.eye_not_selected);
                }
        }
    }

    @Override
    public void onBackPressed() { }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }
}
