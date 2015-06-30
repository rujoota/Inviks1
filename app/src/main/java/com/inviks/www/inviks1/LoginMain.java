package com.inviks.www.inviks1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;


public class LoginMain extends BaseActivityClass
{
    EditText email,password;
    TextView err;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        email=(EditText)findViewById(R.id.txtEmailLogin);
        password=(EditText)findViewById(R.id.txtPasswordLogin);
        err=(TextView)findViewById(R.id.lblErrorLogin);
    }
    public void onOkClick(View view)
    {
        new LoginCheck().execute(email.getText().toString(),password.getText().toString());
    }
    public void onCancelClick(View view)
    {
        this.finish();
    }
    public void onForgotClick(View view)
    {
        Intent intent=new Intent(this,ForgotPassword.class);
        startActivityForResult(intent,1);
    }
    public void onRegisterClick(View view)
    {
        Intent intent=new Intent(this,NewUserRegistration.class);
        startActivityForResult(intent,1);
    }
    class LoginCheck extends AsyncTask<String, String, Void>
    {
        //private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream is = null;
        String result = "";

        @Override
        protected Void doInBackground(String... params) {
            String url_select = getString(R.string.serviceURL)+"users/userLogin";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url_select);

            try {
                httpGet.setHeader("userId", params[0]);
                httpGet.setHeader("pwd", params[1]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int ret = httpResponse.getStatusLine().getStatusCode();
                result = new Integer(ret).toString();

            } catch (Exception e) {

                Log.e("Inviks", "Error in http connection " + e.toString());

            }
            return null;
        }

        protected void onPostExecute(Void v) {
            if(result.equals("200")) //ok
            {
                err.setVisibility(View.INVISIBLE);
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String isUserLoggedIn = sharedPreferences.getString("isLoggedIn", "");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.isLoggedIn_sharedPref_string),"yes");
                editor.putString(getString(R.string.loggedInUser_sharedPref_string),email.getText().toString());
                editor.apply();
                finish();
            }
            else if(result.equals("400")) //bad request
            {
                err.setVisibility(View.VISIBLE);
            }
        }
    }
}
