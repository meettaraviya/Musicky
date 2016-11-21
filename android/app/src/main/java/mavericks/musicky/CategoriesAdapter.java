package mavericks.musicky;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by MEET on 21-Nov-16.
 */

public class CategoriesAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;
    public ArrayList<Boolean> mUserPref;

    public CategoriesAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
        mUserPref = new ArrayList<>();
        for(int i=0; i<items.size(); i++) mUserPref.add(false);
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
        View rowView = mInflater.inflate(R.layout.categories_listitem, parent, false);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        checkBox.setText(mDataSource.get(position));
        Log.e("INSIDE",((Integer) mDataSource.size()).toString());
        for(int i=0; i<mDataSource.size();i++)
        Log.e("INSIDE",mDataSource.get(i));
        checkBox.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUserPref.set(position,!mUserPref.get(position));
                    }
                }
        );

        return rowView;
    }
}
