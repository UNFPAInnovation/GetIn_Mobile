package org.sana.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.sana.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AmbulanceDriverListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public AmbulanceDriverListFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initialize fields that depend on the Activity Context
        // i.e. the Adapter

        // set the list item click listener
        getListView().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ambulance_driver_list, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Implement click to call functionality
    }

    public static class AmbulanceDriverCursorAdapter extends CursorAdapter {

        final int mLayoutResource;

        public AmbulanceDriverCursorAdapter(Context context, Cursor c) {
            super(context, c, true);
            // Replace -1 with layout resource id for row layout
            mLayoutResource = -1;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // inflate mLayoutResource
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // read current cursor row and load into child views declared
            // in mLayoutResource
        }
    }
}
