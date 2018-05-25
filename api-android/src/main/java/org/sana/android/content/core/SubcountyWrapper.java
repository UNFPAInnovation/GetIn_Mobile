package org.sana.android.content.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Models;
import org.sana.android.provider.Subcounties;
import org.sana.api.ILocation;
import org.sana.core.Subcounty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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

    public String getName() {
        return getString(getColumnIndex(Subcounties.Contract.NAME));
    }

    public static ContentValues toValues(Subcounty object){
        ContentValues contentValues = Models.toValues(object);
        contentValues.put(Subcounties.Contract.NAME, object.name);
        if(object.district != null && !TextUtils.isEmpty(object.district.name))
            contentValues.put(Subcounties.Contract.DISTRICT, object.district.name);
        return contentValues;
    }

    public static void insertOrUpdate(Context context, Collection<Subcounty> objects){
        if(objects == null) return;
        if(objects.size() == 0) return;
        Collection<ContentValues> collection = new ArrayList<>(objects.size());
        Iterator<Subcounty> iterator = objects.iterator();
        while(iterator.hasNext()){
            collection.add(toValues(iterator.next()));
        }
        ModelWrapper.bulkInsertOrUpdate(context, Subcounties.CONTENT_URI, collection);
    }
}
