package org.sana.android.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.sana.android.service.NotificationTriggerService;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    String TAG = NotificationAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "alarm started");
        Intent i = new Intent(context, NotificationTriggerService.class);
        context.startService(i);
    }
}
