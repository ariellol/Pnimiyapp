package com.lipstudio.pnimiyapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AttendanceSheet implements Serializable {

    private String title;
    private String currentSheetDate;
    private String currentHour;
    private String dayOfWeek;
    private ArrayList<Attendance> attendances;
    private long sheetId;
    public AttendanceSheet(String title){
        this.title = title;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentSheetDate = dateFormat.format(Calendar.getInstance().getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        currentHour = timeFormat.format(Calendar.getInstance().getTime());

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        dayOfWeek = dayFormat.format(Calendar.getInstance().getTime());
    }

    public AttendanceSheet(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentSheetDate = dateFormat.format(Calendar.getInstance().getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        currentHour = timeFormat.format(Calendar.getInstance().getTime());

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        dayOfWeek = dayFormat.format(Calendar.getInstance().getTime());
    }

    public AttendanceSheet(String title, ArrayList<Attendance> attendances, String date, String day, String hour){
        this.title = title;
        this.attendances = attendances;
        this.currentSheetDate = date;
        this.dayOfWeek = day;
        this.currentHour = hour;
    }

    public String getTitle() {
        return title;
    }

    public String getCurrentSheetDate() {
        return currentSheetDate;
    }

    public String getCurrentHour() {
        return currentHour;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public String toString() {
        return "AttendanceSheet{" +
                "title='" + title + '\'' +
                ", currentSheetDate='" + currentSheetDate + '\'' +
                ", currentHour='" + currentHour + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                '}';
    }

    public ArrayList<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(ArrayList<Attendance> attendances) {
        this.attendances = attendances;
    }

    public void setSheetId(long sheetId) {
        this.sheetId = sheetId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
