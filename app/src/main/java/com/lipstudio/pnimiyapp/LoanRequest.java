package com.lipstudio.pnimiyapp;

public class LoanRequest extends Request{

    private int moneyAmount;

    public LoanRequest(long userId, String date, long id, String title, String description, int moneyAmount) {
        super(userId, date, id, title, description);
        this.moneyAmount = moneyAmount;
    }

    public LoanRequest(long userId, String date, String title, String description, int moneyAmount) {
        super(userId, date, title, description);
        this.moneyAmount = moneyAmount;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }
}
