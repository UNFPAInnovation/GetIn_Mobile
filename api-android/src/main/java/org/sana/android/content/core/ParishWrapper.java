package org.sana.android.content.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Models;
import org.sana.android.provider.Parishes;
import org.sana.api.ILocation;
import org.sana.core.Parish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

    public static ContentValues toValues(Parish object){
        ContentValues contentValues = Models.toValues(object);
        contentValues.put(Parishes.Contract.NAME, object.name);
        if(object.subcounty != null && !TextUtils.isEmpty(object.subcounty.name))
            contentValues.put(Parishes.Contract.SUBCOUNTY, object.subcounty.name);
        return contentValues;
    }

    public static void insertOrUpdate(Context context, Collection<Parish> objects){
        if(objects == null) return;
        if(objects.size() == 0) return;
        Collection<ContentValues> collection = new ArrayList<>(objects.size());
        Iterator<Parish> iterator = objects.iterator();
        while(iterator.hasNext()){
            collection.add(toValues(iterator.next()));
        }
        ModelWrapper.bulkInsertOrUpdate(context, Parishes.CONTENT_URI, collection);
    }
}
