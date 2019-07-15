package org.sana.android.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.sana.android.activity.MainActivity;
import org.sana.android.app.Main;
import org.sana.android.util.SanaUtil;

import java.util.Calendar;
import java.util.Date;

public class NotificationTriggerService extends IntentService {
    private static final String TAG = NotificationTriggerService.class.getSimpleName();

    public NotificationTriggerService() {
        super("NotificationTriggerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        triggerAppUsageReminderNotification();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String activityName = cn.getPackageName();
        Log.d(TAG, "onHandleIntent: current activity " + activityName);

        // only show notification when app is not open
//        if (activityName.contains("org.sana.android")) {
        if (false) {
            Log.d(TAG, "onStop: save show pref true");
        } else {
            Log.d(TAG, "onHandleIntent: display notification started");
            Date currentDate = Calendar.getInstance().getTime();
            long lastOpenedDateLong = SanaUtil.getAppLastOpened(getApplicationContext());
            Date diff = new Date(lastOpenedDateLong - currentDate.getTime());
            Log.d(TAG, "onHandleIntent: date difference " + diff);

            int days = daysBetween(new Date(lastOpenedDateLong), currentDate);
            Log.d(TAG, "onHandleIntent: days between " + days);

            if (days >= 3) {
                Log.d(TAG, "onHandleIntent: days greater than 3");
                Intent intentActivity = new Intent(this, MainActivity.class);
                NotificationUtils notificationUtils = new NotificationUtils(this);
                notificationUtils.showNotificationMessage("Use GetIn", "Please don't forget to use the GetIn app to accept jobs", intentActivity, 20);
            }
        }
        stopService(intent);
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    private void triggerAppUsageReminderNotification() {
        Log.d(TAG, "triggerAppUsageReminderNotification: started");
        Intent alarmIntent = new Intent(this, NotificationTriggerService.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 4376725, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}