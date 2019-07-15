package org.sana.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import org.sana.R;

public class NotificationUtils {
    private static final String TAG = NotificationUtils.class.getSimpleName();
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(final String title, final String message, Intent intent, int id) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        showSmallNotification(title, message, resultPendingIntent, id);
    }

    private void showSmallNotification(String title, String message, PendingIntent resultPendingIntent, int id) {
        NotificationManager mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_ALL);
        mNotificationManager.notify(id, mBuilder.build());
    }
}
