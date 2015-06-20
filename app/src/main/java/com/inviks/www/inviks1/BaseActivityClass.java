package com.inviks.www.inviks1;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.lang.reflect.Method;

public class BaseActivityClass extends ActionBarActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        //actionBar.show();
        //actionBar.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        else if(item.getItemId()== R.id.action_cart)
        {
            /*SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String isUserLoggedIn = sharedPreferences.getString("isLoggedIn", "");
            if (null != isUserLoggedIn && isUserLoggedIn.toLowerCase().equals("yes"))
            {
                // update cart for that user
                //new UpdateCart().execute(thisMedicine.getMedicineId());
            }
            else
            {
                String cartItems = sharedPreferences.getString("cartMedicineIds", "");
                String cartQtys = sharedPreferences.getString("cartMedicineQtys", "");
                Intent intent=new Intent(this,CartView.class);
                startActivity(intent);
                //Toast.makeText(this,cartItems+" "+cartQtys , Toast.LENGTH_LONG).show();
            }*/
            Intent intent=new Intent(this,CartView.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
