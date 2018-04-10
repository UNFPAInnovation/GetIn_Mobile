package org.sana.android.app;

import android.net.Uri;

/**
 * Declares a class will handle an error report.
 */
public interface ErrorHandler {

    void onHandleReport(Uri uri);

    void onHandleReport(Uri uri, String message);
}
