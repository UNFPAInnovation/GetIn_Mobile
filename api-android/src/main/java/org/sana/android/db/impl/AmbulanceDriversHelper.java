package org.sana.android.db.impl;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.AmbulanceDrivers;
import org.sana.android.provider.BaseContract;
import org.sana.core.AmbulanceDriver;

/**
 * Created by winkler.em@gmail.com, on 11/18/2016.
 */
public class AmbulanceDriversHelper extends TableHelper<AmbulanceDriver> {
    public static final String TAG = AmbulanceDriversHelper.class.getSimpleName();


    protected AmbulanceDriversHelper() {
        super(AmbulanceDriver.class);
    }

    final static AmbulanceDriversHelper INSTANCE = new AmbulanceDriversHelper();

    public static final AmbulanceDriversHelper getInstance(){
        return INSTANCE;
    }
    @Override
    public String onCreate() {
        return "CREATE TABLE " + getTable() + " ("
                + BaseContract._ID 			+ " INTEGER PRIMARY KEY,"
                + BaseContract.UUID 			+ " TEXT,"
                + BaseContract.CREATED 		+ " DATE,"
                + BaseContract.MODIFIED 		+ " DATE,"
                // Fill in the fields of the AmbulanceDriver class
                + AmbulanceDrivers.Contract.PHONE_NUMBER + " TEXT"
                + ");";
    }

    @Override
    public String onUpgrade(int oldVersion, int newVersion) {
        return null;
    }
}
