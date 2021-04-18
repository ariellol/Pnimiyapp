package com.lipstudio.pnimiyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static com.lipstudio.pnimiyapp.UserHelper.*;

public class InstagramHelper extends SQLiteOpenHelper {

    Context context;

    public static final String POST_ID = "postId";
    public static final String POST_DESCRIPTION = "postDescription";
    public static final String USER_ID = "userId";
    public static final String IMAGE_LINK = "imageLink";
    public static final String URL_LINK = "urlLink";

    public static final String INSTAGRAM_TABLE = "InstagramTable";

    public static final String CREATE_INSTAGRAM_TABLE = "CREATE TABLE IF NOT EXISTS " + INSTAGRAM_TABLE +
            " (" + POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + POST_DESCRIPTION + " VARCHAR, " + IMAGE_LINK + " VARCHAR, " + URL_LINK + ", "
            + USER_ID + " INTEGER, "+ INSTAGRAM_TABLE + " REAL);";



    SQLiteDatabase database;

    public InstagramHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void open(){
        database = getWritableDatabase();
    }


    public long insertNewPost(Post post){

        ContentValues postValues = new ContentValues();
        postValues.put(POST_DESCRIPTION,post.getDescription());
        postValues.put(IMAGE_LINK,post.getImageLink());
        postValues.put(URL_LINK,post.getUrlLink());
        postValues.put(USER_ID,post.getUserId());

        return database.insert(INSTAGRAM_TABLE,null,postValues);
    }

    public ArrayList<Post> getAllPosts(){
        ArrayList<Post> posts = new ArrayList<>();
        String select_query = "SELECT * FROM " + INSTAGRAM_TABLE + " ORDER BY " + POST_ID    + " DESC";
        Cursor cursor = database.rawQuery(select_query,null);

        while (cursor.moveToNext()){
            if (cursor.getCount()>0){
                String postDescription = cursor.getString(cursor.getColumnIndex(POST_DESCRIPTION));
                String imageUri = cursor.getString(cursor.getColumnIndex(IMAGE_LINK));
                String linkUri = cursor.getString(cursor.getColumnIndex(URL_LINK));
                long userId = cursor.getLong(cursor.getColumnIndex(USER_ID));
                long postId = cursor.getLong(cursor.getColumnIndex(POST_ID));
                posts.add(new Post(postDescription,imageUri,linkUri,postId,userId));
            }
        }
        return posts;
    }

    public void deletePost(long postId){
        database.delete(INSTAGRAM_TABLE,POST_ID + " = ?",new String[]{String.valueOf(postId)});
    }

    public void updatePost(Post post){
        ContentValues postNewValues = new ContentValues();
        postNewValues.put(POST_DESCRIPTION,post.getDescription());
        postNewValues.put(IMAGE_LINK,post.getImageLink());
        postNewValues.put(URL_LINK,post.getUrlLink());

        database.update(INSTAGRAM_TABLE,postNewValues,POST_ID + " = ?", new String[]{String.valueOf(post.getPostId())});
    }
}
