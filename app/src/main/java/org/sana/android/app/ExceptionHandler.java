package org.sana.android.app;

import android.net.Uri;

/**
 * Created by winkler.em@gmail.com, on 09/29/2016.
 */
public class ExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    public static final String TAG = ExceptionHandler.class.getSimpleName();
    private final ErrorHandler handler;
    private final String device;

    public ExceptionHandler(ErrorHandler handler, String device) {
        this.handler = handler;
        this.device = device;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Uri uri = ErrorManager.writeReport(device, exception);
        String message = ErrorManager.normalizeMessage(exception);
        handler.onHandleReport(uri, message);
    }
}
