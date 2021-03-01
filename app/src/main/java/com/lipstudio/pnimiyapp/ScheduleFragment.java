package com.lipstudio.pnimiyapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.UnicodeSetIterator;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ScheduleFragment extends Fragment implements CalendarView.OnDateChangeListener, View.OnClickListener {


    CalendarView calendarView;
    Dialog dialog;
    ImageView editMode;
    ImageView watchMode;
    TextView eventDateDialog;
    boolean editModeBool = false;
    Context context;
    RelativeLayout layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment,container,false);

        context = getActivity();
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.event_message_dialog);
        layout = (RelativeLayout) inflater.inflate(R.layout.event_message_dialog,container,false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        eventDateDialog = dialog.findViewById(R.id.eventDateDialog);

        watchMode = view.findViewById(R.id.watchMode);
        editMode = view.findViewById(R.id.editMode);
        editMode.setOnClickListener(this);
        watchMode.setOnClickListener(this);
        watchMode.setTag(1);
        editMode.setTag(0);
        return view;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        eventDateDialog.setText(dayOfMonth+"."+month+"."+year);
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewParams.setMargins(0,dpToPx(40),0,0);
        if(editModeBool){

            EditText eventTitleDialogEt = new EditText(context);
            eventTitleDialogEt.setLayoutParams(viewParams);
            eventTitleDialogEt.setTextColor(Color.BLACK);
            eventTitleDialogEt.setTextSize(18);
            eventTitleDialogEt.setGravity(Gravity.CENTER);
            eventTitleDialogEt.setHint("כותרת אירוע");

            EditText descriptionDialogEt = new EditText(context);
            descriptionDialogEt.setLayoutParams(viewParams);
            descriptionDialogEt.setTextColor(Color.BLACK);
            descriptionDialogEt.setTextSize(18);
            descriptionDialogEt.setGravity(Gravity.CENTER);
            descriptionDialogEt.setHint("תיאור אירוע");

            layout.addView(eventTitleDialogEt);
            layout.addView(descriptionDialogEt);
            dialog.setContentView(layout);
        }

         else{
            TextView descriptionDialogTv = new TextView(context);
            descriptionDialogTv.setLayoutParams(viewParams);
            TextView eventTitleDialogTv = new TextView(context);
            eventTitleDialogTv.setLayoutParams(viewParams);
        }
         dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(0) && v.getId() == editMode.getId()){
            editMode.setTag(1);
            watchMode.setTag(0);
            editMode.setImageResource(R.drawable.edit_active);
            watchMode.setImageResource(R.drawable.eye_not_selected);
            editModeBool = true;
        }
        else if(v.getTag().equals(0) && v.getId() == watchMode.getId()){
            editMode.setTag(0);
            watchMode.setTag(1);
            editMode.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            watchMode.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
            editModeBool = false;
        }
    }

    private int dpToPx(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }
}
