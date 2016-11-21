package mavericks.musicky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Categories extends AppCompatActivity {
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);

        mListView = (ListView) findViewById(R.id.categories_list);
// 1
        final ArrayList<String> listItems = new ArrayList<>();
// 3
        listItems.add("Pop");
        listItems.add("Rock");
        listItems.add("Jazz");
        listItems.add("EDM");
// 4
        Log.e("list",listItems.get(3));
        final CategoriesAdapter adapter = new CategoriesAdapter(this , listItems);
        mListView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.categories_submit);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean somethingSelected = false;
                        int n=0;
                        for(int i=0; i<adapter.mUserPref.size(); i++){
                            if(adapter.mUserPref.get(i)){
                                somethingSelected =true;
                                n++;
                            }
                        }
                        String [] userPrefStrings = new String[n];
                        n=0;
                        for(int i=0; i<n; i++){
                            while(!adapter.mUserPref.get(n)) n++;
                            userPrefStrings[i] = listItems.get(n);
                        }
                        if(!somethingSelected){
                            Toast.makeText(getApplicationContext(),"Please select atleast 1 category",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), SongList.class);
                            intent.putExtra("favcategs",userPrefStrings);
                            startActivity(intent);
                        }

                    }
                }
        );
    }
    public class Getsongs extends AsyncTask<Void, Void, Void> {
        // Get user defined values

        @Override
        protected void onPreExecute(){
            //dialog = ProgressDialog.show(MainFeedActivity.this, null, "Posting...");
        }


        @Override
        protected Void doInBackground(Void Params[]){
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://www.google.com";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR","That didn't work!");
                }
            }){
            @Override
                protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                // Parameters will be feedback_id, pk with answers, token.
                params.put("token", token);
                params.put("feedback_id", ((Integer)pk).toString());


                return params;                }
            };
            queue.add(stringRequest);
        }
    }

}
