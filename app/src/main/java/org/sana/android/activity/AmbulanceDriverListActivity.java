package org.sana.android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.sana.R;
import org.sana.android.fragment.AmbulanceDriverListFragment;
import org.sana.android.provider.AmbulanceDrivers;

import java.util.ArrayList;

public class AmbulanceDriverListActivity extends FragmentActivity implements AmbulanceDriverListFragment.OnDriverSelectedListener{

    AmbulanceDriverListFragment mAmbulanceDriverListFragment;
    protected ProgressDialog mProgressDialog = null;
    private ArrayList<String> driverDetailsArrayList = new ArrayList<String>();
    private Dialog dialog;
    private Cursor mCursor;
    private CursorAdapter mCursorAdapter;
    private Uri mUri = AmbulanceDrivers.CONTENT_URI;

    static final String[] mProjection = new String[] {
            AmbulanceDrivers.Contract._ID,
            AmbulanceDrivers.Contract.FIRST_NAME,
            AmbulanceDrivers.Contract.LAST_NAME,
            AmbulanceDrivers.Contract.PHONE_NUMBER
    };

    public static final String TAG = AmbulanceDriverListActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_driver_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment.getClass() == AmbulanceDriverListFragment.class){
            mAmbulanceDriverListFragment = (AmbulanceDriverListFragment) fragment;
            mAmbulanceDriverListFragment.setOnDriverSelectedListener(this);
            if(mAmbulanceDriverListFragment.sync(this, AmbulanceDrivers.CONTENT_URI)) {
                showProgressDialog(getString(R.string.general_synchronizing),
                        getString(R.string.general_fetching_drivers));
            }
        }
    }
    public void showProgressDialog(String title, String message){
        Log.i(TAG,"hideProgressDialog(String,String)");
        hideProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }
    public void hideProgressDialog(){
        Log.i(TAG, "hideProgressDialog()");
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onDriverSelected(long driverId) {
        int maxIndex = AmbulanceDriverListFragment.data.size()/2;

        String selection = AmbulanceDrivers.Contract._ID +"=" +(int)driverId;
        //queries the ambulance driver and returns the results
        mCursor = getContentResolver().query(
                mUri,
                mProjection,
                selection,
                null,
                null
        );

        //handle the exception or error in case the cursor is null
        if(mCursor == null){
            //handle the error
            Log.e(TAG,"the cursor is null");
        }else if(mCursor.getCount()<1){
            //in case there is no match
            Log.v(TAG, "no match found");
        }else{
            //handle the results from the cursor

            mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                    mCursor,new String[]{AmbulanceDrivers.Contract.FIRST_NAME, AmbulanceDrivers.Contract.LAST_NAME,
            AmbulanceDrivers.Contract.PHONE_NUMBER},new int[]{android.R.id.text1,android.R.id.text2,
            R.id.phoneNumber});
            //Toast.makeText(this, "results from the cursor \n", Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Driver details")
                    .setAdapter(mCursorAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            builder.show();

        }

    }

}
