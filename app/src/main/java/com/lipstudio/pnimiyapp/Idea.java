package com.lipstudio.pnimiyapp;

public class Idea extends Request{

    public Idea(String title, String description, long userId, String date) {
        super(userId,date,title,description);
    }

    public Idea(String title, String description, long userId, String date, long id) {
        super(userId,date,id,title,description);
    }

}
