/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String DYNAMIC_MOVIE_URL =
            "https://api.themoviedb.org/3/movie/";

    private static final String SORT_MOVIE_URL =
            "https://api.themoviedb.org/3/discover/movie/";

    private static final String STATIC_POPULAR_URL =
            "http://api.themoviedb.org/3/movie/";


    private static final String POPULAR_BASE_URL = STATIC_POPULAR_URL;

    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String find = "find";

    private static final String API = "api_key";
    private static final String api_key = "f6adb7bb919d0b253c04644930bc8df3";
    /* The number of days we want our API to return */


    final static String QUERY_PARAM = "q";


    //http://api.themoviedb.org/3/movie/popular?api_key=f6adb7bb919d0b253c04644930bc8df3

   // https://api.themoviedb.org/3/movie/383498?api_key=f6adb7bb919d0b253c04644930bc8df3
    public static URL buildUrl() {
        // COMPLETED (1) Fix this method to return the URL used to query Open Weather Map's API
        Uri builtUri = Uri.parse(POPULAR_BASE_URL).buildUpon()
                .appendPath("popular")
                .appendQueryParameter(API, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    //http://api.themoviedb.org/3/movie/top_rated?api_key=[API_KEY]
    public static URL buildTopUrl() {
        // COMPLETED (1) Fix this method to return the URL used to query Open Weather Map's API
        Uri builtUri = Uri.parse(DYNAMIC_MOVIE_URL).buildUpon()
                .appendPath("top_rated")
                .appendQueryParameter(API, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
//https://api.themoviedb.org/3/movie/383498?api_key=f6adb7bb919d0b253c04644930bc8df3
    //http://api.themoviedb.org/3/discover/movie?sort_by=highest_rated.desc&api_key=f6adb7bb919d0b253c04644930bc8df3&page=1
    // http://api.themoviedb.org/3/discover/movie?sort_by=most_popular.desc&api_key=f6adb7bb919d0b253c04644930bc8df3&page=1
    public static URL buildUrl(String id) {
        // COMPLETED (1) Fix this method to return the URL used to query Open Weather Map's API
        Uri builtUri = Uri.parse(DYNAMIC_MOVIE_URL).buildUpon()
                .appendPath(""+id+"")
                .appendQueryParameter(API, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
    //http://api.themoviedb.org/3/movie/299536/reviews?api_key=f6adb7bb919d0b253c04644930bc8df3
    public static URL buildReviewUrl(String id) {
        // COMPLETED (1) Fix this method to return the URL used to query Open Weather Map's API
        Uri builtUri = Uri.parse(DYNAMIC_MOVIE_URL).buildUpon()
                .appendPath(""+id+"")
                .appendPath("reviews")
                .appendQueryParameter(API, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
    public static URL buildSortUrl(String type) {
        // COMPLETED (1) Fix this method to return the URL used to query Open Weather Map's API
        Uri builtUri = Uri.parse(SORT_MOVIE_URL).buildUpon()
                .appendQueryParameter("sort_by",""+type+".desc")
                .appendQueryParameter(API, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.v(TAG, "Built URI " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v(TAG, "ERROR Built URI " + url);
        }


        return url;
    }
    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The Url to use to query the weather server.
     */
    public static URL buildUrl(Double lat, Double lon) {
        /** This will be implemented in a future lesson **/
        return null;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}