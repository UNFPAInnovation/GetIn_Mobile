package org.sana.android.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.sana.android.content.Intents;
import org.sana.android.content.Uris;
import org.sana.android.provider.Counties;
import org.sana.android.provider.Districts;
import org.sana.android.provider.Locations;
import org.sana.android.provider.Parishes;
import org.sana.android.provider.Subcounties;
import org.sana.android.util.Dates;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by winkler.em@gmail.com, on 03/28/2017.
 */
public class SynchronizationManager {
    public static final String TAG = SynchronizationManager.class.getSimpleName();

    /** Default sync delta set to one hour */
    public static final long DELTA_SYNC_LOCALITY = 1000*60*60;

    public static void sync(Context context, Uri uri){
        sync(context, uri, null);
    }

    public static void sync(Context context, Uri uri, String keyExtra){
        long last = getLastSynch(context, uri, keyExtra);
        Uri synchUri = uri;
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        calendar.setTimeInMillis(last);
        synchUri = uri.buildUpon().appendQueryParameter("modified__gt",
                    Dates.toSQL(calendar.getTime())).build();

        // Build the synch Intent
        Intent intent = new Intent(Intents.ACTION_READ, synchUri);
        intent.putExtra(Intents.EXTRA_SYNCH, now);
        if(!TextUtils.isEmpty(keyExtra))
            intent.putExtra(Intents.EXTRA_SYNCH_KEY, keyExtra);
        context.startService(intent);
    }

    public static String getSynchKey(Uri uri){
        String key = "";
        String type = Uris.getType(uri);
        if(!TextUtils.isEmpty(type)) {
            String[] resolver = type.split("\\.");
            if (resolver.length >= 1) {
                key = resolver[resolver.length - 1].toLowerCase();
            }
        }
        return String.format("%s_sync", key);

    }

    public static String getSynchKey(Uri uri, String suffix){
        String key = "";
        String type = Uris.getType(uri);
        if(!TextUtils.isEmpty(type)) {
            String[] resolver = type.split("\\.");
            if (resolver.length >= 1) {
                key = resolver[resolver.length - 1].toLowerCase();
            }
        }
        if(TextUtils.isEmpty(suffix))
            return String.format("%s_sync", key);
        else
            return String.format("%s_%s_sync", key, suffix);
    }

    public static long getLastSynch(Context context, Uri uri){
        return getLastSynch(context, uri, null);
    }

    public static long getLastSynch(Context context, Uri uri, String keyExtra){
        long last = 0L;
        String key = getSynchKey(uri, keyExtra);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        last = preferences.getLong(key, 0L);
        return last;
    }

    public static void setLastSynch(Context context, Uri uri, String keyExtra){
        if(Uris.isItemType(uri)){
            return;
        }
        long synch = 0;
        String synchValue = uri.getQueryParameter("modified__gt");
        if(TextUtils.isEmpty(synchValue)){
            Date now = new Date();
            synch = now.getTime();
        } else {
            try {
                synch = Dates.fromSQL(synchValue).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String key = getSynchKey(uri,keyExtra);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong(key, synch).commit();
    }

    public static void setLastSynch(Context context, Uri uri){
        setLastSynch(context, uri, null);
    }

    public static void setSynchTime(Context context, Uri uri, String keyExtra,
                                    long time){
        if(Uris.isItemType(uri)){
            return;
        }
        String key = getSynchKey(uri,keyExtra);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong(key, time).commit();
    }

    /**
     * Sync the locality objects for the system-i.e. Location, Parish, etc.
     *
     * @param context
     * @param delta Time difference for invoking sync
     */
    public static void syncLocalities(Context context, long delta){
        long last = 0;
        long now = Calendar.getInstance().getTimeInMillis();
        Uri uri = Locations.CONTENT_URI;
        String key = getSynchKey(uri);
        last = getLastSynch(context,uri);
        if((now - last) > delta) {
            Intent intent = new Intent(Intents.ACTION_READ, uri);
            intent.putExtra(Intents.EXTRA_SYNCH, now);
            intent.putExtra(Intents.EXTRA_SYNCH_KEY, key);
            context.startService(intent);
        }
        uri = Parishes.CONTENT_URI;
        key = getSynchKey(uri);
        last = getLastSynch(context,uri);
        if((now - last) > delta) {
            Intent intent = new Intent(Intents.ACTION_READ, uri);
            intent.putExtra(Intents.EXTRA_SYNCH, now);
            intent.putExtra(Intents.EXTRA_SYNCH_KEY, key);
            context.startService(intent);
        }
        uri = Subcounties.CONTENT_URI;
        key = getSynchKey(uri);
        last = getLastSynch(context,uri);
        if((now - last) > delta) {
            Intent intent = new Intent(Intents.ACTION_READ, uri);
            intent.putExtra(Intents.EXTRA_SYNCH, now);
            intent.putExtra(Intents.EXTRA_SYNCH_KEY, key);
            context.startService(intent);
        }
        uri = Counties.CONTENT_URI;
        key = getSynchKey(uri);
        last = getLastSynch(context,uri);
        if((now - last) > delta) {
            Intent intent = new Intent(Intents.ACTION_READ, uri);
            intent.putExtra(Intents.EXTRA_SYNCH, now);
            intent.putExtra(Intents.EXTRA_SYNCH_KEY, key);
            context.startService(intent);
        }
        uri = Districts.CONTENT_URI;
        key = getSynchKey(uri);
        last = getLastSynch(context,uri);
        if((now - last) > delta) {
            Intent intent = new Intent(Intents.ACTION_READ, uri);
            intent.putExtra(Intents.EXTRA_SYNCH, now);
            intent.putExtra(Intents.EXTRA_SYNCH_KEY, key);
            context.startService(intent);
        }
    }

    /**
     * Sync the locality objects for the system-i.e. Location, Parish, etc,-
     * and set to sync no more than one the value of {@linkplain #DELTA_SYNC_LOCALITY}
     *
     * @param context
     */
    public static void syncLocalities(Context context){
        syncLocalities(context, DELTA_SYNC_LOCALITY);
    }
}
