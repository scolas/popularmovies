package com.example.android.popularmovies.Model;

import java.util.List;

/**
 * Created by scott on 5/27/18.
 * original title
 movie poster image thumbnail
 A plot synopsis (called overview in the api)
 user rating (called vote_average in the api)
 release date
 */

public class Movie {

    private String mTitle;
    private List<String> alsoKnownAs = null;
    private String mPlot;
    private String mRating;
    private String mDate;
    private String mImage;

    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(String title, String plot, String rating, String date, String image, List<String> ingredients) {
        this.mTitle = title;
        this.mPlot = plot;
        this.mRating = rating;
        this.mImage = image;
        this.mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
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

}
