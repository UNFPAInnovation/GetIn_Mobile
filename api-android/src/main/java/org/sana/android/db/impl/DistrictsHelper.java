package org.sana.android.db.impl;

import android.content.ContentValues;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.Districts;
import org.sana.core.District;

/**
 * Created by winkler.em@gmail.com, on 06/28/2016.
 */
public class DistrictsHelper extends TableHelper<District> {
    public static final String TAG = DistrictsHelper.class.getSimpleName();

    private static final DistrictsHelper HELPER = new DistrictsHelper();

    public static DistrictsHelper getInstance() {
        return HELPER;
    }

    protected DistrictsHelper() {
        super(District.class);
    }

    @Override
    public ContentValues onInsert(ContentValues values) {
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
                + Districts.Contract._ID + " INTEGER PRIMARY KEY,"
                + Districts.Contract.UUID + " TEXT NOT NULL,"
                + Districts.Contract.CREATED + " DATE,"
                + Districts.Contract.MODIFIED + " DATE,"
                + Districts.Contract.NAME + " TEXT,"
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
        StringBuilder builder = new StringBuilder();
        if (oldVersion < newVersion) {
        }
        if(builder.length() > 0)
            return builder.toString();
        else
            return null;
    }
}
