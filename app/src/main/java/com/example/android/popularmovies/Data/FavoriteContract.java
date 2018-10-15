package com.example.android.popularmovies.Data;

/**
 * Created by scott on 7/22/18.
 */
import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {
    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI =  Uri.parse("content://"+AUTHORITY);

    public static final String movie_path = "movies";



    // COMPLETED (1) Create an inner class named WaitlistEntry class that implements the BaseColumns interface
    public static final class FavoriteEntry implements BaseColumns {
        // COMPLETED (2) Inside create a static final members for the table name and each of the db columns

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_FAVORITE_ID = "ID";
        public static final String COLUMN_FAVORITE_POSTER_PATH = "Path";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(movie_path).build();

    }

}
