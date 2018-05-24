package org.sana.android.content.core;

import android.database.Cursor;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Subcounties;
import org.sana.api.ILocation;
import org.sana.core.Subcounty;

/**
 */
public class SubcountyWrapper extends ModelWrapper<Subcounty> implements ILocation {
    public static final String TAG = SubcountyWrapper.class.getSimpleName();


    public SubcountyWrapper(Cursor cursor){
        super(cursor);
    }

    @Override
    public Subcounty getObject() {
        Subcounty object = new Subcounty();
        object.setName(getName());
        return object;
    }

    @Override
    public String getName() {
        return getString(getColumnIndex(Subcounties.Contract.NAME));
    }
}
