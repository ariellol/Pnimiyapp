package com.lipstudio.pnimiyapp;

public class Event {

    private String title;
    private String description;
    private String date;
    private long id;

    public Event(String title, String date,String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public Event(String title, String description, String date, long id) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", id=" + id +
                '}';
    }
}
