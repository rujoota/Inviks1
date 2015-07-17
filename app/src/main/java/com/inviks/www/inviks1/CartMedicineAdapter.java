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
import android.widget.Toast;

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
        public int currentPosition;
        TextView medicineName;
        ImageButton delete;
        TextView price,qtyLable,priceLable;
        Spinner drpQty;
        ViewHolder(View view,int position)
        {
            this.currentPosition=position;
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row=convertView;
        ViewHolder holder=null;
        if(row==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.cart_gridview_adapter,parent, false);
            holder=new ViewHolder(row,position);
            row.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)row.getTag();
        }

        final MedicinesInCart obj = medObjInCart.get(position);
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
        holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                medObjInCart.remove(position);
                notifyDataSetChanged();
                new DeleteCartItem().execute(obj.getOrderId(),obj.getMedicineId());
            }
        });
        return row;
    }
    class DeleteCartItem extends AsyncTask<String, Void, String>
    {
        InputStream is = null;
        String result = "";
        String textToSearch;

        @Override
        protected String doInBackground(String... params)
        {
            String url_select = context.getString(R.string.serviceURL) + "orders/deleteItemFromCart";

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url_select);
            httpGet.setHeader("orderId", params[0]);
            httpGet.setHeader("medicineId", params[1]);

            try
            {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int ret = httpResponse.getStatusLine().getStatusCode();
                result = new Integer(ret).toString();
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error in http connection - DeleteCartItem" + e.toString());
                return("Exception in get medicine details data.\n"+e.getMessage());
            }
            return("ok");
        }

        protected void onPostExecute(String v)
        {
            super.onPostExecute(v);
            // no exception found on previous call
            if(!v.toLowerCase().contains("exception"))
            {
                // if no medicines in cart then...
                if(result.equals("200")) //ok, item got deleted
                {
                   // no need to do anything
                    Toast.makeText(context,"Deleted record",Toast.LENGTH_SHORT).show();
                }
                else if(result.equals("400")) //bad request-maybe orderid or userid was invalid
                {
                    Toast.makeText(context,"There are some problems deleting this",Toast.LENGTH_SHORT).show();
                    Log.i("Inviks","bad request for delete");
                }
                else//maybe some other problem
                {
                    Toast.makeText(context,"There are issues deleting this",Toast.LENGTH_SHORT).show();
                    Log.i("Inviks","some other problem");
                }
            }
        }
    }
}