package com.inviks.www.inviks1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inviks.DBClasses.Medicine;
import com.inviks.DBClasses.MedicinesInCart;
import com.inviks.Helper.Helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.Inflater;


public class CartView extends BaseActivityClass
{
    ListView listView;
    SharedPreferences sharedPreferences;
    ArrayList<Medicine> medicineArrayList;
    ArrayList<MedicinesInCart> medObjInCart;
    TextView qtyLable,priceLable,emptyMsg;
    Button checkout;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        listView=(ListView)findViewById(R.id.gridView);
        qtyLable=(TextView)findViewById(R.id.lblQtyCartDisplay);
        priceLable=(TextView)findViewById(R.id.lblPriceCartDisplay);
        emptyMsg=(TextView)findViewById(R.id.lblCartEmptyMsg);
        checkout=(Button)findViewById(R.id.btnCheckoutCart);
        manageCart();
    }
    private void manageCart()
    {
        String userId="",orderId="";
        if (Helper.isUserLoggedIn(this))
        {
            // update cart for that user
            //new UpdateCart().execute(thisMedicine.getMedicineId());
            userId=Helper.getCurrentLoggedinUser(this);
        }
        else
        {
            orderId=Helper.getSharedPref(this,"orderIdForCart");
        }
        new GetCartItems().execute(userId,orderId);
    }

    class GetCartItems extends AsyncTask<String, Void, String>
    {
        InputStream is = null;
        String result = "";
        String textToSearch;

        @Override
        protected String doInBackground(String... params)
        {
            String url_select = getString(R.string.serviceURL) + "orders/getDataFromCart";

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url_select);
            httpGet.setHeader("userId", params[0]);
            httpGet.setHeader("orderId", params[1]);

            try
            {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error in http connection CartView" + e.toString());
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
                Log.e("Inviks", "Error converting result in CartView" + e.toString());
            }
            try
            {
                Gson gson = new Gson();
                Type typeMedicine = new TypeToken<ArrayList<MedicinesInCart>>(){}.getType();
                medObjInCart = gson.fromJson(result, typeMedicine);
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error parsing data in CartView" + e.toString());
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
                if(null==medObjInCart || medObjInCart.size()<=0)
                {
                    priceLable.setVisibility(View.INVISIBLE);
                    qtyLable.setVisibility(View.INVISIBLE);
                    emptyMsg.setVisibility(View.VISIBLE);
                    checkout.setVisibility(View.INVISIBLE);
                }
                else
                {
                    priceLable.setVisibility(View.VISIBLE);
                    qtyLable.setVisibility(View.VISIBLE);
                    emptyMsg.setVisibility(View.INVISIBLE);
                    checkout.setVisibility(View.VISIBLE);
                    //listView.setAdapter(new CartMedicineAdapter(context, medObjInCart));
                    try
                    {

                        listView.setAdapter(new CartMedicineAdapter(context, medObjInCart));
                    }
                    catch (Exception ex)
                    {
                        Log.e("Inviks","Problems in cartview loading:"+ex.getMessage());
                    }
                }
            }
        }
    }
}

