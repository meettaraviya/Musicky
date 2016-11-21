package mavericks.musicky;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SongRateList extends AppCompatActivity {
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songrate_list);

        mListView = (ListView) findViewById(R.id.songratelist_list);
// 1
        final ArrayList<String> listItems = new ArrayList<>();
// 3
        listItems.add("Pop");
        listItems.add("Rock");
        listItems.add("Jazz");
        listItems.add("EDM");
// 4
        Log.e("list",listItems.get(3));
        final SongRatelistAdapter adapter = new SongRatelistAdapter(this , listItems);
        mListView.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.songratelist_submit);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean somethingSelected = false;
                        Integer n=0;
                        for(int i=0; i<adapter.mUserPref.size(); i++){
                            if(adapter.mUserPref.get(i)!=-1){
                                somethingSelected =true;
                                n++;
                            }
                        }
                        Log.e("RATEING",n.toString());
                        if(n<3){
                            Toast.makeText(getApplicationContext(),"Please rate atleast 3 songs",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(),SongRateList.class);
                            startActivity(intent);
                        }

                    }
                }
        );
    }
}
