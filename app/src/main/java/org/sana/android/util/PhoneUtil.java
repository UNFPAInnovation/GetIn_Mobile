package org.sana.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by winkler.em@gmail.com, on 07/12/2017.
 */
public class PhoneUtil {
    public static final String TAG = PhoneUtil.class.getSimpleName();

    public static String formatNumber(String number){
        if(TextUtils.isEmpty(number)){
            return "";
        }
        String numberStr = number.replace(" ","");
        // TODO handle other international numbers
        if(numberStr.startsWith("256")){
            numberStr = "+" + numberStr;
        }
        return numberStr;
    }

    public static void call(Context context, String number){
        String numberStr = formatNumber(number);
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + numberStr));
        context.startActivity(intent);
    }

    public static void callForResult(Activity activity, String number, int requestCode){
        String numberStr = formatNumber(number);
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + numberStr));
        activity.startActivityForResult(intent, requestCode);
    }
}
