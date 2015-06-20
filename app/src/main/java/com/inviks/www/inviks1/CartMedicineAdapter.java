package com.inviks.www.inviks1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inviks.DBClasses.Medicine;
import com.inviks.DBClasses.MedicinesInCart;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.support.v7.appcompat.R.*;
import static android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item;

public class CartMedicineAdapter extends BaseAdapter
{
    ArrayList<MedicinesInCart> medObjInCart;
    Context context;
    CartMedicineAdapter(Context context,ArrayList<MedicinesInCart> medicinesInCarts)
    {
        this.context=context;
        this.medObjInCart=medicinesInCarts;
    }

    class ViewHolder
    {
        TextView medicineName;
        ImageButton delete;
        TextView price,qtyLable,priceLable;
        Spinner drpQty;
        ViewHolder(View view)
        {
            medicineName=(TextView)view.findViewById(R.id.lblMedicineNameCart);
            delete=(ImageButton)view.findViewById(R.id.btnDeleteCart);
            price=(TextView)view.findViewById(R.id.lblPriceCart);
            drpQty = (Spinner)view.findViewById(R.id.drpQty);

            // implement onclick listener
            //implement onchange lister for qty
        }
    }
    @Override
    public int getCount()
    {
        return medObjInCart.size();
    }

    @Override
    public Object getItem(int position)
    {
        return medObjInCart.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View row=convertView;
        ViewHolder holder=null;
        if(row==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.cart_gridview_adapter,parent, false);
            holder=new ViewHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)row.getTag();

        }

            MedicinesInCart obj = medObjInCart.get(position);
            holder.medicineName.setText(obj.getMedicineName());
            holder.price.setText(obj.getTotal() + "Rs");
            String[] displayQtys = new String[obj.getQtyAvailable()];
            int selectedItemIndex = 0;
            for (int i = 1; i <= displayQtys.length; i++)
            {
                if (i == obj.getQtyOrdered())
                    selectedItemIndex = i - 1;
                displayQtys[i - 1] = String.valueOf(i);
            }
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, displayQtys);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.drpQty.setAdapter(adapter);
            holder.drpQty.setSelection(selectedItemIndex);

        return row;
    }

}