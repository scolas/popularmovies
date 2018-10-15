package com.example.android.popularmovies.RetroRequest;

/**
 * Created by scott on 7/30/18.
 */


import com.example.android.popularmovies.Model.AllMovieRequest;
import com.example.android.popularmovies.Model.AllReviewRequest;
import com.example.android.popularmovies.Model.AllTrailerRequest;

import retrofit2.http.Query;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface MovieApiEndpoint {

    @GET("movie/top_rated")
    retrofit2.Call<AllMovieRequest> getTopRated(@Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    retrofit2.Call<AllReviewRequest> getReviews(@Path("id") String id, @Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    retrofit2.Call<AllTrailerRequest> getMovieTrailers(@Path("id") String id, @Query("api_key") String api_key);



}
