package com.inviks.www.inviks1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginMain extends BaseActivityClass
{
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        email=(EditText)findViewById(R.id.txtEmailLogin);
        password=(EditText)findViewById(R.id.txtPasswordLogin);
    }
    public void onOkClick(View view)
    {

    }
    public void onCancelClick(View view)
    {
        this.finish();
    }
    public void onForgotClick(View view)
    {

    }
    public void onRegisterClick(View view)
    {
        Intent intent=new Intent(this,NewUserRegistration.class);
        startActivityForResult(intent,1);
    }
}
