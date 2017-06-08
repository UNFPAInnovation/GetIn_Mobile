package org.sana.android.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.sana.android.service.impl.ApplicationService;

public class UpdateReceiver extends BroadcastReceiver {
    public UpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent update = new Intent(context, ApplicationService.class);
        update.setAction(ApplicationService.UPDATE_CHECK);
        context.startService(intent);
    }
}
