package com.example.android.popularmovies.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by scott on 5/27/18.
 * original title
 movie poster image thumbnail
 A plot synopsis (called overview in the api)
 user rating (called vote_average in the api)
 release date
 */
@Entity(tableName = "favorites")
public class Movie {

    @PrimaryKey
    @SerializedName("id")
    private int mId;


    @SerializedName("title")
    private String mTitle;


    @SerializedName("plot")
    private String mPlot;

    @SerializedName("rating")
    private String mRating;

    @SerializedName("release_date")
    private String mDate;

    @SerializedName("image")
    private String mImage;

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(int id, String title, String plot, String rating, String date, String image, List<String> ingredients) {
        this.mId = id;
        this.mTitle = title;
        this.mPlot = plot;
        this.mRating = rating;
        this.mImage = image;
        this.mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }



    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        this.mRating = rating;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getPlot() {
        return mPlot;
    }

    public void setPlot(String plot) {
        this.mPlot = plot;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getImage() {
        return mImage;
    }


    public int getId() {
        return mId;
    }

    public void setId(int Id) {
        this.mId = Id;
    }
}
