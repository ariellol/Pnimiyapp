package com.lipstudio.pnimiyapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ScheduleFragment extends Fragment implements View.OnClickListener {

    com.applandeo.materialcalendarview.CalendarView calendarView;
    Dialog dialog;
    ImageView editMode;
    ImageView watchMode;
    TextView eventDateDialog;
    boolean editModeBool = false;
    Context context;
    ArrayList<Event> events;
    ArrayList<Event> eventsInDate;
    Event currentEvent;

    int currentEventCount;
    TextView titleTv;
    TextView descriptionTv;
    TextView eventsNumTv;
    ImageView leftArrow;
    ImageView rightArrow;

    LinearLayout dayScheduleLayout;
    MaterialTimePicker timePicker;
    TextView noLuz;
    ImageView openEventDialog;

    Dialog eventInDayDialog;
    EditText eventInDayTitle;
    Button timePick;
    Button addEventInDay;
    String time;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);

        context = getActivity();
        calendarView = view.findViewById(R.id.calendarView);

        eventInDayDialog = new Dialog(context);
        eventInDayDialog.setContentView(R.layout.add_day_event_dialog);
        eventInDayTitle = eventInDayDialog.findViewById(R.id.dayEventTitleDialog);
        timePick = eventInDayDialog.findViewById(R.id.pick_time_dialog);
        addEventInDay = eventInDayDialog.findViewById(R.id.add_event_btn_dialog);

        dayScheduleLayout = view.findViewById(R.id.dayScheduleLayout);
        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        timePicker = builder.build();
        noLuz = view.findViewById(R.id.noLuz);

        openEventDialog = view.findViewById(R.id.open_day_event_dialog);
        openEventDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventInDayDialog.show();
                timePick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker.show(getActivity().getSupportFragmentManager(),"timePicker");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

                        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                time = timePicker.getHour()+ ":" + timePicker.getMinute();
                                Log.e("timePicked",time);
                                try {
                                    Log.e("timeFormatted",timeFormat.parse(time).toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });


        dialog = new Dialog(context);
        dialog.setContentView(R.layout.event_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        watchMode = view.findViewById(R.id.watchMode);
        editMode = view.findViewById(R.id.editMode);
        editMode.setOnClickListener(this);
        watchMode.setOnClickListener(this);
        watchMode.setTag(1);
        editMode.setTag(0);

        ScheduleHelper schedHelper = new ScheduleHelper(context);
        schedHelper.open();
        events = schedHelper.getAllEvents();
        schedHelper.close();

        List<EventDay> eventDays = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date;

        for (int i = 0; i <events.size() ; i++) {
            try {
                Log.e("eventDate",events.get(i).getDate());
                date = dateFormat.parse(events.get(i).getDate());
                Calendar calendarEvent = Calendar.getInstance();
                calendarEvent.setTime(date);
                eventDays.add(new EventDay(calendarEvent,R.drawable.ic_baseline_message_24));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

        }
        calendarView.setEvents(eventDays);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String date = formatter.format(eventDay.getCalendar().getTime());
                Log.e("eventDay",date);

                if (editModeBool) {
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.edit_event_message_dialog);
                    eventDateDialog = dialog.findViewById(R.id.eventDateDialog);
                    eventDateDialog.setText(date);

                    ImageView addEvent = dialog.findViewById(R.id.addEventBtn);
                    final EditText titleEditText = dialog.findViewById(R.id.eventTitleDialog);
                    final EditText descriptionEditText = dialog.findViewById(R.id.eventDescriptionDialog);

                    addEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (descriptionEditText.getText().toString().equals("") && titleEditText.getText().toString().equals("")) {
                                Toast.makeText(context, "עלייך למלא את כל השדות.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ScheduleHelper scheduleHelper = new ScheduleHelper(context);
                            scheduleHelper.open();
                            events.add(scheduleHelper.insertEvent(
                                    new Event(titleEditText.getText().toString(), date, descriptionEditText.getText().toString())
                            ));
                            scheduleHelper.close();
                            Toast.makeText(context, "האירוע נוצר בהצלחה!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    dialog.setContentView(R.layout.event_message_dialog);
                    dialog.setCanceledOnTouchOutside(true);
                    eventDateDialog = dialog.findViewById(R.id.eventDateDialog);
                    eventDateDialog.setText(date);

                    titleTv = dialog.findViewById(R.id.eventTitleDialog);
                    descriptionTv = dialog.findViewById(R.id.eventDescriptionDialog);
                    eventsNumTv = dialog.findViewById(R.id.eventsNumTextView);
                    leftArrow = dialog.findViewById(R.id.left_arrow);
                    rightArrow = dialog.findViewById(R.id.right_arrow);

                    eventsInDate = new ArrayList<>();

                    for (int i = 0; i < events.size(); i++) {
                        if (events.get(i).getDate().equals(date)) {
                            eventsInDate.add(events.get(i));
                        }
                    }
                }

                if (eventsInDate.isEmpty()) {
                    rightArrow.setImageResource(R.drawable.arrow_right_disabled);
                    leftArrow.setImageResource(R.drawable.arrow_left_disabled);
                    descriptionTv.setText("אין אירועים בתאריך זה");
                    dialog.show();
                    return;
                }
                else {
                    titleTv.setText(eventsInDate.get(0).getTitle());
                    descriptionTv.setText(eventsInDate.get(0).getDescription());
                    eventsNumTv.setText("1 מתוך " + eventsInDate.size());
                    currentEvent = eventsInDate.get(0);
                    currentEventCount = 1;
                }

                checkIfHasRight();
                checkIfHasLeft();

                rightArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("rightArrow", "entered");
                        goRight();
                    }
                });


                leftArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("leftArrow", "entered");
                        goLeft();
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    private boolean checkIfHasLeft() {
        checkIfHasRight();
        if (currentEvent.getId() == eventsInDate.get(eventsInDate.size() - 1).getId()) {
            leftArrow.setImageResource(R.drawable.arrow_left_disabled);
            leftArrow.setTag(0);
            return false;
        }
        leftArrow.setTag(1);
        leftArrow.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        return true;
    }

    private void checkLeftOnly() {
        if (currentEvent.getId() == eventsInDate.get(eventsInDate.size() - 1).getId()) {
            leftArrow.setImageResource(R.drawable.arrow_left_disabled);
            leftArrow.setTag(0);
            return;
        }
        leftArrow.setTag(1);
        leftArrow.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
    }

    private boolean checkIfHasRight() {
        checkLeftOnly();
        if (currentEvent.getId() == eventsInDate.get(0).getId()) {
            rightArrow.setImageResource(R.drawable.arrow_right_disabled);
            rightArrow.setTag(0);
            return false;
        }
        rightArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
        rightArrow.setTag(1);
        Log.e("eventInfo", "rightArrow, you made it");
        return true;
    }

    private void goLeft() {
        if (leftArrow.getTag().equals(0))
            return;
        for (int i = 0; i < eventsInDate.size(); i++) {
            if (currentEvent.getId() == eventsInDate.get(i).getId()) {
                currentEventCount++;
                currentEvent = eventsInDate.get(i + 1);
                titleTv.setText(currentEvent.getTitle());
                descriptionTv.setText(currentEvent.getDescription());
                eventsNumTv.setText(currentEventCount + " מתוך " + eventsInDate.size());
                break;
            }
        }
        checkIfHasLeft();
    }

    private void goRight() {
        if (rightArrow.getTag().equals(0))
            return;
        Log.e("eventInfo", currentEvent.toString());
        Log.e("evntInfo", eventsInDate.get(0).toString());
        for (int i = eventsInDate.size() - 1; i > 0; i--) {
            if (currentEvent.getId() == eventsInDate.get(i).getId()) {
                currentEventCount--;
                currentEvent = eventsInDate.get(i - 1);
                titleTv.setText(currentEvent.getTitle());
                descriptionTv.setText(currentEvent.getDescription());
                eventsNumTv.setText(currentEventCount + " מתוך " + eventsInDate.size());
                break;
            }
        }
        checkIfHasRight();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null) {
            return;
        }
        if (v.getTag().equals(0) && v.getId() == editMode.getId()) {
            editMode.setTag(1);
            watchMode.setTag(0);
            editMode.setImageResource(R.drawable.edit_active);
            watchMode.setImageResource(R.drawable.eye_not_selected);
            editModeBool = true;
            noLuz.setVisibility(View.GONE);
            openEventDialog.setVisibility(View.VISIBLE);
            //addDayEventViews();
        } else if (v.getTag().equals(0) && v.getId() == watchMode.getId()) {
            editMode.setTag(0);
            watchMode.setTag(1);
            editMode.setImageResource(R.drawable.ic_mode_edit_black_24dp);
            watchMode.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
            noLuz.setVisibility(View.VISIBLE);
            openEventDialog.setVisibility(View.GONE);
            editModeBool = false;
        }
    }

    public void addDayEventViews(){
        if(!editModeBool)
            return;

    }

    public int dpToPx(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }
}
