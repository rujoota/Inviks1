package com.inviks.www.inviks1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.inviks.DBClasses.Medicine;

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
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter
{
    List<Medicine> medicineArrayList;
    private LayoutInflater layoutInflater;
    public SearchAdapter(Context context,List<Medicine> medicineArrayList) {
        //super(context,R.layout.search_adapter);
        layoutInflater = LayoutInflater.from(context);
        this.medicineArrayList=medicineArrayList;
    }

    @Override
    public int getCount()
    {
        return medicineArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return medicineArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate=get ready/prepare

        convertView=layoutInflater.inflate(R.layout.search_adapter, null);
        //String arrItem=getItem(position);
        TextView medicine=(TextView)convertView.findViewById(R.id.lblMedicine);
        TextView price=(TextView)convertView.findViewById(R.id.lblPrice);
        TextView form=(TextView)convertView.findViewById(R.id.lblForm);
        TextView composition=(TextView)convertView.findViewById(R.id.lblComposition);
        TextView company=(TextView)convertView.findViewById(R.id.lblCompanyName);
        TextView desc=(TextView)convertView.findViewById(R.id.lblShortDescription);

        Medicine item=medicineArrayList.get(position);
        medicine.setText(item.getMedicineName());
        price.setText(item.getPrice() + " Rs");
        form.setText("("+item.getForm()+")");
        composition.setText("("+item.getComposition()+"mg)");
        company.setText(item.getCompanyName());
        desc.setText(item.getShortDescription());
        return convertView;
    }
}