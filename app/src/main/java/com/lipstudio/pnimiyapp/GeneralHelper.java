package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_NAME;
import static com.lipstudio.pnimiyapp.UserHelper.DATABASE_VERSION;

public class GeneralHelper extends SQLiteOpenHelper {

    public static final String GENERAL_TABLE = "generalTable";

    public static final String QUOTE_COLUMN = "quoteColumn";
    public static final String ANNOUNCEMENT_COLUMN = "announcementColumn";
    public static final String ROW_ID = "rowId";

    public static final String CREATE_GENERAL_TABLE = "CREATE TABLE IF NOT EXISTS " + GENERAL_TABLE +
            " (" + QUOTE_COLUMN + " VARCHAR, " + ANNOUNCEMENT_COLUMN + " VARCHAR, " + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GENERAL_TABLE + " REAL)";

    SQLiteDatabase database;

    public GeneralHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void open() {
        database = getWritableDatabase();
    }

    public void insertQuoteAndAnnouncement() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUOTE_COLUMN, "ציטוט כאן");
        contentValues.put(ANNOUNCEMENT_COLUMN, "הודעות ופרסומים קצרים כאן");
        database.insert(GENERAL_TABLE, null, contentValues);
    }

    public void updateQuote(String quote) {

        ContentValues quoteValue = new ContentValues();
        quoteValue.put(QUOTE_COLUMN, quote);
        database.update(GENERAL_TABLE, quoteValue, ROW_ID + " = ?", new String[]{String.valueOf(1)});
    }

    public void updateAnnouncement(String announcement) {

        ContentValues announcementValue = new ContentValues();
        announcementValue.put(ANNOUNCEMENT_COLUMN, announcement);
        database.update(GENERAL_TABLE, announcementValue, ROW_ID + " = ?", new String[]{String.valueOf(1)});
    }

    public String[] getQuoteAndAnnouncement(){
        String generalDetails[] = new String[2];

        String getFirst = "SELECT * FROM " + GENERAL_TABLE + " ORDER BY " + ROW_ID +" ASC LIMIT 1";

        Cursor cursor = database.rawQuery(getFirst,null);
        if (cursor.getCount()>0) {
            cursor.moveToNext();
            generalDetails[0] = cursor.getString(cursor.getColumnIndex(QUOTE_COLUMN));
            generalDetails[1] = cursor.getString(cursor.getColumnIndex(ANNOUNCEMENT_COLUMN));
            return generalDetails;
        }
        return null;
    }
}
