package org.sana.android.db.impl;

import android.content.ContentValues;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.Subcounties;
import org.sana.core.Subcounty;

/**
 */
public class SubcountiesHelper extends TableHelper<Subcounty> {
    public static final String TAG = SubcountiesHelper.class.getSimpleName();

    private static final SubcountiesHelper HELPER = new SubcountiesHelper();

    public static SubcountiesHelper getInstance() {
        return HELPER;
    }

    protected SubcountiesHelper() {
        super(Subcounty.class);
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
                + Subcounties.Contract._ID + " INTEGER PRIMARY KEY,"
                + Subcounties.Contract.UUID + " TEXT NOT NULL,"
                + Subcounties.Contract.CREATED + " DATE,"
                + Subcounties.Contract.MODIFIED + " DATE,"
                + Subcounties.Contract.NAME + " TEXT,"
                + Subcounties.Contract.DISTRICT + " INTEGER"
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
