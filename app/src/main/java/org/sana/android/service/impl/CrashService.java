package org.sana.android.service.impl;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.MultipartEntity;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.sana.BuildConfig;
import org.sana.R;
import org.sana.android.app.ConnectionManager;
import org.sana.android.app.ErrorManager;
import org.sana.android.content.Intents;
import org.sana.android.content.Uris;
import org.sana.android.net.MDSInterface2;
import org.sana.android.provider.Updates;
import org.sana.android.util.Dates;
import org.sana.net.http.HttpTaskFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CrashService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String TAG = CrashService.class.getSimpleName();
    public static final String ACTION_SEND = BuildConfig.APPLICATION_ID + ".service.impl.action.SEND_REPORT";

    public static final String ACTION_RESEND = BuildConfig.APPLICATION_ID +
            ".service.impl.action.RESEND_REPORT";

    public static final int ID = CrashService.class.hashCode();

    public CrashService() {
        super("CrashService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent()");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND.equals(action)) {
                final Uri param1 = intent.getParcelableExtra(Intents.EXTRA_REPORT);
                final String message = intent.getStringExtra(Intents.EXTRA_MESSAGE);
                handleActionSend(param1, String.valueOf(message));
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSend(Uri report, String message) {
        Log.d(TAG, "handleActionSend()");
        try {
            if (!Uris.isEmpty(report)) {
                File file = new File(report.getPath());
                if (file.exists() && file.isDirectory()) {
                    return;
                } else if (!file.exists())
                    return;
            }
            if (!ConnectionManager.connected(this)) {
                registerResend(report);
                return;
            }
            Uri remote = getUri(this);
            URI uri = URI.create(remote.toString());
            HttpPost method = new HttpPost(uri);
            String authKey = getAuth(this);
            method.addHeader("Authorization", authKey);
            Date created = new Date();
            //created.setTime(created.getTime() - Dates.getTzOffset());
            Part[] parts;
            if (!Uris.isEmpty(report)) {
                parts = new Part[]{
                        new FilePart("report", new File(report.getPath())),
                        new StringPart("device", ConnectionManager.getDeviceName(this)),
                        new StringPart("created", Dates.toSQL(created)),
                        new StringPart("message", ErrorManager.normalizeMessage(message)),
                        new StringPart("version", String.valueOf(BuildConfig.VERSION_CODE))
                };
            } else {
                parts = new Part[]{
                        new StringPart("device", ConnectionManager.getDeviceName(this)),
                        new StringPart("created", Dates.toSQL(created)),
                        new StringPart("message", ErrorManager.normalizeMessage(message)),
                        new StringPart("version", String.valueOf(BuildConfig.VERSION_CODE))
                };
            }
            MultipartEntity mpEntity = new MultipartEntity(parts);
            method.setEntity(mpEntity);
            HttpClient client = HttpTaskFactory.CLIENT_FACTORY.produce();
            HttpResponse response = client.execute(method);
            StatusLine status = response.getStatusLine();
            Log.d(TAG, "Crash report POST: code=" + status.getStatusCode());
            ErrorManager.flush(getApplicationContext());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleActionSend(Uri report) {
        handleActionSend(report, "None");
    }

    private void handleActionSend(String message) {
        handleActionSend(Uri.EMPTY, message);
    }

    private void registerResend(Uri report, String message) {
        try {
            Intent intent = new Intent(CrashService.ACTION_SEND);
            intent.putExtra(Intents.EXTRA_REPORT, report)
                    .putExtra(Intents.EXTRA_MESSAGE, message);
            PendingIntent mPendingIntent = PendingIntent.getService(getApplicationContext(),
                    report.hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 30000, mPendingIntent);
        } catch (Exception e) {

        }

    }

    private void registerResend(Uri report) {
        registerResend(report, "None");
    }

    public static String getAuth(Context context) {
        return "Bearer " + context.getString(R.string.key_api_secret);
    }

    public static Uri getUri(Context context) {
        String authority = MDSInterface2.getAuthority(context);
        String scheme = MDSInterface2.getScheme(context);
        Uri uri = Uris.iriToUri(scheme, authority, Updates.CONTENT_URI);
        Uri.Builder builder = uri.buildUpon();
        builder.appendEncodedPath("report/submit/");
        return builder.build();
    }
}
