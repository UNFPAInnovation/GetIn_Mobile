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

import java.net.URI;
import java.text.ParseException;

import org.sana.core.Patient;
import org.sana.util.DateUtil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable implementation of Patient.
 * 
 * @author Sana Development
 *
 */
public class PatientParcel extends Patient implements Parcelable {

	public static final String TAG = PatientParcel.class.getSimpleName();

	/**
	 * @param in
	 */
	public PatientParcel(Parcel in) {
		setUuid(in.readString());
		try {
			setCreated(DateUtil.parseDate(in.readString()));
			setModified(DateUtil.parseDate(in.readString()));
			setDob(DateUtil.parseDate(in.readString()));
		} catch (ParseException e) {			
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		setGiven_name(in.readString());
		setSurname(in.readString());
		setDob(in.readInt());
		setPhone_number(in.readString());
		setPowerholder_phone_(in.readString());
		setLocation(in.readString());
		setLast_menstruation_date(in.readString());
		setMarital_status(in.readString());
		setEducation_level(in.readString());
		setContraceptive_use(in.readString());
		setANC_status(in.readString());
		setNext_ANC_visit(in.readString());
		setExpected_date_of_delivery(in.readString());
		setSMS_intrest(in.readString());
		//setFollow_up(in.readString());
		setCUG_status(in.readString());
		setGeneral_comment(in.readString());
		
		//setGender(in.readString());
		//setImage(URI.create(in.readString()));
        //setSystemId(in.readString());
		//setConfirmed((in.readInt() == 1));
	//	setDobEstimated((in.readInt() == 1));
	
	}

    /**
     * Copy constructor for creating a new instance of the Parcelable
     * representation of a Patient.
     *
     * @param patient
     */
    public PatientParcel(Patient patient) {
        setUuid(patient.getUuid());
        setCreated(patient.getCreated());
        setModified(patient.getModified());
        setDob(patient.getDob());
        setGiven_name(patient.getGiven_name());
        setSurname(patient.getSurname());
        //setGender(patient.getGender());
        //setImage(patient.getImage());
        //setSystemId(patient.getSystemId());
		//setConfirmed(patient.getConfirmed());
		//setDobEstimated(patient.isDobEstimated());
		setPhone_number(patient.getPhone_number());
		setPowerholder_phone_number(patient.getPowerholder_phone_number());
		setLocation(patient.getLocation());
		setLast_menstruation_date(patient.getLast_menstruation_date());
		setMarital_status(patient.getMarital_status());
		setEducation_level(patient.getEducation_level());
		setContraceptive_use(patient.getContraceptive_use());
		setANC_status(patient.getANC_status());
		setNext_ANC_visit(patient.getANC_visit());
		setExpected_date_of_delivery(patient.getExpected_date_of_delivery());
		setSMS-intrest(patient.getSMS_intrest());
		//setFollow_up(patient.getFollow_up());
		setCUG_status(patient.getCUG_status());
		setGeneral_comment(patient.getGeneral_comment());
    }

    public PatientParcel(){}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return PatientParcel.class.hashCode();
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getUuid());
		dest.writeString(DateUtil.format(getCreated()));
		dest.writeString(DateUtil.format(getModified()));
		dest.writeString(DateUtil.format(getDob()));
		dest.writeString(getGiven_name());
		dest.writeString(getSurname());
		dest.writeString(getPhone_number());
		dest.writeString(getPowerholder_phone_number());
		dest.writeString(getLocation());
		dest.writeString(getLast_menstruation_date());
		dest.writeString(getMarital_status());
		dest.writeString(getEducation_level());
		dest.writeString(getContraceptive_use);
		dest.writeString(getNext_ANC_status());
		dest.writeString(getNext_ANC_visit());
		dest.writeString(getExpected_date_of_delivery());
		dest.writeString(getSMS_intrest());
		//dest.writeString(getFollow_up());
		dest.writeString(getCUG_status());
		dest.writeString(getGeneral_comment());
		
		
		/*dest.writeString(getGender());
		dest.writeString(getImage().toString());
        dest.writeString(getSystemId());
		dest.writeInt((getConfirmed())? 1: 0);
		dest.writeInt((isDobEstimated())? 1: 0);*/
		
	}
	
	public static final Parcelable.Creator<PatientParcel> CREATOR = 
			new Parcelable.Creator<PatientParcel>() {

				@Override
				public PatientParcel createFromParcel(Parcel source) {
					return new PatientParcel(source);
				}

				@Override
				public PatientParcel[] newArray(int size) {
					return new PatientParcel[size];
				}
			};
}
