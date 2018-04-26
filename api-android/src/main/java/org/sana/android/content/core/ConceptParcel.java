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

import org.sana.api.IConcept;
import org.sana.core.Concept;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable implementation of {@link org.sana.core.Concept}.
 * 
 * @author Sana Development
 *
 */
public class ConceptParcel extends Concept implements Parcelable{
	public static final String TAG = ConceptParcel.class.getSimpleName();

	/**
	 * Creates an uninitialized instance.
	 */
	public ConceptParcel(){}
	
	public ConceptParcel(Parcel in){
        ModelParcel.readFromParcel(this, in);
        setName(in.readString());
        setConstraints(in.readString());
        setDescription(in.readString());
        setDatatype(in.readString());
        setDisplayName(in.readString());
        setMediatype(in.readString());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
        ModelParcel.writeToParcel(this, dest);
		dest.writeString(getName());
        dest.writeString(getConstraints());
        dest.writeString(getDescription());
        dest.writeString(getDatatype());
        dest.writeString(getDisplayName());
        dest.writeString(getMediatype());
	}
	
	public static final Parcelable.Creator<ConceptParcel> CREATOR = 
			new Parcelable.Creator<ConceptParcel>() {

				@Override
				public ConceptParcel createFromParcel(Parcel source) {
					return new ConceptParcel(source);
				}

				@Override
				public ConceptParcel[] newArray(int size) {
					ConceptParcel[] array = new ConceptParcel[size];
                    for(int i=0; i < size;i++){
                        array[i] = new ConceptParcel();
                    }
                    return array;
				}
			};

    /**
     * Initialize a new Parcelable Concept from an object adhering to
     * the IConcept interface.
     *
     * @param obj The object to copy
     * @return A new instance
     */
    public static ConceptParcel get(IConcept obj){
        ConceptParcel parcel = new ConceptParcel();
        parcel.setUuid(obj.getUuid());
        parcel.setCreated(obj.getCreated());
        parcel.setModified(obj.getModified());
        parcel.setName(obj.getName());
        parcel.setConstraints(obj.getConstraints());
        parcel.setDescription(obj.getDescription());
        parcel.setDatatype(obj.getDatatype());
        parcel.setDisplayName(obj.getDisplayName());
        parcel.setMediatype(obj.getMediatype());
        return parcel;
    }
}
