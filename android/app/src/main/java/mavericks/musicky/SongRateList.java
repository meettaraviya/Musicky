package mavericks.musicky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongRateList extends AppCompatActivity {
    private ListView mListView;

    Map<String,String> rating;
    String response;
    Context context;
    String unique_id;
    public JSONArray songs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.songrate_list);
        response = getIntent().getExtras().getString("songs");
        unique_id = getIntent().getExtras().getString("id");
        //Log.e("NewResp",response);
        mListView = (ListView) findViewById(R.id.songratelist_list);
// 1
        rating = new HashMap<>();
        final ArrayList<String> listItems = new ArrayList<>();
// 3
        try{
            songs = new JSONArray(response);
            //Log.e("songs",songs.toString());
            for(Integer i=0; i<songs.length(); i++){

                listItems.add(songs.getJSONObject(i).getString("pk"));
                //rating.put(songs.getJSONObject(i).getJSONObject("fields").getString("name"),"-1");
                Log.e("song name",songs.getJSONObject(i).getString("pk"));
            }
            Log.e("i","1");
        }
        catch (JSONException e){
            listItems.add("Error !");
        }
// 4
        final SongRatelistAdapter adapter = new SongRatelistAdapter(this , listItems);
        mListView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.songratelist_submit);

        Log.e("i","2");
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer n=0;
                        try {
                            for (Integer i = 0; i < adapter.mUserPref.size(); i++) {
                                if (adapter.mUserPref.get(i) != -1) {
                                    Log.e("i2",i.toString());
                                    rating.put(songs.getJSONObject(i).getString("pk"), adapter.mUserPref.get(i).toString());

                                    n++;
                                }
                            }
                        }
                        catch (JSONException e){
                            Toast.makeText(context,"There was some error!",Toast.LENGTH_SHORT).show();
                            Log.e("JSON","SRL");
                        }
                        Log.e("RATEING",n.toString());

                        Log.e("n",Integer.toString(n));
                        if(n<1){
                            Toast.makeText(getApplicationContext(),"Please rate atleast 3 songs",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e("Do","send rating");
                            Sendrating sendrating = new Sendrating();
                            new Thread(sendrating,"sendrating").start();
                        }

                    }
                }
        );
    }
    private class Sendrating implements Runnable {
        public Sendrating() {

        }

        @Override
        public void run() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.base_url) + "api/rate/";
            Log.e("URL", url);
            Log.e("LOGIN", "sending request...");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            if(responseString=="-1") {
                                Log.e("ERR","-1 resp");
                                Toast.makeText(getApplicationContext(),"Some error ocurred 2",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                Log.e("NO ERR",responseString);
                                Intent intent = new Intent(context,Dashboard.class);
                                intent.putExtra("id",unique_id);
                                intent.putExtra("response",responseString);
                                startActivity(intent);
                            }



                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN", "response not received");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {


                    rating.put("id",unique_id);

                    return rating;
                }

            };

            queue.add(stringRequest);
        }
    }
}
