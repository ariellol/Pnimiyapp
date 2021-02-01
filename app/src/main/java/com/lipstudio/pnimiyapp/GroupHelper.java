package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class GroupHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_TABLE = "groupTable";
    public static final String EDUCATOR_ID = "educatorId";
    public static final String DATABASE_NAME = "users.db";
    public static final String SHINSHIN_ID = "shinshinId";
    public static final String GRADE = "grade";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + GROUP_TABLE +
            " (" + GRADE + "VARCHAR PRIMARY KEY, " + GROUP_NAME + " VARCHAR," + EDUCATOR_ID + " INTEGER," +
            SHINSHIN_ID + " INTEGER," + GROUP_TABLE + "REAL)";


    SQLiteDatabase database;

    public GroupHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.i("database","groups table Created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
        onCreate(db);
    }

    public void open(){
        database = getWritableDatabase();
    }

    public void insertGroup(Group group){
        ContentValues values = new ContentValues();
        values.put(GRADE,group.getGrade());
        values.put(GROUP_NAME,group.getName());
        values.put(EDUCATOR_ID,group.getEducator().getId());
        values.put(SHINSHIN_ID,group.getShinshin().getId());

        Log.i("database","group has been created");
    }

}
