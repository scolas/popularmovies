package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Data.FavoriteContract;

/**
 * Created by scott on 7/22/18.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private int mCount;

    public FavoriteListAdapter(Context context) {this.mContext = context;}

    public FavoriteListAdapter(Context context, int count) {
        this.mContext = context;
        this.mCount = count;
        //this.mCursor = cursor;
    }

    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorite_list_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {


        //String name = mCursor.getString(mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_FAVORITE_ID));
        String name = "Trailer 1";


        holder.trailerTextView.setText(name);
        // COMPLETED (9) Set the holder's partySizeTextView text to the party size
        // Display the party count
    }

    @Override
    public int getItemCount() {
       // return mCursor.getCount();
        return mCount;
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        TextView trailerTextView;

        public FavoriteViewHolder(View itemView){
            super(itemView);
            trailerTextView = (TextView) itemView.findViewById(R.id.favorite_text_view);
        }

    }


}
