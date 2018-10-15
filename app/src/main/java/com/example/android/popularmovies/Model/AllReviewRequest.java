package com.example.android.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by scott on 7/31/18.
 */

public class AllReviewRequest {

    @SerializedName("id")
    private int mId;
    @SerializedName("results")
    private List<Review> mReviews;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }
}
