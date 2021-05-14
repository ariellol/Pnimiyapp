package com.lipstudio.pnimiyapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.DrmInitData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class AdminPageFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    TextView toolbarTitle;
    Toolbar toolbar;

    TableLayout tableLayout;
    ArrayList<Idea> ideas;
    ArrayList<OutRequest> outRequests;
    ArrayList<LoanRequest> loanRequests;
    ArrayList<Request> allRequests;
    RequestHelper requestHelper;

    Context context;
    UserHelper userHelper;

    Dialog showRequestDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        requestHelper = new RequestHelper(context);
        requestHelper.open();
        ideas = requestHelper.getAllIdeas();
        outRequests = requestHelper.getAllOutRequests();
        loanRequests = requestHelper.getAllLoanRequests();
        requestHelper.close();
        userHelper = new UserHelper(context);
        allRequests = new ArrayList<>();
        allRequests.addAll(ideas);
        allRequests.addAll(outRequests);
        allRequests.addAll(loanRequests);
        orderRequestsByDate();
        userHelper.open();

        for (int i =0; i<allRequests.size(); i++){
            allRequests.get(i).setTag(i);
            allRequests.get(i).setUserName(userHelper.getUserNameById(allRequests.get(i).getUserId()));
        }
    }

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

        tableLayout = view.findViewById(R.id.requestsTable);

        toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbar = getActivity().findViewById(R.id.toolbar);

        createTableRows();
        showRequestDialog = new Dialog(context);
        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.addUserBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUserFragment())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("הוספת משתמש");
                toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case R.id.changeUserBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListChangeUser())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("שנה הגדרות משתמש");
                toolbar.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                break;

            case R.id.attendanceHistory:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceHistoryFragment())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("היסטוריית טפסי נוכחות");
                toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
                break;

            case R.id.addAttendanceBtn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment())
                        .addToBackStack(null).commit();
                toolbarTitle.setText("הוספת טופס נוכחות");
                toolbar.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                break;
        }

        if (v instanceof TableRow) {
            long position = (int) v.getTag();

            showRequestDialog.setContentView(R.layout.request_custom_layout);
            showRequestDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView requestDate = showRequestDialog.findViewById(R.id.request_date);
            requestDate.setText(allRequests.get((int) position).getDate());

            TextView requestUserName = showRequestDialog.findViewById(R.id.requestName);
            requestUserName.setText(allRequests.get((int) position).getUserName());

            TextView requestTitle = showRequestDialog.findViewById(R.id.requestTitle);
            requestTitle.setText(allRequests.get((int) position).getTitle());

            TextView requestDescription = showRequestDialog.findViewById(R.id.requestDescription);
            requestDescription.setText(allRequests.get((int) position).getDescription());

            if (allRequests.get((int) position) instanceof LoanRequest){
                TextView loanAmount = showRequestDialog.findViewById(R.id.moneyAmountTextView);
                loanAmount.setText(loanAmount.getText().toString()+((LoanRequest) allRequests.get((int) position)).getMoneyAmount());
                loanAmount.setVisibility(View.VISIBLE);
            }
            else if(allRequests.get((int) position) instanceof OutRequest){
                TextView fromDateTextView = showRequestDialog.findViewById(R.id.fromDateTextView);
                TextView toDateTextView = showRequestDialog.findViewById(R.id.toDateTextView);
                fromDateTextView.setText(fromDateTextView.getText().toString() + ((OutRequest) allRequests.get((int) position)).getFromDate());
                toDateTextView.setText(toDateTextView.getText().toString() + ((OutRequest) allRequests.get((int) position)).getToDate());
                LinearLayout datesLayout = showRequestDialog.findViewById(R.id.datesRequest);
                datesLayout.setVisibility(View.VISIBLE);
            }

            showRequestDialog.show();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }

    public void createTableRows(){
        for (int i = 0; i < allRequests.size() ; i++) {
            TableRow tableRow = new TableRow(context);
            TextView userNameTextView = new TextView(context);
            TextView typeTextView = new TextView(context);
            TextView dateTextView = new TextView(context);

            tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (i % 2 == 0)
                tableRow.setBackgroundResource(R.color.almostWhiteGreen);
            else
                tableRow.setBackgroundResource(R.color.weirdLightGreen);

            tableRow.setGravity(Gravity.RIGHT);
            tableRow.setTextDirection(View.TEXT_DIRECTION_RTL);
            tableRow.setTag(allRequests.get(i).getTag());

            if (allRequests.get(i) instanceof Idea)
                typeTextView.setText("הצעה");
            else if(allRequests.get(i) instanceof LoanRequest)
                typeTextView.setText("הלוואה");
            else
                typeTextView.setText("יציאה");

            typeTextView.setWidth(dpToPx(110));
            typeTextView.setPadding(0,0,10,0);

            userNameTextView.setText(allRequests.get(i).getUserName());
            userNameTextView.setWidth(dpToPx(140));

            dateTextView.setText(allRequests.get(i).getDate());
            dateTextView.setWidth(dpToPx(110));

            tableRow.addView(typeTextView);
            tableRow.addView(userNameTextView);
            tableRow.addView(dateTextView);
            tableRow.setOnClickListener(this);

            tableLayout.addView(tableRow);
        }
    }

    private int dpToPx(int dps){
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void orderRequestsByDate() {
        allRequests.sort(new Comparator<Request>() {
            @Override
            public int compare(Request o1, Request o2) {
                try {
                    return new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDate())
                            .compareTo(new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }
}


