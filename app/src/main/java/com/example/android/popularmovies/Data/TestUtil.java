package com.example.android.popularmovies.Data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 7/22/18.
 */

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ID, "189");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ID, "199");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ID, "12343424");
        list.add(cv);


        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (FavoriteContract.FavoriteEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }
}
