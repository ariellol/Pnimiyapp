package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<Event> {

    Context context;
    ArrayList<Event> events;
    public EventAdapter(@NonNull Context context,ArrayList<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return convertView;
    }
}
