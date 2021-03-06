package org.sana.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import org.sana.R;
import org.sana.android.Constants;
import org.sana.android.app.Locales;
import org.sana.android.app.Preferences;
import org.sana.android.content.Intents;
import org.sana.android.provider.VHTs;
import org.sana.android.widget.ScrollCompleteListener;
import org.sana.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Marting on 20/12/2016.
 */

public class VHTListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    public static final String TAG = VHTListFragment.class.getName();

    static final String[] mProjection = new String[] {
            VHTs.Contract._ID,
            VHTs.Contract.FIRST_NAME,
            VHTs.Contract.LAST_NAME,
            VHTs.Contract.PHONE_NUMBER,
            VHTs.Contract.LOCATION
    };
    private Uri mUri;
    private VHTListFragment.VHTCursorAdapter mAdapter;
    private OnVHTSelectedListener mListener;
    private int delta =1000*60;
    public static ArrayList<String> data = new ArrayList<String>();

    public VHTListFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUri = getActivity().getIntent().getData();
        if (mUri == null) {
            mUri = VHTs.CONTENT_URI;
        }
        // initialize fields that depend on the Activity Context
        // i.e. the Adapter

        mAdapter = new VHTListFragment.VHTCursorAdapter(getActivity(), null, 0);
        setListAdapter(mAdapter);

        LoaderManager.enableDebugLogging(true);
        getActivity().getSupportLoaderManager().initLoader(2, null, this);
        // set the list item click listener
        getListView().setOnItemClickListener(this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(),
                mUri,
                mProjection,
                null, null, VHTs.FIRST_NAME_SORT_ORDER);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || (cursor !=null && cursor.getCount() == 0)) {
            setEmptyText(getString(R.string.msg_drivers_vht));
        }
        if(cursor != null){
            //mAdapter.swapCursor(cursor);
            ((VHTListFragment.VHTCursorAdapter) this.getListAdapter()).swapCursor(cursor);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        ((VHTListFragment.VHTCursorAdapter) this.getListAdapter()).swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Implement click to call functionality
        Object number = view.getTag();
        if(number != null){
            // TODO Should probably check that number is valid pattern
            Intent intent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:" + String.valueOf(number)));
//            Log.v(TAG, "the telephone number is" +String.valueOf(number));
            if(number.toString().length() == 10){
                startActivity(intent);
            }else {
                Toast.makeText(VHTListFragment.this.getContext(), "invalid phone number", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (mListener != null)
                mListener.onVHTSelected(id);
        }
    }
    /**
     * Events specific to this AmbulanceDriverListFragment
     *
     * @author Sana Development Team
     */
    public interface OnVHTSelectedListener {
        /**
         * Callback when a vht is selected in the list.
         *
         * @param vhtId The selected patient's ID.
         */
        public void onVHTSelected(long vhtId);
    }

    /**
     * Sets a listener to this fragment.
     *
     * @param listener
     */
    public void setOnVHTSelectedListener(OnVHTSelectedListener listener) {
        mListener = listener;
    }


    /**
     * adapter for vht contact information
     * @author Marting
     */
    public static class VHTCursorAdapter extends CursorAdapter implements SectionIndexer {

        final int mLayoutResource;
        private final LayoutInflater mInflater;
        private ScrollCompleteListener mScrollListener = null;

        private int[] mRowStates = new int[0];
        private AlphabetIndexer mAlphaIndexer = null;
        private final String mAlphabet;

        private static final int STATE_UNKNOWN = 0;
        private static final int STATE_LABELED = 1;
        private static final int STATE_UNLABELED = 2;


        public VHTCursorAdapter(Context context, Cursor c) {
            super(context, c, true);
            mInflater = LayoutInflater.from(context);
            mAlphabet = " " + mContext.getString(R.string.cfg_alphabet);
            // Replace -1 with layout resource id for row layout
            mLayoutResource = R.id.header;

            init(c);
        }
        public VHTCursorAdapter(Context context, Cursor c, int flags){
            super(context, c,  flags);
            mInflater = LayoutInflater.from(context);
            mAlphabet = " " + mContext.getString(R.string.cfg_alphabet);

            String locale = Preferences.getString(context, Constants.PREFERENCE_LOCALE, "en");
            Locales.updateLocale(context, locale);
            // Replace -1 with layout resource id for row layout
            mLayoutResource = R.id.header;

            init(c);
        }
        private void init(Cursor c) {
            if (c == null) {
                return;
            }
            //mWrapper = new PatientWrapper(c);
            //c.setNotificationUri(context.getContentResolver(), Patients.CONTENT_URI);
            mRowStates = new int[c.getCount()];
            Arrays.fill(mRowStates, STATE_UNKNOWN);
            if(mRowStates.length > 0)
                mRowStates[0] = STATE_LABELED;
            mAlphaIndexer = new AlphabetIndexer(c,
                    1,
                    mAlphabet);
            mAlphaIndexer.setCursor(c);
        }
        public Cursor index(Cursor cursor){
            if(cursor != null){
                mRowStates = new int[cursor.getCount()];
                mAlphaIndexer = new AlphabetIndexer(cursor,
                        1,
                        mAlphabet);
                mAlphaIndexer.setCursor(cursor);
                Arrays.fill(mRowStates, STATE_UNKNOWN);
            } else {
                mRowStates = new int[0];
                mAlphaIndexer = null;
            }
            if(mRowStates.length > 0)
                mRowStates[0] = STATE_LABELED;
            return cursor;
        }
        @Override
        public void changeCursor (Cursor cursor){
            Log.d(TAG+".mAdapter", "changeCursor(Cursor)");
            index(cursor);
            super.changeCursor(cursor);
        }

        @Override
        public Cursor swapCursor(Cursor newCursor) {
            Log.i(TAG + ".mAdapter", "swapCursor(Cursor)");
            index(newCursor);
            return super.swapCursor(newCursor);
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // inflate mLayoutResource
            View view = mInflater.inflate(R.layout.vht_list_item, null);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // read current cursor row and load into child views declared
            // in mLayoutResource

            int position = this.getCursor().getPosition();

            String firstName = ((Cursor)getItem(position)).getString(1);
            String lastName = ((Cursor)getItem(position)).getString(2);
            String phoneNumber = ((Cursor)getItem(position)).getString(3);
            String location = ((Cursor)getItem(position)).getString(4);

            //format the first name and the last name and then set the name textview with the formatted string
            String displayName = StringUtil.formatPatientDisplayName(firstName, lastName);
            TextView nameTextView = (TextView) view.findViewById(R.id.vht_name);
            nameTextView.setText(displayName);

            data.add(" "+displayName+" "+phoneNumber);
//            data.add(phoneNumber);
            //create the phone number textview and set its text to the string from the database
            TextView phoneNumberTextView = (TextView) view.findViewById(R.id.vht_phone_number);
            phoneNumberTextView.setText(phoneNumber);

            //location textView
            TextView locationTextView = (TextView) view.findViewById(R.id.vht_location);
            locationTextView.setText(location);
            // Set view tag to the phone number
            view.setTag(phoneNumber);
            //create the location textView and set its text to the string from the database
//            TextView locationTextView = (TextView) view.findViewById(R.id.ambulance_driver_location);
//            locationTextView.setText(location);


            // Alphabet divider
            boolean needsSeparator = false;
            // pos is 0 based array index,
            int pos = ((Cursor) getItem(position)).getPosition();
            char currentSectionLabel = getSectionLabel(displayName);

            Log.d(TAG, "...Checking if needs row separator label. " +
                    "position="+pos);
            switch (mRowStates[pos]) {
                case STATE_LABELED:
                    needsSeparator = true;
                    break;
                case STATE_UNLABELED:
                    needsSeparator = false;
                    break;
                case STATE_UNKNOWN:
                default:
                    // First cell always needs to be sectioned
                    if (pos == 0) {
                        needsSeparator = true;
                        mRowStates[pos] = STATE_LABELED;
                    } else {
                        char prevSectionLabel = getSectionLabel(
                                formatName(((Cursor) getItem(position -1))));
                        Log.d(TAG,"...prev section=" + prevSectionLabel +", " +
                                "current section=" + currentSectionLabel);
                        if (prevSectionLabel != currentSectionLabel) {
                            needsSeparator = true;
                            mRowStates[pos] = STATE_LABELED;
                        } else {
                            needsSeparator = false;
                            mRowStates[pos] = STATE_UNLABELED;
                        }
                        ((Cursor) this.getItem(position)).moveToPosition(pos);
                    }
                    break;
            }
            if (needsSeparator) {
                Log.d(TAG, "...adding separator");
                view.findViewById(R.id.header).setVisibility(View.VISIBLE);
                TextView label = (TextView) view.findViewById(R.id.txt_section);
                label.setText(("" + currentSectionLabel).toUpperCase(Locale.getDefault()));
            } else {
                Log.d(TAG, "...hiding separator");
                view.findViewById(R.id.header).setVisibility(View.GONE);
            }

            if(cursor.isLast() || cursor.getPosition() >= (cursor.getCount()*0.8)){
                if(mScrollListener != null)
                    mScrollListener.onScrollComplete();
            }
        }

        @Override
        public Object[] getSections() {
            if(mAlphaIndexer == null)
                return null;
            return mAlphaIndexer.getSections();
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            if(mAlphaIndexer == null)
                return 0;
            return mAlphaIndexer.getPositionForSection(sectionIndex);
        }

        @Override
        public int getSectionForPosition(int position) {
            if(mAlphaIndexer == null)
                return 0;
            return mAlphaIndexer.getSectionForPosition(position);
        }
        public char getSectionLabel(String str){
            str = str.trim();
            if (!TextUtils.isEmpty(str)) {
                str = str.substring(0, 1).toLowerCase(Locale.getDefault());
            } else {
                str = " ";
            }
            return str.charAt(0);
        }
        public String formatName(Cursor cursor){
            String givenName = cursor.getString(cursor.getColumnIndex(VHTs.Contract.FIRST_NAME
            ));
            String familyName = cursor.getString(cursor.getColumnIndex(VHTs.Contract.LAST_NAME
            ));
            String displayName = StringUtil.formatPatientDisplayName(givenName,
                    familyName);
            return displayName;
        }
    }
    public final boolean sync(Context context, Uri uri){
        Log.d(TAG, "sync(Context,Uri)");
        boolean result = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        long lastSync = prefs.getLong("vht_sync", 0);
        long now = System.currentTimeMillis();
//        Log.d(TAG, "last: " + lastSync +", now: " + now+ ", delta: " + (now-lastSync) + ", doSync: " + ((now - lastSync) > 86400000));
//        // TODO
        // Once a day 86400000
        if((now - lastSync) > delta){
//            Logf.W(TAG, "sync(): synchronizing vht list");
            prefs.edit().putLong("vht_sync", now).commit();
            Intent intent = new Intent(Intents.ACTION_READ,uri);
            context.startService(intent);
            result = true;
        }
        return result;
    }

}
