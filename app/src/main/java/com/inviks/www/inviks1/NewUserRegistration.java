package com.inviks.www.inviks1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class NewUserRegistration extends BaseActivityClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_registration);
    }
    public void onOkClick(View view)
    {

    }
    public void onCancelClick(View view)
    {
        this.finish();
    }
}
