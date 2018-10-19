package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by scott on 5/28/18.
 */

public class JsonUtils {
/*"page":1,
        "total_results":19975,
        "total_pages":999,
        "results":[
    {
        "vote_count":1171,
            "id":383498,
            "video":false,
            "vote_average":8,
            "title":"Deadpool 2",
            "popularity":525.876302,
            "poster_path":"\/to0spRl1CMDvyUbOnbb4fTk3VAd.jpg",
            "original_language":"en",
            "original_title":"Deadpool 2",
            "genre_ids":[  ],
        "backdrop_path":"\/3P52oz9HPQWxcwHOwxtyrVV1LKi.jpg",
            "adult":false,
            "overview":"Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life.",
            "release_date":"2018-05-15"
    },*/


    public static String[] parseMoviesJson(String json) throws JSONException {
        Movie movie = new Movie();
        String[] parsedMovieData = null;


        JSONArray pMoviesArray = new JSONArray();
        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesArray = (JSONArray) moviesJson.getJSONArray("results");
        parsedMovieData = new String[moviesArray.length()];


        if (moviesArray != null) {

            for (int i = 0; i < moviesArray.length(); i++) {
                parsedMovieData[i] = moviesArray.getJSONObject(i).getString("poster_path");
            }
             /* try {
                for (int i = 0; i < moviesArray.length(); i++) {
                  JSONObject moviesObj = new JSONObject();
                    moviesObj.put("id", moviesArray.getJSONObject(i).getString("id"));
                    moviesObj.put("title", moviesArray.getJSONObject(i).getString("title"));
                    moviesObj.put("poster_path", moviesArray.getJSONObject(i).getString("poster_path"));
                    moviesObj.put("release_date", moviesArray.getJSONObject(i).getString("release_date"));


                    pMoviesArray.put(moviesObj);
                }
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }*/

        }


        return parsedMovieData;
    }


    public static String[] parseMoviesIdJson(String json) throws JSONException {

        String[] parsedIDData = null;

        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesArray = (JSONArray) moviesJson.getJSONArray("results");
        parsedIDData = new String[moviesArray.length()];


        if (moviesArray != null) {

            for (int i = 0; i < moviesArray.length(); i++) {
                parsedIDData[i] = moviesArray.getJSONObject(i).getString("id");
            }
        }

        return parsedIDData;
    }

    public static Movie parseMovieJson(String json) throws JSONException {
        Movie movie = new Movie();


        JSONObject movieJson = new JSONObject(json);

        movie.setTitle(movieJson.getString("original_title"));
        movie.setImage(movieJson.getString("poster_path"));
        movie.setPlot(movieJson.getString("overview"));
        movie.setRating(movieJson.getString("vote_average"));
        movie.setDate(movieJson.getString("release_date"));


        // f6adb7bb919d0b253c04644930bc8df3


        return movie;
    }

    //http://api.themoviedb.org/3/movie/299536/reviews?api_key=f6adb7bb919d0b253c04644930bc8df3

    public static String[][] parseMovieReviewJson(String json) throws JSONException {
        Movie movie = new Movie();

        String[][] parsedReviewData = null;

        JSONObject movieReviewJson = new JSONObject(json);
        JSONArray moviewReviewArray = (JSONArray) movieReviewJson.getJSONArray("results");
        //parsedReviewData = new ArrayList[moviewReviewArray.length()];

        if (moviewReviewArray != null) {
            try {
                for (int i = 0; i < moviewReviewArray.length(); i++) {
                    String author, content;
                    String[] reviews = {};


                    reviews[0] = moviewReviewArray.getJSONObject(i).getString("author");
                    reviews[1] = moviewReviewArray.getJSONObject(i).getString("content");
                    parsedReviewData[i] = reviews;
                }



            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return parsedReviewData;

    }

}
