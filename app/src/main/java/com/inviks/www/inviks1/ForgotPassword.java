package com.inviks.www.inviks1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.inviks.Helper.Helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ForgotPassword extends BaseActivityClass {
    Context context=this;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email=(EditText)findViewById(R.id.txtEmailForgot);
    }
    public void onOkClick(View view)
    {
        new ForgotPasswordAction().execute(email.getText().toString(),String.valueOf(Helper.generateRandom(1000, 9999)));
    }
    public void onCancelClick(View view)
    {
        finish();
    }
    class ForgotPasswordAction extends AsyncTask<String, String, Void>
    {
        InputStream is = null;
        String result = "";
        String code,mailId;
        @Override
        protected Void doInBackground(String... params) {
            String url_select = getString(R.string.serviceURL)+"users/forgotPassword";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url_select);

            try
            {
                httpGet.setHeader("userId", params[0]);
                httpGet.setHeader("code", params[1]);
                code=params[1];
                mailId=params[0];
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
                // it calls a stored procedure which returns result in form of true, not found or pending
                result = br.readLine().toLowerCase();
                result=result.trim().replace("\"","");
                is.close();
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error converting result " + e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            if(result.equals("true")) //ok
            {
                String subject = "Inviks: Regarding your forgotten password";
                String body = "Hi,<br/>Click on the following link to reset your password for Inviks app:<br/><a href='http://localhost/forgotpassword.php?userid="+email.getText().toString()+"&code="+code+"'>Click here</a>"
                        +"<br/>This link will help you to put new password for Inviks app.<br/><br/>Thanks and regards,<br/>Team Inviks";
                new Helper().sendMail(context, mailId, subject, body);
                Toast.makeText(context, "Please check your email to reset your password", Toast.LENGTH_LONG).show();
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
            else if(result.equals("not found")) //when email id is not in db
            {
                email.setError("Email id does not exist!");
            }
            else if(result.equals("pending"))
            {
                email.setError("Your email id is not yet verified. Kindly check our older mail to verify it.");
            }
            else// mostly result=204 for exceptions or null
            {

            }
        }
    }
}
