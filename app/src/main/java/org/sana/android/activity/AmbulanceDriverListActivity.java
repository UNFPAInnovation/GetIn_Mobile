package org.sana.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.sana.R;
import org.sana.android.fragment.AmbulanceDriverListFragment;

public class AmbulanceDriverListActivity extends FragmentActivity {

    AmbulanceDriverListFragment mAmbulanceDriverListFragment;
    public static final String TAG = AmbulanceDriverListActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_driver_list);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment.getClass() == AmbulanceDriverListFragment.class){
            mAmbulanceDriverListFragment = (AmbulanceDriverListFragment) fragment;
        }
    }
}
