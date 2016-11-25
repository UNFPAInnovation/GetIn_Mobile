package org.sana.core;

import com.google.gson.annotations.Expose;

/**
 * Created by winkler.em@gmail.com, on 11/18/2016.
 */
public class AmbulanceDriver extends Model{
    public static final String TAG = AmbulanceDriver.class.getSimpleName();
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
