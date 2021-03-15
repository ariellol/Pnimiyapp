package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DayEventAdapter extends ArrayAdapter<DayEvent> {

    Context context;
    ArrayList<DayEvent> dayEvents;
    public DayEventAdapter(@NonNull Context context, int resource, ArrayList<DayEvent> dayEvents) {
        super(context, resource,dayEvents);
        this.context = context;
        this.dayEvents = dayEvents;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.day_event_custom_layout,parent,false);

        TextView title = convertView.findViewById(R.id.eventDayTitle);
        TextView time = convertView.findViewById(R.id.eventDayTime);

        DayEvent dayEvent = dayEvents.get(position);
        title.setText(dayEvent.getTitle());
        time.setText(dayEvent.getTimePicked());

        return convertView;
    }
}
