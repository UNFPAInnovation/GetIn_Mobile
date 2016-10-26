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

import android.os.Parcel;
import android.os.Parcelable;

import org.sana.core.Patient;
import org.sana.util.DateUtil;

import java.net.URI;
import java.text.ParseException;

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
		setFamily_name(in.readString());
		setGender(in.readString());
		setpNumber(in.readString());

		setHolder_pNumber(in.readString());
		try {
			setCreated(DateUtil.parseDate(in.readString()));
			setModified(DateUtil.parseDate(in.readString()));
			setDob(DateUtil.parseDate(in.readString()));

		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		setMarital_status(in.readString());
		setEducation_level(in.readString());
		setContraceptive_use(in.readString());
		setANC_status(in.readString());

		try {

			setANC_visit(DateUtil.parseDate(in.readString()));

		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		setEDD(in.readString());
		setreceive_sms(in.readString());
		setfollow_up(in.readString());
		setCUG_status(in.readString());
		setcomment(in.readString());
		setBleeding(in.readString());
		setFever(in.readString());
		setSwollen_feet(in.readString());
		setBlurred_vision(in.readString());

		setImage(URI.create(in.readString()));
		setSystemId(in.readString());
		setConfirmed((in.readInt() == 1));
		setDobEstimated((in.readInt() == 1));
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
		setFamily_name(patient.getFamily_name());
		setGender(patient.getGender());
		setpNumber(patient.getpNumber());

		setHolder_pNumber(patient.getHolder_pNumber());
		setLMD(patient.getLMD());
		setMarital_status(patient.getMarital_status());
		setEducation_level(patient.getEducation_level());
		setContraceptive_use(patient.getContraceptive_use());
		setANC_status(patient.getANC_status());
		setANC_visit(patient.getANC_visit());
		setEDD(patient.getEDD());
		setreceive_sms(patient.getReceive_sms());
		setfollow_up(patient.getFollow_up());
		setCUG_status(patient.getCUG_status());
		setcomment(patient.getComment());
		setBleeding(patient.getBleeding());
		setFever(patient.getFever());
		setSwollen_feet(patient.getSwollen_feet());
		setBlurred_vision(patient.getBlurred_vision());

		setImage(patient.getImage());
		setSystemId(patient.getSystemId());
		setConfirmed(patient.getConfirmed());
		setDobEstimated(patient.isDobEstimated());
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
		dest.writeString(getFamily_name());
		dest.writeString(getGender());
		dest.writeString(getpNumber());
		dest.writeString(getHolder_pNumber());
		dest.writeString(DateUtil.format(getLMD()));
		dest.writeString(getMarital_status());
		dest.writeString(getEducation_level());
		dest.writeString(getContraceptive_use());
		dest.writeString(getANC_status());
		dest.writeString(DateUtil.format(getANC_visit()));
		dest.writeString(getEDD());
		dest.writeString(getReceive_sms());
		dest.writeString(getFollow_up());
		dest.writeString(getCUG_status());
		dest.writeString(getComment());
		dest.writeString(getBleeding());
		dest.writeString(getFever());
		dest.writeString(getSwollen_feet());
		dest.writeString(getBlurred_vision());

		dest.writeString(getImage().toString());
		dest.writeString(getSystemId());
		dest.writeInt((getConfirmed())? 1: 0);
		dest.writeInt((isDobEstimated())? 1: 0);
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
