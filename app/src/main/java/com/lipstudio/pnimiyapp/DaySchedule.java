package com.lipstudio.pnimiyapp;

import java.util.ArrayList;

public class DaySchedule extends Event{


    ArrayList<Event> events;

    public DaySchedule(ArrayList<Event> events,String title, String date, String description) {
        super(title, description, date);
        this.events = events;
    }

    public DaySchedule(String title, String date, String description) {
        super(title, description, date);
        this.events = new ArrayList<>();
    }


    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
