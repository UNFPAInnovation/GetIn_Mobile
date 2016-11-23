package org.sana.android.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.sana.R;
import org.sana.android.Constants;
import org.sana.android.app.Locales;
import org.sana.android.app.Preferences;
import org.sana.android.provider.AmbulanceDrivers;
import org.sana.android.widget.ScrollCompleteListener;
import org.sana.util.StringUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class AmbulanceDriverListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    static final String[] mProjection = new String[] {
            AmbulanceDrivers.Contract._ID,
            AmbulanceDrivers.Contract.FIRST_NAME,
            AmbulanceDrivers.Contract.LAST_NAME,
            AmbulanceDrivers.Contract.LOCATION
    };
    private Uri mUri;
    private AmbulanceDriverCursorAdapter mAdapter;

    public AmbulanceDriverListFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUri = getActivity().getIntent().getData();
        if (mUri == null) {
            mUri = AmbulanceDrivers.CONTENT_URI;
        }
        // initialize fields that depend on the Activity Context
        // i.e. the Adapter

        mAdapter = new AmbulanceDriverCursorAdapter(getActivity(), null, 0);
        setListAdapter(mAdapter);

        LoaderManager.enableDebugLogging(true);
        getActivity().getSupportLoaderManager().initLoader(2, null, this);
        // set the list item click listener
        getListView().setOnItemClickListener(this);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_ambulance_driver_list, container, false);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(),
                mUri,
                mProjection,
                null, null, AmbulanceDrivers.FIRST_NAME_SORT_ORDER);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || (cursor !=null && cursor.getCount() == 0)) {
            setEmptyText(getString(R.string.msg_drivers_vht));
        }
        if(cursor != null){
            //mAdapter.swapCursor(cursor);
            ((AmbulanceDriverCursorAdapter) this.getListAdapter()).swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        ((AmbulanceDriverCursorAdapter) this.getListAdapter()).swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Implement click to call functionality
    }

    /**
     * adapter for ambulance driver contact information
     * @author Marting
     */
    public static class AmbulanceDriverCursorAdapter extends CursorAdapter {

        final int mLayoutResource;

        private final LayoutInflater mInflater;

        private ScrollCompleteListener mScrollListener = null;


        public AmbulanceDriverCursorAdapter(Context context, Cursor c) {
            super(context, c, true);
            mInflater = LayoutInflater.from(context);
            // Replace -1 with layout resource id for row layout
            mLayoutResource = R.id.header;
        }
        public AmbulanceDriverCursorAdapter(Context context, Cursor c, int flags){
            super(context, c,  flags);
            mInflater = LayoutInflater.from(context);

            String locale = Preferences.getString(context, Constants.PREFERENCE_LOCALE, "en");
            Locales.updateLocale(context, locale);
            // Replace -1 with layout resource id for row layout
            mLayoutResource = R.id.header;
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // inflate mLayoutResource
            View view = mInflater.inflate(R.layout.ambulancedriver_list_item, null);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // read current cursor row and load into child views declared
            // in mLayoutResource

            int position = this.getCursor().getPosition();

            String firstName = ((Cursor)getItem(position)).getString(2);
            String lastName = ((Cursor)getItem(position)).getString(3);
            String phoneNumber = ((Cursor)getItem(position)).getString(1);
            String location = ((Cursor)getItem(position)).getString(4);

            //format the first name and the last name and then set the name textview with the formatted string
            String displayName = StringUtil.formatPatientDisplayName(firstName, lastName);
            TextView nameTextView = (TextView) view.findViewById(R.id.ambulance_driver_name);
            nameTextView.setText(displayName);

            //create the phone number textview and set its text to the string from the database
            TextView phoneNumberTextView = (TextView) view.findViewById(R.id.ambulance_driver_phone_number);
            phoneNumberTextView.setText(phoneNumber);

            //create the location textView and set its text to the string from the database
            TextView locationTextView = (TextView) view.findViewById(R.id.ambulance_driver_location);
            locationTextView.setText(location);


            if(cursor.isLast() || cursor.getPosition() >= (cursor.getCount()*0.8)){
                if(mScrollListener != null)
                    mScrollListener.onScrollComplete();
            }
        }
    }
}
