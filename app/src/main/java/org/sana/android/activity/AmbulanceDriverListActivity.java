package org.sana.android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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


        String [] data = {
                ""+AmbulanceDriverListFragment.data.get(maxIndex-(int)driverId)
//                ""+AmbulanceDriverListFragment.data.get((int)driverId-1),
//                ""+AmbulanceDriverListFragment.data.get((int)driverId-1)
        };
//        Toast.makeText(this, ""+AmbulanceDriverListFragment.data.size()+" the id is "+driverId + " max index: "+maxIndex
//                , Toast.LENGTH_SHORT).show();

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

}
