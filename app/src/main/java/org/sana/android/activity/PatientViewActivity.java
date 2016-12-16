package org.sana.android.activity;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.sana.R;
import org.sana.android.content.Uris;
import org.sana.android.content.core.PatientWrapper;
import org.sana.android.fragment.PatientListFragment;
import org.sana.android.procedure.Procedure;
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
    TextView age, trimester, mapdate, edd;
    Button back;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girl_details);
        age = (TextView) findViewById(R.id.girlDetail_age);
        trimester = (TextView) findViewById(R.id.girlDetail_trimester);
        mapdate = (TextView) findViewById(R.id.girlDetail_mapping_date);
        edd = (TextView) findViewById(R.id.girlDetail_edd);
        back = (Button) findViewById(R.id.btn_exit1);


        mSubject = getIntent().getData();
        String uuid = mSubject.getLastPathSegment();

        Uri ur=  withAppendedUuid(Patients.CONTENT_URI,uuid);

        Patient patient=  getPatient(mSubject);

        patient.getDob();
       Date lmd = patient.getLMD();
        String p3 = patient.getUuid();
        Date p4 = patient.getCreated();

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        Date x = c2.getTime();
        long days = x.getTime() - lmd.getTime();

       long days1 = days / (1000 * 60 * 60 * 24);
        int age1 = (int) (days1);

        int gestation_days = 280;

        c2.setTime(lmd);
        c2.add(Calendar.DATE, gestation_days);
        age.setText(String.valueOf(age1));
        Date edd1 = c2.getTime();
        edd.setText(sdf.format(edd1));
    }



}

















