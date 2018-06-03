package org.sana.android.db.impl;

import android.content.ContentValues;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.Counties;
import org.sana.core.County;

/**
 */
public class CountiesHelper extends TableHelper<County> {
    public static final String TAG = CountiesHelper.class.getSimpleName();

    private static final CountiesHelper HELPER = new CountiesHelper();

    public static CountiesHelper getInstance() {
        return HELPER;
    }

    protected CountiesHelper() {
        super(County.class);
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
                + Counties.Contract._ID + " INTEGER PRIMARY KEY,"
                + Counties.Contract.UUID + " TEXT NOT NULL,"
                + Counties.Contract.CREATED + " DATE,"
                + Counties.Contract.MODIFIED + " DATE,"
                + Counties.Contract.NAME + " TEXT,"
                + Counties.Contract.DISTRICT + " INTEGER"
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
            if(newVersion == 10 || (oldVersion < 10 && newVersion > 10)){
                builder.append(onCreate());
            }
        }
        if(builder.length() > 0)
            return builder.toString();
        else
            return null;
    }
}
