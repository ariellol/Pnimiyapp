package com.lipstudio.pnimiyapp;

import java.text.AttributedCharacterIterator;

public class Request {

    private long userId;
    private String date;
    private long id;
    private String userName;
    private String description;
    private String title;
    private int tag;

    public Request(long userId, String date, long id, String title, String description) {
        this.userId = userId;
        this.date = date;
        this.id = id;
        this.userName = userName;
        this.description = description;
        this.title = title;
    }

    public Request(long userId, String date, String title, String description) {
        this.userId = userId;
        this.date = date;
        this.userName = userName;
        this.description = description;
        this.title = title;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
