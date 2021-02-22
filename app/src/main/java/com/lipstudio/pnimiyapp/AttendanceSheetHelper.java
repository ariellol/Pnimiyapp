package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class AttendanceSheetHelper extends SQLiteOpenHelper {

    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ID = "attendanceId";
    public static final String SHEET_TABLE = "sheetTable";

    public static final String ATTENDANCE_TABLE = "attendanceTable";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_ATTENDANCE_CODE = "attendanceCode";

    public static final String DATABASE_NAME = "users.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_SHEET_TABLE = "CREATE TABLE IF NOT EXISTS " + SHEET_TABLE +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " VARCHAR, " + COLUMN_DATE + " VARCHAR, " + COLUMN_DAY + " VARCHAR," + COLUMN_HOUR + " VARCHAR, " +
             SHEET_TABLE + " REAL);";

    public static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS " + ATTENDANCE_TABLE +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_ID + " INTEGER, " + COLUMN_ATTENDANCE_CODE + " INTEGER, " +
            ATTENDANCE_TABLE + " REAL);";

    SQLiteDatabase database;

    public AttendanceSheetHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHEET_TABLE);
        db.execSQL(CREATE_ATTENDANCE_TABLE);
        Log.e("AttendanceSheetDb", "both Tables Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ATTENDANCE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SHEET_TABLE);
        onCreate(db);
    }

    public void open(){
        database = this.getWritableDatabase();
    }

    public AttendanceSheet insertAttendanceSheet(AttendanceSheet sheet){

        ContentValues sheetValues = new ContentValues();

        sheetValues.put(COLUMN_TITLE,sheet.getTitle());
        sheetValues.put(COLUMN_DATE,sheet.getCurrentSheetDate());
        sheetValues.put(COLUMN_DAY,sheet.getDayOfWeek());
        sheetValues.put(COLUMN_HOUR,sheet.getCurrentHour());

        long lastId = database.insert(SHEET_TABLE,null, sheetValues);

        ContentValues attendanceValues = new ContentValues();

        for (int i =0; i< sheet.getAttendances().size(); i++){
           attendanceValues.put(COLUMN_USER_ID,sheet.getAttendances().get(i).getUserId());
            attendanceValues.put(COLUMN_ATTENDANCE_CODE,sheet.getAttendances().get(i).getAttendanceCode());
        }
        database.insert(ATTENDANCE_TABLE, null, attendanceValues);

        sheet.setSheetId(lastId);
        Log.e("AttendanceSheetDb", "sheet Created and added to both tables.");
        return sheet;
    }

    public ArrayList<Attendance>getAttendancesById(long id){
        ArrayList<Attendance> attendances = new ArrayList<>();
        String select_query = "SELECT * FROM " + ATTENDANCE_TABLE + " WHERE " + id + " = " + COLUMN_ID;
        Cursor cursor = database.rawQuery(select_query,null);

        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Attendance attendance = new Attendance(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ATTENDANCE_CODE)));
                attendances.add(attendance);
            }
        }
        return attendances;
    }

    public ArrayList<AttendanceSheet> getAllAttendanceSheets() {
        ArrayList<AttendanceSheet> attendanceSheets = new ArrayList<>();
        ArrayList<Attendance> attendances;
        String select_query = "SELECT * FROM " + SHEET_TABLE;
        Cursor cursor = database.rawQuery(select_query, null);

        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
                String hour = cursor.getString(cursor.getColumnIndex(COLUMN_HOUR));
                attendances = getAttendancesById(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                attendanceSheets.add()
            }
        }
        return attendanceSheets;
    }


}