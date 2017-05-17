package org.sana.android.app;

import android.content.Context;

import org.sana.android.Constants;
import org.sana.android.content.core.ObserverWrapper;
import org.sana.core.Location;
import org.sana.core.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<Location> getCurrentUserLocations(Context context){
        List<Location> objects = new ArrayList<>();
        Observer object = getCurrentUser(context);
        if(object != null){
            objects = object.getLocations();
        }
        return objects;
    }

    public static class Session{
        private Observer mObserver = null;
        private Location mLocation = null;
        private String mKey = null;

        public Observer getObserver(){ return mObserver; }

        public Location getLocation(){ return mLocation; }
    }
    static Map<String, Session> mSessions = new HashMap<>();

    public static String add(Context context, Observer observer){
        Session session = new Session();
        session.mObserver = observer;
        session.mKey = observer.getUuid();
        mSessions.put(session.mKey, session);
        return session.mKey;
    }

    public static Session get(Context context, String key){
        return mSessions.get(key);
    }

    public static void remove(Context context, String key){
        mSessions.remove(key);
    }

    public static void clear(){
        mSessions.clear();
    }
}
