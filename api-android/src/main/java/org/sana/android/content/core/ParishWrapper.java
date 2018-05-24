package org.sana.android.content.core;

import android.database.Cursor;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Parishes;
import org.sana.api.ILocation;
import org.sana.core.Parish;

/**
 */
public class ParishWrapper extends ModelWrapper<Parish> implements ILocation {
    public static final String TAG = ParishWrapper.class.getSimpleName();


    public ParishWrapper(Cursor cursor){
        super(cursor);
    }

    @Override
    public Parish getObject() {
        Parish object = new Parish();
        object.setName(getName());
        return object;
    }

    @Override
    public String getName() {
        return getString(getColumnIndex(Parishes.Contract.NAME));
    }
}
