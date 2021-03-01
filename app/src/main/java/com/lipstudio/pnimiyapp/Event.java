package com.lipstudio.pnimiyapp;

public class Event {

    String hour;
    String title;
    String description;

    public Event(String hour, String title, String description) {
        this.hour = hour;
        this.title = title;
        this.description = description;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
