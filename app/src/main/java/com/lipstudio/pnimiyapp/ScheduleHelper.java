package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ScheduleHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "users.db";
    public static final String EVENT_TABLE = "eventTable";
    public static final int DATABASE_VERSION = 1;

    public static final String EVENT_TITLE = "eventTitle";
    public static final String EVENT_DATE = "eventDate";
    public static final String EVENT_DESCRPTION = "eventDescription";
    public static final String EVENT_ID = "eventId";

    public static final String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENT_TABLE +
            " (" + EVENT_TITLE + " VARCHAR, " + EVENT_DESCRPTION + " VARCHAR, " + EVENT_DATE + " VARCHAR, " +
            EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_TABLE + " REAL);";

    SQLiteDatabase database;

    public ScheduleHelper(@Nullable Context context) {
        super(context, EVENT_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
    }

    public void open(){
        database = getWritableDatabase();
        Log.e("database","Database is open.");
    }

    public Event insertEvent(Event event){
        ContentValues values = new ContentValues();

        values.put(EVENT_TITLE, event.getTitle());
        values.put(EVENT_DESCRPTION, event.getDescription());
        values.put(EVENT_DATE, event.getDate());

        long lastId = database.insert(EVENT_TABLE,null,values);

        event.setId(lastId);
        return event;
    }

    public ArrayList<Event> getEventsByDate(String date){
        ArrayList<Event> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE + " WHERE " + EVENT_DATE  +" = " + date;
        Cursor cursor = database.rawQuery(selectQuery,null);

        if (cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(EVENT_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(EVENT_DESCRPTION));
                long id = cursor.getInt(cursor.getColumnIndex(EVENT_ID));

                events.add(new Event(title, description, date, id));
            }
        }

        return events;
    }

    public ArrayList<Event> getAllEvents(){
        ArrayList<Event> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE;
        Cursor cursor = database.rawQuery(selectQuery,null);

        if (cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(EVENT_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(EVENT_DESCRPTION));
                String date = cursor.getString(cursor.getColumnIndex(EVENT_DATE));
                long id = cursor.getInt(cursor.getColumnIndex(EVENT_ID));

                events.add(new Event(title, description, date, id));
            }
        }

        return events;
    }

}
