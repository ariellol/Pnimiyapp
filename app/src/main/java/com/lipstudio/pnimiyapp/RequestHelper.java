package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_NAME;
import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_VERSION;

public class RequestHelper extends SQLiteOpenHelper {

    public static final String LOAN_REQUEST_TABLE = "loanRequestTable";
    public static final String OUT_REQUEST_TABLE = "outRequestTable";
    public static final String IDEA_BOX_TABLE = "ideaBoxTable";

    public static final String IDEA_TITLE = "ideaTitle";
    public static final String IDEA_DESCRIPTION = "ideaDescription";
    public static final String IDEA_SENT_DATE = "ideaDate";
    public static final String IDEA_ID = "ideaId";

    public static final String USER_ID = "userId";
    public static final String REQUEST_TITLE = "requestTitle";
    public static final String REQUEST_DESCRIPTION = "requestDescription";
    public static final String REQUEST_SENT_DATE = "requestDate";
    public static final String REQUEST_ID = "requestId";
    public static final String REQUEST_MONEY_AMOUNT = "moneyAmount";

    public static final String REQUEST_FROM_DATE = "fromDate";
    public static final String REQUEST_TO_DATE = "toDate";

    public static final String CREATE_LOAN_REQUEST_TABLE = "CREATE TABLE IF NOT EXISTS " + LOAN_REQUEST_TABLE +
            " ( " + REQUEST_TITLE + " VARCHAR, " + REQUEST_DESCRIPTION + " VARCHAR, " + USER_ID + " INTEGER, " + REQUEST_SENT_DATE + " VARCHAR, " +
            REQUEST_MONEY_AMOUNT + " INTEGER, " +REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LOAN_REQUEST_TABLE + " REAL)";

    public static final String CREATE_OUT_REQUEST_TABLE = "CREATE TABLE IF NOT EXISTS " + OUT_REQUEST_TABLE +
            " ( " + REQUEST_TITLE + " VARCHAR, " + REQUEST_DESCRIPTION + " VARCHAR, " + USER_ID + " INTEGER, " + REQUEST_SENT_DATE + " VARCHAR, " +
            REQUEST_FROM_DATE + " VARCHAR, " + REQUEST_TO_DATE + " VARCHAR, " +REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            OUT_REQUEST_TABLE + " REAL)";

    public static final String CREATE_IDEA_TABLE = "CREATE TABLE IF NOT EXISTS " + IDEA_BOX_TABLE +
            " (" + IDEA_TITLE + " VARCHAR, " + IDEA_DESCRIPTION + " VARCHAR, " + IDEA_SENT_DATE + " VARCHAR, " +
            USER_ID + " INTEGER, " + IDEA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IDEA_BOX_TABLE + " REAL)";

    SQLiteDatabase database;

    public RequestHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void open(){
        database = getWritableDatabase();
    }

    public void insertLoanRequest(LoanRequest loanRequest){
        ContentValues requestValues = new ContentValues();
        requestValues.put(USER_ID,loanRequest.getUserId());
        requestValues.put(REQUEST_TITLE,loanRequest.getTitle());
        requestValues.put(REQUEST_DESCRIPTION,loanRequest.getDescription());
        requestValues.put(REQUEST_MONEY_AMOUNT,loanRequest.getMoneyAmount());
        requestValues.put(REQUEST_SENT_DATE,loanRequest.getDate());

        database.insert(LOAN_REQUEST_TABLE,null,requestValues);
    }

    public void insertOutRequest(OutRequest outRequest){
        ContentValues requestValues = new ContentValues();
        requestValues.put(USER_ID,outRequest.getUserId());
        requestValues.put(REQUEST_TITLE,outRequest.getTitle());
        requestValues.put(REQUEST_DESCRIPTION,outRequest.getDescription());
        requestValues.put(REQUEST_SENT_DATE,outRequest.getDate());
        requestValues.put(REQUEST_FROM_DATE,outRequest.getFromDate());
        requestValues.put(REQUEST_TO_DATE,outRequest.getToDate());

        database.insert(OUT_REQUEST_TABLE,null,requestValues);
    }

    public Idea insertIdea(Idea idea){
        ContentValues ideaValues = new ContentValues();

        ideaValues.put(IDEA_TITLE,idea.getTitle());
        ideaValues.put(IDEA_DESCRIPTION,idea.getDescription());
        ideaValues.put(IDEA_SENT_DATE,idea.getDate());
        ideaValues.put(USER_ID,idea.getUserId());

        long ideaId = database.insert(IDEA_BOX_TABLE,null,ideaValues);

        idea.setId(ideaId);
        return idea;
    }

    public ArrayList<Idea> getAllIdeas(){
        ArrayList<Idea> ideas = new ArrayList<>();
        String sql_str = "SELECT * FROM " + IDEA_BOX_TABLE + " ORDER BY " + IDEA_ID + " DESC";
        Cursor cursor = database.rawQuery(sql_str, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(IDEA_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(IDEA_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(IDEA_SENT_DATE));
                long userId = cursor.getLong(cursor.getColumnIndex(USER_ID));
                long ideaId = cursor.getLong(cursor.getColumnIndex(IDEA_ID));

                ideas.add(new Idea(title,description,userId,date,ideaId));
            }
        }
        return ideas;
    }

    public ArrayList<OutRequest> getAllOutRequests(){
        ArrayList<OutRequest> outRequests = new ArrayList<>();
        String sql_str = "SELECT * FROM " + OUT_REQUEST_TABLE + " ORDER BY " + REQUEST_ID + " DESC";

        Cursor cursor = database.rawQuery(sql_str, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(REQUEST_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(REQUEST_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(REQUEST_SENT_DATE));
                String fromDate = cursor.getString(cursor.getColumnIndex(REQUEST_FROM_DATE));
                String toDate = cursor.getString(cursor.getColumnIndex(REQUEST_TO_DATE));
                long userId = cursor.getLong(cursor.getColumnIndex(USER_ID));
                long id = cursor.getLong(cursor.getColumnIndex(REQUEST_ID));

                outRequests.add(new OutRequest(userId,date,id,title,description,fromDate,toDate));
            }
        }
        return outRequests;
    }

    public ArrayList<LoanRequest> getAllLoanRequests(){
        ArrayList<LoanRequest> loanRequests = new ArrayList<>();
        String sql_str = "SELECT * FROM " + LOAN_REQUEST_TABLE + " ORDER BY " + REQUEST_ID + " DESC";

        Cursor cursor = database.rawQuery(sql_str, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(REQUEST_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(REQUEST_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(REQUEST_SENT_DATE));
                int moneyAmount = cursor.getInt(cursor.getColumnIndex(REQUEST_MONEY_AMOUNT));
                long userId = cursor.getLong(cursor.getColumnIndex(USER_ID));
                long id = cursor.getLong(cursor.getColumnIndex(REQUEST_ID));

                loanRequests.add(new LoanRequest(userId,date,id,title,description,moneyAmount));
            }
        }
        return loanRequests;
    }
}
