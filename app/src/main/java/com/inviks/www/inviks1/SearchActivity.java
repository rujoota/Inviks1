package com.inviks.www.inviks1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.inviks.DBClasses.Medicine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class SearchActivity extends BaseActivityClass
{
    ArrayList<String> arrName;
    ArrayList<String> arrPrice;
    ArrayList<Medicine> medicineResults = new ArrayList<Medicine>();
    ArrayList<Medicine> filteredResults = new ArrayList<Medicine>();
    SearchView searchView;
    ListView searchListView;
    Boolean found = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchListView = (ListView) findViewById(R.id.listViewSearch);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
                                                     {
                                                         @Override
                                                         public void onFocusChange(View v, boolean hasFocus)
                                                         {

                                                         }

                                                     }

        );
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (newText.length() > 1)
                {
                    searchListView.setVisibility(View.VISIBLE);
                    new SearchDataSource().execute(newText);

                } else
                {
                    searchListView.setVisibility(View.INVISIBLE);
                }
                return false;
            }

        });

    }
    private void changeIntent(int position)
    {
        Medicine medicine = filteredResults.get(position);
        Intent intent = new Intent(this,ItemDetails.class);
        intent.putExtra("selectedMedicineId",medicine.getMedicineId());
        startActivity(intent);
    }
    Context currentContext=this;
    class SearchDataSource extends AsyncTask<String, Void, String>
    {
        InputStream is = null;
        String result = "";
        String textToSearch;

        @Override
        protected String doInBackground(String... params)
        {
            String url_select = getString(R.string.serviceURL) + "orders/searchMedicine";


            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url_select);
            httpGet.setHeader("searchQuery", params[0]);
            textToSearch=params[0];

            try
            {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is = httpEntity.getContent();

            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error in http connection " + e.toString());
            }
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error converting result " + e.toString());
            }
            try
            {
                getMedicinesInJson();
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error parsing data " + e.toString());
                return("Exception in searching data.\n"+e.getMessage());
            }
            return("ok");
        }

        protected void onPostExecute(String v)
        {
            super.onPostExecute(v);
            if(!v.toLowerCase().contains("exception"))
            {
                filterMedicineArray(textToSearch);
                searchListView.setAdapter(new SearchAdapter(currentContext, filteredResults));
                searchListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                changeIntent(position);
                            }
                        }
                );
            }
        }



        private void getMedicinesInJson() throws Exception
        {
            JSONArray medicineArr = new JSONArray(result);
            for (int i = 0; i < medicineArr.length(); i++)
            {
                JSONObject Jasonobject = null;
                Jasonobject = medicineArr.getJSONObject(i);
                String name = Jasonobject.getString("medicineName");
                Medicine medicine = new Medicine();
                if (name != null)
                {
                    medicine.setMedicineName(name);
                    medicine.setMedicineId(Jasonobject.getString("medicineId"));
                    medicine.setPrice(Jasonobject.getDouble("price"));
                    medicine.setForm(Jasonobject.getString("form"));
                    medicine.setCompanyName(Jasonobject.getString("companyName"));
                    medicine.setComposition(Jasonobject.getDouble("composition"));
                    medicine.setShortDescription(Jasonobject.getString("shortDescription"));
                }
                found=false;
                for(int j=0;j<medicineResults.size();j++)
                {
                    if(medicineResults.get(j).getMedicineId().equals(medicine.getMedicineId()))
                    {
                        found=true;
                    }
                }
                if(!found)
                {
                    medicineResults.add(medicine);
                }
            }
        }
        private void filterMedicineArray(String textToSearch)
        {
            filteredResults.clear();
            for(int i=0;i<medicineResults.size();i++)
            {
                String name=medicineResults.get(i).getMedicineName().toLowerCase();
                if(name.contains(textToSearch.toLowerCase()))
                {
                    filteredResults.add(medicineResults.get(i));
                }
            }
        }

    }

}
