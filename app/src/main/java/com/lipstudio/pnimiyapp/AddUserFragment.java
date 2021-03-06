package com.lipstudio.pnimiyapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddUserFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener{

    Spinner userTypeSpinner;
    EditText fname;
    EditText lname;
    EditText password;
    EditText rePassword;
    EditText id;
    CheckBox[] grades;
    ArrayList<String> selectedGrades;
    Context context;
    Button pickContact;

    public static final int REQUEST_CODE = 0;
    public static final int OK_CODE = 1;
    public static final int NOT_OK_CODE = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_user,container,false);
        context = getActivity();
        fname = view.findViewById(R.id.fnameRegister);
        lname = view.findViewById(R.id.lnameRegister);
        id = view.findViewById(R.id.idRegister);
        password = view.findViewById(R.id.passwordRegister);
        rePassword = view.findViewById(R.id.rePasswordRegister);
        pickContact = view.findViewById(R.id.pick_phone_number);
        pickContact.setOnClickListener(this);
        view.findViewById(R.id.addUser).setOnClickListener(this);
        fname.setOnFocusChangeListener(this);
        lname.setOnFocusChangeListener(this);
        id.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        rePassword.setOnFocusChangeListener(this);
        grades = new CheckBox[7];
        grades[0] = view.findViewById(R.id.ז);
        grades[1] = view.findViewById(R.id.ח);
        grades[2] = view.findViewById(R.id.ט);
        grades[3] = view.findViewById(R.id.י);
        grades[4] = view.findViewById(R.id.יא);
        grades[5] = view.findViewById(R.id.יב);
        grades[6] = view.findViewById(R.id.noGrade);
        selectedGrades = new ArrayList<>();

        userTypeSpinner = view.findViewById(R.id.userTypeSpinner);
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(context,
                R.array.userTypes, R.layout.support_simple_spinner_dropdown_item);
        userTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeAdapter);
        userTypeSpinner.setOnFocusChangeListener(this);

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
        return view;
    }


    private boolean filterRegister() {
        if (fname.getText().toString().equals("") || lname.getText().toString().equals("")
                || id.getText().toString().equals("") || password.getText().toString().equals("") ||
                rePassword.getText().toString().equals("") || userTypeSpinner.getSelectedItem().toString().equals("סוג משתמש*") ||
                pickContact.getText().toString().equals("")) {

            Toast.makeText(context, "שדות לא תקינים, נסה שוב.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Pattern.compile( "[0-9]" ).matcher( fname.getText().toString()).find() ||
                Pattern.compile( "[0-9]" ).matcher( lname.getText().toString()).find()){
            Toast.makeText(context, "שם או שם משפחה לא יכולים להכיל מספרים", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().toString().length()<10 &&
                !Pattern.compile( "[0-9]" ).matcher( lname.getText().toString()).find()){
            Toast.makeText(context, "סיסמה חייבת להכיל לפחות 10 תווים עם אותיות ומספרים", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().toString().length() != rePassword.getText().toString().length()){
            Toast.makeText(context, "הסיסמאות חייבות להיות תואמות", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!selectedGrades.isEmpty() && grades[6].isChecked()){
            Toast.makeText(context, "לא ניתן לבחור שכבה ולסמן 'אין שכבה'", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(userTypeSpinner.getSelectedItem().toString().equals("חניך") && selectedGrades.size()!=1){
            Toast.makeText(context, "אם סוג המשתמש הוא חניך, חובה לבחור שכבה אחת", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pickContact.getText().toString().startsWith("+972")){
            pickContact.setText(pickContact.getText().toString().replace("+972","0"));
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
    public void onClick(View v) {


        if (v.getId() == R.id.pick_phone_number){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CODE);
            Intent contactsIntent = new Intent(Intent.ACTION_PICK,ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(contactsIntent,1);
            return;
        }
        if(!filterRegister())
            return;
        User user = null;
        String firstName = fname.getText().toString();
        String lastName = lname.getText().toString();
        String pass = password.getText().toString();
        long userId = Long.parseLong(id.getText().toString());
        String phoneNumber = pickContact.getText().toString();
        UserHelper userHelper = new UserHelper(context);

        switch (userTypeSpinner.getSelectedItem().toString()){
            case "חניך":
                user = new UserStudent(firstName,lastName,userId,pass,selectedGrades,phoneNumber);
                break;

            case "מדריך":
                user = new UserEducator(firstName,lastName,userId,pass,selectedGrades,phoneNumber);
                break;

            case "שינשין":
                user = new UserShinshin(firstName,lastName,userId,pass,selectedGrades,phoneNumber);
                break;
        }
        userHelper.open();
        userHelper.insertUser(user);
        ArrayList<User> users = userHelper.getAllUsers();
        for (int i =0; i <users.size(); i++){
            Log.i("user",users.get(i).toString());
        }
        Log.e("userToGroup",userHelper.getAllUserToGroup());
        userHelper.close();
        Toast.makeText(context, "המשתמש נוצר בהצלחה!", Toast.LENGTH_LONG).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            Log.e("allFine","yes");
        Uri contactUri = null;
        if (data != null) {
            contactUri = data.getData();
        }
        else{
            return;
        }
        Cursor cursor = context.getContentResolver().query(contactUri,null,null,null,null);
            cursor.moveToFirst();
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(phoneNumber.startsWith("+972"))
                phoneNumber = phoneNumber.replace("+972","0");

            if(phoneNumber.contains("-"))
                phoneNumber = phoneNumber.replace("-","");

            if(phoneNumber.contains(" "))
            phoneNumber = phoneNumber.replace(" ","");

            pickContact.setText(phoneNumber);
            cursor.close();

    }
}
