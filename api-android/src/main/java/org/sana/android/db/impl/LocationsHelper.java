package org.sana.android.db.impl;

import android.content.ContentValues;
import android.util.Log;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.Locations;
import org.sana.core.Location;

/**
 * Created by winkler.em@gmail.com, on 06/28/2016.
 */
public class LocationsHelper extends TableHelper<Location> {
    public static final String TAG = LocationsHelper.class.getSimpleName();

    private static final LocationsHelper HELPER = new LocationsHelper();

    public static LocationsHelper getInstance() {
        return HELPER;
    }

    protected LocationsHelper() {
        super(Location.class);
    }

    @Override
    public ContentValues onInsert(ContentValues values) {
        if (!values.containsKey(Locations.Contract.CODE)) {
            values.put(Locations.Contract.CODE, 0);
        }
        return super.onInsert(values);
    }

    /**
     * Returns a table create statement.
     *
     * @return A SQL CREATE statement.
     */
    @Override
    public String onCreate() {
        return "CREATE TABLE " + getTable() + " ("
                + Locations.Contract._ID + " INTEGER PRIMARY KEY,"
                + Locations.Contract.UUID + " TEXT NOT NULL,"
                + Locations.Contract.CREATED + " DATE,"
                + Locations.Contract.MODIFIED + " DATE,"
                + Locations.Contract.NAME + " TEXT,"
                + Locations.Contract.CODE + " INTEGER DEFAULT '0',"
                + Locations.Contract.PARISH + " TEXT"
                + ");";
    }

    /**
     * Returns an upgrade statement for a table if the version has been
     * incremented.
     *
     * @param oldVersion The current version of the table in the database.
     * @param newVersion The version it will be upgraded to.
     * @return The statement string which will upgrade this table
     */
    @Override
    public String onUpgrade(int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgrade(" + oldVersion + ", " + newVersion + ")");
        StringBuilder builder = null;
        if (oldVersion < newVersion) {
            if (oldVersion == 6 && newVersion == 7){
                builder.append(onCreate());
            }
            if(newVersion == 10 || (newVersion < 10 && newVersion > 10)){
                builder.append("ALTER TABLE " + getTable() + " ADD COLUMN "
                        + Locations.Contract.PARISH + " TEXT;");
            }
        }
        if(builder.length() > 0)
            return builder.toString();
        else
            return null;
    }
}
