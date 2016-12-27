package org.sana.android.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sana.R;
import org.sana.android.content.core.EncounterWrapper;
import org.sana.android.content.core.ObservationWrapper;
import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.Encounters;
import org.sana.android.provider.Observations;
import org.sana.api.IObservation;
import org.sana.core.Encounter;
import org.sana.core.Observation;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class EncounterListDetailFragment extends BaseFragment {

    ViewGroup mContainer = null;
    Uri mUri = null;
    public EncounterListDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encounter_list_detail, container, false);
        mContainer = (ViewGroup) view.findViewById(R.id.ll_encounters);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUri = getActivity().getIntent().getData();
        String uuid = ModelWrapper.getUuid(mUri, getActivity());
        loadAppointmentNotes(mContainer, uuid);
    }

    // TODO finish this - these could be moved to a discrete component
    public void loadAppointmentNotes(ViewGroup root, String uuid) {
        // query encounter table
        String selection = Encounters.Contract.SUBJECT + "= '" + uuid + "'";
        EncounterWrapper wrapper = null;
        List<Encounter> encounters = new ArrayList<>();
        try {
            wrapper = new EncounterWrapper(getActivity().getContentResolver().query(Encounters.CONTENT_URI, null,
                    selection, null, BaseContract.CREATED +" ASC"));
            while (wrapper.moveToNext()) {
                encounters.add((Encounter)wrapper.getObject());
            }
        } catch (Exception e) {

        } finally {
            if (wrapper != null) wrapper.close();
        }
        // Replace this with ListView etc. that holds each follow up child View
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (Encounter e: encounters) {
            // For each encounter in query
            // Replace 0 with resourceId for follow up note view
            View child = inflateEncounter(inflater, root,
                    R.layout.list_encounter_detail_item,
                    R.layout.list_encounter_detail_observation_item, e);
            root.addView(child);
        }
        // This is going to require downsync to get any appointment
        // notes from other VHTs and midwives
    }

    // TODO
    public View inflateEncounter(LayoutInflater inflater, ViewGroup root,
                                 int resId, int subResId, Encounter encounter){
        // Inflate root view of follow up note child
        ViewGroup child = (ViewGroup) inflater.inflate(resId, root, false);

        // Set date view
        final java.text.DateFormat df = DateFormat.getDateFormat(getActivity());
        TextView date = (TextView) child.findViewById(R.id.txt_date);
        date.setText(df.format(encounter.getCreated()));
        // query observation table by UUID of Encounter
        String selection = Observations.Contract.ENCOUNTER +" = '" +
                encounter.getUuid() +"'";
        ObservationWrapper wrapper = null;
        try {
            wrapper = new ObservationWrapper(getActivity().getContentResolver().query(
                    Observations.CONTENT_URI,null,
                    selection, null, BaseContract.CREATED+" ASC"));
            while(wrapper.moveToNext()){
                View details = inflateObservations(inflater, child, subResId,
                        wrapper.getObject());
                child.addView(details);
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(wrapper !=null) wrapper.close();
        }
        return child;
    }

    // TODO
    public View inflateObservations(LayoutInflater inflater, ViewGroup root,
                                    int resId, IObservation object){
        // inflate view
        View view = inflater.inflate(resId, root, false);

        // load concept
        TextView concept = (TextView) view.findViewById(R.id.txt_concept);
        String conceptName = object.getConcept().getName().toLowerCase();
        String first = conceptName.substring(0,1).toUpperCase();
        String remainder = conceptName.substring(1);
        concept.setText(first.concat(remainder));

        // load value
        TextView value = (TextView) view.findViewById(R.id.txt_value);
        value.setText(object.getValue_text());

        return view;
    }
}
