package org.sana.android.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.sana.android.service.impl.ApplicationService;

import java.util.Calendar;

/**
 * Created by winkler.em@gmail.com, on 06/05/2017.
 */
public class Main extends Application {
    public static final String TAG = Main.class.getSimpleName();
    private static final int UPDATE_CHECK = 0;

    @Override
    public void onCreate() {
        super.onCreate();
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
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pi);
    }
}
