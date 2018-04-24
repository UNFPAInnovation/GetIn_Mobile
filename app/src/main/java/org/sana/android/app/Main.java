package org.sana.android.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.sana.android.content.Intents;
import org.sana.android.service.impl.ApplicationService;
import org.sana.android.service.impl.DispatchService;

import java.util.Calendar;

/**
 * Created by winkler.em@gmail.com, on 06/05/2017.
 */
public class Main extends Application implements ErrorHandler {
    public static final String TAG = Main.class.getSimpleName();
    private static final int UPDATE_CHECK = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        String device = ConnectionManager.getDeviceName(this);
        Thread.currentThread().setUncaughtExceptionHandler(
                new ExceptionHandler(this, device));
        onCreateUpdateAlarm(this);
    }

    private void onCreateUpdateAlarm(Context context){
        Intent check = new Intent(this, ApplicationService.class);
        check.setAction(ApplicationService.UPDATE_CHECK);
        startService(check);
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(this, UpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, UPDATE_CHECK,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis() + 10000,
                AlarmManager.INTERVAL_HOUR*4, pi);
    }

    public void onHandleReport(Uri reportUri, String message) {
        try {
            Preferences.setLoggedIn(this, false);
            Intent dispatcher = DispatchService.shutdown(this);
            stopService(dispatcher);
            //TODO Add dirty start flag
            //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            //SharedPreferences.Editor editor = sp.edit();
            // Get restart intent
            PackageManager pm = getApplicationContext().getPackageManager();
            String packageName = getApplicationContext().getPackageName();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Intents.EXTRA_REPORT, reportUri);
            intent.putExtra(Intents.EXTRA_MESSAGE, message);
            intent.putExtra(Intents.EXTRA_ERROR, 1);
            startActivity(intent);

            //Intent error = ErrorManager.getReport(this, reportUri, message);
            //AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //PendingIntent next = PendingIntent.getService(getBaseContext(), 1, error, PendingIntent.FLAG_ONE_SHOT);
            //PendingIntent crash = PendingIntent.getService(getBaseContext(), 2, error, PendingIntent.FLAG_ONE_SHOT);
            //am.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, next);
            //am.set(AlarmManager.RTC, System.currentTimeMillis() + 4000, crash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void onHandleReport(Uri reportUri) {
        onHandleReport(reportUri, "null");
    }
}
