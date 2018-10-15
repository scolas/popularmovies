package com.example.android.popularmovies.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;

/**
 * Created by scott on 6/2/18.
 */

public class SortPreferences {




    static public void setSort(int id) {
        /** This will be implemented in a future lesson **/
    }





    public static String getPreferredSorting(Context context) {
        // COMPLETED (1) Return the user's preferred location
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String keyForSort = context.getString(R.string.pref_sort_key);
        String defaultSort = context.getString(R.string.pref_sort_default);
        String preferredSort = prefs.getString(keyForSort, defaultSort);
        String sort = context.getString(R.string.highest_rated);
        String highest_rated = context.getString(R.string.highest_rated);
        String fav = context.getString(R.string.favs);
        String userPrefersSort;


        //if (sort.equals(preferredSort)) {
        if (preferredSort.equals(highest_rated)) {
            userPrefersSort = "highest_rated";
        }//else if (sort.equals(fav)){
        else if (preferredSort.equals(fav)){
            userPrefersSort = "favorite";
        }else {
            userPrefersSort = "most_popular";
        }
        return userPrefersSort;
        //return prefs.getString(keyForSort, defaultSort);
    }



    public static boolean isSort(Context context) {
        /** This will be implemented in a future lesson **/
        return true;
    }


    private static String getSortType() {
        /** This will be implemented in a future lesson **/
        return "";
    }


}
