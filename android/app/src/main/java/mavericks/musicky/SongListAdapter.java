package mavericks.musicky;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import static mavericks.musicky.R.id.textView;

/**
 * Created by MEET on 22-Nov-16.
 */

public class SongListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;
    private String final_response;

    public SongListAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.soglist_item, parent, false);
        final TextView textView = (TextView) rowView.findViewById(R.id.textView2);
        textView.setText(mDataSource.get(position));
        Log.e("INSIDE",((Integer) mDataSource.size()).toString());
        for(int i=0; i<mDataSource.size();i++)
            Log.e("INSIDE",mDataSource.get(i));
        textView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String search = textView.getText().toString();
                        searchAndFind searchi = new searchAndFind(search);
                        new Thread(searchi,"temp").start();
                        Intent intent = new Intent(mContext,MusicActivity.class);
                        intent.putExtra("searchfield",final_response);
                        mContext.startActivity(intent);
                    }
                }
        );

        return rowView;
    }
    class searchAndFind implements Runnable {
        String inp1;

        public searchAndFind(String input) {
            inp1 = input;
        }

        @Override
        public void run() {
            try {
                String base1 = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyAI5YtqVjFmlp-2Y4r4gPM4wZ2DAQDwL5M&q=";
                inp1 = inp1.replace(' ','+');
                base1 = base1 + inp1;
                URL final_s1 = new URL(base1);
                URLConnection connect = final_s1.openConnection();
                InputStream response = connect.getInputStream();
                String inputStreamString = new Scanner(response, "UTF-8").useDelimiter("\\A").next();

                JSONObject response_JSON = new JSONObject(inputStreamString);
//                Log.e("TIefeeevEEEEEEE",response.toString());


                String resp = response_JSON.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId");
                final_response = resp;
                Log.e("TIMEEEEEEEEE", final_response);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
