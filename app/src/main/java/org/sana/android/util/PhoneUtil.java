package org.sana.android.util;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
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
}
