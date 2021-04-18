package com.lipstudio.pnimiyapp;



import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import static com.lipstudio.pnimiyapp.ScheduleFragment.getViewByPosition;


public class HomeFragment extends Fragment implements View.OnClickListener{

    SharedPreferences userPref;
    Context context;
    TextView greetings;
    TextView announcementTextView;
    TextView quoteTextView;
    String[] quoteAndAnnouncement;
    GeneralHelper generalHelper;

    TextView noLuz;
    ImageView openEventDialog;
    MaterialTimePicker timePicker;
    Dialog eventInDayDialog;
    EditText eventInDayTitle;
    Button timePick;
    Button addEventInDay;
    String time;
    TextView timePicked;

    ListView dayEventListView;
    ArrayList<DayEvent> dayEvents;
    DayEventAdapter dayEventAdapter;
    LinearLayout.LayoutParams containerParams;
    ArrayList<DayEvent> selectedDayEvents;

    BottomNavigationView bottomNav;
    Menu bottomMenu;
    boolean isEditDaySchedule = false;
    int deleteNum = 0;
    Menu menu;
    boolean editModeBool = false;

    SimpleDateFormat formatter;
    ScheduleHelper schedHelper;

    LinearLayout announcementLayout;
    Dialog dialogEditAnnouncement;
    EditText editAnnouncement;
    Button updateAnnouncement;

    RelativeLayout quoteLayout;
    Dialog dialogEditQuote;
    EditText editQuote;
    Button updateQuote;

    Dialog dialogDeleteEvent;
    Button deleteEventBtn;
    Button cancelDelete;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(Calendar.getInstance().getTime());

        context = getActivity();

        schedHelper = new ScheduleHelper(context);
        schedHelper.open();
        dayEvents = schedHelper.getEventsByDate(currentDate);
        schedHelper.close();
        orderScheduleByTime();

        generalHelper = new GeneralHelper(context);
        generalHelper.open();
        quoteAndAnnouncement = new String[2];
        quoteAndAnnouncement = generalHelper.getQuoteAndAnnouncement();
        generalHelper.close();

        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_home,container,false);
        context = getActivity();
        userPref = context.getSharedPreferences("userSharedPreferences",Context.MODE_PRIVATE);
        greetings = parent.findViewById(R.id.greetings);
        greetings.setText(greetings.getText().toString() + userPref.getString("userName","אורח"));

        announcementTextView = parent.findViewById(R.id.announcementTextView);
        quoteTextView = parent.findViewById(R.id.quoteTextView);
        quoteTextView.setText(quoteAndAnnouncement[0]);
        announcementTextView.setText(quoteAndAnnouncement[1]);

        bottomNav = parent.findViewById(R.id.scheduleBottomNav);
        bottomNav.setItemIconTintList(null);
        bottomNav.setSelected(false);
        bottomMenu = bottomNav.getMenu();
        containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout listViewLayout = parent.findViewById(R.id.scheduleContainer);
        listViewLayout.setLayoutParams(containerParams);

        dayEventAdapter = new DayEventAdapter(context, 0, dayEvents);
        dayEventListView = parent.findViewById(R.id.dayEventListView);
        dayEventListView.setAdapter(dayEventAdapter);
        dayEventAdapter.notifyDataSetChanged();

        eventInDayDialog = new Dialog(context);
        eventInDayDialog.setContentView(R.layout.add_day_event_dialog);
        eventInDayTitle = eventInDayDialog.findViewById(R.id.dayEventTitleDialog);
        timePick = eventInDayDialog.findViewById(R.id.pick_time_dialog);
        addEventInDay = eventInDayDialog.findViewById(R.id.add_event_btn_dialog);
        timePicked = eventInDayDialog.findViewById(R.id.timePicked);
        openEventDialog = parent.findViewById(R.id.open_day_event_dialog);

        announcementLayout = parent.findViewById(R.id.announcementLayout);
        dialogEditAnnouncement = new Dialog(context);
        dialogEditAnnouncement.setContentView(R.layout.dialog_edit_announcement);
        editAnnouncement = dialogEditAnnouncement.findViewById(R.id.editAnnouncementEditText);
        updateAnnouncement = dialogEditAnnouncement.findViewById(R.id.updateAnnouncement);
        announcementLayout.setOnClickListener(this);
        updateAnnouncement.setOnClickListener(this);

        quoteLayout = parent.findViewById(R.id.quoteLayout);
        dialogEditQuote = new Dialog(context);
        dialogEditQuote.setContentView(R.layout.dialog_edit_quote);
        editQuote = dialogEditQuote.findViewById(R.id.editQuoteEditText);
        updateQuote = dialogEditQuote.findViewById(R.id.updateQuote);
        quoteLayout.setOnClickListener(this);
        updateQuote.setOnClickListener(this);

        dialogDeleteEvent = new Dialog(context);
        dialogDeleteEvent.setContentView(R.layout.dialog_delete_event);
        deleteEventBtn = dialogDeleteEvent.findViewById(R.id.delete_event);
        cancelDelete = dialogDeleteEvent.findViewById(R.id.cancel_delete_event);

        resizeListView();
        noLuz = parent.findViewById(R.id.noLuz);
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

        return parent;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SharedPreferences userPref = getActivity().getSharedPreferences("userSharedPreferences",Context.MODE_PRIVATE);
        if (userPref.getBoolean("editSchedule",false)){
            inflater.inflate(R.menu.edit_watch_mode_menu, menu);
            this.menu = menu;
            menu.getItem(1).setIcon(R.drawable.ic_mode_edit_black_24dp);
            dayEventListView.setItemsCanFocus(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editMode) {
            item.setIcon(R.drawable.edit_active);
            menu.getItem(0).setIcon(R.drawable.eye_not_selected);
            editModeBool = true;
            noLuz.setVisibility(View.GONE);
            openEventDialog.setVisibility(View.VISIBLE);
            dayEventListView.setItemsCanFocus(true);
        }
        else {
            dayEventListView.setItemsCanFocus(false);
            item.setIcon(R.drawable.ic_remove_red_eye_black_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_mode_edit_black_24dp);
            if (dayEvents.size() == 0)
                noLuz.setVisibility(View.VISIBLE);
            else
                noLuz.setVisibility(View.GONE);

            openEventDialog.setVisibility(View.GONE);
            editModeBool = false;
        }
        return true;
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

    @Override
    public void onClick(View v) {

            if (editModeBool) {
                switch (v.getId()) {

                    case R.id.announcementLayout:
                        dialogEditAnnouncement.show();
                        editAnnouncement.setText(quoteAndAnnouncement[1]);
                        break;
                    case R.id.quoteLayout:
                        dialogEditQuote.show();
                        editQuote.setText(quoteAndAnnouncement[0]);
                        break;
                    case R.id.updateAnnouncement:
                        generalHelper.open();
                        quoteAndAnnouncement[1] = editAnnouncement.getText().toString();
                        generalHelper.updateAnnouncement(quoteAndAnnouncement[1]);
                        generalHelper.close();
                        announcementTextView.setText(quoteAndAnnouncement[1]);
                        dialogEditAnnouncement.dismiss();
                        break;
                    case R.id.updateQuote:
                        generalHelper.open();
                        quoteAndAnnouncement[0] = editQuote.getText().toString();
                        generalHelper.updateQuote(quoteAndAnnouncement[0]);
                        generalHelper.close();
                        quoteTextView.setText(quoteAndAnnouncement[0]);
                        dialogEditQuote.dismiss();
                        break;
                    case R.id.delete_event:
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
                        break;

            }
        }

    }
}
