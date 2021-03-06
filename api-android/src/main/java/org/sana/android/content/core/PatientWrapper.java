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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.sana.android.content.Uris;
import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.Patients;
import org.sana.android.provider.Subjects;
import org.sana.android.util.Dates;
import org.sana.api.IPatient;
import org.sana.core.Location;
import org.sana.core.Patient;
import org.sana.util.UUIDUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author Sana Development
 *
 */
public class PatientWrapper extends ModelWrapper<IPatient> implements IPatient {
	public static final String TAG = PatientWrapper.class.getSimpleName();

	/**
	 * @param cursor
	 */
	public PatientWrapper(Cursor cursor) {
		super(cursor);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IPatient#getGiven_name()
	 */
	@Override
	public String getGiven_name() {
		return getStringField(Patients.Contract.GIVEN_NAME);
	}
    /* (non-Javadoc)
	 * @see org.sana.api.IPatient#getpNumber()
	 */
    @Override
    public String getpNumber() {
        return getStringField(Patients.Contract.PNUMBER);
    }

    /* (non-Javadoc)
	 * @see org.sana.api.IPatient#Holder_pNumber()
	 */
    @Override
    public String getHolder_pNumber() {
        return getStringField(Patients.Contract.HOLDER_pNUMBER);
    }

    @Override
    public Date getLMD() {
        return getDateField(Patients.Contract.LMD);
    }

    @Override
    public String getMarital_status() {
        return getStringField(Patients.Contract.MARITAL_STATUS);
    }

    @Override
    public String getEducation_level() {
        return getStringField(Patients.Contract.EDUCATION_LEVEL);
    }

    @Override
    public String getContraceptive_use() {
        return getStringField(Patients.Contract.CONTRACEPTIVE_USE);
    }

    @Override
    public String getANC_status() {
        return getStringField(Patients.Contract.ANC_STATUS);
    }

    @Override
    public Date getANC_visit() {
        return getDateField(Patients.Contract.ANC_VISIT);
    }

    @Override
    public String getAMBULANCE_need() {return getStringField(Patients.Contract.AMBULANCE_NEED);}

    @Override
    public String getAMBULANCE_response() {
        return getStringField(Patients.Contract.AMBULANCE_RESPONSE);
    }
    @Override
    public String getEDD() {
        return getStringField(Patients.Contract.EDD);
    }

    @Override
    public String getReceive_sms() {
        return getStringField(Patients.Contract.RECEIVE_SMS);
    }

    @Override
    public String getFollow_up() {
        return getStringField(Patients.Contract.FOLLOW_UP);
    }

    @Override
    public String getCUG_status() {
        return getStringField(Patients.Contract.CUG_STATUS);
    }

    @Override
    public String getComment() {
        return getStringField(Patients.Contract.COMMENT);
    }

    @Override
    public String getBleeding() {
        return getStringField(Patients.Contract.BLEEDING);
    }

    @Override
    public String getFever() {
        return getStringField(Patients.Contract.FEVER);
    }

    @Override
    public String getSwollen_feet() {
        return getStringField(Patients.Contract.SWOLLEN_FEET);
    }

    @Override
    public String getBlurred_vision() {
        return getStringField(Patients.Contract.BLURRED_VISION);
    }

    /* (non-Javadoc)
     * @see org.sana.api.IPatient#getFamily_name()
     */
	@Override
	public String getFamily_name() {
		return getStringField(Patients.Contract.FAMILY_NAME);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IPatient#getDob()
	 */
	@Override
	public Date getDob() {
		return getDateField(Patients.Contract.DOB);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IPatient#getGender()
	 */
	@Override
	public String getGender() {
		return getStringField(Patients.Contract.GENDER);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IPatient#getImage()
	 */
	@Override
	public URI getImage() {
		try{
			return URI.create(getStringField(Patients.Contract.IMAGE));
		} catch(Exception e){
		}
		return null;
	}

    public boolean getConfirmed(){
        return getBooleanField(Patients.Contract.CONFIRMED);
    }

    public boolean getDobEstimated(){
        return getBooleanField(Patients.Contract.DOB_ESTIMATED);
    }

    @Override
    public String getLocation() {
        return getStringField(Patients.Contract.LOCATION);
    }
    @Override
    public String getDistrict() {
        return getStringField(Patients.Contract.DISTRICT);
    }
    @Override
    public String getCounty() {
        return getStringField(Patients.Contract.COUNTY);
    }

    @Override
    public String getSubCounty() {
        return getStringField(Patients.Contract.SUBCOUNTY);
    }
    @Override
    public String getParish() {
        return getStringField(Patients.Contract.PARISH);
    }
    @Override
    public String getVillage() {
        return getStringField(Patients.Contract.VILLAGE);
    }

	/* (non-Javadoc)
	 * @see org.sana.android.db.ModelWrapper#getObject()
	 */
	@Override
	public IPatient getObject() {
        Patient obj = new Patient();
        obj.setUuid(getUuid());
        obj.setCreated(getCreated());
        obj.setModified(getModified());
        obj.setDob(getDob());
        obj.setFamily_name(getFamily_name());
        obj.setGiven_name(getGiven_name());
        obj.setGender(getGender());
        obj.setpNumber(getpNumber());
        obj.setHolder_pNumber(getHolder_pNumber());
        obj.setLMD(getLMD());
        obj.setMarital_status(getMarital_status());
        obj.setEducation_level(getEducation_level());
        obj.setContraceptive_use(getContraceptive_use());
        obj.setANC_status(getANC_status());

        obj.setANC_visit(getANC_visit());
        obj.setAMBULANCE_need(getAMBULANCE_need());
        obj.setAMBULANCE_response(getAMBULANCE_response());
        obj.setEDD(getEDD());
        obj.setreceive_sms(getReceive_sms());
        obj.setfollow_up(getFollow_up());
        obj.setCUG_status(getCUG_status());
        obj.setcomment(getComment());
        obj.setBleeding(getBleeding());
        obj.setFever(getFever());
        obj.setSwollen_feet(getSwollen_feet());
        obj.setBlurred_vision(getBlurred_vision());
        obj.setImage(getImage());
        obj.setLocation(getLocation());
        obj.setDistrict(getDistrict());
        obj.setCounty(getCounty());
        obj.setSubCounty(getSubCounty());
        obj.setParish(getParish());
        obj.setVillage(getVillage());
        obj.setSystemId(getSystemId());
        //obj.setDobEstimated(getDobEstimated());
        //obj.setConfirmed(getConfirmed());
		return obj;
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IPatient#getLocation()
	 */


    /**
     * Gets the value of the system identifier stored in the
     * {@link org.sana.android.provider.Patients.Contract#PATIENT_ID PATIENT_ID} column.
     *
     * @return The value or null.
     */
    public String getSystemId(){
        return getStringField(Patients.Contract.PATIENT_ID);
    }

    /**
     * Convenience method to look up a single Patient by the <code>system_id</code>.
     *
     * @param resolver The resolver which will perform the query.
     * @param systemId The system id to query
     * @return
     */
    public static Patient getOneBySystemId(ContentResolver resolver, String systemId){
        PatientWrapper wrapper = new PatientWrapper(ModelWrapper.getOneByFields(
                Patients.CONTENT_URI,
                resolver,
                new String[]{Patients.Contract.PATIENT_ID},
                new String[]{systemId}
        ));
        Patient obj = null;
        if(wrapper != null)
            try{
                if(wrapper.getCount() == 1) {
                    wrapper.moveToFirst();
                    obj = new Patient();
                    obj = (Patient) wrapper.getObject();
                } else {

                }
            } finally {
                wrapper.close();
            }
        return obj;
    }

    public static Patient get(Context context, Uri uri){
        Patient patient = null;
        switch(Uris.getTypeDescriptor(uri)) {
            case Uris.ITEM_UUID:
            case Uris.ITEM_ID:
                PatientWrapper wrapper = null;
                try {
                    wrapper = new PatientWrapper(
                            context.getContentResolver().query(uri, null, null,
                                    null, null));
                    if (wrapper != null && wrapper.getCount() == 1) {
                        if (wrapper.moveToFirst()) {
                            patient = (Patient) wrapper.getObject();
                        }
                    }
                } finally {
                    if (wrapper != null) {
                        wrapper.close();
                    }
                }
                break;
            case Uris.ITEMS:
            default:
                break;
        }
        return patient;
    }

    public static Uri getOrCreate(Context context, Uri uri, ContentValues values){
        Uri result = Uri.EMPTY;
        switch(Uris.getTypeDescriptor(uri)) {
            case Uris.ITEM_UUID:
            case Uris.ITEM_ID:
                if (exists(context, uri)) {
                    context.getContentResolver().update(uri, values, null, null);
                } else {
                    throw new IllegalArgumentException("Item Uri. Does not exist.");
                }
                break;
            case Uris.ITEMS:
                result = context.getContentResolver().insert(uri, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri.");
        }
        return result;
    }

    public static Uri getOrCreate(Context context, ContentValues values){
        return getOrCreate(context, Subjects.CONTENT_URI, values);
    }

    public static Uri getOrCreate(Context context, Patient mPatient){
        ContentValues cv = new ContentValues();
        String uuid = mPatient.getUuid();
        Uri uri = Patients.CONTENT_URI;
        boolean exists = false;
        if(!TextUtils.isEmpty(uuid)){
            exists = ModelWrapper.exists(context, Uris.withAppendedUuid(uri,
                    uuid));
            if(!exists){
                cv.put(Patients.Contract.UUID, uuid);
            } else {
                uri = Uris.withAppendedUuid(uri,uuid);
            }
        } else {
            uuid = UUIDUtil.generatePatientUUID(mPatient.getSystemId()).toString();
            cv.put(Patients.Contract.UUID, uuid);
        }
        cv.put(Patients.Contract.PATIENT_ID, mPatient.getSystemId());
        cv.put(Patients.Contract.GIVEN_NAME, mPatient.getGiven_name());
        cv.put(Patients.Contract.FAMILY_NAME, mPatient.getFamily_name());
        // Format the date for insert
        cv.put(Patients.Contract.DOB, Dates.toSQL(mPatient.getDob()));
        cv.put(Patients.Contract.GENDER, mPatient.getGender());
        cv.put(Patients.Contract.PNUMBER, mPatient.getpNumber());
        cv.put(Patients.Contract.HOLDER_pNUMBER, mPatient.getHolder_pNumber());
        cv.put(Patients.Contract.LMD, Dates.toSQL(mPatient.getLMD()));
        cv.put(Patients.Contract.MARITAL_STATUS, mPatient.getMarital_status());
        cv.put(Patients.Contract.EDUCATION_LEVEL, mPatient.getEducation_level());
        cv.put(Patients.Contract.CONTRACEPTIVE_USE, mPatient.getContraceptive_use());
        cv.put(Patients.Contract.ANC_STATUS, mPatient.getANC_status());
        if(mPatient.getANC_visit() != null) {
            cv.put(Patients.Contract.ANC_VISIT, Dates.toSQL(mPatient.getANC_visit()));
        }

        cv.put(Patients.Contract.AMBULANCE_NEED, mPatient.getAMBULANCE_need());
        cv.put(Patients.Contract.AMBULANCE_RESPONSE, mPatient.getAMBULANCE_response());
        cv.put(Patients.Contract.EDD, mPatient.getEDD());
        cv.put(Patients.Contract.RECEIVE_SMS, mPatient.getReceive_sms());
        cv.put(Patients.Contract.FOLLOW_UP, mPatient.getFollow_up());
        cv.put(Patients.Contract.CUG_STATUS, mPatient.getCUG_status());
        cv.put(Patients.Contract.COMMENT, mPatient.getComment());
        cv.put(Patients.Contract.BLEEDING, mPatient.getBleeding());
        cv.put(Patients.Contract.FEVER, mPatient.getFever());
        cv.put(Patients.Contract.SWOLLEN_FEET, mPatient.getSwollen_feet());
        cv.put(Patients.Contract.BLURRED_VISION, mPatient.getBlurred_vision());
        cv.put(Patients.Contract.IMAGE, String.valueOf(mPatient.getImage()));
        //TODO update db and uncomment
        //cv.put(Patients.Contract.CONFIRMED, mPatient.getConfirmed());
        //cv.put(Patients.Contract.DOB_ESTIMATED, mPatient.isDobEstimated());
        cv.put(Patients.Contract.LOCATION, mPatient.getLocation());
        cv.put(Patients.Contract.DISTRICT, mPatient.getDistrict());
        cv.put(Patients.Contract.COUNTY, mPatient.getCounty());
        cv.put(Patients.Contract.SUBCOUNTY, mPatient.getSubCounty());
        cv.put(Patients.Contract.PARISH, mPatient.getParish());
        cv.put(Patients.Contract.VILLAGE, mPatient.getVillage());
        if(exists){
            context.getContentResolver().update(uri,cv,null,null);
        } else {
            uri = context.getContentResolver().insert(Patients.CONTENT_URI,cv);
        }
        return uri;
    }

    public static ContentValues toValues(Patient object){
        ContentValues cv = new ContentValues();
        // TODO Check this.
        cv.put(Patients.Contract.UUID, object.getUuid());
        cv.put(Patients.Contract.PATIENT_ID, object.getSystemId());
        cv.put(Patients.Contract.GIVEN_NAME, object.getGiven_name());
        cv.put(Patients.Contract.FAMILY_NAME, object.getFamily_name());
        // Format the date for insert
        cv.put(Patients.Contract.DOB, Dates.toSQL(object.getDob()));
        cv.put(Patients.Contract.GENDER, object.getGender());
        cv.put(Patients.Contract.PNUMBER, object.getpNumber());
        cv.put(Patients.Contract.HOLDER_pNUMBER, object.getHolder_pNumber());


        cv.put(Patients.Contract.LMD, Dates.toSQL(object.getLMD()));
        cv.put(Patients.Contract.MARITAL_STATUS, object.getMarital_status());
        cv.put(Patients.Contract.EDUCATION_LEVEL, object.getEducation_level());
        cv.put(Patients.Contract.CONTRACEPTIVE_USE, object.getContraceptive_use());
        cv.put(Patients.Contract.ANC_STATUS, object.getANC_status());

        if (object.getANC_visit()!=null){
        cv.put(Patients.Contract.ANC_VISIT, Dates.toSQL(object.getANC_visit()));}
        //cv.put(Patients.Contract.AMBULANCE_NEED, object.getAMBULANCE_need());
        //cv.put(Patients.Contract.AMBULANCE_RESPONSE, object.getAMBULANCE_response());
        cv.put(Patients.Contract.EDD, object.getEDD());
        cv.put(Patients.Contract.RECEIVE_SMS, object.getReceive_sms());
        cv.put(Patients.Contract.FOLLOW_UP, object.getFollow_up());
        cv.put(Patients.Contract.CUG_STATUS, object.getCUG_status());
        //cv.put(Patients.Contract.COMMENT, object.getComment());
        //cv.put(Patients.Contract.BLEEDING, object.getBleeding());
        //cv.put(Patients.Contract.FEVER, object.getFever());
        //cv.put(Patients.Contract.SWOLLEN_FEET, object.getSwollen_feet());
        //cv.put(Patients.Contract.BLURRED_VISION, object.getBlurred_vision());
        if(object.getLocation() != null)
            cv.put(Patients.Contract.LOCATION, object.getLocation());
        cv.put(Patients.Contract.DISTRICT, object.getDistrict());
        cv.put(Patients.Contract.COUNTY, object.getCounty());
        cv.put(Patients.Contract.SUBCOUNTY, object.getSubCounty());
        cv.put(Patients.Contract.PARISH, object.getParish());
        cv.put(Patients.Contract.VILLAGE, object.getVillage());
        return cv;
    }

    public static Map<String, String> toForm(Patient object){
        Map<String,String> form = new HashMap<>();
        // TODO Check this
        form.put(Patients.Contract.UUID, object.getUuid());
        form.put(Patients.Contract.PATIENT_ID, object.getSystemId());
        form.put(Patients.Contract.GIVEN_NAME, object.getGiven_name());
        form.put(Patients.Contract.FAMILY_NAME, object.getFamily_name());
        // Format the date for insert
        form.put(Patients.Contract.DOB, Dates.toSQL(object.getDob()));
        form.put(Patients.Contract.GENDER, object.getGender());
        form.put(Patients.Contract.PNUMBER, object.getpNumber());
        form.put(Patients.Contract.HOLDER_pNUMBER, object.getHolder_pNumber());
        form.put(Patients.Contract.LMD, Dates.toSQL(object.getLMD()));
        form.put(Patients.Contract.MARITAL_STATUS, object.getMarital_status());
        form.put(Patients.Contract.EDUCATION_LEVEL, object.getEducation_level());
        form.put(Patients.Contract.CONTRACEPTIVE_USE, object.getContraceptive_use());
        form.put(Patients.Contract.ANC_STATUS, object.getANC_status());

        if(object.getANC_visit()!= null){
        form.put(Patients.Contract.ANC_VISIT, Dates.toSQL(object.getANC_visit()));}
        //form.put(Patients.Contract.AMBULANCE_NEED,object.getAMBULANCE_need());
        //form.put(Patients.Contract.AMBULANCE_NEED,object.getAMBULANCE_response());
        form.put(Patients.Contract.EDD, object.getEDD());
        form.put(Patients.Contract.RECEIVE_SMS, object.getReceive_sms());
        form.put(Patients.Contract.FOLLOW_UP, object.getFollow_up());
        form.put(Patients.Contract.CUG_STATUS, object.getCUG_status());
        //form.put(Patients.Contract.COMMENT, object.getComment());
        //form.put(Patients.Contract.BLEEDING, object.getBleeding());
        //form.put(Patients.Contract.FEVER, object.getFever());
        //form.put(Patients.Contract.SWOLLEN_FEET, object.getSwollen_feet());
        //form.put(Patients.Contract.BLURRED_VISION, object.getBlurred_vision());
        form.put(Patients.Contract.LOCATION, object.getLocation());
        form.put(Patients.Contract.DISTRICT, object.getDistrict());
        form.put(Patients.Contract.COUNTY, object.getCounty());
        form.put(Patients.Contract.SUBCOUNTY, object.getSubCounty());
        form.put(Patients.Contract.PARISH, object.getParish());
        form.put(Patients.Contract.VILLAGE,object.getVillage());
        form.put(Patients.Contract.PATIENT_ID, object.getSystemId());
        return form;
    }
}
