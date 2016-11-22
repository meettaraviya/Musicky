package mavericks.musicky;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public String unique_id;
    public String response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        unique_id = getIntent().getExtras().getString("id");
        response = getIntent().getExtras().getString("response");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ListView mListView = (ListView) findViewById(R.id.recoms_list);
        try{
            final ArrayList<String> recomtext = new ArrayList<>();
            JSONArray recoms = new JSONObject(response).getJSONArray("recom");
            for(int i=0; i<recoms.length(); i++){
                recomtext.add(recoms.getJSONObject(i).getString("pk"));
            }
            SongListAdapter adapter = new SongListAdapter(this, recomtext);
            mListView.setAdapter(adapter);

            mListView = (ListView) findViewById(R.id.mysongs_list);
            final ArrayList<String> msongstext = new ArrayList<>();
            JSONArray msongs = new JSONObject(response).getJSONArray("mysongs");
            for(int i=0; i<msongs.length(); i++){
                msongstext.add(msongs.getJSONObject(i).getString("pk"));
                Log.e("mysong",msongs.getJSONObject(i).getString("pk"));
            }
            adapter = new SongListAdapter(this, recomtext);
            mListView.setAdapter(adapter);

        }
        catch (Exception e){
            Log.e("JSON",e.toString());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        View l = getLayoutInflater().inflate(R.layout.content_dashboard,null);
        if(l==null) Log.e("null0","nn");
        View mysongsview = l.findViewById(R.id.mysongs_list);
        View searchview = l.findViewById(R.id.search);
        View recomview = l.findViewById(R.id.recoms_list);


        if (id == R.id.nav_mysongs) {
            Log.e("ND","mysongs");
            mysongsview.setVisibility(View.VISIBLE);
            recomview.setVisibility(View.GONE);
            searchview.setVisibility(View.GONE);
        } else if (id == R.id.nav_recom) {
            Log.e("ND","recom");
            mysongsview.setVisibility(View.GONE);
            searchview.setVisibility(View.GONE);
            recomview.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_search) {
            Log.e("ND","search");
            searchview.setVisibility(View.VISIBLE);
            recomview.setVisibility(View.GONE);
            mysongsview.setVisibility(View.GONE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
