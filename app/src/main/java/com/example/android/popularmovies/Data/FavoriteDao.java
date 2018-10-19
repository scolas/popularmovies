package com.example.android.popularmovies.Data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.Model.Movie;

import java.util.List;


/**
 * Created by scott on 10/16/18.
 */

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM Favorites")
    LiveData<List<Movie>> getAllFavoriteMovies();


    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);
}
