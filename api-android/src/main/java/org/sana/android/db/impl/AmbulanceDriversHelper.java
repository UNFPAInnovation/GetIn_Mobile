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
                + AmbulanceDrivers.Contract.PHONE_NUMBER + " TEXT,"
                + AmbulanceDrivers.Contract.FIRST_NAME + " TEXT,"
                + AmbulanceDrivers.Contract.LAST_NAME + " TEXT,"
                + AmbulanceDrivers.Contract.LOCATION + " TEXT,"
                + AmbulanceDrivers.Contract.SUBCOUNTY + " TEXT"
                + ");";
    }

    @Override
    public String onUpgrade(int oldVersion, int newVersion) {
        StringBuilder builder = new StringBuilder();
        if(oldVersion < newVersion && newVersion == 8){
            builder.append("ALTER TABLE " + getTable() + " ADD COLUMN "
                    + AmbulanceDrivers.Contract.SUBCOUNTY + " TEXT;");
        }
        if(builder.length() > 0)
            return builder.toString();
        else
            return null;
    }
}
