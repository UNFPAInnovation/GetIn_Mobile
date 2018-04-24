package org.sana.android.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import org.sana.BuildConfig;
import org.sana.android.content.Intents;
import org.sana.android.service.impl.CrashService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by winkler.em@gmail.com, on 09/20/2016.
 */
public final class ErrorManager {
    public static final String TAG = ErrorManager.class.getSimpleName();

    public static final String LOG_DIRECTORY = "reports";
    private static final String LINE_SEPARATOR = "\n";
    static final String fileNameFormat = "getin-%04d-%02d-%02d-%02d%02d%02d.log";
    static final FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            return (filename.startsWith("getin") &&
                    filename.endsWith(".log"));
        }
    };

    private ErrorManager() {
    }

    public static Intent getReport(Context context, Uri report, String message) {
        Intent intent = new Intent(CrashService.ACTION_SEND);
        intent.putExtra(Intents.EXTRA_REPORT, report);
        intent.putExtra(Intents.EXTRA_MESSAGE, normalizeMessage(message));
        return intent;
    }

    public static Intent getReport(Context context, Uri report) {
        Intent intent = new Intent(CrashService.ACTION_SEND);
        intent.putExtra(Intents.EXTRA_REPORT, report);
        return intent;
    }

    public static void sendReport(Context context, Uri report, String message) {
        Intent intent = getReport(context, report, message);
        context.startService(intent);
    }

    public static void sendReport(Context context, Uri report) {
        Intent intent = getReport(context, report);
        context.startService(intent);
    }

    public static void sendReport(Context context, Throwable exception) {
        String device = Preferences.getDeviceName(context);
        Uri report = writeReport(device, exception);
        String message = normalizeMessage(exception);
        sendReport(context, report, message);
    }

    public static void sendReportDelayed(Context context, Uri report, String message) {
        Intent intent = getReport(context, report, message);

    }

    /**
     * Writes the stack trace of an exception and other environment information
     * to a file formatted for sending to the mds client log
     *
     * @param exception
     * @return
     */
    public static Uri writeReport(File file, String device, Throwable exception) {
        Uri uri = Uri.EMPTY;
        exception.printStackTrace();

        PrintWriter writer;
        InputStreamReader is;
        try {
            uri = Uri.fromFile(file);
            writer = new PrintWriter(file);
            writer.write("************ CAUSE OF ERROR ************\n\n");
            exception.printStackTrace(writer);

            writer.write("\n************ DEVICE INFORMATION ***********\n");
            writer.write("Device id:" + device);
            writer.write(LINE_SEPARATOR);
            writer.write("Application version:" + BuildConfig.VERSION_CODE);
            writer.write(LINE_SEPARATOR);
            writer.write("Brand: ");
            writer.write(Build.BRAND);
            writer.write(LINE_SEPARATOR);
            writer.write("Device: ");
            writer.write(Build.DEVICE);
            writer.write(LINE_SEPARATOR);
            writer.write("Model: ");
            writer.write(Build.MODEL);
            writer.write(LINE_SEPARATOR);
            writer.write("Id: ");
            writer.write(Build.ID);
            writer.write(LINE_SEPARATOR);
            writer.write("Product: ");
            writer.write(Build.PRODUCT);
            writer.write(LINE_SEPARATOR);
            writer.write("\n************ FIRMWARE ************\n");
            writer.write("SDK: ");
            writer.write(Build.VERSION.SDK);
            writer.write(LINE_SEPARATOR);
            writer.write("Release: ");
            writer.write(Build.VERSION.RELEASE);
            writer.write(LINE_SEPARATOR);
            writer.write("Incremental: ");
            writer.write(Build.VERSION.INCREMENTAL);
            writer.write(LINE_SEPARATOR);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public static Uri writeReport(Context context, String device, Throwable exception) {
        File dir = new File(context.getFilesDir(), LOG_DIRECTORY);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdirs();
            }
        } else {
            dir.mkdirs();
        }
        String fileName = getFileName();
        File file = new File(dir, fileName);
        return writeReport(file, device, exception);
    }

    public static Uri writeReport(String device, Throwable exception) {
        String fileName = getFileName();
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fileName);
        return writeReport(file, device, exception);
    }

    public static Uri writeReport(Throwable exception) {
        return writeReport("", exception);
    }

    public static synchronized final String getFileName() {
        Calendar calendar = Calendar.getInstance();
        String fileName = String.format(fileNameFormat,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
        return fileName;
    }

    public static synchronized void flush(Context context) {
        flush();
        File dir = new File(context.getFilesDir(), LOG_DIRECTORY);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdirs();
            }
        } else {
            dir.mkdirs();
        }
        for (File file : dir.listFiles(filter)) {
            file.delete();
        }
    }

    public static synchronized void flush() {
        File dir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        for (File file : dir.listFiles(filter)) {
            file.delete();
        }
    }

    public static synchronized boolean flush(Uri uri) {

        File file = new File(uri.getPath());
        boolean deleted = false;
        if (file.exists() && !file.isDirectory()) {
            deleted = file.delete();
        }
        return deleted;
    }

    public static String normalizeMessage(Throwable exception) {
        if (exception == null)
            return normalizeMessage("");
        Throwable cause = getRootCause(exception);
        StringBuilder builder = new StringBuilder();
        builder.append(cause.getClass().getSimpleName());
        if (!TextUtils.isEmpty(cause.getMessage())) {
            builder.append(":");
            builder.append(cause.getMessage());
        }
        return normalizeMessage(builder.toString());
    }

    public static String normalizeMessage(String msg) {
        String message = (TextUtils.isEmpty(msg)) ? "None" :
                msg.substring(0, Math.min(msg.length(), 255));
        return message;
    }

    public static Throwable getRootCause(Throwable exception) {
        Throwable cause = exception;
        // Need to certain we get root cause here.
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}
