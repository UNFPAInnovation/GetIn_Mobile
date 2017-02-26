
package org.sana.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.sana.R;
import org.sana.android.Constants;
import org.sana.android.activity.BaseActivity;
import org.sana.android.app.Locales;
import org.sana.android.app.Preferences;
import org.sana.android.content.Intents;
import org.sana.android.content.core.ObserverWrapper;
import org.sana.android.provider.Patients;
import org.sana.android.provider.Patients.Contract;
import org.sana.android.util.Bitmaps;
import org.sana.android.util.Dates;
import org.sana.android.util.Logf;
import org.sana.android.widget.ScrollCompleteListener;
import org.sana.api.IObserver;
import org.sana.core.Location;
import org.sana.core.Observer;
import org.sana.core.Patient;
import org.sana.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment displaying all patients.
 * 
 * @author Sana Development Team
 */
public class PatientListFragment extends ListFragment implements LoaderCallbacks<Cursor>, ScrollCompleteListener {

    public static final String TAG = PatientListFragment.class.getSimpleName();

    private static final int PATIENTS_LOADER = 0;
    static final String[] mProjection = new String[] {
		    Contract._ID,
            Contract.UUID,
		    Contract.GIVEN_NAME,
		    Contract.FAMILY_NAME,
		//Contract.PATIENT_ID,
		//Contract.LOCATION,
		    Contract.IMAGE,
            Contract.VILLAGE,
            Contract.PNUMBER,
            Contract.DOB
		};
    
    private Uri mUri;
    private PatientCursorAdapter mAdapter;
    private OnPatientSelectedListener mListener;
    Handler mHandler; 
    private boolean doSync = false;
    private int delta =1000*60;
    private ScrollCompleteListener mScrollListener = null;

    //
    // Activity Methods
    //

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    	Locales.updateLocale(this.getActivity(), getString(R.string.force_locale));
        delta = getResources().getInteger(R.integer.sync_delta_subjects);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        
        // signal the dispatcher to sync
        mUri = getActivity().getIntent().getData();
        if (mUri == null) {
            mUri = Patients.CONTENT_URI;
        }
    	Log.d(TAG, "onActivityCreated(): sync?");
        mAdapter = new PatientCursorAdapter(getActivity(), null, 0);
        setListAdapter(mAdapter);
        mAdapter.setOnScrollCompleteListener(this);
        // Do we sync with server
        delta = getActivity().getResources().getInteger(R.integer.sync_delta_subjects);
        //sync(getActivity(), Subjects.CONTENT_URI);
    	LoaderManager.enableDebugLogging(true);
        getActivity().getSupportLoaderManager().initLoader(PATIENTS_LOADER, null, this);
    }

    /** {@inheritDoc} */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Object tag = v.getTag();
        if (mListener != null) {
            mListener.onPatientSelected(id);
        }
    }

    //
    // Loader Callbacks
    //

    /** {@inheritDoc} */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
    	Log.d(TAG, "onCreateLoader() "); 
        CursorLoader loader = new CursorLoader(getActivity(), 
        		mUri,
        		mProjection,
                null, null, Patients.GIVEN_NAME_SORT_ORDER);
        return loader;
    }

    /** {@inheritDoc} */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    	Log.d(TAG, "onLoadFinished() "); 
    	
        if (cursor == null || (cursor !=null && cursor.getCount() == 0)) {
            setEmptyText(getString(R.string.msg_no_patients));
        }
        if(cursor != null)
            //mAdapter.swapCursor(cursor);
        	((PatientCursorAdapter) this.getListAdapter()).swapCursor(cursor);
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    	Log.d(TAG, "onLoaderReset() ");
    	//mAdapter.swapCursor(null);
    	((PatientCursorAdapter) this.getListAdapter()).swapCursor(null);
    }

    /**
     * Events specific to this PatientListFragment
     * 
     * @author Sana Development Team
     */
    public interface OnPatientSelectedListener {
        /**
         * Callback when a patient is selected in the list.
         * 
         * @param patientId The selected patient's ID.
         */
        public void onPatientSelected(long patientId);

        //public void onPatientViewSelected(View view, long id);
    }

    /**
     * Sets a listener to this fragment.
     * 
     * @param listener
     */
    public void setOnPatientSelectedListener(OnPatientSelectedListener listener) {
        mListener = listener;
    }

    static class ViewHolder{
        Patient patient;
        ImageView image;
        TextView name;
        TextView systemId;
        TextView location;
        TextView edd;
        TextView label;
        int position = 1;
    }
    
    /**
     * Adapter for patient information
     * 
     * @author Sana Development Team
     */
    public static class PatientCursorAdapter extends CursorAdapter implements SectionIndexer{
    	
    	//private final Activity context;
        private int[] mRowStates = new int[0];
        private AlphabetIndexer mAlphaIndexer = null;
        private final LayoutInflater mInflater;
        
        private static final int STATE_UNKNOWN = 0;
        private static final int STATE_LABELED = 1;
        private static final int STATE_UNLABELED = 2;
        
        private static final String ALPHABET = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private final String mAlphabet;
        private String dateFormat = null;
        private SimpleDateFormat sdf;
        private ScrollCompleteListener mScrollListener = null;
        private String[] months;

        int given_nameCol = 0;
        int family_nameCol = 0;
        int dobCol = 0;
        int pnumberCol = 0;
        int villageCol = 0;
        int imageCol = 0;
        int uuidCol = 0;

        public PatientCursorAdapter(Context context, Cursor c) {
        	super(context.getApplicationContext(),c,false);
            mInflater = LayoutInflater.from(context);
            dateFormat = context.getString(R.string.display_date_format);
            sdf = new SimpleDateFormat(dateFormat);
            mAlphabet = " " + mContext.getString(R.string.cfg_alphabet);
            String locale = Preferences.getString(context, Constants.PREFERENCE_LOCALE, "en");
            months = context.getResources().getStringArray(R.array.months_long_format);
    		init(c);
        }
        
        public PatientCursorAdapter(Context context) {
            this(context, null, 0);
        }
        
        public PatientCursorAdapter(Context context, Cursor c, int flags) {
        	super(context,c, flags);
            mInflater = LayoutInflater.from(context);
            dateFormat = context.getString(R.string.display_date_format);
            sdf = new SimpleDateFormat(dateFormat);
            mAlphabet = " " + mContext.getString(R.string.cfg_alphabet);
            String locale = Preferences.getString(context, Constants.PREFERENCE_LOCALE, "en");
            Locales.updateLocale(context, locale);
            months = context.getResources().getStringArray(R.array.months_long_format);
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
            setColumnIndexes(c);
        }

        public void setColumnIndexes(Cursor cursor){
            if(cursor == null) return;

            given_nameCol = cursor.getColumnIndex(Contract.GIVEN_NAME);
            family_nameCol = cursor.getColumnIndex(Contract.FAMILY_NAME);
            dobCol = cursor.getColumnIndex(Contract.DOB);
            pnumberCol = cursor.getColumnIndex(Contract.PNUMBER);
            imageCol = cursor.getColumnIndex(Contract.IMAGE);
            villageCol = cursor.getColumnIndex(Contract.VILLAGE);
            uuidCol= cursor.getColumnIndex(Contract.UUID);

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
            setColumnIndexes(cursor);
        	super.changeCursor(cursor);
        }
        
        @Override
        public Cursor swapCursor(Cursor newCursor) {
        	Log.i(TAG + ".mAdapter", "swapCursor(Cursor)");
        	index(newCursor);
            setColumnIndexes(newCursor);
            return super.swapCursor(newCursor);
        }
        
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
        	Log.d(TAG+".mAdapter", "bindView(): cursor position: " + ((cursor != null)? cursor.getPosition(): 0));
        	int position = this.getCursor().getPosition();
            String uuid = cursor.getString(uuidCol);
            view.setTag(uuid);
            // Set patient name and image
            ImageView image = (ImageView)view.findViewById(R.id.image);
            String imagePath = cursor.getString(imageCol);
            
        	//image.setImageResource(R.drawable.unknown);
            if(imagePath != null){
            	try {
                    image.setImageBitmap(Bitmaps.decodeSampledBitmapFromFile(
                            Uri.parse(imagePath).getPath(), 128, 128));
                } catch(java.io.FileNotFoundException e){
                    Log.e(TAG, e.getMessage());
                    image.setImageResource(R.drawable.ic_contact_picture);
            	} catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    image.setImageResource(R.drawable.ic_contact_picture);
            	}
            } 
            
            String familyName = cursor.getString(family_nameCol);
            String givenName = cursor.getString(given_nameCol);
            String displayName = StringUtil.formatPatientDisplayName(givenName, familyName);
            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(displayName);

           // TextView systemId = (TextView)view.findViewById(R.id.system_id);
          //  String id = ((Cursor) this.getItem(position)).getString(3);
            //String id = mWrapper.getStringField(Contract.PATIENT_ID);
           // systemId.setText((TextUtils.isEmpty(id)? "000000":id));

            TextView phoneNumber =  (TextView)view.findViewById(R.id.phoneNumber1);
            String phoneNumberVal = cursor.getString(pnumberCol);
            phoneNumber.setText(phoneNumberVal);

            TextView dobView = (TextView)view.findViewById(R.id.dob);
            String dobStr = cursor.getString(dobCol);
            String localDobStr = null;
            Date dob = null;
            try {
                localDobStr = this.getDateStringFromSQL(dobStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //String id = mWrapper.getStringField(Contract.PATIENT_ID);
            dobView.setText((TextUtils.isEmpty(localDobStr)? dobStr:
                    localDobStr));

            TextView village = (TextView)view.findViewById(R.id.village);
            String villageVal = cursor.getString(villageCol);
            village.setText(villageVal);


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
            // Handle scroll complete when we bind last item in cursor
            Log.d(TAG, "...cursor count=" + cursor.getCount());
            Log.d(TAG, "...cursor position=" + cursor.getPosition());
            if(cursor.isLast() || cursor.getPosition() >= (cursor.getCount()*0.8)){
                if(mScrollListener != null)
                    mScrollListener.onScrollComplete();
            }
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
        	Log.d(TAG+".mAdapter", "get view ");
            return super.getView(position,convertView, parent);
        }
        
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        	Log.d(TAG+".mAdapter", "new view  cursor position: " + ((cursor != null)? cursor.getPosition(): 0)); 
            View view = mInflater.inflate(R.layout.patient_list_item, null);
            //bindView(view, context, cursor);
            return view;
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

		@Override
	    public Object[] getSections()
	    {
			if(mAlphaIndexer == null)
				return null;
	        return mAlphaIndexer.getSections();
	    }

        public String formatName(Cursor cursor){
            String givenName = cursor.getString(cursor.getColumnIndex(Contract
                    .GIVEN_NAME));
            String familyName = cursor.getString(cursor.getColumnIndex(Contract
                    .FAMILY_NAME));
            String displayName = StringUtil.formatPatientDisplayName(givenName,
                    familyName);
            return displayName;
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

        public String getDateStringFromSQL(String date) throws ParseException {
            Date d = Dates.fromSQL(date);
            DateTime dt = new DateTime(d);
            int month = dt.getMonthOfYear();
            int dayOfMonth = dt.getDayOfMonth();
            int year = dt.getYear();
            String localizedMonth = months[month - 1];
            return String.format("%02d %s %04d", dayOfMonth, localizedMonth, year);
        }
        public String getDateString(Date date) {
            return sdf.format(date);
        }

        public void setOnScrollCompleteListener(ScrollCompleteListener listener){
            mScrollListener = listener;
        }
    }
    
    public final boolean sync(Context context, Uri uri){
    	Log.d(TAG, "sync(Context,Uri)");
        boolean result = false;
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	long lastSync = prefs.getLong("patient_sync", 0);
    	long now = System.currentTimeMillis();
    	Log.d(TAG, "last: " + lastSync +", now: " + now+ ", delta: " + (now-lastSync) + ", doSync: " + ((now - lastSync) > 86400000)); 
    	// TODO
    	// Once a day 86400000
    	if((now - lastSync) > delta){
            // village is a text field so we need to send individually
            List<String> villageNames = getVillageNamesForObserver();
            for(String village:villageNames) {
                // Append the village list query parameter
                Uri.Builder builder = uri.buildUpon();
                builder.appendQueryParameter("village", village);
                Intent intent = new Intent(Intents.ACTION_READ, builder.build());
                context.startService(intent);
            }
            Logf.W(TAG, "sync(): synchronizing patient list");
            prefs.edit().putLong("patient_sync", now).commit();
            result = true;
    	}
        return result;
    }

    public final boolean syncForced(Context context, Uri uri){
        Log.d(TAG, "syncForced(Context,Uri)");
        List<String> villageNames = getVillageNamesForObserver();
        boolean result = false;
        for(String village:villageNames) {
            Uri.Builder builder = uri.buildUpon();
            builder.appendQueryParameter("village", village);
            Intent intent = new Intent(Intents.ACTION_READ, builder.build());
            context.startService(intent);

        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        long now = System.currentTimeMillis();
        // Once a day 86400000
        prefs.edit().putLong("patient_sync", now).commit();
        result = true;
        return result;
    }

    public void setOnScrollCompleteListener(ScrollCompleteListener listener){
        mScrollListener = listener;
    }

    public final void onScrollComplete(){
        Log.i(TAG, "onScrollComplete()");
        if(mScrollListener != null)
            mScrollListener.onScrollComplete();
    }

    public void filter(String constraint){
        mAdapter.getFilter().filter(constraint);
    }

    public String getVillageQueryString(){
        // Get current logged in username - this is a little hacked
        String user = Preferences.getString(getActivity(), Constants.PREFERENCE_EMR_USERNAME);
        // Get logged in user list of village names
        Observer observer = (Observer) ObserverWrapper.getOneByUsername(
                getActivity().getContentResolver(), user);
        ObserverWrapper.initializeRelated(getActivity(), observer);
        List<Location> locations = observer.getLocations();
        List<String> villages = new ArrayList<>();
        for(Location location: locations){
            villages.add(location.getName());
        }
        return TextUtils.join(",", villages);
    }

    public List<String> getVillageNamesForObserver(){
        // Get current logged in username - this is a little hacked
        String user = Preferences.getString(getActivity(), Constants.PREFERENCE_EMR_USERNAME);
        // Get logged in user list of village names
        Observer observer = (Observer) ObserverWrapper.getOneByUsername(
                getActivity().getContentResolver(), user);
        ObserverWrapper.initializeRelated(getActivity(), observer);
        List<Location> locations = observer.getLocations();
        List<String> villages = new ArrayList<>();
        for(Location location: locations){
            villages.add(location.getName());
        }
        return villages;
    }
}
