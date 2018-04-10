package org.sana.android.app.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import org.sana.android.app.ConnectionManager;

/**
 * Created by winkler.em@gmail.com, on 04/10/2018.
 */
public class ConnectionManagerTest  extends AndroidTestCase {
    public static final String TAG = ConnectionManagerTest.class.getSimpleName();
    Context mContext = null;
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
    }
    public void testGetDeviceName(){
        String name = null;
        name = ConnectionManager.getDeviceName(mContext, true);
        Log.d(TAG, "...tetGetDeviceName(Context, boolean) -> " + name);
        assertNotNull(name);
        name = ConnectionManager.getDeviceName(mContext);
        Log.d(TAG, "...tetGetDeviceName() -> " + name);
        assertNotNull(name);
        name = ConnectionManager.getDeviceName(mContext, true);
        Log.d(TAG, "...tetGetDeviceName(Context, boolean) -> " + name);
        assertNotNull(name);
    }
}
