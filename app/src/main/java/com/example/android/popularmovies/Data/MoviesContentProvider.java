package com.example.android.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.UriMatcher;



import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Created by scott on 9/18/18.
 */

public class MoviesContentProvider extends ContentProvider {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "favorites";
    public static final String COLUMN_FAVORITE_ID = "ID";
    public static final String COLUMN_FAVORITE_POSTER_PATH = "Path";
    public static final int Movie = 100;
    public static final int Moive_Fav = 101;
    public static final int Moive_ID = 102;
    public static final UriMatcher uriMatcher = buildUriMatcher();
    public static final String movie_path = "movies";
    private FavoriteDbHelper DatabaseHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        //DatabaseHelper dbHelper = new DatabaseHelper(context);
        DatabaseHelper = new FavoriteDbHelper(context);
        return true;
    }

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.movie_path, Moive_Fav);
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.movie_path+"/#",Moive_ID);
        return uriMatcher;
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = DatabaseHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor mCursor;
        //"content://com.example.android.popularmovies.Data"
        switch (match){
            case Movie:
                mCursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case Moive_Fav:
                mCursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db =  DatabaseHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        Uri finalUri;

        switch (match) {
            case Movie:
                long id = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    finalUri = ContentUris.withAppendedId(FavoriteContract.FavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);


        return  finalUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = DatabaseHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case Moive_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }





    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ID + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_POSTER_PATH + " TEXT NOT NULL " +
                "); ";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_FAVORITE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
            onCreate(db);
        }
    }
}
