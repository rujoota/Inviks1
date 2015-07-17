package com.inviks.www.inviks1;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inviks.DBClasses.Medicine;
import com.inviks.Helper.Helper;

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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ItemDetails extends BaseActivityClass implements QuantityDialog.Communicator
{
    TabHost tabHost;
    String recievedId;
    TextView medicineName, price, description, salt, composition,pack,discountedPrice,company,qty;
    ListView substitutesView;
    ArrayList<Medicine> medicineArrayList;
    Medicine thisMedicine;
    List<Medicine> substitutesArr;
    int selectedQty;
    Context context=this;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Bundle medicineData=getIntent().getExtras();
        if(medicineData==null)
        {
            return;
        }
        else
        {
            tabHost=(TabHost)findViewById(R.id.tabHost);
            medicineName=(TextView)findViewById(R.id.lblDetailMedicineName);
            price=(TextView)findViewById(R.id.lblPrice);
            salt=(TextView)findViewById(R.id.lblSalt);
            description=(TextView)findViewById(R.id.lblDescription);
            composition=(TextView)findViewById(R.id.lblComposition);
            pack=(TextView)findViewById(R.id.lblDetailPack);
            discountedPrice=(TextView)findViewById(R.id.lblDiscountedPrice);
            company=(TextView)findViewById(R.id.lblDetailCompanyName);
            qty=(TextView)findViewById(R.id.lblQuantity);
            tabHost.setup();
            TabHost.TabSpec tab1=tabHost.newTabSpec("tab1");
            tab1.setContent(R.id.tab1);
            tab1.setIndicator("Details");
            tabHost.addTab(tab1);
            TabHost.TabSpec tab2=tabHost.newTabSpec("tab2");
            tab2.setContent(R.id.tab2);
            tab2.setIndicator("Substitutes");
            tabHost.addTab(tab2);
            recievedId=medicineData.getString("selectedMedicineId");
            substitutesView=(ListView)findViewById(R.id.listViewSubstitutes);
            new GetItemDetails().execute(recievedId);
        }
    }
    private void changeIntent(int position)
    {
        Medicine substitute = substitutesArr.get(position);
        Intent intent = new Intent(this,ItemDetails.class);
        intent.putExtra("selectedMedicineId",substitute.getMedicineId());
        startActivity(intent);
    }
    QuantityDialog myDialog;
    Context currentContext=this;
    public void addToCart(View v)
    {
        // pop for quantity
        FragmentManager manager=getFragmentManager();
        myDialog=new QuantityDialog();
        myDialog.show(manager, String.valueOf(thisMedicine.getQuantity()));

    }

    @Override
    public void okCallBack(int quantity)
    {
        selectedQty = quantity;
        myDialog.dismiss();
        // if user logged in...
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String isUserLoggedIn = sharedPreferences.getString("isLoggedIn", "");
        String userId="",medicineId=thisMedicine.getMedicineId(),orderId="";
        if (null != isUserLoggedIn && isUserLoggedIn.toLowerCase().equals("yes"))
        {
            userId=sharedPreferences.getString("loggedInUser", "");
        }
        else // if not logged in
        {
            orderId=sharedPreferences.getString("orderIdForCart","");
        }
        new UpdateCart().execute(medicineId, String.valueOf(selectedQty),orderId,userId);
    }

    class GetItemDetails extends AsyncTask<String, Void, String>
    {
        InputStream is = null;
        String result = "";
        String textToSearch;


        @Override
        protected String doInBackground(String... params)
        {
            String url_select = getString(R.string.serviceURL) + "orders/getMedicineDetails";

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url_select);
            httpGet.setHeader("medicineId", params[0]);
            textToSearch=params[0];

            try
            {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
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
                Gson gson = new Gson();
                Type typeMedicine = new TypeToken<ArrayList<Medicine>>(){}.getType();
                medicineArrayList = gson.fromJson(result, typeMedicine);
                thisMedicine =(Medicine)medicineArrayList.get(0);
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error parsing data " + e.toString());
                return("Exception in get medicine details data.\n"+e.getMessage());
            }
            return("ok");
        }

        protected void onPostExecute(String v)
        {
            super.onPostExecute(v);
            if(!v.toLowerCase().contains("exception"))
            {
                medicineName.setText(thisMedicine.getMedicineName());
                price.setText(String.valueOf(thisMedicine.getPrice()));
                if(thisMedicine.getDiscount()!=0)
                {
                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    double discounted= thisMedicine.getPrice()-(thisMedicine.getPrice()* thisMedicine.getDiscount()/100);
                    discountedPrice.setText(String.valueOf(discounted));
                }
                company.setText(thisMedicine.getCompanyName());
                salt.setText(thisMedicine.getSalt());
                qty.setText(thisMedicine.getQuantity()==0?"Out of stock": thisMedicine.getQuantity()+" available");
                composition.setText(String.valueOf(thisMedicine.getComposition())+"mg");
                if(thisMedicine.getFormInfo()!=null)
                    pack.setText("1 "+ thisMedicine.getFormInfo()+"("+ thisMedicine.getPackSize()+" "+ thisMedicine.getPackUnit()+")");
                else
                    pack.setText(thisMedicine.getForm()+"("+ thisMedicine.getPackSize()+" "+ thisMedicine.getPackUnit()+")");
                String desc= thisMedicine.getLongDescription();
                 desc = desc.replace("\\n", System.getProperty("line.separator"));
                description.setText(desc);
                if(medicineArrayList.size()>1)
                {
                    substitutesArr=medicineArrayList.subList(1,medicineArrayList.size());
                    substitutesView.setAdapter(new SearchAdapter(currentContext,substitutesArr));
                    substitutesView.setOnItemClickListener(
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
        }


    }
    class UpdateCart extends AsyncTask<String, String, Void>
    {
        InputStream is = null;
        String result = "";

        @Override
        protected Void doInBackground(String... params) {
            String url_select = getString(R.string.serviceURL)+"orders/addInCart";

            try
            {
                // this method passes logged in userid and/or orderid. If both are null, then it creates a new order id for this phone in shared pref
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url_select);
                httpGet.setHeader("medicineId", params[0]);
                httpGet.setHeader("selectedQty", params[1]);
                httpGet.setHeader("orderId", params[2]);
                httpGet.setHeader("userId", params[3]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
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
                Log.e("Inviks", "Error converting result UpdateCart" + e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            // result can be either true, orderid newly generated or exception
            if(!result.toLowerCase().contains("exception") && !result.isEmpty())
            {
                // if true means all is well
                if(!result.toLowerCase().equals("true") && result.toLowerCase().contains("iord"))
                {
                    // if its not true means its order id
                    Helper.putSharedPref(context, "orderIdForCart",result.trim().replace("\"",""));
                }
                Toast.makeText(context,"Item added to cart",Toast.LENGTH_LONG).show();
            }
            else
            {
                // if exception occurred
                Toast.makeText(context,"Exception:"+result,Toast.LENGTH_LONG).show();
            }
        }
    }
}
