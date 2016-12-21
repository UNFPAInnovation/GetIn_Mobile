package org.sana.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.View;

import org.sana.R;
import org.sana.android.fragment.EncounterListDetailFragment;

public class EncounterListDetailActivity extends BaseActivity {

    EncounterListDetailFragment mFragment = null;
    CursorAdapter mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter_list_detail);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragment instanceof  EncounterListDetailFragment){
            mFragment = (EncounterListDetailFragment) fragment;
        }
    }

    public void onExit(View view){
        finish();
    }
}
