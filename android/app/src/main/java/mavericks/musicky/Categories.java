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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.Scanner;

public class Categories extends AppCompatActivity {
    private ListView mListView;
    Context context;
    public String unique_id;
    public String final_response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
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

                        ArrayList<String> userPrefStrings = new ArrayList<String>();
                        for(int i=0; i<adapter.mUserPref.size(); i++){
                            if(adapter.mUserPref.get(i)){
                                somethingSelected =true;
                                userPrefStrings.add(listItems.get(i));

                            }
                        }
                        if(!somethingSelected){
                            Toast.makeText(getApplicationContext(),"Please select atleast 1 category",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            EditText mText=(EditText) findViewById(R.id.search_text);
                            String search=mText.getText().toString();
                            searchAndFind search_online = new searchAndFind(search);
                            new Thread(search_online,"API_Search").start();
                            SendCategs sendCategs = new SendCategs(userPrefStrings);
                            new Thread(sendCategs,"SendCategs").start();
                        }

                    }
                }
        );

    }
    private class searchAndFind implements Runnable {
        String inp1;
        public searchAndFind(String input)
        {
            inp1=input;
        }

        @Override
        public void run() {
            try {
                String base1 = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyAI5YtqVjFmlp-2Y4r4gPM4wZ2DAQDwL5M&q=";
                base1=base1+inp1;
                URL final_s1=new URL(base1);
                URLConnection connect= final_s1.openConnection();
                InputStream response = connect.getInputStream();
                String inputStreamString = new Scanner(response,"UTF-8").useDelimiter("\\A").next();
                Log.e("RESPONSEEEEEEEEEEEEEEEE",inputStreamString);
//
//
//                BufferedReader streamReader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
//                StringBuilder responseStrBuilder = new StringBuilder();
//                String inputStr;
//
//
//                while ((inputStr = streamReader.readLine()) != null) {
//                    responseStrBuilder.append(inputStr);
//                }
                JSONObject response_JSON=new JSONObject(inputStreamString);
//                Log.e("TIefeeevEEEEEEE",response.toString());


                String resp= response_JSON.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId");
                final_response=resp;
                Log.e("TIMEEEEEEEEE",final_response);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private class SendCategs implements Runnable {
        ArrayList<String>  fc;
        public SendCategs(ArrayList<String> favCategs) {
            fc = favCategs;
            for(int i=0; i<favCategs.size(); i++) Log.e("favcat",favCategs.get(i));
            for(int i=0; i<fc.size(); i++) Log.e("fc",fc.get(i));
        }

        @Override
        public void run() {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = getString(R.string.base_url) + "api/getlist/";
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
                                Intent intent = new Intent(context,SongRateList.class);
                                intent.putExtra("songs",responseString);
                                intent.putExtra("id",unique_id);
                                intent.putExtra("searchfield",final_response);  
                                startActivity(intent);
                            }



                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN", "response not received");
                    for(int i=0; i<fc.size(); i++) if(fc.get(i)!=null) Log.e("RESP",fc.get(i));
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    SecureRandom random = new SecureRandom();
                    unique_id = new BigInteger(20, random).toString(32);
                    params.put("id",unique_id);

                    for(Integer i=0; i<fc.size(); i++){
                        params.put(i.toString(),fc.get(i));
                        Log.e("WEEEES",fc.get(i));
                    }
                    return params;
                }

            };

            queue.add(stringRequest);
        }
    }
}
