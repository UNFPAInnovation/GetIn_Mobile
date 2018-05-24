package org.sana.android.content.core;

import android.database.Cursor;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Districts;
import org.sana.api.ILocation;
import org.sana.core.District;

/**
 */
public class DistrictWrapper extends ModelWrapper<District> implements ILocation {
    public static final String TAG = DistrictWrapper.class.getSimpleName();


    public DistrictWrapper(Cursor cursor){
        super(cursor);
    }

    @Override
    public District getObject() {
        District object = new District();
        object.setName(getName());
        return object;
    }

    @Override
    public String getName() {
        return getString(getColumnIndex(Districts.Contract.NAME));
    }
}
