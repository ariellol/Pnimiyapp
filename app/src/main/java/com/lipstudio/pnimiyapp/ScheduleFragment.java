package com.lipstudio.pnimiyapp;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ScheduleFragment extends Fragment {

    BottomNavigationView bottomNav;
    boolean isEditDaySchedule = false;
    int deleteNum = 0;
    Menu bottomMenu;
    View view;

    com.applandeo.materialcalendarview.CalendarView calendarView;
    Dialog dialog;
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

    TextView noLuz;
    ImageView openEventDialog;
    MaterialTimePicker timePicker;
    Dialog eventInDayDialog;
    EditText eventInDayTitle;
    Button timePick;
    Button addEventInDay;
    String time;
    TextView timePicked;

    ArrayList<DayEvent> dayEvents;
    DayEventAdapter dayEventAdapter;
    ListView dayEventListView;
    LinearLayout.LayoutParams containerParams;
    ArrayList<DayEvent> selectedDayEvents;

    SimpleDateFormat formatter;
    ScheduleHelper schedHelper;
    Menu menu;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(Calendar.getInstance().getTime());
        context = getActivity();

        schedHelper = new ScheduleHelper(context);
        schedHelper.open();
        events = schedHelper.getAllEvents();
        dayEvents = schedHelper.getEventsByDate(currentDate);
        schedHelper.close();
        orderScheduleByTime();
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.schedule_fragment, container, false);

        bottomNav = view.findViewById(R.id.scheduleBottomNav);
        bottomNav.setItemIconTintList(null);
        bottomNav.setSelected(false);
        bottomMenu = bottomNav.getMenu();
        containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout listViewLayout = view.findViewById(R.id.scheduleContainer);
        listViewLayout.setLayoutParams(containerParams);

        dayEventAdapter = new DayEventAdapter(context, 0, dayEvents);
        dayEventListView = view.findViewById(R.id.dayScheduleListView);
        dayEventListView.setAdapter(dayEventAdapter);
        dayEventAdapter.notifyDataSetChanged();

        eventInDayDialog = new Dialog(context);
        eventInDayDialog.setContentView(R.layout.add_day_event_dialog);
        eventInDayTitle = eventInDayDialog.findViewById(R.id.dayEventTitleDialog);
        timePick = eventInDayDialog.findViewById(R.id.pick_time_dialog);
        addEventInDay = eventInDayDialog.findViewById(R.id.add_event_btn_dialog);
        timePicked = eventInDayDialog.findViewById(R.id.timePicked);
        openEventDialog = view.findViewById(R.id.open_day_event_dialog);


        resizeListView();
        noLuz = view.findViewById(R.id.noLuz);
        if (dayEvents.size() > 0) {
            noLuz.setVisibility(View.GONE);
        }

        openEventDialog.setOnClickListener(v -> {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(0).setMinute(0).setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);

            eventInDayDialog.show();

            timePick.setOnClickListener(v1 -> {
                timePicker = builder.build();
                timePicker.show(getActivity().getSupportFragmentManager(), null);
                timePicker.addOnPositiveButtonClickListener(v2 -> {
                    time = timePicker.getHour() + ":" + timePicker.getMinute();
                    try {
                        Date tempDate = timeFormat.parse(time);
                        time = timeFormat.format(tempDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    timePicked.setText("השעה שנבחרה " + "\n" + time);
                });
            });
            eventInDayTitle.setText(eventInDayTitle.getText().toString());
            addEventInDay.setOnClickListener(v12 -> {
                        try {
                            if (time.equals("") && eventInDayTitle.getText().toString().equals("")) {
                                Toast.makeText(context, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
                            } else {
                                DayEvent currentEvent = new DayEvent(eventInDayTitle.getText().toString(), time);
                                ScheduleHelper dayEventHelper = new ScheduleHelper(context);

                                    dayEventHelper.open();
                                    currentEvent = dayEventHelper.insertDayEvent(currentEvent);
                                    dayEventHelper.close();
                                    dayEvents.add(currentEvent);

                                dayEventAdapter.notifyDataSetChanged();
                                resizeListView();
                                orderScheduleByTime();
                                eventInDayTitle.setText("");
                                time = "";
                                timePicked.setText("");
                                eventInDayDialog.dismiss();
                            }
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }

            );
        });

        selectedDayEvents = new ArrayList<>();

        dayEventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(!editModeBool){
                    return false;
                }

                bottomNav.setVisibility(View.VISIBLE);
                isEditDaySchedule = true;

                if (view.getTag() != null) {
                    view.setBackground(null);
                    view.setTag(null);
                    deleteNum--;
                    selectedDayEvents.remove(dayEvents.get(position));

                    if (deleteNum == 0) {
                        bottomNav.setVisibility(View.GONE);
                        isEditDaySchedule = false;
                    }
                }
                    else {
                        selectedDayEvents.add(dayEvents.get(position));
                        view.setTag("editMode");
                        deleteNum++;
                        view.setBackgroundColor(getResources().getColor(R.color.superLightCyan));
                    }

                return true;
            }
        });

        dayEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditDaySchedule) {
                    if (view.getTag() == null) {
                        view.setTag("editMode");
                        view.setBackgroundColor(getResources().getColor(R.color.superLightCyan));
                        deleteNum++;
                        selectedDayEvents.add(dayEvents.get(position));
                    } else {
                        view.setBackground(null);
                        view.setTag(null);
                        deleteNum--;
                        selectedDayEvents.remove(dayEvents.get(position));
                        if (deleteNum == 0) {
                            isEditDaySchedule = false;
                            bottomNav.setVisibility(View.GONE);
                        }

                    }

                }
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.deleteEventItem) {
                    schedHelper.open();
                    schedHelper.deleteDayEvents(selectedDayEvents);
                    schedHelper.close();
                    for(int i = 0; i<selectedDayEvents.size(); i++){
                        for (int j = 0; j<dayEvents.size(); j++){
                            if(selectedDayEvents.get(i).getId() == dayEvents.get(j).getId()){
                                dayEvents.remove(j);
                                break;
                            }
                        }
                    }
                    dayEventAdapter.notifyDataSetChanged();
                    selectedDayEvents.clear();
                    resizeListView();
                }

                else{
                    for (int i = 0; i <selectedDayEvents.size(); i++) {
                        View dayEventView = getViewByPosition(i,dayEventListView);
                        dayEventView.setBackground(null);
                        dayEventView.setTag(null);
                    }
                    selectedDayEvents.clear();
                    dayEventAdapter.notifyDataSetChanged();
                }
                bottomNav.setVisibility(View.GONE);
                isEditDaySchedule = false;
                deleteNum = 0;
                return true;
            }
        });

        // Calendar Section
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.event_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        calendarView = view.findViewById(R.id.calendarView);
        updateEvents();

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                String date = formatter.format(eventDay.getCalendar().getTime());

                if (editModeBool) {
                    dialog.setContentView(R.layout.edit_event_message_dialog);
                    eventDateDialog = dialog.findViewById(R.id.eventDateDialog);
                    eventDateDialog.setText(date);

                    ImageView addEvent = dialog.findViewById(R.id.addEventBtn);
                    final EditText titleEditText = dialog.findViewById(R.id.eventTitleDialog);
                    final EditText descriptionEditText = dialog.findViewById(R.id.eventDescriptionDialog);

                    addEvent.setOnClickListener(v -> {
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

                        updateEvents();
                    });
                } else {
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

                    if (eventsInDate.isEmpty()) {
                        rightArrow.setImageResource(R.drawable.arrow_right_disabled);
                        leftArrow.setImageResource(R.drawable.arrow_left_disabled);
                        descriptionTv.setText("אין אירועים בתאריך זה");
                        dialog.show();
                        return;
                    } else {
                        titleTv.setText(eventsInDate.get(0).getTitle());
                        descriptionTv.setText(eventsInDate.get(0).getDescription());
                        eventsNumTv.setText("1 מתוך " + eventsInDate.size());
                        currentEvent = eventsInDate.get(0);
                        currentEventCount = 1;
                    }

                    checkIfHasRight();
                    checkIfHasLeft();

                    rightArrow.setOnClickListener(v -> goRight());


                    leftArrow.setOnClickListener(v -> goLeft());
                }
                dialog.show();
            }
        });

        return view;
    }

    private void checkIfHasLeft() {
        checkIfHasRight();
        if (currentEvent.getId() == eventsInDate.get(eventsInDate.size() - 1).getId()) {
            leftArrow.setImageResource(R.drawable.arrow_left_disabled);
            leftArrow.setTag(0);
        } else {
            leftArrow.setTag(1);
            leftArrow.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }
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

    private void checkIfHasRight() {
        checkLeftOnly();
        if (currentEvent.getId() == eventsInDate.get(0).getId()) {
            rightArrow.setImageResource(R.drawable.arrow_right_disabled);
            rightArrow.setTag(0);
        } else {
            rightArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
            rightArrow.setTag(1);
            Log.e("eventInfo", "rightArrow, you made it");
        }

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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_watch_mode_menu, menu);
        this.menu = menu;
        menu.getItem(1).setIcon(R.drawable.edit_active);
        dayEventListView.setItemsCanFocus(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editMode) {
            item.setIcon(R.drawable.ic_mode_edit_black_24dp);
            menu.getItem(0).setIcon(R.drawable.ic_remove_red_eye_black_24dp);
            editModeBool = true;
            noLuz.setVisibility(View.GONE);
            openEventDialog.setVisibility(View.VISIBLE);
            dayEventListView.setItemsCanFocus(true);
        } else {
            dayEventListView.setItemsCanFocus(false);
            item.setIcon(R.drawable.eye_not_selected);
            menu.getItem(1).setIcon(R.drawable.edit_active);
            if (dayEvents.size() == 0)
                noLuz.setVisibility(View.VISIBLE);
            else
                noLuz.setVisibility(View.GONE);

            openEventDialog.setVisibility(View.GONE);
            editModeBool = false;
        }
        return true;
    }

    private void updateEvents() {
        List<EventDay> eventDays = new ArrayList<>();
        Date date;
        for (int i = 0; i < events.size(); i++) {
            try {
                date = formatter.parse(events.get(i).getDate());
                Calendar calendarEvent = Calendar.getInstance();
                calendarEvent.setTime(date);
                eventDays.add(new EventDay(calendarEvent, R.drawable.ic_baseline_message_24));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        calendarView.setEvents(eventDays);
    }

    private void resizeListView() {
        if(dayEvents.size() == 0){
            containerParams.height = dayEventListView.getHeight();
        }
        else{
            View currentView = dayEventAdapter.getView(dayEvents.size() - 1, null, dayEventListView);
            currentView.measure(0, 0);
            containerParams.height = (currentView.getMeasuredHeight() + dayEventListView.getDividerHeight()) * (dayEvents.size() + 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void orderScheduleByTime() {
        dayEvents.sort(new Comparator<DayEvent>() {
            @Override
            public int compare(DayEvent o1, DayEvent o2) {
                try {
                    return new SimpleDateFormat("hh:mm").parse(o1.getTimePicked())
                            .compareTo(new SimpleDateFormat("hh:mm").parse(o2.getTimePicked()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }




}
