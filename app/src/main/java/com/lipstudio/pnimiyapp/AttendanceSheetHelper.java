package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_NAME;
import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_VERSION;

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


    public static final String CREATE_SHEET_TABLE = "CREATE TABLE IF NOT EXISTS " + SHEET_TABLE +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " VARCHAR, " + COLUMN_DATE + " VARCHAR, " + COLUMN_DAY + " VARCHAR," + COLUMN_HOUR + " VARCHAR, " +
             SHEET_TABLE + " REAL);";

    public static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS " + ATTENDANCE_TABLE +
            " (" + COLUMN_ID + " INTEGER, " + COLUMN_USER_ID + " INTEGER, " + COLUMN_ATTENDANCE_CODE + " INTEGER, " +
            ATTENDANCE_TABLE + " REAL);";

    static SQLiteDatabase database;

    public AttendanceSheetHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void open(){
        database = this.getWritableDatabase();
    }

    public void insertAttendanceSheet(AttendanceSheet sheet){

        ContentValues sheetValues = new ContentValues();

        sheetValues.put(COLUMN_TITLE,sheet.getTitle());
        sheetValues.put(COLUMN_DATE,sheet.getCurrentSheetDate());
        sheetValues.put(COLUMN_DAY,sheet.getDayOfWeek());
        sheetValues.put(COLUMN_HOUR,sheet.getCurrentHour());

        long lastId = database.insert(SHEET_TABLE,null, sheetValues);

        ContentValues attendanceValues = new ContentValues();

        for (int i =0; i< sheet.getAttendances().size(); i++){
            Log.e("inside",i+"");
           attendanceValues.put(COLUMN_USER_ID,sheet.getAttendances().get(i).getUserId());
            attendanceValues.put(COLUMN_ATTENDANCE_CODE,sheet.getAttendances().get(i).getAttendanceCode());
            attendanceValues.put(COLUMN_ID,lastId);
            database.insert(ATTENDANCE_TABLE, null, attendanceValues);
        }

        sheet.setSheetId(lastId);
        Log.e("AttendanceSheetDb", "sheet Created and added to both tables.");

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
        String select_query = "SELECT * FROM " + SHEET_TABLE + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = database.rawQuery(select_query, null);

        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
                String hour = cursor.getString(cursor.getColumnIndex(COLUMN_HOUR));
                long id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                attendances = getAttendancesById(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));

                attendanceSheets.add(new AttendanceSheet(title,id,attendances,date,day,hour));
            }
        }
        return attendanceSheets;
    }

    public void deleteAttendanceSheets(ArrayList<AttendanceSheet> toDeleteAttendance){

        for (int i = 0; i < toDeleteAttendance.size() ; i++) {
            Log.e("databaseSheet",toDeleteAttendance.get(i).getSheetId()+"");
            database.delete(SHEET_TABLE,COLUMN_ID+ " = ?", new String[]{String.valueOf(toDeleteAttendance.get(i).getSheetId() )} );
            database.delete(ATTENDANCE_TABLE,COLUMN_ID+" = ?", new String[]{String.valueOf(toDeleteAttendance.get(i).getSheetId())});
        }
    }
    public static void removeUserFromAttendances(long id){
        database.delete(ATTENDANCE_TABLE,COLUMN_USER_ID+ " = ?", new String[]{String.valueOf(id)});
    }


}