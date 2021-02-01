package com.lipstudio.pnimiyapp;

import java.util.ArrayList;

public class Group {
    private ArrayList<UserStudent> userStudents;
    private UserEducator educator;
    private UserShinshin shinshin;
    private String grade;
    private String name;

    public Group(String grade, String name){
        this.grade = grade;
        this.name = name;
    }

    public ArrayList<UserStudent> getUserStudents() {
        return userStudents;
    }

    public void setUserStudents(ArrayList<UserStudent> userStudents) {
        this.userStudents = userStudents;
    }

    public UserEducator getEducator() {
        return educator;
    }

    public void setEducator(UserEducator educator) {
        this.educator = educator;
    }

    public UserShinshin getShinshin() {
        return shinshin;
    }

    public void setShinshin(UserShinshin shinshin) {
        this.shinshin = shinshin;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
