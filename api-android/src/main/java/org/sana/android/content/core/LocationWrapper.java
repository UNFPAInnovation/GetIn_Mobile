/**
 * Copyright (c) 2013, Sana
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the Sana nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL Sana BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sana.android.content.core;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import org.sana.android.content.Uris;
import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.Locations;
import org.sana.android.provider.Models;
import org.sana.api.ILocation;
import org.sana.core.Location;
import org.sana.util.UUIDUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sana Development
 *
 */
public class LocationWrapper extends ModelWrapper<ILocation> implements ILocation {

    public static final String TAG = LocationWrapper.class.getSimpleName();

    /**
     * @param cursor
     */
    public LocationWrapper(Cursor cursor) {
        super(cursor);
    }

    /* (non-Javadoc)
     * @see org.sana.android.db.impl.Proxy#nextObject()
     */
    @Override
    public ILocation getObject() {
        Location location = new Location();
        location.setUuid(getUuid());
        location.setName(getName());
        //location.setCode(getCode());
        return location;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ILocation> iterator() {
        moveToFirst();
        return new ProxyIterator<ILocation>(this);
    }

    /* (non-Javadoc)
     * @see org.sana.api.ILocation#getName()
     */
    @Override
    public String getName() {
        return getString(getColumnIndex(Locations.Contract.NAME));
    }

    public int getCode() {
        return getIntField(Locations.Contract.CODE);
    }

    /**
     * Convenience wrapper to returns a Location representing a single row matched
     * by the uuid value.
     *
     * @param resolver The resolver which will perform the query.
     * @param uuid The uuid to select by.
     * @return A cursor with a single row.
     * @throws IllegalArgumentException if multiple objects are returned.
     */
    public static ILocation getOneByUuid(ContentResolver resolver, String uuid) {
        LocationWrapper wrapper = new LocationWrapper(ModelWrapper.getOneByUuid(
                Locations.CONTENT_URI, resolver, uuid));
        ILocation object = null;
        if (wrapper != null)
            try {
                object = wrapper.next();
            } finally {
                wrapper.close();
            }
        return object;
    }

    public static List<Location> getList(Context context) {
        List<Location> locations = new ArrayList<>();
        LocationWrapper wrapper = null;
        try {
            wrapper = new LocationWrapper(context.getContentResolver().query(
                    Locations.CONTENT_URI, null, null, null, Locations.Contract.NAME + " ASC"));
            while (wrapper.moveToNext()) {
                locations.add((Location) wrapper.getObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (wrapper != null) wrapper.close();
        }
        return locations;
    }

    /**
     * Convenience wrapper to return a wrapped cursor which references all of
     * the Location entries ordered by {@link org.sana.android.provider.BaseContract#CREATED CREATED}
     * in ascending order, or, oldest first.
     *
     * @param resolver The resolver which will perform the query.
     * @return A cursor with the result or null.
     */
    public static LocationWrapper getAllByCreatedAsc(ContentResolver resolver) {
        return new LocationWrapper(ModelWrapper.getAllByCreatedAsc(
                Locations.CONTENT_URI, resolver));
    }

    /**
     * Convenience wrapper to return a wrapped cursor which references all of
     * the Location entries ordered by {@link org.sana.android.provider.BaseContract#CREATED CREATED}
     * in descending order, or, newest first.
     *
     * @param resolver The resolver which will perform the query.
     * @return A cursor with the result or null.
     */
    public static LocationWrapper getAllByCreatedDesc(ContentResolver resolver) {
        return new LocationWrapper(ModelWrapper.getAllByCreatedDesc(
                Locations.CONTENT_URI, resolver));
    }

    /**
     * Convenience wrapper to return a wrapped cursor which references all of
     * the Location entriesordered by {@link org.sana.android.provider.BaseContract#MODIFIED MODIFIED}
     * in ascending order, or, oldest first.
     *
     * @param resolver The resolver which will perform the query.
     * @return A cursor with the result or null.
     */
    public static LocationWrapper getAllByModifiedAsc(ContentResolver resolver) {
        return new LocationWrapper(ModelWrapper.getAllByModifiedAsc(
                Locations.CONTENT_URI, resolver));
    }

    /**
     * Convenience wrapper to return a wrapped cursor which references all of
     * the Location entries ordered by {@link org.sana.android.provider.BaseContract#MODIFIED MODIFIED}
     * in descending order, or, newest first.
     *
     * @param resolver The resolver which will perform the query.
     * @return A cursor with the result or null.
     */
    public static LocationWrapper getAllByModifiedDesc(ContentResolver resolver) {
        return new LocationWrapper(ModelWrapper.getAllByModifiedDesc(
                Locations.CONTENT_URI, resolver));
    }


    public static Uri getOrCreate(Context context, Location object) {
        ContentValues cv = new ContentValues();
        String uuid = object.getUuid();
        ;
        Uri uri = Locations.CONTENT_URI;
        boolean exists = false;
        if (!TextUtils.isEmpty(uuid)) {
            exists = ModelWrapper.exists(context, Uris.withAppendedUuid(uri,
                    uuid));
            if (!exists) {
                cv.put(BaseContract.UUID, uuid);
            } else {
                uri = Uris.withAppendedUuid(uri, uuid);
            }
        } else {
            uuid = UUIDUtil.generate(UUIDUtil.LOCATION_UUID, object.getName()).toString();
            cv.put(BaseContract.UUID, uuid);
        }
        cv.put(Locations.Contract.NAME, object.getName());
        //cv.put(Locations.Contract.CODE, object.getCode());
        if (exists) {
            context.getContentResolver().update(uri, cv, null, null);
        } else {
            uri = context.getContentResolver().insert(Locations.CONTENT_URI, cv);
        }
        return uri;
    }

    public static ILocation get(Context context, Uri uri) {
        Cursor cursor = ModelWrapper.getOne(context, uri);
        if (cursor == null) return null;
        LocationWrapper wrapper = new LocationWrapper(cursor);
        ILocation object = null;
        if (wrapper != null) {
            try {
                if (wrapper.moveToFirst())
                    object = wrapper.getObject();
            } finally {
                wrapper.close();
            }
        }
        return object;
    }

    public static void insertOrUpdate(Context context, Collection<Location> objects){
        if(objects == null) return;
        if(objects.size() == 0) return;
        Collection<ContentValues> collection = new ArrayList<>(objects.size());
        Iterator<Location> iterator = objects.iterator();
        while(iterator.hasNext()){
            collection.add(toValues(iterator.next()));
        }
        ModelWrapper.bulkInsertOrUpdate(context, Locations.CONTENT_URI, collection);
    }

    public static ContentValues toValues(Location object){
        ContentValues contentValues = Models.toValues(object);
        contentValues.put(Locations.Contract.NAME, object.name);
        if(object.parish != null && !TextUtils.isEmpty(object.parish.name))
            contentValues.put(Locations.Contract.PARISH, object.parish.name);
        return contentValues;
    }

    // Copied code
    // TODO Uncomment and implement in super classes if needed
    /*
    public static int update(Context context, Location object) {
        final Entity entity = new Entity(object);
        return update(context, entity);
    }


    public static class Entity implements Models.Entity<Location> {

        final Location object;

        public Entity(Location object) {
            this.object = object;
        }

        @Override
        public ContentValues getValues() {
            ContentValues values = new ContentValues();
            values.put(Locations.Contract.NAME, object.getName());
            //values.put(Locations.Contract.CODE, object.getCode());
            return values;
        }

        @Override
        public Location getObject() {
            return object;
        }
    }
    */
}
