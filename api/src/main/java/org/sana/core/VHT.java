package org.sana.core;

import com.google.gson.annotations.Expose;

/**
 * Created by Marting on 21/11/2016.
 */

public class VHT extends Model {
    public static final String TAG = VHT.class.getSimpleName();
    // Add fields
    @Expose
    public String phoneNumber;
    @Expose
    public String firstName;
    @Expose
    public String lastName;
    @Expose
    public String location;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
