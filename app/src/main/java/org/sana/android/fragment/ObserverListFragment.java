package org.sana.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.sana.R;
import org.sana.android.content.Uris;
import org.sana.android.content.core.ObserverWrapper;
import org.sana.android.provider.Observers;
import org.sana.core.Observer;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link ModelSelectedListener}
 * interface.
 */
public class ObserverListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ModelSelectedListener<Observer> mListener = null;
    private ObserverCursorAdapter mAdapter = null;
    private Uri mUri = Observers.CONTENT_URI;
//    static final String[] mProjection = null;
    public static final String TAG = ObserverListFragment.class.getName();

    static final String[] mProjection = new String[] {
            Observers.Contract._ID,
            Observers.Contract.FIRST_NAME,
            Observers.Contract.LAST_NAME,
            Observers.Contract.PHONE_NUMBER,
            Observers.Contract.LOCATIONS
    };
    static final String mSelect = Observers.Contract.ROLE +" = 'vht'";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ObserverListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ObserverListFragment newInstance(int columnCount) {
        ObserverListFragment fragment = new ObserverListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observer_list, container, false);
        // TODO Anything else related to the view that needs to happen here
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ModelSelectedListener) {
            mListener = (ModelSelectedListener<Observer>) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ModelSelectedListener<Observer>");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO initialize mAdapter
        mAdapter = new ObserverCursorAdapter(getActivity(), null, 0);
        setListAdapter(mAdapter);

        LoaderManager.enableDebugLogging(true);
        getActivity().getSupportLoaderManager().initLoader(Uris.OBSERVER_DIR, null, this);
        // set the list item click listener
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity(),
                mUri,
                null,//mProjection,
                mSelect, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || (cursor !=null && cursor.getCount() == 0)) {
            setEmptyText(getString(R.string.msg_drivers_vht));
        }
        if(cursor != null){
            ((ObserverCursorAdapter) this.getListAdapter()).swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((ObserverCursorAdapter)this.getListAdapter()).swapCursor(null);
    }

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
                }

            } else {
                if (mListener != null) {
                    mListener.onModelSelected((Observer)mAdapter.getItem(position));
                }
            }
        }

    public static class ObserverCursorAdapter extends CursorAdapter{

        protected Map<Integer, Observer> mHolders = new HashMap<>();


        public ObserverCursorAdapter(Context context, Cursor c, int resId) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View child = inflater.inflate(R.layout.list_item_observer, parent, false);
            return child;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ObserverWrapper wrapper = new ObserverWrapper(cursor);
            if(wrapper == null || wrapper.isBeforeFirst() || cursor.getCount() == 0)
                return;
            Observer obj = (Observer) wrapper.getObject();
            view.setTag(obj.getPhoneNumber());
            // TODO fill in any fields etc
            setText(view, R.id.first_name, obj.getFirstName());
            setText(view, R.id.last_name, obj.getLastName());
            setText(view, R.id.phone_number, obj.getPhoneNumber());
            mHolders.put(cursor.getPosition(), obj);
            Log.v(TAG,",.,.,.<><><><><><.,.,.,.<><><><>"+obj.getFirstName());
        }

        public Object getItem(int position){
            return mHolders.get(position);
        }

        protected void setText(View root, int resId, String text){
            TextView child = (TextView) root.findViewById(resId);
            child.setText(text);
        }
    }
}
