package com.lipstudio.pnimiyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddUserActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    Spinner userTypeSpinner;
    EditText fname;
    EditText lname;
    EditText password;
    EditText rePassword;
    EditText mail;
    EditText id;
    CheckBox[] grades;
    ArrayList<String> selectedGrades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        fname = findViewById(R.id.fnameRegister);
        lname = findViewById(R.id.lnameRegister);
        id = findViewById(R.id.idRegister);
        password = findViewById(R.id.passwordRegister);
        rePassword = findViewById(R.id.rePasswordRegister);
        mail = findViewById(R.id.mail);
        fname.setOnFocusChangeListener(this);
        lname.setOnFocusChangeListener(this);
        id.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        rePassword.setOnFocusChangeListener(this);
        mail.setOnFocusChangeListener(this);
        grades = new CheckBox[7];
        grades[0] = findViewById(R.id.ז);
        grades[1] = findViewById(R.id.ח);
        grades[2] = findViewById(R.id.ט);
        grades[3] = findViewById(R.id.י);
        grades[4] = findViewById(R.id.יא);
        grades[5] = findViewById(R.id.יב);
        grades[6] = findViewById(R.id.noGrade);
        selectedGrades = new ArrayList<>();

        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.userTypes, R.layout.support_simple_spinner_dropdown_item);
        userTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeAdapter);

        for (int i = 0; i < grades.length; i++) {
            grades[i].getText().toString().replace("שכבה","");
            grades[i].getText().toString().replace("'","");
            grades[i].getText().toString().replace(" ","");
            grades[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedGrades.add(buttonView.getText().toString());
                    }
                    else {
                        for (int i = 0; i < selectedGrades.size(); i++) {
                            if (selectedGrades.get(i).equals(buttonView.getText().toString())) {
                                selectedGrades.remove(i);
                                break;
                            }

                        }
                    }
                }

            });

        }
    }


     public void addUser(View view){

        if(!filterRegister())
            return;
        User user = null;
        String firstName = fname.getText().toString();
        String lastName = lname.getText().toString();
        String pass = password.getText().toString();
        long userId = Long.parseLong(id.getText().toString());
        String email = mail.getText().toString();
        UserHelper userHelper = new UserHelper(this);

        switch (userTypeSpinner.getSelectedItem().toString()){
            case "חניך":
                user = new UserStudent(firstName,lastName,userId,pass,email,selectedGrades);
                break;

            case "מדריך":
                user = new UserEducator(firstName,lastName,userId,pass,email,selectedGrades);
                break;

            case "שינשין":
                user = new UserShinshin(firstName,lastName,userId,pass,email,selectedGrades);
                break;
        }
        userHelper.open();
        userHelper.insertUser(user);
         ArrayList<User> users = userHelper.getAllUsers();
         for (int i =0; i <users.size(); i++){
             Log.i("user",users.get(0).toString());
         }
         Log.e("userToGroup",userHelper.getAllUserToGroup());
        userHelper.close();
        Toast.makeText(this, "המשתמש נוצר בהצלחה!", Toast.LENGTH_LONG).show();

         onBackPressed();

     }


    private boolean filterRegister() {
        if (fname.getText().toString().equals("") || lname.getText().toString().equals("")
                || id.getText().toString().equals("") || password.getText().toString().equals("") ||
                rePassword.getText().toString().equals("") || userTypeSpinner.getSelectedItem().toString().equals("סוג משתמש*")) {

            Toast.makeText(this, "שדות לא תקינים, נסה שוב.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Pattern.compile( "[0-9]" ).matcher( fname.getText().toString()).find() ||
                Pattern.compile( "[0-9]" ).matcher( lname.getText().toString()).find()){
            Toast.makeText(this, "שם או שם משפחה לא יכולים להכיל מספרים", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().toString().length()<10 &&
                !Pattern.compile( "[0-9]" ).matcher( lname.getText().toString()).find()){
            Toast.makeText(this, "סיסמה חייבת להכיל לפחות 10 תווים עם אותיות ומספרים", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().toString().length() != rePassword.getText().toString().length()){
            Toast.makeText(this, "הסיסמאות חייבות להיות תואמות", Toast.LENGTH_SHORT).show();
        }

        if(!selectedGrades.isEmpty() && grades[6].isChecked()){
            Toast.makeText(this, "לא ניתן לבחור שכבה ולסמן 'אין שכבה'", Toast.LENGTH_SHORT).show();
        }

        if(userTypeSpinner.getSelectedItem().toString().equals("חניך") && selectedGrades.size()!=1){
            Toast.makeText(this, "אם סוג המשתמש הוא חניך, חובה לבחור שכבה אחת", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}


