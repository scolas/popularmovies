package com.example.android.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.android.popularmovies.Data.FavoriteContract.*;
import com.example.android.popularmovies.MainActivity;

/**
 * Created by scott on 7/22/18.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "favorites.db";


    private static final int DATABASE_VERSION = 2;

    public Context context;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FavoriteEntry.COLUMN_FAVORITE_ID + " TEXT NOT NULL, " +
                    FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH + " TEXT NOT NULL " +
                    "); ";


            sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
        }
        catch (SQLException e ){
            Toast.makeText(context, " Error mysqli !"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
