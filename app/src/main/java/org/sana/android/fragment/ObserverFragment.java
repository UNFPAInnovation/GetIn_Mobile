package org.sana.android.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sana.R;
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
public class ObserverFragment extends ListFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ModelSelectedListener<Observer> mListener = null;
    static final String[] mProjection = null;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ObserverFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ObserverFragment newInstance(int columnCount) {
        ObserverFragment fragment = new ObserverFragment();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static class ObserverCursorAdapter extends CursorAdapter{

        protected Map<Long, Observer> mHolders = new HashMap<>();


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
            final int firstNameCol = cursor.getColumnIndex(Observers.Contract.FIRST_NAME);
            final int lastNameCol = cursor.getColumnIndex(Observers.Contract.LAST_NAME);
            final int phoneNumberCol = cursor.getColumnIndex(Observers.Contract.PHONE_NUMBER);
            setText(view, R.id.first_name, cursor.getString(firstNameCol));
            setText(view, R.id.last_name, cursor.getString(lastNameCol));
            setText(view, R.id.phone_number, cursor.getString(phoneNumberCol));
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
