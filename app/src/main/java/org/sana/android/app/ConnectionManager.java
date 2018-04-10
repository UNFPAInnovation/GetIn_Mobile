package org.sana.android.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import org.sana.BuildConfig;

import java.util.Calendar;
import java.util.UUID;

/**
 * Utility methods for getting connection information.
 */
public class ConnectionManager {
    public static final String TAG = ConnectionManager.class.getSimpleName();
    private static ConnectionManager manager = null;
    private static CellStateListener mListener = new CellStateListener();

    TelephonyManager mTelephonyManager;

    private ConnectionManager() {
    }

    public static void obtain(Context context) {
        manager = new ConnectionManager();
        manager.mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        manager.mTelephonyManager.listen(mListener,
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public static void release() {
        manager.mTelephonyManager.listen(mListener,
                PhoneStateListener.LISTEN_NONE);
        manager = null;
    }

    public static boolean connected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static boolean connectionIsStrong(Context context, int minStrength) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            return false;
        } else {
            return (mListener.mStrength < minStrength);

        }
    }

    public static class CellStateListener extends PhoneStateListener {
        public int mStrength = 0;
        public boolean isGSM = false;

        public CellStateListener() {
            super();
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                isGSM = true;
                if (signalStrength.getGsmSignalStrength() != 99)
                    mStrength = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    mStrength = signalStrength.getGsmSignalStrength();
            } else {
                isGSM = false;
                mStrength = signalStrength.getCdmaDbm();
            }
        }
    }

    public static String getDeviceName(Context context, boolean reset) {
        final String imeiDefault = "000000000000000";
        // Check if set in the preferences already and return if set
        String name = null;
        if(!reset) {
            name = Preferences.getDeviceName(context);
            if (!TextUtils.isEmpty(name))
                return name;
        }
        // Try the telephony manager ut do not expect much
        TelephonyManager mManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // Iterate through potential available id's
        name = mManager.getLine1Number();
        Log.d(TAG, "getLine1Number()() -> name=" + name);
        if (TextUtils.isEmpty(name)) {
            name = mManager.getDeviceId();
            Log.d(TAG, "getDeviceId() -> name=" + name);
            if (TextUtils.isEmpty(name) ||
                    (name != null && name.equals(imeiDefault))) {
                if (TextUtils.isEmpty(name)) {
                    name = mManager.getSubscriberId();
                    Log.d(TAG, "getSubscriberId() -> name=" + name);
                }
                if (TextUtils.isEmpty(name)) {
                    name = mManager.getSimSerialNumber();
                    Log.d(TAG, "getSimSerialNumber() -> name=" + name);
                }
            }
        }
        // Still empty at this point set using default constructor
        if (TextUtils.isEmpty(name)) {
            name = UUID.randomUUID().toString();
            Preferences.setDeviceId(context,
                    UUID.randomUUID().toString());
        }
        // Finally set the name
        if (!TextUtils.isEmpty(name)) {
            Preferences.setDeviceName(context, name);
        }
        Log.d(TAG, "getDeviceName() -> name=" + name);
        return name;
    }

    public static String getDeviceName(Context context) {
        return  getDeviceName(context, false);
    }
}
