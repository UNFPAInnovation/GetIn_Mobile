package org.sana.android.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.sana.R;
import org.sana.android.db.DatabaseHelper;
import org.sana.android.fragment.AmbulanceDriverListFragment;
import org.sana.android.fragment.VHTListFragment;
import org.sana.android.provider.AmbulanceDrivers;
import org.sana.android.provider.VHTs;

import java.util.ArrayList;

/**
 * Created by Marting on 20/12/2016.
 */

public class VHTList extends FragmentActivity implements VHTListFragment.OnVHTSelectedListener{

    VHTListFragment mVHTListFragment;
    protected ProgressDialog mProgressDialog = null;
    private ArrayList<String> vhtDetailsArrayList = new ArrayList<String>();

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
        if(fragment.getClass() == VHTListFragment.class){
            mVHTListFragment = (VHTListFragment) fragment;
            mVHTListFragment.setOnVHTSelectedListener(this);
            if(mVHTListFragment.sync(this, AmbulanceDrivers.CONTENT_URI)) {
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
    public void onVHTSelected(long driverId) {

        int maxIndex = AmbulanceDriverListFragment.data.size()/2;


        String [] data = {
                ""+AmbulanceDriverListFragment.data.get((int)driverId)
//                ""+AmbulanceDriverListFragment.data.get((int)driverId-1),
//                ""+AmbulanceDriverListFragment.data.get((int)driverId-1)
        };
        Toast.makeText(this, ""+AmbulanceDriverListFragment.data.size()+" the id is "+driverId, Toast.LENGTH_SHORT).show();

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
     * @return ArrayList
     */
    public ArrayList<String> getVHTDetails(long vhtId){
        //open database helper class
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        //get a readable database
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final String selectQuery = "SELECT first_name, last_name, phone_number FROM vht ";//WHERE " + BaseContract._ID+
        // "=" +vhtId;
        //fetching the data using the cursor
        final Cursor cursor = db.rawQuery(selectQuery,null);

        startManagingCursor(cursor);
        while(cursor.moveToNext()){
            vhtDetailsArrayList.add(cursor.getString(cursor.getColumnIndex(VHTs.Contract.FIRST_NAME)));
            vhtDetailsArrayList.add(cursor.getString(cursor.getColumnIndex(VHTs.Contract.LAST_NAME)));
            vhtDetailsArrayList.add(cursor.getString(cursor.getColumnIndex(VHTs.Contract.PHONE_NUMBER)));
        }

        return vhtDetailsArrayList;
    }
}

