package com.lipstudio.pnimiyapp;

public class Attendance {

    private long userId;
    private int attendanceCode;
    private long attendanceSheetId;

    public Attendance(long userId, int attendanceCode){
        this.userId = userId;
        this.attendanceCode = attendanceCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getAttendanceCode() {
        return attendanceCode;
    }

    public void setAttendanceCode(int attendanceCode) {
        this.attendanceCode = attendanceCode;
    }

    public long getAttendanceSheetId() {
        return attendanceSheetId;
    }

    public void setAttendanceSheetId(long attendanceSheetId) {
        this.attendanceSheetId = attendanceSheetId;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "userId=" + userId +
                ", attendanceCode=" + attendanceCode +
                '}';
    }
}
