package org.sana.android.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.sana.android.content.Intents;
import org.sana.android.content.Uris;
import org.sana.android.util.Dates;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by winkler.em@gmail.com, on 03/28/2017.
 */
public class SynchronizationManager {
    public static final String TAG = SynchronizationManager.class.getSimpleName();

    public static void sync(Context context, Uri uri){
        sync(context, uri, null);
    }

    public static void sync(Context context, Uri uri, String keyExtra){
        long last = getLastSynch(context, uri, keyExtra);
        Uri synchUri = uri;
        if(last > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(last);
            synchUri = uri.buildUpon().appendQueryParameter("modified__gt",
                    Dates.toSQL(calendar.getTime())).build();
        }
        context.startService(new Intent(Intents.ACTION_READ, synchUri));
    }

    public static String getSynchKey(Uri uri){
        String type = Uris.getType(uri);
        String[] resolver = type.split("\\.");
        return String.format("%s_sync", resolver[resolver.length - 1].toLowerCase());
    }

    public static String getSynchKey(Uri uri, String suffix){
        String type = Uris.getType(uri);
        String[] resolver = type.split("\\.");
        if(TextUtils.isEmpty(suffix))
            return String.format("%s_sync", resolver[resolver.length - 1].toLowerCase());
        else
            return String.format("%s_%s_sync", resolver[resolver.length - 1].toLowerCase(), suffix);
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
}
