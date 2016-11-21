package org.sana.core;

/**
 * Created by winkler.em@gmail.com, on 11/18/2016.
 */
public class AmbulanceDriver extends Model{
    public static final String TAG = AmbulanceDriver.class.getSimpleName();
    // Add fields
    public String phoneNumber;
    public String firstName;
    public String surname;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
