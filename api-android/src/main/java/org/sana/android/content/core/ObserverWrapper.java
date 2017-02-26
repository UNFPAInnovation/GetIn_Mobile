/**
 * Copyright (c) 2013, Sana
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sana nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
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

import org.sana.android.content.Uris;
import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Locations;
import org.sana.android.provider.Observers;
import org.sana.android.util.Dates;
import org.sana.api.IObserver;
import org.sana.core.Location;
import org.sana.core.Observer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sana Development
 *
 */
public class ObserverWrapper extends ModelWrapper<IObserver> implements
		IObserver {
	public static final String TAG = ObserverWrapper.class.getSimpleName();

	/**
	 * @param cursor
	 */
	public ObserverWrapper(Cursor cursor) {
		super(cursor);
	}
	
	/* (non-Javadoc)
	 * @see org.sana.api.IObserver#getUsername()
	 */
	@Override
	public String getUsername() {
		return getStringField(Observers.Contract.USERNAME);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IObserver#getPassword()
	 */
	@Override
	public String getPassword() {
		return getStringField(Observers.Contract.PASSWORD);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IObserver#getRole()
	 */
	@Override
	public String getRole() {
		return getStringField(Observers.Contract.ROLE);
	}

	@Override
	public String getPhoneNumber() {
		return getStringField(Observers.Contract.PHONE_NUMBER);
	}

    @Override
    public String getFirstName() {
        return getStringField(Observers.Contract.FIRST_NAME);
    }

    @Override
    public String getLastName() {
        return getStringField(Observers.Contract.LAST_NAME);
    }

    @Override
    public List<Location> getLocations() {
        String[] uuids = getStringArrayField(Observers.Contract.LOCATIONS);
        List<Location> locations = new ArrayList<>(uuids.length);
        for(String uuid: uuids){
            Location location = new Location();
            location.setUuid(uuid);
            locations.add(location);
        }
        return locations;
    }

	/* (non-Javadoc)
	 * @see org.sana.android.db.ModelWrapper#getObject()
	 */
	@Override
	public IObserver getObject() {
		if(this.getCount() == 0)
			throw new NullPointerException("No objects returned");
		ObserverParcel object = new ObserverParcel();
		object.setUsername(getUsername());
		object.setPassword(getPassword());
		object.setRole(getRole());
		object.setPhoneNumber(getPhoneNumber());
		object.setUuid(getUuid());
		object.setCreated(getCreated());
		object.setModified(getModified());
        object.setFirstName(getFirstName());
        object.setLastName(getLastName());
        object.setLocations(getLocations());
		return object;
	}
	
	public static IObserver getOneByAuth(ContentResolver resolver, String username,
			String password){
		final ObserverWrapper wrapper = new ObserverWrapper(
				ModelWrapper.getOneByFields(Observers.CONTENT_URI, resolver,
						new String[]{Observers.Contract.USERNAME,
									Observers.Contract.PASSWORD },
						new String[]{ username,password	}
						));
		IObserver object = null;

		if(wrapper != null){
			try{
				if(wrapper.moveToFirst() && wrapper.getCount() == 1)
					object = wrapper.getObject();
			} finally {
				wrapper.close();
			}
		}
		return object;
	}
	
	/**
	 * Returns an IObserver representing a single row 
	 * @param resolver the ContentResolver to use for performing the query
	 * @param username the username to query
	 * @return an IObserver object
	 * @throws IllegalArgumentException if no row with the specified username
	 * 	was found
	 */
	public static IObserver getOneByUsername(ContentResolver resolver, 
			String username)
	{
		ObserverWrapper wrapper = new ObserverWrapper(
				ModelWrapper.getOneByField(Observers.CONTENT_URI,
						resolver,Observers.Contract.USERNAME,username));
		IObserver object = null;
		if(wrapper != null){
			try{
				if(wrapper.moveToFirst())
					object = wrapper.getObject();
			} finally {
				wrapper.close();
			}
		}
		return object;
	}
	
	public static IObserver getOneByUuid(ContentResolver resolver, String uuid){
		ObserverWrapper wrapper = new ObserverWrapper(ModelWrapper.getOneByUuid(
				Observers.CONTENT_URI, resolver, uuid));
		IObserver object = null;
		if(wrapper != null){
			try{
				if(wrapper.moveToFirst())
					object = wrapper.getObject();
			} finally {
				wrapper.close();
			}
		}
		return object;
	}

    public static ContentValues toValues(Observer object){
        ContentValues values = new ContentValues();
        values.put(Observers.Contract.UUID, object.getUuid());
        values.put(Observers.Contract.CREATED, Dates.toSQL(object.getCreated()));
        values.put(Observers.Contract.MODIFIED, Dates.toSQL(object.getModified()));
        values.put(Observers.Contract.USERNAME, object.getUsername());
        values.put(Observers.Contract.PASSWORD, object.getPassword());
        values.put(Observers.Contract.PHONE_NUMBER, object.getPhoneNumber());
        values.put(Observers.Contract.ROLE, object.getRole());
        values.put(Observers.Contract.FIRST_NAME, object.getFirstName());
        values.put(Observers.Contract.LAST_NAME, object.getLastName());
        values.put(Observers.Contract.ROLE, object.getRole());
        // TODO
        List<String> locationUUIDs = new ArrayList<>();
        for(Location location: object.getLocations()){
            locationUUIDs.add(location.getUuid());
        }
        values.put(Observers.Contract.LOCATIONS, TextUtils.join(",",locationUUIDs));
        return values;
    }

    public static ContentValues toValues(Observer object, String[] excludes){
        ContentValues values = toValues(object);
        for(String exclude: excludes){
            values.remove(exclude);
        }
        return values;
    }

    public static void initializeRelated(Context context, Observer observer){
        List<Location> locations = new ArrayList<>();
        for(Location uninitialized: observer.getLocations()){
            Uri uri = Uris.withAppendedUuid(Locations.CONTENT_URI,
                    uninitialized.getUuid());
            Location location = (Location) LocationWrapper.get(context,uri);
            locations.add(location);
        }
        observer.setLocations(locations);
    }
}
