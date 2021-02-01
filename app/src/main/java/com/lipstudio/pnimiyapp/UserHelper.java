package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.FontsContract;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class UserHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final String USER_TABLE = "usersTable";
    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "userId";
    public static final String COLUMN_FNAME = "firstName";
    public static final String COLUMN_LNAME = "lastName";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TYPE = "userType";

    public static final String GROUP_ID = "groupId";
    public static final String USER_TO_GROUP_TABLE = "userToGroup";
    public static final String CREATE_TABLE_USER_TO_GROUP = "CREATE TABLE IF NOT EXISTS " + USER_TO_GROUP_TABLE +
            " (" + GROUP_ID + " VARCHAR, " + COLUMN_ID + " VARCHAR, "  +  USER_TO_GROUP_TABLE + " REAL)";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE +
            " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_FNAME + " VARCHAR, " +
            COLUMN_LNAME + " VARCHAR, " + COLUMN_PASSWORD + " VARCHAR, " + COLUMN_TYPE + " VARCHAR, "
            + USER_TABLE + " REAL)";

    String[] allColumns ={COLUMN_ID,COLUMN_FNAME,COLUMN_LNAME,COLUMN_PASSWORD,COLUMN_TYPE};
    SQLiteDatabase database;

    public UserHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("database","UsersHelper Created.");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_USER_TO_GROUP);
        Log.e("database"," users table Created.");
    }

    public void open(){
        database = getWritableDatabase();
        Log.e("database","Database is open.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_query = "DROP TABLE IF EXISTS";
        db.execSQL(drop_query + USER_TABLE);
        db.execSQL(drop_query + USER_TO_GROUP_TABLE);
        onCreate(db);
    }

    public void insertUser(User user){
        Log.e("database","Inserting new user.");
        ContentValues values = new ContentValues();

        String type = "student";
        if(user instanceof UserShinshin)
            type = "shinshin";
        else if(user instanceof UserEducator)
            type = "educator";
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_FNAME, user.getFirstName());
        values.put(COLUMN_LNAME, user.getLastName());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_TYPE,type);

        database.insert(USER_TABLE,null,values);
        insertUserToGroup(user);

        Log.i("database","User has been created.");
    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        String sql_str = "SELECT * FROM " + USER_TABLE;
        Cursor cursor = database.rawQuery(sql_str,null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                String fname = cursor.getString(cursor.getColumnIndex(COLUMN_FNAME));
                String lname = cursor.getString(cursor.getColumnIndex(COLUMN_LNAME));
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));

                if(type.equals("student"))
                    users.add(new UserStudent(fname, lname, id, password, getUserGroup(id)));
                else if (type.equals("educator"))
                    users.add(new UserEducator(fname, lname, id, password, getUserGroup(id)));
                else
                    users.add(new UserShinshin(fname, lname, id, password, getUserGroup(id)));
            }
        }

        return users;
    }


    public void insertUserToGroup(User user){
        ContentValues values = new ContentValues();
        for (int i = 0; i < user.getGroup().size(); i++) {
            values.put(GROUP_ID,user.getGroup().get(i));
            values.put(COLUMN_ID,user.getId());
            database.insert(USER_TO_GROUP_TABLE,null,values);
        }
    }

    public String getAllUserToGroup(){
        String allUsersToGroups = "";
        String query = "SELECT * FROM " + USER_TO_GROUP_TABLE;

        Cursor cursor = database.rawQuery(query,null);

        if (cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                allUsersToGroups += "{ GroupId:" + cursor.getString(cursor.getColumnIndex(GROUP_ID)) +
                        ", UserId: " + cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) + " } ";
            }
        }
         return allUsersToGroups;
    }

    public ArrayList<String> getUserGroup(long id){
        ArrayList<String> groups = new ArrayList<>();
        String query = "SELECT * FROM " + USER_TO_GROUP_TABLE;
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                if(id == cursor.getLong(cursor.getColumnIndex(COLUMN_ID))){
                    groups.add(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
                }

            }
        }
        return groups;
    }
}
