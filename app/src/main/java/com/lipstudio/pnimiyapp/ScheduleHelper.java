package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_NAME;
import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_VERSION;

public class ScheduleHelper extends SQLiteOpenHelper{

    public static final String EVENT_TABLE = "eventTable";
    public static final String EVENT_DAY_TABLE = "eventDayTable";

    public static final String EVENT_TITLE = "eventTitle";
    public static final String EVENT_DATE = "eventDate";
    public static final String EVENT_DESCRIPTION = "eventDescription";
    public static final String EVENT_ID = "eventId";

    public static final String EVENT_DAY_TITLE = "eventDayTitle";
    public static final String EVENT_DAY_TIME = "eventDayTime";
    public static final String EVENT_DAY_DATE = "eventDayDate";
    public static final String EVENT_DAY_ID = "eventDayId";

    public static final String CREATE_EVENT_DAY_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENT_DAY_TABLE +
            " (" + EVENT_DAY_TITLE + " VARCHAR, " + EVENT_DAY_TIME + " VARCHAR, " + EVENT_DAY_DATE + " VARCHAR, " +
            EVENT_DAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_DAY_TABLE + " REAL);";

    public static final String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENT_TABLE +
            " (" + EVENT_TITLE + " VARCHAR, " + EVENT_DESCRIPTION + " VARCHAR, " + EVENT_DATE + " VARCHAR, " +
            EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_TABLE + " REAL);";

    SQLiteDatabase database;

    public ScheduleHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        values.put(EVENT_DESCRIPTION, event.getDescription());
        values.put(EVENT_DATE, event.getDate());

        long lastId = database.insert(EVENT_TABLE,null,values);

        event.setId(lastId);
        return event;
    }


    public ArrayList<Event> getAllEvents(){
        ArrayList<Event> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EVENT_TABLE;
        Cursor cursor = database.rawQuery(selectQuery,null);

        if (cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(EVENT_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(EVENT_DATE));
                long id = cursor.getInt(cursor.getColumnIndex(EVENT_ID));

                events.add(new Event(title, description, date, id));
            }
        }

        return events;
    }

    public DayEvent insertDayEvent(DayEvent event){
        ContentValues values = new ContentValues();

        values.put(EVENT_DAY_TITLE, event.getTitle());
        values.put(EVENT_DAY_DATE, event.getDate());
        values.put(EVENT_DAY_TIME,event.getTimePicked());

        long lastId = database.insert(EVENT_DAY_TABLE,null,values);

        event.setId(lastId);
        return event;
    }

    public ArrayList<DayEvent> getEventsByDate(String date){
        ArrayList<DayEvent> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + EVENT_DAY_TABLE + " WHERE " + EVENT_DAY_DATE + " = '" + date +"'";
        Log.e("dateIs",date);
        Cursor cursor = database.rawQuery(selectQuery,null);

        if (cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(EVENT_DAY_TITLE));
                String time = cursor.getString(cursor.getColumnIndex(EVENT_DAY_TIME));
                long id = cursor.getInt(cursor.getColumnIndex(EVENT_DAY_ID));

                events.add(new DayEvent(title,time,date,id));
            }
        }

        return events;
    }

    public void deleteOtherEvents(String date){
        database.delete(EVENT_DAY_TABLE,EVENT_DAY_DATE + " != ?" , new String[]{"'"+date+"'"});
    }

    public void deleteDayEvents(ArrayList<DayEvent> events){
        for (int i = 0; i<events.size(); i++){
            Log.e("eventId",events.get(i).getId()+"");
            database.delete(EVENT_DAY_TABLE, EVENT_DAY_ID + " = ?", new String[]{String.valueOf(events.get(i).getId())});
        }
    }

    public void updateDayEvent(DayEvent event){

        ContentValues values = new ContentValues();
        values.put(EVENT_DAY_TIME,event.getTimePicked());
        values.put(EVENT_DAY_TITLE,event.getTitle());
        database.update(EVENT_DAY_TABLE,values,EVENT_DAY_ID + " = ?", new String[]{String.valueOf(event.getId())});
    }


}
