package org.sana.android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.sana.R;
import org.sana.android.db.DatabaseHelper;
import org.sana.android.fragment.AmbulanceDriverListFragment;
import org.sana.android.provider.AmbulanceDrivers;
import org.sana.net.Response;

import java.util.ArrayList;

public class AmbulanceDriverListActivity extends FragmentActivity implements AmbulanceDriverListFragment.OnDriverSelectedListener {

    AmbulanceDriverListFragment mAmbulanceDriverListFragment;
    protected ProgressDialog mProgressDialog = null;
    private ArrayList<String> driverDetailsArrayList = new ArrayList<String>();
    private Dialog dialog;
    public static final String TAG = AmbulanceDriverListActivity.class.getName();
    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            Log.d(TAG, "context: " + context.getClass().getSimpleName() +
                    ", intent: " + intent.toUri(Intent.URI_INTENT_SCHEME));
            handleBroadcast(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_driver_list);
    }
    @Override
    public void onPause() {
        super.onPause();
        hideProgressDialog();
        LocalBroadcastManager.getInstance(
                getApplicationContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerLocalBroadcastReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment.getClass() == AmbulanceDriverListFragment.class) {
            mAmbulanceDriverListFragment = (AmbulanceDriverListFragment) fragment;
            mAmbulanceDriverListFragment.setOnDriverSelectedListener(this);
            if (mAmbulanceDriverListFragment.sync(this, AmbulanceDrivers.CONTENT_URI)) {
                showProgressDialog(getString(R.string.general_synchronizing),
                        getString(R.string.general_fetching_drivers));
            }
        }
    }

    public void showProgressDialog(String title, String message) {
        Log.i(TAG, "hideProgressDialog(String,String)");
        hideProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        Log.i(TAG, "hideProgressDialog()");
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onDriverSelected(long driverId) {

        int maxIndex = AmbulanceDriverListFragment.data.size() / 2;


        String[] data = {
                "" + AmbulanceDriverListFragment.data.get((int) driverId)
//                ""+AmbulanceDriverListFragment.data.get((int)driverId-1),
//                ""+AmbulanceDriverListFragment.data.get((int)driverId-1)
        };
        Toast.makeText(this, "" + AmbulanceDriverListFragment.data.size() + " the id is " + driverId, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Driver details")
                .setItems(data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user clicked okay
                    }
                })
                .create();
        builder.show();

    }

    /**
     * get the database items and populate
     * an array list.
     *
     * @return ArrayList
     */
    public ArrayList<String> getDriverDetails(long driverId) {
        //open database helper class
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        //get a readable database
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final String selectQuery = "SELECT first_name, last_name, phone_number FROM ambulancedriver ";//WHERE " + BaseContract._ID+
        // "=" +driverId;
        //fetching the data using the cursor
        final Cursor cursor = db.rawQuery(selectQuery, null);

        startManagingCursor(cursor);
        while (cursor.moveToNext()) {
            driverDetailsArrayList.add(cursor.getString(cursor.getColumnIndex(AmbulanceDrivers.Contract.FIRST_NAME)));
            driverDetailsArrayList.add(cursor.getString(cursor.getColumnIndex(AmbulanceDrivers.Contract.LAST_NAME)));
            driverDetailsArrayList.add(cursor.getString(cursor.getColumnIndex(AmbulanceDrivers.Contract.PHONE_NUMBER)));
        }

        return driverDetailsArrayList;
    }

    public IntentFilter buildFilter() {
        Log.i(TAG, "buildFilter()");
        IntentFilter filter = new IntentFilter(Response.RESPONSE);
        filter.addDataScheme(AmbulanceDrivers.CONTENT_URI.getScheme());
        try {
            filter.addDataType(AmbulanceDrivers.CONTENT_TYPE);
            filter.addDataType(AmbulanceDrivers.CONTENT_ITEM_TYPE);
        } catch (IntentFilter.MalformedMimeTypeException e) {

        }
        return filter;
    }


    protected void registerLocalBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter){
        Log.i(TAG, "registerLocalBroadcastReceiver(BroadcastReceiver,IntentFilter)");
        LocalBroadcastManager.getInstance(
                getApplicationContext()).registerReceiver(receiver, filter);
    }

    protected void registerLocalBroadcastReceiver(BroadcastReceiver receiver){
        Log.i(TAG, "registerLocalBroadcastReceiver(BroadcastReceiver)");
        IntentFilter filter = buildFilter();
        registerLocalBroadcastReceiver(receiver, filter);
    }

    protected void handleBroadcast(Intent intent){
        Log.i(TAG,"handleBroadcast(Intent)");
        // Extract data included in the Intent
        Log.d(TAG, "...intent: " + ((intent != null)?
                intent.toUri(Intent.URI_INTENT_SCHEME):
                "null"
        ));
        int result = intent.getIntExtra(Response.CODE, 400);
        Log.d(TAG, "...code=" + result);
        if (result == 100) {
            Log.d(TAG, "...code=100, CONTINUE" );
            // do nothing
        }  else if (result == 200){
            Log.d(TAG, "...code=" + result + ", SUCCESS");
            hideProgressDialog();
        } else if (result >= 400){
            Log.w(TAG, "...code=" + result + ", Something went wrong?");
            hideProgressDialog();
        }
    }
}
