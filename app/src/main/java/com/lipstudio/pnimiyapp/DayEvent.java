package com.lipstudio.pnimiyapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DayEvent {

    private String title;
    private String timePicked;
    private long id;
    private String date;

    public DayEvent(String title,String timePicked){
        this.title = title;
        this.timePicked = timePicked;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.format(Calendar.getInstance().getTime());
    }

    public DayEvent(String title,String timePicked, String date, long id){
        this.title = title;
        this.timePicked = timePicked;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimePicked() {
        return timePicked;
    }

    public void setTimePicked(String timePicked) {
        this.timePicked = timePicked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DayEvent{" +
                "title='" + title + '\'' +
                ", timePicked='" + timePicked + '\'' +
                ", id=" + id +
                ", date='" + date + '\'' +
                '}';
    }
}
