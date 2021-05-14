package com.lipstudio.pnimiyapp;

public class OutRequest extends Request{

    private String fromDate;
    private String toDate;

    public OutRequest(long userId, String date, long id,String title, String description, String fromDate, String toDate) {
        super(userId, date, id,title,description);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public OutRequest(long userId, String date,String title, String description, String fromDate, String toDate) {
        super(userId, date,title,description);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
