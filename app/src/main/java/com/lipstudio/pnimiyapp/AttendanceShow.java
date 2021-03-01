package com.lipstudio.pnimiyapp;

public class AttendanceShow {

    String attendance;
    String fullName;

    public AttendanceShow(String attendance, String fullName) {
        this.attendance = attendance;
        this.fullName = fullName;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
