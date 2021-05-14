package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.lipstudio.pnimiyapp.AttendanceSheetHelper.ATTENDANCE_TABLE;
import static com.lipstudio.pnimiyapp.AttendanceSheetHelper.COLUMN_USER_ID;
import static com.lipstudio.pnimiyapp.AttendanceSheetHelper.CREATE_ATTENDANCE_TABLE;
import static com.lipstudio.pnimiyapp.AttendanceSheetHelper.CREATE_SHEET_TABLE;
import static com.lipstudio.pnimiyapp.GeneralHelper.CREATE_GENERAL_TABLE;
import static com.lipstudio.pnimiyapp.GeneralHelper.GENERAL_TABLE;
import static com.lipstudio.pnimiyapp.InstagramHelper.CREATE_INSTAGRAM_TABLE;
import static com.lipstudio.pnimiyapp.InstagramHelper.INSTAGRAM_TABLE;
import static com.lipstudio.pnimiyapp.RequestHelper.CREATE_IDEA_TABLE;
import static com.lipstudio.pnimiyapp.RequestHelper.CREATE_LOAN_REQUEST_TABLE;
import static com.lipstudio.pnimiyapp.RequestHelper.CREATE_OUT_REQUEST_TABLE;
import static com.lipstudio.pnimiyapp.RequestHelper.IDEA_BOX_TABLE;
import static com.lipstudio.pnimiyapp.RequestHelper.LOAN_REQUEST_TABLE;
import static com.lipstudio.pnimiyapp.RequestHelper.OUT_REQUEST_TABLE;
import static com.lipstudio.pnimiyapp.ScheduleHelper.CREATE_EVENT_DAY_TABLE;
import static com.lipstudio.pnimiyapp.ScheduleHelper.CREATE_EVENT_TABLE;
import static com.lipstudio.pnimiyapp.ScheduleHelper.EVENT_DAY_TABLE;

public class UserHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final String USER_TABLE = "usersTable";
    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "userId";
    public static final String COLUMN_FNAME = "firstName";
    public static final String COLUMN_LNAME = "lastName";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TYPE = "userType";
    public static final String COLUMN_EDIT_CONTENT = "editContent";
    public static final String COLUMN_EDIT_SCHEDULE = "editSchedule";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";

    public static final String GROUP_ID = "groupId";
    public static final String USER_TO_GROUP_TABLE = "userToGroup";

    public static final String CREATE_TABLE_USER_TO_GROUP = "CREATE TABLE IF NOT EXISTS " + USER_TO_GROUP_TABLE +
            " (" + GROUP_ID + " VARCHAR, " + COLUMN_ID + " VARCHAR, " + USER_TO_GROUP_TABLE + " REAL)";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_FNAME + " VARCHAR, " +
            COLUMN_LNAME + " VARCHAR, " + COLUMN_PASSWORD + " VARCHAR, " + COLUMN_TYPE + " VARCHAR, "
            + COLUMN_EDIT_CONTENT + " INTEGER, " + COLUMN_EDIT_SCHEDULE + " INTEGER, " + COLUMN_PHONE_NUMBER + " VARCHAR, "
            + USER_TABLE + " REAL)";


    SQLiteDatabase database;
    Context context;

    public UserHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Log.e("database", "UsersHelper Created.");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("database", " onCreate called.");
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_USER_TO_GROUP);
        db.execSQL(CREATE_SHEET_TABLE);
        db.execSQL(CREATE_ATTENDANCE_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_EVENT_DAY_TABLE);
        db.execSQL(CREATE_INSTAGRAM_TABLE);
        db.execSQL(CREATE_GENERAL_TABLE);
        db.execSQL(CREATE_IDEA_TABLE);
        db.execSQL(CREATE_OUT_REQUEST_TABLE);
        db.execSQL(CREATE_LOAN_REQUEST_TABLE);

        Log.e("database", " all tables has been Created.");
    }

    public void open() {
        database = getWritableDatabase();
        Log.e("database", "Database is open.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String drop_query = "DROP TABLE IF EXISTS ";
            db.execSQL(drop_query + USER_TABLE);
            db.execSQL(drop_query + USER_TO_GROUP_TABLE);
            db.execSQL(drop_query + EVENT_DAY_TABLE);
            db.execSQL(drop_query + INSTAGRAM_TABLE);
            db.execSQL(drop_query + GENERAL_TABLE);
            db.execSQL(drop_query + IDEA_BOX_TABLE);
            db.execSQL(drop_query + OUT_REQUEST_TABLE);
            db.execSQL(drop_query + LOAN_REQUEST_TABLE);
            onCreate(db);
    }

    public void insertUser(User user) {
        Log.e("database", "Inserting new user.");
        ContentValues values = new ContentValues();
        int editContent = 0;
        int editSchedule = 0;
        String type = "student";
        if (user instanceof UserShinshin) {
            type = "shinshin";
            editContent = 1;
            editSchedule = 1;
        } else if (user instanceof UserEducator) {
            type = "educator";
            editContent = 1;
            editSchedule = 1;
        }
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_FNAME, user.getFirstName());
        values.put(COLUMN_LNAME, user.getLastName());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_EDIT_CONTENT, editContent);
        values.put(COLUMN_EDIT_SCHEDULE, editSchedule);
        values.put(COLUMN_PHONE_NUMBER,user.getPhoneNumber());

        database.insert(USER_TABLE, null, values);
        insertUserToGroup(user);

        Log.i("database", "User has been created.");
    }

    public String getUserNameById(long id){
        Cursor cursor = database.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = " + id,null);

        if (cursor.getCount()>0){
            cursor.moveToNext();
            String fname = cursor.getString(cursor.getColumnIndex(COLUMN_FNAME));
            String lname = cursor.getString(cursor.getColumnIndex(COLUMN_LNAME));


            return fname + " " + lname;
        }
        return "";
    }

    public User getUserById(long id){
        Cursor cursor = database.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = " + id,null);
        User user;
        if (cursor.getCount()>0){
            cursor.moveToNext();
            String fname = cursor.getString(cursor.getColumnIndex(COLUMN_FNAME));
            String lname = cursor.getString(cursor.getColumnIndex(COLUMN_LNAME));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
            boolean editContent = false;
            if (cursor.getInt(cursor.getColumnIndex(COLUMN_EDIT_CONTENT)) == 1)
                editContent = true;

            boolean editSchedule = false;
            if (cursor.getInt(cursor.getColumnIndex(COLUMN_EDIT_SCHEDULE)) == 1)
                editSchedule = true;

            if (type.equals("student"))
                user = new UserStudent(fname, lname, id, password, getUserGroup(id), editContent, editSchedule,phoneNumber);
            else if (type.equals("educator"))
                user = new UserEducator(fname, lname, id, password, getUserGroup(id),phoneNumber);
            else
                user = new UserShinshin(fname, lname, id, password, getUserGroup(id),phoneNumber);

            return user;

        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql_str = "SELECT * FROM " + USER_TABLE;
        Cursor cursor = database.rawQuery(sql_str, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                String fname = cursor.getString(cursor.getColumnIndex(COLUMN_FNAME));
                String lname = cursor.getString(cursor.getColumnIndex(COLUMN_LNAME));
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
                boolean editContent = false;
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_EDIT_CONTENT)) == 1)
                    editContent = true;

                boolean editSchedule = false;
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_EDIT_SCHEDULE)) == 1)
                    editSchedule = true;

                if (type.equals("student"))
                    users.add(new UserStudent(fname, lname, id, password, getUserGroup(id), editContent, editSchedule,phoneNumber));
                else if (type.equals("educator"))
                    users.add(new UserEducator(fname, lname, id, password, getUserGroup(id),phoneNumber));
                else
                    users.add(new UserShinshin(fname, lname, id, password, getUserGroup(id),phoneNumber));
            }
        }
        return users;
    }


    public void insertUserToGroup(User user) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < user.getGroup().size(); i++) {
            values.put(GROUP_ID, user.getGroup().get(i));
            values.put(COLUMN_ID, user.getId());
            database.insert(USER_TO_GROUP_TABLE, null, values);
        }
    }

    public String getAllUserToGroup() {
        String allUsersToGroups = "";
        String query = "SELECT * FROM " + USER_TO_GROUP_TABLE;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                allUsersToGroups += "{ GroupId:" + cursor.getString(cursor.getColumnIndex(GROUP_ID)) +
                        ", UserId: " + cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) + " } ";
            }
        }
        return allUsersToGroups;
    }

    public ArrayList<String> getUserGroup(long id) {
        ArrayList<String> groups = new ArrayList<>();
        String query = "SELECT * FROM " + USER_TO_GROUP_TABLE;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (id == cursor.getLong(cursor.getColumnIndex(COLUMN_ID))) {
                    groups.add(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
                }

            }
        }
        return groups;
    }

    public void updateUser(User user) {
        String type = "student";
        if (user instanceof UserEducator)
            type = "educator";
        else if (user instanceof UserShinshin)
            type = "shinshin";

        ContentValues values = new ContentValues();
        values.put(COLUMN_FNAME, user.getFirstName());
        values.put(COLUMN_LNAME, user.getLastName());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_PHONE_NUMBER,user.getPhoneNumber());
        if (user instanceof UserStudent) {
            if (((UserStudent) user).isEditSchedule())
                values.put(COLUMN_EDIT_SCHEDULE, 1);
            else
                values.put(COLUMN_EDIT_SCHEDULE, 0);
            if (((UserStudent) user).isEditContent())
                values.put(COLUMN_EDIT_CONTENT, 1);
            else
                values.put(COLUMN_EDIT_CONTENT, 0);
        }
        else
            values.put(COLUMN_EDIT_SCHEDULE,1);
            values.put(COLUMN_EDIT_CONTENT,1);

        database.update(USER_TABLE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});

        ContentValues groupValues = new ContentValues();
        groupValues.put(GROUP_ID, user.getGroup().get(0));
        database.update(USER_TO_GROUP_TABLE, groupValues, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(User user) {
        database.delete(USER_TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        database.delete(USER_TO_GROUP_TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        database.delete(ATTENDANCE_TABLE, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public User loginUser(long id, String password) {

        String selectUserQuery = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor;
        try {
            cursor = database.rawQuery(selectUserQuery, null);
        } catch (NullPointerException npe) {
            Log.e("userNotFound","exception");

            return null;
        }
        if (cursor.getCount() <= 0) {
            Log.e("userNotFound","exception");
                return null;
        }
        if (cursor.moveToNext()) {
            Log.e("user ",cursor.getString(cursor.getColumnIndex(COLUMN_FNAME)) +  " " + cursor.getString(cursor.getColumnIndex(COLUMN_LNAME)) + " " + cursor.getLong(cursor.getColumnIndex(COLUMN_ID)) + " " + cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            if (cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)).equals(password)) {
                String fName = cursor.getString(cursor.getColumnIndex(COLUMN_FNAME));
                String lName = cursor.getString(cursor.getColumnIndex(COLUMN_LNAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
                User user;
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                ArrayList<String> groups = getUserGroup(id);

                if (type.equals("educator")) {
                    user = new UserEducator(fName, lName, id, password, groups,phoneNumber);
                }
                else if(type.equals("student")){
                    int editContentInteger = cursor.getInt(cursor.getColumnIndex(COLUMN_EDIT_CONTENT));
                    boolean editContent = false;
                    int editScheduleInteger = cursor.getInt(cursor.getColumnIndex(COLUMN_EDIT_SCHEDULE));
                    boolean editSchedule = false;

                    if (editContentInteger == 1)
                        editContent = true;
                    if (editScheduleInteger == 1)
                        editSchedule = true;

                    user = new UserStudent(fName, lName, id, password, groups, editContent, editSchedule,phoneNumber);
                }
                else {
                    user = new UserShinshin(fName,lName,id,password,groups,phoneNumber);
                }
                return user;
            }
        }

        return null;
    }

    public String getUserPhoneById(long id){
        Cursor cursor = database.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = " + id,null);

        if (cursor.getCount()>0){
            cursor.moveToNext();
            String phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
            return phoneNumber;
        }
        return "";
    }

    public void insertMyUser(){
        ArrayList<String> groups = new ArrayList<>();
        groups.add("שכבה ז'");
        UserEducator userEducator = new UserEducator("אריאל", "ליפנהולץ", 123456789,"אריאלהמדריך123",groups,"0507900567");
        insertUser(userEducator);
    }

}
