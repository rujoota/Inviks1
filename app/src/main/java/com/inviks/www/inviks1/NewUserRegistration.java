package com.inviks.www.inviks1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.inviks.Helper.Helper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;


public class NewUserRegistration extends BaseActivityClass {

    EditText name,email,pwd,confirmPwd;
    Context context=this;
    CheckBox accept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_registration);
        name=(EditText)findViewById(R.id.txtNameRegister);
        email=(EditText)findViewById(R.id.txtEmailRegister);
        pwd=(EditText)findViewById(R.id.txtPasswordRegister);
        confirmPwd=(EditText)findViewById(R.id.txtConfirmPwdRegister);
        accept=(CheckBox)findViewById(R.id.chkAcceptRegister);
    }
    public void onOkClick(View view)
    {
        if(isValid())
        {
            Helper.sendMail(email.getText().toString(),"Verification code for Inviks Medicine App","Hi,\nYour verification code for Inviks App is:\n1234");
        }
            //new NewUserAdd().execute(name.getText().toString(),email.getText().toString(),pwd.getText().toString());
    }
    private boolean isValid()
    {
        boolean error=false;
        if(Helper.isEmpty(name.getText()))
        {
            name.setError("Please enter valid name");
            error=true;
        }
        if(Helper.isEmpty(email.getText()))
        {
            email.setError("Please enter valid email");
            error=true;
        }
        else if(!Helper.isValidEmail(email.getText()))
        {
            email.setError("Please enter valid email");
            error=true;
        }
        if(Helper.isEmpty(pwd.getText()))
        {
            pwd.setError("Please enter valid password");
            error=true;
        }
        else if(pwd.getText().length()<6)
        {
            pwd.setError("Password should be at least 6 characters");
            error=true;
        }
        if(Helper.isEmpty(confirmPwd.getText()))
        {
            confirmPwd.setError("Please enter valid password");
            error=true;
        }
        else if(!confirmPwd.getText().toString().equals(pwd.getText().toString()))
        {
            confirmPwd.setError("Passwords do not match");
            error=true;
        }
        if(!accept.isChecked())
        {
            accept.setError("Please accept terms & conditions");
            error=true;
        }
        return !error;
    }
    public void onCancelClick(View view)
    {
        this.finish();
    }
    class NewUserAdd extends AsyncTask<String, String, Void>
    {
        //private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream is = null;
        String result = "";

        @Override
        protected Void doInBackground(String... params) {
            String url_select = getString(R.string.serviceURL)+"users/registerUser";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url_select);

            try
            {
                httpGet.setHeader("name", params[0]);
                httpGet.setHeader("userId", params[1]);
                httpGet.setHeader("pwd", params[2]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int ret = httpResponse.getStatusLine().getStatusCode();
                result = new Integer(ret).toString();
            }
            catch (Exception e)
            {

                Log.e("Inviks", "Error in http connection " + e.toString());

            }
            return null;
        }

        protected void onPostExecute(Void v) {
            if(result.equals("200")) //ok
            {
                /*SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String isUserLoggedIn = sharedPreferences.getString("isLoggedIn", "");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.isLoggedIn_sharedPref_string),"yes");
                editor.putString(getString(R.string.loggedInUser_sharedPref_string),email.getText().toString());
                editor.apply();*/

                Toast.makeText(context,"Please activate your id from your email inbox",Toast.LENGTH_LONG).show();
                Thread t = new Thread( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(4000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        finish();
                    }
                });
                t.start();

            }
            else if(result.equals("400")) //bad request
            {
                email.setError("Email id already exists!");
            }
            else // mostly result=204 for exceptions
            {

            }
        }
    }
}
