package org.sana.android.activity;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.sana.R;
import org.sana.android.content.Uris;
import org.sana.android.content.core.PatientWrapper;
import org.sana.android.fragment.PatientListFragment;
import org.sana.android.procedure.Procedure;
import org.sana.android.provider.Encounters;
import org.sana.android.provider.Patients;
import org.sana.api.IModel;
import org.sana.api.task.EncounterTask;
import org.sana.core.Observer;
import org.sana.core.Patient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by MainBoardroom on 11/17/2016.
 */
public class PatientViewActivity extends MainActivity {
    TextView age, trimester5, mapdate, edd,village, family_name, last_name;
    Button back;
    // Replace with date only format - do not include time.
    static final SimpleDateFormat sdf = new SimpleDateFormat(IModel.DATE_FORMAT,
            Locale.US);

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
        age = (TextView) findViewById(R.id.girlDetail_age);
        trimester5 = (TextView) findViewById(R.id.girlDetail_trimester);
        mapdate = (TextView) findViewById(R.id.girlDetail_mapping_date);
        edd = (TextView) findViewById(R.id.girlDetail_edd);
        last_name = (TextView) findViewById(R.id.girlDetail_last_name);
        family_name = (TextView) findViewById(R.id.girlDetail_family_name);
        village = (TextView) findViewById(R.id.girlDetail_village);
        back = (Button) findViewById(R.id.btn_exit1);
        // Initialize other Views
        // Exit button

        mSubject = getIntent().getData();
        String uuid = mSubject.getLastPathSegment();
        Patient patient=  getPatient(mSubject);

       Date dob =  patient.getDob();
        Date lmd = patient.getLMD();
        String familyName = patient.getFamily_name();
        String lastName = patient.getGiven_name();
        String Village = patient.getVillage();
       // String p3 = patient.getUuid();
        Date p4 = patient.getCreated();


//calculate the age
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        Date x = c2.getTime();
        long days = x.getTime() - dob.getTime();

        long days1 = days / (1000 * 60 * 60 * 24);
        int age1 = (int) (days1);

        // Calculate trimester
        Calendar c3 = Calendar.getInstance();
        c3.setTime(new Date());
        Date x1 = c3.getTime();
        long trimester2 = x1.getTime() - lmd.getTime();

        long trimester1 = trimester2 / (1000 * 60 * 60 * 24);
        int trimester = (int) (trimester2);
        int trim ;
        if (trimester <84){
             trim = 1;
        }
        else if (84<trimester &&trimester<= 168){
             trim = 2;
        }
     else {
            trim =3;
        }

        // calculate EDD

        int gestation_days = 280;

        c2.setTime(lmd);
        c2.add(Calendar.DATE, gestation_days);
        Date edd1 = c2.getTime();
        // Show date only - do not include time

        //setTextFields
        age.setText(String.valueOf(age1));
        last_name.setText(lastName);
        family_name.setText(familyName);
       village.setText(Village);
        trimester5.setText(String.valueOf(trim));
        mapdate.setText(sdf.format(p4));
        edd.setText(sdf.format(edd1));

        // Are there any other mapping details to load?

        // Fill in appointment notes
        // Need a ListView for appointment notes
        // Something like the following
        // mListView = (ListView)findViewById();
        // loadAppointments(mListView, uuid);
    }

    public void loadAppointmentNotes(ListView view, String uuid){
        // query encounter table
        // selection = Encounters.Contract.SUBJECT = 'uuid'
        // projection = new String[]{ BaseContract.ID, BaseContract.CREATED };
        // getContentResolver().query(Encounters.CONTENT_URI, projection, selection, null, null);


        // For each encounter in query
            // Load meta data for each encounter to a view
            // Probably just the date cursor from query column(1)

            // query observation table
            // selection = Observations.Contract.ENCOUNTER +" = '" +
            // projection =
            //getContentResolver().query(Observations.CONTENT_URI,null, selection, null, null);

        // This is going to require downsync to get any appointment
        // notes from other VHTs and midwives
    }


}

















