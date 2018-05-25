package org.sana.android.content.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Counties;
import org.sana.android.provider.Models;
import org.sana.api.ILocation;
import org.sana.core.County;
import org.sana.core.District;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
        object.district = getDistrict();
        return object;
    }

    @Override
    public String getName() {
        return getString(getColumnIndex(Counties.Contract.NAME));
    }


    public District getDistrict() {
        String name = getString(getColumnIndex(Counties.Contract.DISTRICT));
        District district = new District();
        district.name = name;
        return district;
    }

    public static ContentValues toValues(County object){
        ContentValues contentValues = Models.toValues(object);
        contentValues.put(Counties.Contract.NAME, object.name);
        if(object.district != null && !TextUtils.isEmpty(object.district.name))
            contentValues.put(Counties.Contract.DISTRICT, object.district.name);
        return contentValues;
    }

    public static void insertOrUpdate(Context context, Collection<County> objects){
        if(objects == null) return;
        if(objects.size() == 0) return;
        Collection<ContentValues> collection = new ArrayList<>(objects.size());
        Iterator<County> iterator = objects.iterator();
        while(iterator.hasNext()){
            collection.add(toValues(iterator.next()));
        }
        ModelWrapper.bulkInsertOrUpdate(context, Counties.CONTENT_URI, collection);
    }
}
