package org.sana.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import org.sana.R;
import org.sana.android.content.Intents;
import org.sana.android.fragment.ModelSelectedListener;
import org.sana.android.provider.Observers;
import org.sana.core.Observer;

public class ObserverList extends BaseActivity implements ModelSelectedListener<Observer> {

    // ms*sec*min*hrs
    private int delta =1000*1*1*1;
//    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer_list);
        sync(this, Observers.CONTENT_URI);
    }
    
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

    }


    public final boolean sync(Context context, Uri uri) {
        boolean result = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long lastSync = prefs.getLong("observer_sync", 0);
        long now = System.currentTimeMillis();
        if((now - lastSync) > delta){
            prefs.edit().putLong("observer_sync", now).commit();
            Intent intent = new Intent(Intents.ACTION_READ,uri);
            context.startService(intent);
            result = true;
        }
        return result;
    }

    @Override
    public void onModelSelected(Observer instance) {
        // TODO Implement the "call" functionality, etc.
    }
}
