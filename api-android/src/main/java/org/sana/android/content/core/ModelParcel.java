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

import java.text.ParseException;
import java.util.Arrays;

import org.sana.android.provider.BaseContract;
import org.sana.core.Model;
import org.sana.util.DateUtil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable implementation of Model class.
 * 
 * @author Sana Development
 *
 */
public class ModelParcel extends Model implements Parcelable {
    public static final String TAG = ModelParcel.class.getSimpleName();
    
    public ModelParcel(Parcel in){
        setUuid(in.readString());
        try {
            setCreated(DateUtil.parseDate(in.readString()));
            setModified(DateUtil.parseDate(in.readString()));
        } catch (ParseException e) {            
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
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
        dest.writeString(DateUtil.format(getCreated()));
        dest.writeString(DateUtil.format(getModified()));
    }

    public static final Parcelable.Creator<ModelParcel> CREATOR = 
        new Parcelable.Creator<ModelParcel>() 
    {

        @Override
        public ModelParcel createFromParcel(Parcel source) {
            return new ModelParcel(source);
        }

        @Override
        public ModelParcel[] newArray(int size) {
            ModelParcel[] array = new ModelParcel[size];
            Arrays.fill(array,null);
            return array;
        }
    };

    public static void writeToParcel(Model obj, Parcel dest) {
        dest.writeString(obj.getUuid());
        dest.writeString(DateUtil.format(obj.getCreated()));
        dest.writeString(DateUtil.format(obj.getModified()));
    }

    public static void readFromParcel(Model obj, Parcel in){
        obj.setUuid(in.readString());
        String field = BaseContract.CREATED;
        try {
            obj.setCreated(DateUtil.parseDate(in.readString()));
            field = BaseContract.MODIFIED;
            obj.setModified(DateUtil.parseDate(in.readString()));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid timestamp: '" + field + "'");
        }
    }
}
