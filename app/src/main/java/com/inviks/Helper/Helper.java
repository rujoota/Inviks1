package com.inviks.Helper;


import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.style.ParagraphStyle;
import android.util.Log;

import com.inviks.www.inviks1.ChangeLocationDialog;
import com.inviks.www.inviks1.R;

import java.util.Properties;

import javax.mail.Session;
import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FromStringTerm;

public class Helper {
    Session session = null;
    String from,pass;
    String []to;
    static String sharedPrefFileName="userInfo";
    public static Boolean isUserLoggedIn(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE);
        String isUserLoggedIn = sharedPreferences.getString(context.getString(R.string.isLoggedIn_sharedPref_string), "").toLowerCase();
        if(isUserLoggedIn.equals("yes"))
            return true;
        else
            return false;
    }
    public static void removeSharedPref(Context context,String key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getCurrentLoggedinUser(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE);
        String user = sharedPreferences.getString(context.getString(R.string.loggedInUser_sharedPref_string), "");
        return user;
    }
    public static void putSharedPref(Context context,String key,String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE);
        String isUserLoggedIn = sharedPreferences.getString(key, "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isEmpty(CharSequence target)
    {
        target=target.toString().trim();
        return TextUtils.isEmpty(target);
    }
    public static int generateRandom(int min,int max)
    {
        Random random = new Random();
        int randomInt = random.nextInt((max - min) + 1) + min;
        if (randomInt > max)
        {
            randomInt -= min;
        }
        return randomInt;
    }
    public void sendMail(Context context, String to,String subject,String body)
    {
        from=context.getString(R.string.fromEmail);
        pass=context.getString(R.string.fromPassword);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });
        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute(to,subject,body);
    }
    class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try
            {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params[0]));
                message.setSubject(params[1]);
                message.setContent(params[2], "text/html; charset=utf-8");
                Transport.send(message);
            }
            catch(MessagingException e)
            {
                Log.e("Inviks",e.getMessage());
            }
            catch(Exception e)
            {
                Log.e("Inviks", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
