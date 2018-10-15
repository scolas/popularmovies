package com.example.android.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by scott on 8/1/18.
 */

public class AllTrailerRequest {

    @SerializedName("id")
    private int mId;
    @SerializedName("results")
    private List<Trailer> mTrailers;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        mTrailers = trailers;
    }

}
