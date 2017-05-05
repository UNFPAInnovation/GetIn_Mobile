package org.sana.android.app;

import android.content.Context;

import org.sana.android.Constants;
import org.sana.android.content.core.ObserverWrapper;
import org.sana.core.Observer;

/**
 * Created by winkler.em@gmail.com, on 05/05/2017.
 */
public final class SessionManager {
    public static final String TAG = SessionManager.class.getSimpleName();

    public static Observer getCurrentUser(Context context){
        Observer object = null;
        String username = Preferences.getString(context,
                Constants.PREFERENCE_EMR_USERNAME, null);
        if(username != null){
            object = (Observer) ObserverWrapper.getOneByUsername(context, username);
            if(object != null){
                ObserverWrapper.initializeRelated(context, object);
            }
        }
        return object;
    }
}
