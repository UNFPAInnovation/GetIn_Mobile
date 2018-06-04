package org.sana.android.db.impl;

import android.content.ContentValues;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.Parishes;
import org.sana.core.Parish;

/**
 */
public class ParishesHelper extends TableHelper<Parish> {
    public static final String TAG = ParishesHelper.class.getSimpleName();

    private static final ParishesHelper HELPER = new ParishesHelper();

    public static ParishesHelper getInstance() {
        return HELPER;
    }

    protected ParishesHelper() {
        super(Parish.class);
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
                + Parishes.Contract._ID + " INTEGER PRIMARY KEY,"
                + Parishes.Contract.UUID + " TEXT NOT NULL,"
                + Parishes.Contract.CREATED + " DATE,"
                + Parishes.Contract.MODIFIED + " DATE,"
                + Parishes.Contract.NAME + " TEXT,"
                + Parishes.Contract.SUBCOUNTY + " INTEGER"
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

    @Override
    public String onSort(){
        return Parishes.DEFAULT_SORT_ORDER;
    }
}
