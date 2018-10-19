package com.example.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies.Data.AppDatabase;
import com.example.android.popularmovies.Model.Movie;

import java.util.List;

/**
 * Created by scott on 10/17/18.
 */

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> mFavorites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        mFavorites = database.movieFavoritesDao().getAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavorites() {
        return mFavorites;
    }
}
