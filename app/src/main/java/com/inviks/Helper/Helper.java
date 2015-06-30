package com.inviks.Helper;


import android.text.TextUtils;

public class Helper {
    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isEmpty(CharSequence target)
    {
        target=target.toString().trim();
        return TextUtils.isEmpty(target);
    }
    public static void sendMail(String emailId,String subject,String body)
    {

    }
}
