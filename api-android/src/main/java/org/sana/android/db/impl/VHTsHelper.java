package org.sana.android.db.impl;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.VHTs;
import org.sana.core.VHT;

/**
 * Created by Marting on 21/11/2016.
 */

public class VHTsHelper extends TableHelper<VHT> {

    public static final String INSTANCE = VHTsHelper.class.getName();

    protected VHTsHelper(){
        super(VHT.class);
    }
    @Override
    public String onCreate() {
        return "CREATE TABLE " + getTable() + " ("
                + BaseContract._ID 			+ " INTEGER PRIMARY KEY,"
                + BaseContract.UUID 			+ " TEXT,"
                + BaseContract.CREATED 		+ " DATE,"
                + BaseContract.MODIFIED 		+ " DATE,"
                // Fill in the fields of the VHT class
                + VHTs.Contract.PHONE_NUMBER + " TEXT,"
                + VHTs.Contract.FIRST_NAME + " TEXT,"
                +VHTs.Contract.LAST_NAME + " TEXT,"
                + ");";
    }

    @Override
    public String onUpgrade(int oldVersion, int newVersion) {
        return null;
    }
}
