package com.inviks.www.inviks1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.widget.Toast;

public class MainActivity extends BaseActivityClass{ //implements ChangeLocationDialog.Communicator{
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.setIcon(R.drawable.ic_action_threebar);//doesnt work
        setListView();
    }

    private void setListView()
    {
        String[] arr={getString(R.string.listViewOption1),getString(R.string.listViewOption2),getString(R.string.listViewOption3)};
        int[] iconArr={R.drawable.hp_location_icon,R.drawable.hp_search_icon,R.drawable.hp_prescription_icon};
        //ListAdapter adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        ListAdapter adapter=new CustomAdapter(this,arr,iconArr);
        ListView listView=(ListView)findViewById(R.id.homePageListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String itemName = String.valueOf(parent.getItemAtPosition(position));
                        changeIntent(position);
                    }
                }
        );
    }
    private void changeIntent(int position)
    {
        try
        {
            if (position == 0)
            {
                showDialog();
            } else if (position == 1)
            {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            else if(position==2)
            {
                //Intent searchIntent = new Intent(this, SearchActivity.class);
                //startActivity(searchIntent);
            }
        }
        catch (Exception ex)
        {
            Log.i("Inviks","Exception in changeIntent of main Activity:\n"+ex.getMessage());
        }
    }
    /*public void getLocationString(String msg) {
        //Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }*/

    public void showDialog()
    {
        FragmentManager manager=getFragmentManager();
        ChangeLocationDialog myDialog=new ChangeLocationDialog();
        myDialog.show(manager, "Change location");
    }
    public void login(View view)
    {
        Intent intent=new Intent(this,LoginMain.class);
        startActivityForResult(intent,0);
    }
}