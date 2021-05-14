package com.lipstudio.pnimiyapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.xpath.XPathFunctionResolver;

public class RequestsFragment extends Fragment implements View.OnClickListener {

    SharedPreferences userPref;
    RequestHelper requestHelper;
    Context context;
    RadioButton requestOutButton;
    RadioButton requestLoanButton;
    EditText titleEditText;

    LinearLayout requestOutLayout;
    Button fromDateButton;
    Button toDateButton;

    LinearLayout requestLoanLayout;
    EditText moneyAmountEditText;

    EditText requestReason;
    Button sendRequestButton;

    boolean isRequestLoan = false;

    DatePickerDialog datePicker;
    SimpleDateFormat dateFormat;
    String fromDate;
    String toDate;
    Date tempDate;
    String currentDate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.requests_fragment,container,false);
        context = getActivity();
        userPref = context.getSharedPreferences("userSharedPreferences",Context.MODE_PRIVATE);
        requestHelper = new RequestHelper(context);

        titleEditText = parent.findViewById(R.id.requestTopic);
        requestOutButton = parent.findViewById(R.id.request_out);
        requestLoanButton = parent.findViewById(R.id.requestLoan);
        requestLoanButton.setOnClickListener(this);
        requestOutButton.setOnClickListener(this);

        requestLoanLayout = parent.findViewById(R.id.layout_loan_request);
        moneyAmountEditText = parent.findViewById(R.id.moneyAmount);

        requestOutLayout = parent.findViewById(R.id.layout_out_request);
        fromDateButton = parent.findViewById(R.id.fromDateButton);
        toDateButton = parent.findViewById(R.id.toDateButton);
        fromDateButton.setOnClickListener(this);
        toDateButton.setOnClickListener(this);

        requestReason = parent.findViewById(R.id.requestDescriptionEditText);
        sendRequestButton = parent.findViewById(R.id.sendRequest);
        sendRequestButton.setOnClickListener(this);

        datePicker = new DatePickerDialog(context);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = dateFormat.format(Calendar.getInstance().getTime());
        return parent;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.request_out){
            requestLoanLayout.setVisibility(View.GONE);
            requestOutLayout.setVisibility(View.VISIBLE);
            titleEditText.setText("");
            requestReason.setText("");
            isRequestLoan = false;
        }
        else if(v.getId() == R.id.requestLoan){
            requestOutLayout.setVisibility(View.GONE);
            requestLoanLayout.setVisibility(View.VISIBLE);
            titleEditText.setText("");
            requestReason.setText("");
            fromDateButton.setText("");
            toDateButton.setText("");
            fromDate = "";
            toDate = "";
            isRequestLoan = true;
        }

        else if(v.getId() == R.id.fromDateButton){
            datePicker.show();
            datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    fromDate = dayOfMonth +"/" + month + "/" + year;
                    try {
                         tempDate = dateFormat.parse(fromDate);
                        Log.e("fromDatePicked",dateFormat.format(tempDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fromDateButton.setText(fromDate);
                }
            });
        }
        else if(v.getId() == R.id.toDateButton){
            datePicker.show();
            datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    toDate = dayOfMonth +"/" + month + "/" + year;
                    try {
                        tempDate = dateFormat.parse(toDate);
                        Log.e("toDatePicked",dateFormat.format(tempDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    toDateButton.setText(toDate);
                }
            });
        }

        else{

            if(titleEditText == null || requestReason == null) {
                Toast.makeText(context, "חובה למלא את כל השדות.", Toast.LENGTH_SHORT).show();
                return;
            }

            else if(isRequestLoan){
                if(moneyAmountEditText == null) {
                    Toast.makeText(context, "חובה למלא את כל השדות.", Toast.LENGTH_SHORT).show();
                    return;
                }

                else{
                    LoanRequest loanRequest = new LoanRequest(userPref.getLong("userId",0),currentDate,
                            titleEditText.getText().toString(),requestReason.getText().toString(),
                            Integer.parseInt(moneyAmountEditText.getText().toString()));


                    requestHelper.open();
                    requestHelper.insertLoanRequest(loanRequest);
                    requestHelper.close();

                }
            }

            else{

                if(toDate == null|| fromDate == null){
                    Toast.makeText(context, "חובה למלא את כל השדות.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    try {
                        if(dateFormat.parse(fromDate).after(dateFormat.parse(toDate))){
                            Toast.makeText(context, "שדות התאריכים אינם תקינים.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    OutRequest outRequest = new OutRequest(userPref.getLong("userId",0),currentDate,
                            titleEditText.getText().toString(), requestReason.getText().toString(),fromDate,toDate);
                    requestHelper.open();
                    requestHelper.insertOutRequest(outRequest);
                    requestHelper.close();
                }
            }

            Toast.makeText(context, "בקשתך נשלחה בהצלחה!", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, new HomeFragment());
            transaction.addToBackStack(null);
            ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText(getResources().getText(R.string.app_name));
            getActivity().findViewById(R.id.toolbar).setBackgroundResource(R.color.primaryColorGreen);
            transaction.commit();
        }

    }

}
