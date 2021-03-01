package com.lipstudio.pnimiyapp;

import java.util.ArrayList;

public class DaySchedule {

    String date;
    String day;
    ArrayList<Event> events;

    public DaySchedule(String date, String day, ArrayList<Event> events) {
        this.date = date;
        this.day = day;
        this.events = events;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
