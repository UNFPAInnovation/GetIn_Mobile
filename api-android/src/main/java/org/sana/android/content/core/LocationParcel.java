package org.sana.android.content.core;

import android.os.Parcel;
import android.os.Parcelable;

import org.sana.android.util.Dates;
import org.sana.core.Location;
import org.sana.util.DateUtil;

/**
 * Created by winkler.em@gmail.com, on 12/13/2016.
 */
public class LocationParcel extends Location implements Parcelable {
    public static final String TAG = LocationParcel.class.getSimpleName();

    protected LocationParcel(Parcel in) {
    }

    public LocationParcel(){
        super();
    }

    public LocationParcel(Location location){
        setUuid(location.getUuid());
        setCreated(location.getCreated());
        setModified(location.getModified());
        setName(location.getName());
        code = location.code;
    }

    public static final Creator<LocationParcel> CREATOR = new Creator<LocationParcel>() {
        @Override
        public LocationParcel createFromParcel(Parcel in) {
            return new LocationParcel(in);
        }

        @Override
        public LocationParcel[] newArray(int size) {
            return new LocationParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUuid());
        dest.writeString(DateUtil.format(getCreated()));
        dest.writeString(DateUtil.format(getModified()));
        dest.writeString(getName());
        dest.writeInt(this.code);
    }
}
