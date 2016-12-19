package org.sana.android.activity;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.sana.R;
import org.sana.android.content.Intents;
import org.sana.android.content.Uris;
import org.sana.android.content.core.EncounterWrapper;
import org.sana.android.content.core.PatientWrapper;
import org.sana.android.fragment.PatientListFragment;
import org.sana.android.procedure.Procedure;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.Encounters;
import org.sana.android.provider.Observations;
import org.sana.android.provider.Patients;
import org.sana.api.IModel;
import org.sana.api.task.EncounterTask;
import org.sana.core.Encounter;
import org.sana.core.Observation;
import org.sana.core.Observer;
import org.sana.core.Patient;
import org.sana.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by MainBoardroom on 11/17/2016.
 */
public class PatientViewActivity extends MainActivity {
    TextView age, trimester, mapdate, edd, givenName, familyName;
    Button back;
    // Replace with date only format - do not include time.
    static final SimpleDateFormat sdf = new SimpleDateFormat(IModel.DATE_FORMAT,
            Locale.US);
    static java.text.DateFormat shortDateFormat = null;
    Patient patient = null;

    // public Patient getPatient(Uri uri) {
    // return (Patient) PatientWrapper.get(this, uri);
    //}
    public static Uri withAppendedUuid(Uri uri, String uuid) {

        // uuid = mSubject.getLastPathSegment();

        uri = Uris.withAppendedUuid(Patients.CONTENT_URI, uuid);

        return uri;
    }

    protected ListView mListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girl_details);
        shortDateFormat = DateFormat.getDateFormat(this);

        mSubject = getIntent().getData();
        String uuid = mSubject.getLastPathSegment();
        patient = getPatient(mSubject);

        age = (TextView) findViewById(R.id.girlDetail_age);
        trimester = (TextView) findViewById(R.id.girlDetail_trimester);
        mapdate = (TextView) findViewById(R.id.girlDetail_mapping_date);
        edd = (TextView) findViewById(R.id.girlDetail_edd);
        back = (Button) findViewById(R.id.btn_exit1);
        // Initialize other Views
        setText(R.id.girlDetail_mapping_date, shortDateFormat.format(
                patient.getModified()));
        setText(R.id.girlDetail_family_name, patient.getGiven_name());
        setText(R.id.girlDetail_last_name, patient.getFamily_name());
        setText(R.id.girlDetail_village, patient.getVillage());
        setText(R.id.girlDetail_trimester, calculateTrimester(patient.getGestationDays()));
        setText(R.id.girlDetail_age, String.valueOf(patient.getAge()) + " yo");

        // Calculate edd
        Date lmd = patient.getLMD();
        String p3 = patient.getUuid();

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int gestation_days = 280;
        c2.setTime(lmd);
        c2.add(Calendar.DATE, gestation_days);
        Date edd1 = c2.getTime();
        // Show date only - do not include time
        setText(R.id.girlDetail_edd, shortDateFormat.format(edd1));
        //edd.setText(shortDateFormat.format(edd1));

        // Are there any other mapping details to load?

        // Fill in appointment notes
        // Need a ListView for appointment notes
        // Something like the following
        // mListView = (ListView)findViewById();
        // loadAppointments(mListView, uuid);
    }

    public void setText(int id, String val) {
        TextView view = (TextView) findViewById(id);
        view.setText(val);
    }

    // TODO Check that these values are correct
    public String calculateTrimester(int days) {
        if (days <= 104) {
            // First <= 104 days
            return "1st";
        } else if (days > 104 && days <= 195) {
            // Second > 104 and <= 195
            return "2nd";
        } else {
            return "3rd";
        }

    }

    // TODO finish this - these could be moved to a discrete component
    public void loadAppointmentNotes(ViewGroup root, String uuid) {
        // query encounter table
        String selection = Encounters.Contract.SUBJECT + "= '" + patient.getUuid() + "'";
        Cursor cursor = null;
        EncounterWrapper wrapper = null;
        List<Encounter> encounters = new ArrayList<>();
        try {
            cursor = getContentResolver().query(Encounters.CONTENT_URI, null,
                    selection, null, BaseContract.CREATED +" ASC");
            wrapper = new EncounterWrapper(cursor);
            while (wrapper.moveToNext()) {
                encounters.add((Encounter)wrapper.getObject());
            }
        } catch (Exception e) {

        } finally {
            if (wrapper != null) wrapper.close();
        }
        cursor = null;
        // Replace this with ListView etc. that holds each follow up child View
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Encounter e: encounters) {
            // For each encounter in query
            // Replace 0 with resourceId for follow up note view
            View child = inflateEncounter(inflater, root, 0,0, e);
            root.addView(child);
        }
        // This is going to require downsync to get any appointment
        // notes from other VHTs and midwives
    }

    // TODO
    public View inflateEncounter(LayoutInflater inflater, ViewGroup root,
                                 int resId, int subResId, Encounter encounter){
        // Inflate root view of follow up note child
        ViewGroup child = null;

        // query observation table by UUID of Encounter
        // selection = Observations.Contract.ENCOUNTER +" = '" +
        // Return only columns we need
        // projection = ???
        Cursor cursor = null;
        // cursor = getContentResolver().query(Observations.CONTENT_URI,null, selection, null, null);
        //
        // While cursor.moveToNext
        View notes = inflateObservations(inflater, child, subResId, cursor);
        // add as child to root - which is the Encounter meta data
        child.addView(notes);
        return child;
    }

    // TODO
    public View inflateObservations(LayoutInflater inflater, ViewGroup root,
                                 int resId, Cursor cursor){
        // Follows same pattern as inflateEncounters
        // inflate each observation view
        // return loaded view
        return null;
    }

    public void submit(View view){
        switch(view.getId()){
            case R.id.btn_exit1:
                Intent intent = new Intent();
                onSaveAppState(intent);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_call_pnumber:
                if(!TextUtils.isEmpty(patient.getpNumber())) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + patient.getpNumber()));
                }
                break;
            case R.id.btn_call_powerholder:
                if(!TextUtils.isEmpty(patient.getHolder_pNumber())) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + patient.getHolder_pNumber()));
                }
                break;
        }
    }

    /**
     *
     * @param uuid
     */
    public void syncEncounters(String uuid){
        Uri.Builder builder = Encounters.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(Encounters.Contract.SUBJECT, uuid);
        Uri data = builder.build();
        Intent intent = new Intent(Intents.ACTION_READ);
        intent.setData(data);
        startService(intent);
    }

    /**
     *
     * @param uuids
     */
    public void syncObservations(String[] uuids){
        Uri.Builder builder = Observations.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(Observations.Contract.ENCOUNTER + "__in",
                TextUtils.join(",", uuids));
        Uri data = builder.build();
        Intent intent = new Intent(Intents.ACTION_READ);
        intent.setData(data);
        startService(intent);
    }
}
