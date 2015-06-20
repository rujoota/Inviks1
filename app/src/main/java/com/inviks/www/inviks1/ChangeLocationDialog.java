package com.inviks.www.inviks1;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;


public class ChangeLocationDialog extends DialogFragment implements View.OnClickListener{
    Button ok,cancel;
    Communicator com;
    EditText txtPincode;
    TextView lblError;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.change_location_dialog,null);
        //setCancelable(false);
        getDialog().setTitle("Check pincode");
        ok=(Button)view.findViewById(R.id.btnOkChangeLocation);
        cancel=(Button)view.findViewById(R.id.btnCancelChangeLocation);
        txtPincode =(EditText)view.findViewById(R.id.txtPincode);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        lblError =(TextView)view.findViewById(R.id.lblErrorPincode);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        com=(Communicator)activity;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnOkChangeLocation)
        {

            String pincode= txtPincode.getText().toString();
            new task().execute(pincode);

            //com.getLocationString(pincode);
        }
        else if(v.getId()==R.id.btnCancelChangeLocation)
        {
            dismiss();
        }
    }

    interface Communicator
    {
        public void getLocationString(String msg);
    }
    class task extends AsyncTask<String, String, Void>
    {
        //private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream is = null;
        String result = "";

        @Override
        protected Void doInBackground(String... params) {
            String url_select = getString(R.string.serviceURL)+"orders/checkLocation";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url_select);

            try {
                httpGet.setHeader("pincode", params[0]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int ret = httpResponse.getStatusLine().getStatusCode();
                result = new Integer(ret).toString();
                Log.i("ChangeLocationDialog", result);

            } catch (Exception e) {

                Log.e("ChangeLocationDialog", "Error in http connection " + e.toString());

            }
            return null;
        }

        protected void onPostExecute(Void v) {
            if(result.equals("200")) //ok
            {
                lblError.setVisibility(View.INVISIBLE);
                dismiss();
            }
            else if(result.equals("400")) //bad request
            {
                lblError.setVisibility(View.VISIBLE);
                Log.i("ChangeLocationDialog","came here and set invisible");
            }
        }
    }
}
