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
public class PatientViewActivity extends BaseActivity {
    TextView age, trimester, mapdate, edd, givenName, familyName;
    Button back;
    // Replace with date only format - do not include time.
    static final SimpleDateFormat sdf = new SimpleDateFormat(IModel.DATE_FORMAT,
            Locale.US);
    static java.text.DateFormat shortDateFormat = null;
    Patient patient = null;

    protected ListView mListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girl_details);
        shortDateFormat = DateFormat.getDateFormat(this);

        mSubject = getIntent().getData();
        String uuid = mSubject.getLastPathSegment();
        patient = PatientWrapper.get(this, mSubject);

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
        setText(R.id.girlDetail_age, String.valueOf(patient.getAge()) + " yrs");

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
                    startActivityForResult(callIntent,0);
                }
                break;
            case R.id.btn_call_powerholder:
                if(!TextUtils.isEmpty(patient.getHolder_pNumber())) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + patient.getHolder_pNumber()));
                    startActivityForResult(callIntent, 1);
                }
                break;
            case R.id.btn_view_encounters:
                Intent viewIntent = new Intent(this, EncounterListDetailActivity.class);
                viewIntent.setData(getIntent().getData());
                startActivityForResult(viewIntent,2);
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
