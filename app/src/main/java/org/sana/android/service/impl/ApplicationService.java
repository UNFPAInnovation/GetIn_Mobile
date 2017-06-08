/**
 * Copyright (c) 2013, Sana
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sana nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL Sana BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF 
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sana.android.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.sana.BuildConfig;
import org.sana.R;
import org.sana.android.Constants;
import org.sana.android.content.Uris;
import org.sana.android.net.MDSInterface2;
import org.sana.android.provider.Models;
import org.sana.android.provider.Procedures;
import org.sana.android.util.Logf;
import org.sana.android.util.SanaUtil;
import org.sana.clients.Client;
import org.sana.net.Response;
import org.sana.net.http.HttpTaskFactory;
import org.sana.net.http.handler.ClientResponseHandler;
import org.sana.net.http.handler.FileHandler;

import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Sana Development
 *
 */
public class ApplicationService extends IntentService {
	public static final String TAG = ApplicationService.class.getSimpleName();
    public static final String UPDATE_CHECK = "org.sana.intent.action.UPDATE_CHECK";
    public static final String UPDATE_GET = "org.sana.intent.action.UPDATE_GET";
    public static final String UPDATE_INSTALL = "org.sana.intent.action.UPDATE_INSTALL";

    public static final Uri CONTENT_URI = Uri.parse("content://"
            + Models.AUTHORITY + "/mds/clients/");
    public static final String UPDATE_URI = "org.sana.extra.UPDATE_URI";
    public static final String MIMETYPE_PKG = "application/vnd.android.package-archive";

    private static class UpdateTask extends AsyncTask<Void,Integer,Void>{

        private final Context mContext;

        public UpdateTask(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(ApplicationService.isChecking())
                return null;
            ApplicationService.setChecking(true);
            try {
                Client client = getCurrent(mContext);
                if (client.version > version(mContext)) {
                    Log.w(TAG, "...update available!");
                    Uri uri = getDownloadUri(mContext, client);
                    String authKey = getAuth(mContext);
                    String name = "getin-" + BuildConfig.FLAVOR + "-" + client.version + ".apk";
                    Uri apk = getUpdatePackage(name, uri, authKey);
                    if (apk != Uri.EMPTY) {
                        installUpdate(mContext, apk);
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            ApplicationService.setChecking(false);
            return null;
        }
    }

    private static final AtomicBoolean mChecking = new AtomicBoolean(false);

    public static void setChecking(boolean checking){
        mChecking.set(checking);
    }

    public static boolean isChecking(){
        return mChecking.get();
    }

    private static String getAuth(Context context){
        return "Bearer " + context.getString(org.sana.R.string.key_api_secret);
    }

    public static URI getCheckUri(Context context) throws MalformedURLException, URISyntaxException {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String authority = context.getString(R.string.host_mds);
        String scheme = MDSInterface2.getScheme(preferences);
        int port = 443;
        try{
            port = Integer.valueOf(preferences.getString(
                    Constants.PREFERENCE_MDS_PORT, "443"));
        } catch (Exception e){
            e.printStackTrace();
        }
        return Uris.iriToURI(CONTENT_URI,scheme, authority, port,  "");
    }

    public static Uri getDownloadUri(Context context, Client client){
        Uri uri = Uri.EMPTY;
        Uri.Builder builder = uri.buildUpon();
        // TODO Change to when server fixed
        // builder.scheme(MDSInterface2.getScheme(context))
        builder.scheme("http")
                .authority(context.getString(R.string.host_mds));
        if(client != null && !TextUtils.isEmpty(client.app)){
            for(String segment:client.app.split("/")){
                if(!TextUtils.isEmpty(segment))
                    builder.appendPath(segment);
            }
            uri = builder.build();
        }
        return uri;
    }

    public static int version(Context context){
        String pkgName = context.getPackageName();
        // Current version
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        int version = -1;
        try {
            pi = pm.getPackageInfo(pkgName, 0);
            version = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    UpdateTask task = null;

    public ApplicationService() {
		super(Application.class.getName());
		
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Logf.D(TAG, "onHandleIntent(Intent)", "handling");
		//checkForInitialization();
        String action = intent.getAction();
        if(action.equals(UPDATE_CHECK)){
            UpdateTask task = new UpdateTask(this.getApplicationContext());
            task.execute();
        }
	}
	
	final void checkForInitialization(){
		Logf.D(TAG, "initialize()", "Entering");
	    SharedPreferences preferences = 
        		PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	    
	    String dbKey = getString(R.string.cfg_db_init);
	    String dbVersion = getString(R.string.cfg_db_version);
	    // check whether the db is initialized and create if not
	    boolean doInit = preferences.getBoolean(dbKey, false);
		Logf.D(TAG, "initialize()", "dbs initialized: " + doInit);
		
	    if(!doInit){
	    	getContentResolver().acquireContentProviderClient(Procedures.CONTENT_URI).release();
	    	SanaUtil.loadDefaultDatabase(getBaseContext());
	    	preferences.edit().putBoolean(dbKey, true)
	    		.putInt("s_app_sync_period", 1209600000)
	    		.putLong("s_app_sync_last", System.currentTimeMillis())
	    		.putInt(dbVersion, getResources().getInteger(R.integer.cfg_db_version_value))
	    		.commit();
	    }
	}
	
	final void checkForUpdate(){
		Logf.D(TAG, "update()", "Entering");
	    String dbVersion = getString(R.string.cfg_db_version);
	    int version = getResources().getInteger(R.integer.cfg_db_version_value);
	    String dbKey = getString(R.string.cfg_db_init);

	    SharedPreferences preferences = 
        		PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	    // check whether the db is initialized and create if not
	    int currentVersion = preferences.getInt(dbVersion, 0);

	    boolean doUpdate = (currentVersion != 0) && currentVersion < version;
		Log.d(TAG, "...updating: " + doUpdate);
	    if(doUpdate){
	    	getContentResolver().delete(Procedures.CONTENT_URI, null, null);
	    	SanaUtil.loadDefaultDatabase(getBaseContext());
	    	preferences.edit().putBoolean(dbKey, true)
	    		.putInt(dbVersion, version).commit();
	    }
	}

    public static Client getCurrent(Context context){
        URI uri = null;
        Client client = new Client();
        Response<Collection<Client>> response = Response.empty();
        response.message = Collections.EMPTY_LIST;
        final ClientResponseHandler responseHandler = new ClientResponseHandler();
        String token = getAuth(context);
        try {
            uri = getCheckUri(context);
            response = MDSInterface2.apiGet(uri, token, responseHandler);//MDSInterface2.apiGet(uri, token, responseHandler);
            if(response.getMessage() != null && response.getMessage().size() ==1) {
                client = response.getMessage().iterator().next();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static Uri getUpdatePackage(String name, Uri remoteUri, String authKey){
        Uri apkUri = Uri.EMPTY;
        File out = null;
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, name);
        FileHandler handler = new FileHandler(file);
        URI uri = URI.create(remoteUri.toString());
        HttpGet get = new HttpGet(uri);
        get.addHeader("Authorization", authKey);
        HttpClient client = HttpTaskFactory.CLIENT_FACTORY.produce();
        HttpResponse response = null;
        try {
            response = client.execute(get);
            out = handler.handleResponse(response);
            Log.e(TAG, "Size: " + out.length());
            return Uri.fromFile(file);
        } catch (IOException e) {
            Log.e(TAG, "ERROR UPDATING");
            //e.printStackTrace();
        }
        return Uri.EMPTY;
    }

    public static void installUpdate(Context context, Uri uri){
        Log.d(TAG, "installUpdate()");
        Log.d(TAG, "...logged in installing");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, MIMETYPE_PKG);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
