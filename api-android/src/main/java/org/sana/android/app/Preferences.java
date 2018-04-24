package org.sana.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.sana.api.BuildConfig;

/**
 *
 */
public final class Preferences {

    private static final String PREFIX = BuildConfig.APPLICATION_ID + ".preference.";
    public static final String SYNCH_DELTA = Preferences.class.getName() + ".SYNCH_DELTA";
    public static final String RESEND_DELAY = Preferences.class.getName() + ".RESEND_DELAY";
    public static final String INITIALIZED = "_SYNCH_INITIAL";
    public static final String DEVICE_NAME = BuildConfig.APPLICATION_ID + ".preference.DEVICE_NAME";
    public static final String DEVICE_ID = BuildConfig.APPLICATION_ID + ".preference.DEVICE_ID";
    public static final String DEVICE_CONFIRMED = BuildConfig.APPLICATION_ID + ".preference.DEVICE_CONFIRMED";
    public static final String LOGGED_IN = BuildConfig.APPLICATION_ID + ".preference.LOGGED_IN";
    public static final String LAST_UPDATE_CHECK = BuildConfig.APPLICATION_ID + ".preference.LAST_UPDATE_CHECK";

    private Preferences(){}

    public static String getString(Context context, String name,
                                             String defValue){
        return PreferenceManager.getDefaultSharedPreferences(context).getString
                (name,
                defValue);
    }

    public static String getString(Context context, String name){
        return getString(context,name,null);
    }

    public static int getInt(Context context, String name, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                name, defValue);
    }

    public static long getLong(Context context, String name, long defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(
                name, defValue);
    }

    public static boolean getBoolean(Context context, String name, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                name, defValue);
    }

    public static void setBoolean(Context context, String name, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(name, value).commit();
    }

    public static void setLong(Context context, String name, long value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(name, value).commit();
    }

    public static void setDeviceId(Context context, String deviceId){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(DEVICE_ID, deviceId).commit();
    }

    public static String getDeviceId(Context context){
        return getString(context, DEVICE_ID);
    }

    public static void setDeviceName(Context context, String name){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(DEVICE_NAME, name).commit();
    }

    public static String getDeviceName(Context context){
        return getString(context, DEVICE_NAME);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        setBoolean(context.getApplicationContext(), LOGGED_IN, loggedIn);
    }

    public static boolean getLoggedIn(Context context) {
        return getBoolean(context.getApplicationContext(), LOGGED_IN, false);
    }

    public static long lastUpdateCheck(Context context) {
        return getLong(context, LAST_UPDATE_CHECK, 0);
    }

    public static void setLastUpdateCheck(Context context, long timestamp) {
        setLong(context, LAST_UPDATE_CHECK, timestamp);
    }

    public static boolean isInitialized(Context context) {
        return getBoolean(context.getApplicationContext(), Preferences.INITIALIZED, false);
    }
}
