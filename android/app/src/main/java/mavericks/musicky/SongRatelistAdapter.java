package mavericks.musicky;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MEET on 21-Nov-16.
 */

public class SongRatelistAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;
    public ArrayList<Integer> mUserPref;

    public SongRatelistAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
        mUserPref = new ArrayList<>();
        for(int i=0; i<items.size(); i++) mUserPref.add(-1);
        Log.e("size",Integer.toString(mUserPref.size()));
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.songlist_listitem, parent, false);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
        ratingBar.setRating(0);
        Log.e("INSIDE",((Integer) mDataSource.size()).toString());
        for(int i=0; i<mDataSource.size();i++)
            Log.e("INSIDE",mDataSource.get(i));


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                 mUserPref.set(position,(int) (ratingBar.getRating()));

            }
        });
        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        textView.setText(mDataSource.get(position));

        return rowView;
    }
}
