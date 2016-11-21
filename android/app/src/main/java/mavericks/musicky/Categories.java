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
                            EditText mText=(EditText) findViewById(R.id.search_text);
                            String search=mText.getText().toString();
                            Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
                            intent.putExtra("favcategs",userPrefStrings);
                            intent.putExtra("searchfield",search);
                            startActivity(intent);
                        }

                    }
                }
        );
       
    }
}
