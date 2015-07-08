package com.inviks.www.inviks1;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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
import java.util.Random;


public class VerifyCodePopup extends DialogFragment implements View.OnClickListener
{
    Button ok, cancel;
    private int verificationCode;
    EditText code;
    public boolean okClicked;
    Communicator com;
    Context context;
    String loggedInUser;

    VerifyCodePopup(Context context, String email)
    {
        this.context = context;
        this.loggedInUser = email;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.verify_code_popup, null);
        getDialog().setTitle("Enter verification code:");
        ok = (Button) view.findViewById(R.id.btnOkPopup);
        cancel = (Button) view.findViewById(R.id.btnCancelPopup);
        code = (EditText) view.findViewById(R.id.txtCodePopup);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        com = (Communicator) activity;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnOkPopup)
        {
            if (Helper.isEmpty(code.getText()))
            {
                code.setError("This field is mandatory");
            }
            else
            {
                //com.okCallBack(code.getText().toString());
                new NewUserVerify().execute(loggedInUser, code.getText().toString());
            }
        }
        else if (v.getId() == R.id.btnCancelPopup)
        {
            setVerificationCode(-1);
            dismiss();
        }
        else if(v.getId()==R.id.btnResendPopup)
        {
            String subject = "Hello from Inviks, here is your code";
            String body = "Hi,<br/>Your verification code for Inviks App is:<br/>" + Helper.generateRandom(1000, 9999) + "<br/>Please enter this in app when it pops up for verification code. Without this, you will not be completely registered on Inviks.<br/><br/>Thanks and regards,<br/>Team Inviks";
            new Helper().sendMail(context, loggedInUser, subject, body);
        }
    }

    interface Communicator
    {
        public void okCallBack(String code);
    }

    public int getVerificationCode()
    {
        return verificationCode;
    }

    public void setVerificationCode(int value)
    {
        verificationCode = value;
    }

    class NewUserVerify extends AsyncTask<String, String, String>
    {
        //private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream is = null;
        String result = "";

        @Override
        protected String doInBackground(String... params)
        {
            String url_select = getString(R.string.serviceURL) + "users/verifyUser";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url_select);

            try
            {
                httpGet.setHeader("userId", params[0]);
                httpGet.setHeader("code", params[1]);
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
                // it calls a stored procedure which returns result in form of true, false or not found
                result = sb.toString();
            }
            catch (Exception e)
            {
                Log.e("Inviks", "Error converting result " + e.toString());
                return ("exception:" + e.getMessage());
            }
            return ("ok");
        }

        protected void onPostExecute(String v)
        {
            if (!v.toLowerCase().contains("exception") && !v.toLowerCase().contains("not found"))// not found is returned when the email id is invalid
            {
                // only true or false is returned if code is valid or invalid
                if (result.toLowerCase().contains("true"))
                {
                    dismiss();
                    Toast.makeText(context, "Your account has been verified", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String isUserLoggedIn = sharedPreferences.getString("isLoggedIn", "");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.isLoggedIn_sharedPref_string),"yes");
                    editor.putString(getString(R.string.loggedInUser_sharedPref_string),loggedInUser);
                    editor.apply();
                }
                else
                {
                    code.setError("Code you have entered is invalid");
                }
            }
        }
    }
}
