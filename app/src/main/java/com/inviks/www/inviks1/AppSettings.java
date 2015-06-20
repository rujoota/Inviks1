package com.inviks.www.inviks1;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings
{
    private final static String SERVICE_URL="";
    private final static String DATABASE_NAME="";
    private final static String DATABASE_STRING="";
    public static String getValue(Context cntx,String key)
    {
        SharedPreferences sharedPreferences=cntx.getSharedPreferences("inviksPref", Context.MODE_PRIVATE);
        String returnValue= sharedPreferences.getString(key,"");
        if(returnValue.equals(null) || returnValue.isEmpty())
        {
            switch(key)
            {
                case "serviceURL":
                    setValue(cntx,key,SERVICE_URL);
                    return SERVICE_URL;
                case "databaseName":
                    setValue(cntx,key,DATABASE_NAME);
                    return DATABASE_NAME;
                case "databaseString":
                    setValue(cntx,key,DATABASE_STRING);
                    return DATABASE_STRING;
                default:
                    return "";
            }
        }
        else
            return returnValue;
    }
    public static void setValue(Context cntx,String key,String value)
    {
        SharedPreferences sharedPreferences=cntx.getSharedPreferences("inviksPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
}