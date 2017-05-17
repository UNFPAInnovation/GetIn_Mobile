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

import org.sana.core.Location;
import org.sana.core.Observer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Sana Development
 *
 */
public class ObserverParcel extends Observer implements Parcelable {
	public static final String TAG = ObserverParcel.class.getSimpleName();

    public ObserverParcel(){}

    public ObserverParcel(Observer observer){
        super(observer);
    }

    public ObserverParcel(Parcel in){
        setUuid(in.readString());
        setCreated(new Date(in.readLong()));
        setModified(new Date(in.readLong()));
        setUsername(in.readString());
        setPassword(in.readString());
        setRole(in.readString());
        setIsAdmin(Boolean.parseBoolean(in.readString()));
        setFirstName(in.readString());
        setLastName(in.readString());
        setPhoneNumber(in.readString());

        List<LocationParcel> locationParcels = new ArrayList<>();
        in.readTypedList(locationParcels, LocationParcel.CREATOR);
        List<Location> locations = new ArrayList<Location>();
        for (LocationParcel lp : locationParcels) {
            locations.add(lp.getLocation());
        }
        setLocations(locations);
        LocationParcel lp = in.readParcelable(LocationParcel.class.getClassLoader());
        setSubcounty(lp.getLocation());
    }
	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUuid());
        dest.writeLong(getCreated().getTime());
        dest.writeLong(getModified().getTime());
        dest.writeString(getUsername());
        dest.writeString(getPassword());
        dest.writeString(getRole());
        dest.writeString(String.valueOf(isAdmin()));
        dest.writeString(getFirstName());
        dest.writeString(getLastName());
        dest.writeString(getPhoneNumber());
        List<LocationParcel> locationParcels = new ArrayList<>();
        for(Location l:getLocations()){
            locationParcels.add(new LocationParcel(l));
        }
        dest.writeTypedList(locationParcels);
        dest.writeParcelable(new LocationParcel(getSubcounty()), flags);
	}

	public static final Parcelable.Creator<ObserverParcel> CREATOR = 
			new Parcelable.Creator<ObserverParcel>() {

				@Override
				public ObserverParcel createFromParcel(Parcel source) {
					return new ObserverParcel(source);
				}

				@Override
				public ObserverParcel[] newArray(int size) {
					return new ObserverParcel[size];
				}
			};
}
