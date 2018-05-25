package org.sana.android.content.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Districts;
import org.sana.android.provider.Models;
import org.sana.api.ILocation;
import org.sana.core.District;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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


    public static ContentValues toValues(District object){
        ContentValues contentValues = Models.toValues(object);
        contentValues.put(Districts.Contract.NAME, object.name);
        return contentValues;
    }

    public static void insertOrUpdate(Context context, Collection<District> objects){
        if(objects == null) return;
        if(objects.size() == 0) return;
        Collection<ContentValues> collection = new ArrayList<>(objects.size());
        Iterator<District> iterator = objects.iterator();
        while(iterator.hasNext()){
            collection.add(toValues(iterator.next()));
        }
        ModelWrapper.bulkInsertOrUpdate(context, Districts.CONTENT_URI, collection);
    }
}
