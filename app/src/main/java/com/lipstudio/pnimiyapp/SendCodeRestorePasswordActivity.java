package com.lipstudio.pnimiyapp;

import android.Manifest;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SendCodeRestorePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText userIdEditText;
    Button sendCode;
    EditText codeEditText;
    Button getPassword;
    String generatedCode;
    long userId;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        sharedPreferences = getSharedPreferences("userSharedPreferences", Context.MODE_PRIVATE);
        generatedCode = sharedPreferences.getString("resetCode","");
        userId = sharedPreferences.getLong("userId",0);
        Log.e("onCreate","called!");
        userIdEditText = findViewById(R.id.id_to_send_code);
        if (userId != 0) {
            userIdEditText.setText(String.valueOf(userId));
        }
        sendCode = findViewById(R.id.sendCode);
        sendCode.setOnClickListener(this);
        codeEditText = findViewById(R.id.code_sent_edit_text);
        getPassword = findViewById(R.id.getPassword);
        getPassword.setOnClickListener(this);

    }

    private String generate4DigitCode(){
        return ""+((int)(Math.random()*9000)+1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendCode){
            if (userIdEditText.getText().equals("")){
                Toast.makeText(this, "חובה למלא תעודת זהות.", Toast.LENGTH_SHORT).show();
            }
            else {
                UserHelper userHelper = new UserHelper(this);
                userHelper.open();
                String phoneNumber = userHelper.getUserPhoneById(Long.parseLong(userIdEditText.getText().toString()));
                userHelper.close();
                if (phoneNumber.equals("")) {
                    Toast.makeText(this, "משתמש בעל תעודת זהות זו לא קיים.", Toast.LENGTH_SHORT).show();
                }
                else{
                    generatedCode = generate4DigitCode();
                    userId = Long.parseLong(userIdEditText.getText().toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("resetCode",generatedCode);
                    editor.putLong("userId",userId);
                    editor.commit();
                    Intent intent=new Intent(getApplicationContext(),SendCodeRestorePasswordActivity.class);
                    PendingIntent pendingIntent =PendingIntent.getActivity(getApplicationContext(), 1, intent,0);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber,null,"הקוד לשחזור הסיסמה הוא: " + generatedCode,pendingIntent,null);

                }
            }
        }

        else if(v.getId() == R.id.getPassword){

            if (codeEditText == null || generatedCode.isEmpty()){
                Toast.makeText(this, "לא ניתן לשחזר סיסמה.", Toast.LENGTH_SHORT).show();
            }
            else if(codeEditText.getText().toString().equals(generatedCode)){
                Intent intent = new Intent(this,RestorePasswordActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "הקוד אינו תואם.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
