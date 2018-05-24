package org.sana.android.content.core;

import android.database.Cursor;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Counties;
import org.sana.api.ILocation;
import org.sana.core.County;

/**
 */
public class CountyWrapper extends ModelWrapper<County> implements ILocation {
    public static final String TAG = CountyWrapper.class.getSimpleName();


    public CountyWrapper(Cursor cursor){
        super(cursor);
    }

    @Override
    public County getObject() {
        County object = new County();
        object.setName(getName());
        return object;
    }

    @Override
    public String getName() {
        return getString(getColumnIndex(Counties.Contract.NAME));
    }
}
