package org.sana.android.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.sana.R;
import org.sana.android.content.Intents;
import org.sana.android.procedure.Procedure;
import org.sana.android.provider.EncounterTasks;
import org.sana.android.provider.Observations;
import org.sana.android.provider.Tasks;
import org.sana.android.util.Dates;
import org.sana.api.task.EncounterTask;
import org.sana.core.Encounter;
import org.sana.core.Observer;
import org.sana.core.Patient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by winkler.em@gmail.com, on 03/06/2017.
 */
public class EncounterTaskManager {
    public static final String TAG = EncounterTaskManager.class.getSimpleName();

    public static List<EncounterTask> getTask(Context context, Patient patient,
                                              Procedure procedure,
                                              Observer observer,
                                              Date visitDate)
    {
        //invoke methods from (2) above
        //adding the encounterTasks to the list
        List<EncounterTask> encounterTasks = new ArrayList<EncounterTask>();
        encounterTasks.add(calculateFirstVisit(patient, procedure, observer, visitDate));
        return encounterTasks;
    }

    public static Date getDateFromEncounter(Context context, Uri encounter) throws ParseException {
        Date date = null;
        Cursor cursor = null;
        String encounterUUID = encounter.getLastPathSegment();
        try {
            String dateStr = null;
            String selection = Observations.Contract.ENCOUNTER +"='" +
                    encounterUUID +"'";
            String[] projection = new String[]{
                Observations.Contract.VALUE_TEXT
            };
            cursor = context.getContentResolver().query(Observations.CONTENT_URI,
                    projection,selection,null, null);
            if(cursor != null && cursor.moveToFirst()){
                dateStr = cursor.getString(0);
            }
            date = Dates.fromSQL(dateStr);
        } finally {
            if (cursor != null) cursor.close();
        }
        return date;
    }

    public static List<EncounterTask> getVisit(Patient patient, Procedure procedure, Observer observer, Date visitDate) {
        //invoke methods from (2) above
        //adding the encounterTasks to the list
        List<EncounterTask> encounterTasks = new ArrayList<EncounterTask>();
        encounterTasks.add(calculateFirstVisit(patient, procedure, observer, visitDate));
        return encounterTasks;
    }

    public static void createTasks(Context context, String observer, String subject,
                            String procedure, List<EncounterTask> tasks) {
        org.sana.api.task.Status status = org.sana.api.task.Status.ASSIGNED;

        //String uuid = ModelWrapper.getUuid(Patients.CONTENT_URI,getContentResolver());
        //String uuid1 = mObserver.getLastPathSegment();
        //String uuid3 = mSubject.getLastPathSegment();
        //EncounterTask task= new EncounterTask();
        String uuid = UUID.randomUUID().toString();
        //InputStream uuid= this.getResources().openRawResource(R.raw.midwife_appointment_notexml);
        //tasks.

        //UUID  uui= UUID.randomUUID();
        //String uuid2 = uuid.toString();
        //String uuid1 = uui.toString();
        //String uuid2 = uui.toString();

        for (EncounterTask task : tasks) {

            ContentValues values = toValues(uuid, observer, subject, procedure,
                    task.due_on, status.toString());
            context.getContentResolver().insert(
                    EncounterTasks.CONTENT_URI, values);


            Bundle form = toBundle(uuid, observer, subject, procedure,
                    task.due_on, status.toString());

            // send to sync
            Intent intent = new
                    Intent(Intents.ACTION_CREATE, EncounterTasks.CONTENT_URI);
            intent.putExtra("form", form);
            context.startService(intent);
        }
    }

    public static EncounterTask calculateFirstVisit(Patient patient,
                                                    Procedure procedure,
                                                    Observer observer,
                                                    Date visitDate) {
        EncounterTask task = new EncounterTask();
        //assignedTo =task.observer;
//        assignedTo.
        //Uris.withAppendedUuid(EncounterTasks.CONTENT_URI, task.getUuid());
        Date lmd = patient.getLMD();
        String anc = patient.getANC_status();
        Date dob = patient.getDob();
        int age = patient.getAge();

        //String uuid = this.get
        //String uuid1 = mSubject.getLastPathSegment();
        //String uuid3 = mObserver.getLastPathSegment();


        /**
         * TODO
         * think about the procedure or activity to call when the due date is reached
         */

        /*If girls is greater than 12 weeks pregnant and has never attended ANC*
        *Get the day of week when she is mapped
        * Add the number of days to either Tuesday or Thursday
         */
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        Date x = c2.getTime();
        long days = x.getTime() - lmd.getTime();
        int day_of_week = c2.get(Calendar.DAY_OF_WEEK);


        //String Due_Date;

        // long age = x.getTime() - dob.getTime();
        long no_LMD = days / (60 * 24 * 60 * 1000);
        Log.i(TAG, "the number of lmd days are" + no_LMD);

        if (no_LMD > 84 && anc.equals("No") && age < 25) {
            Calendar c1 = Calendar.getInstance();
            //c1.setTime(new Date());

            switch (day_of_week) {

                case Calendar.MONDAY:
                    c1.add(Calendar.DATE, 8); // Adding 8 days
                    Date due_on = c1.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.TUESDAY:
                    c1.add(Calendar.DATE, 7); // Adding 7 days
                    due_on = c1.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.WEDNESDAY:
                    c1.add(Calendar.DATE, 6); // Adding 6 days
                    due_on = c1.getTime();
                    task.due_on = due_on;
                    break;

                case Calendar.THURSDAY:
                    c1.add(Calendar.DATE, 5); // Adding 5 days
                    due_on = c1.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.FRIDAY:
                    c1.add(Calendar.DATE, 4); // Adding 4 days
                    due_on = c1.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.SATURDAY:
                    c1.add(Calendar.DATE, 3); // Adding 3 days
                    due_on = c1.getTime();
                    task.due_on = due_on;
                    break;
                case Calendar.SUNDAY:
                    c1.add(Calendar.DATE, 2); // Adding 2 days
                    due_on = c1.getTime();
                    task.due_on = due_on;

                    break;
                default:


            }

        }
        /* If girl is less than 12 weeks pregnant and has never attended ANC
        Get a random number , add it to the day she was mapped
        Then work out the logic of her appointment being between Tuesday and Thursday
         */
        else if (no_LMD < 84 && anc.equals("No") && age < 25) {
            Calendar c = Calendar.getInstance();
            int new_lmd = (int) no_LMD; // cast lmd to int (maximum of random number to be generated)
            int rand_diff = 84 - new_lmd; // The minimum of the random number to be generated
            Random rand = new Random();

            int randomNum = rand.nextInt((rand_diff - 0)); // generate a random number btn current weeks of gestation and the maximum days left to go to 12th week
            // if (randomNum<42)
            c.add(Calendar.DATE, randomNum);// Add random number to current date
            Date rand_due_on = c.getTime(); //get date instance
            c.setTime(rand_due_on); // set it as calendar object
            int due_day_of_week = c.get(Calendar.DAY_OF_WEEK); // get day of week


            Log.i(TAG, "the new date is" + rand_due_on);

            switch (due_day_of_week) {
                case Calendar.MONDAY:
                    c.add(Calendar.DATE, 3); // Adding 4 days
                    Date due_on = c.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.TUESDAY:
                    c.add(Calendar.DATE, 2); // Adding 3 days
                    due_on = c.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.WEDNESDAY:
                    c.add(Calendar.DATE, 6); // Adding 6 days
                    due_on = c.getTime();
                    task.due_on = due_on;
                    break;

                case Calendar.THURSDAY:
                    c.add(Calendar.DATE, 5); // Adding 5 days
                    due_on = c.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.FRIDAY:
                    c.add(Calendar.DATE, 4); // Adding 4 days
                    due_on = c.getTime();
                    task.due_on = due_on;

                    break;
                case Calendar.SATURDAY:
                    c.add(Calendar.DATE, 5); // Adding 3 days
                    due_on = c.getTime();
                    task.due_on = due_on;
                    break;

                case Calendar.SUNDAY:
                    c.add(Calendar.DATE, 4); // Adding 2 days
                    due_on = c.getTime();
                    task.due_on = due_on;

                    break;
                default:
            }
        }
        /*if girl has ever attended ANC
        *Pick ANC date from card
         */
        else if (anc.equals("Yes") && age < 25) {
            Date due_on = patient.getANC_visit();

            //task.due_on = due_on;
            task.due_on = visitDate;

        }
        return task;
    }


    public static ContentValues toValues(String uuid, String observer,
                                         String subject, String procedure, Date dueOn,
                                         String status)
    {
        ContentValues values = new ContentValues();
        values.put(Tasks.Contract.OBSERVER, observer);
        values.put(Tasks.Contract.SUBJECT, subject);
        values.put(Tasks.Contract.PROCEDURE, procedure);
        values.put(Tasks.Contract.DUE_DATE, Dates.toSQL(dueOn));
        values.put(Tasks.Contract.STATUS, status);
        values.put(Tasks.Contract.UUID, uuid);
        return values;
    }

    public static Bundle toBundle(String uuid, String observer,
                                  String subject, String procedure, Date dueOn,
                                  String status)
    {
        Bundle form = new Bundle();
        form.putString(Tasks.Contract.OBSERVER, observer);
        form.putString(Tasks.Contract.SUBJECT, subject);
        form.putString(Tasks.Contract.PROCEDURE, procedure);
        form.putString(Tasks.Contract.DUE_DATE, Dates.toSQL(dueOn));
        form.putString(Tasks.Contract.STATUS, status);
        form.putString(Tasks.Contract.UUID, uuid);
        return form;
    }
}
