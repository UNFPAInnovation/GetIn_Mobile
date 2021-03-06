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

import java.io.File;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.Observations;
import org.sana.api.IObservation;
import org.sana.core.Concept;
import org.sana.core.Encounter;
import org.sana.core.Observation;
import org.sana.util.DateUtil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author Sana Development
 *
 */
public class ObservationWrapper extends ModelWrapper<IObservation> implements 
	IObservation
{
	public static final String TAG = ObservationWrapper.class.getSimpleName();

    private static final String SELECTION_UNIQUE =
            BaseContract.UUID + "=CAST(? AS TEXT) OR ("
            + Observations.Contract.ENCOUNTER + "=CAST(? AS TEXT) AND "
            + Observations.Contract.ID + "=CAST(? AS TEXT))";

    /**
	 * @param cursor
	 */
	public ObservationWrapper(Cursor cursor) {
		super(cursor);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IObservation#getId()
	 */
	@Override
	public String getId() {
		return getStringField(Observations.Contract.ID);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IObservation#getEncounter()
	 */
	@Override
	public Encounter getEncounter() {
        Encounter encounter = new Encounter();
        encounter.setUuid(getStringField(Observations.Contract.ENCOUNTER));
        return encounter;
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IObservation#getConcept()
	 */
	@Override
	public Concept getConcept() {
        Concept concept = new Concept();
		concept.setName(getStringField(Observations.Contract.CONCEPT));
        return concept;
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IObservation#getValue_complex()
	 */
	@Override
	public String getValue_complex() {
		return getStringField(Observations.Contract.VALUE);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IObservation#getValue_text()
	 */
	@Override
	public String getValue_text() {
		return getStringField(Observations.Contract.VALUE);
	}

	/* (non-Javadoc)
	 * @see org.sana.android.db.ModelWrapper#getObject()
	 */
	@Override
	public IObservation getObject() {
		Observation obj = new Observation();
        obj.setUuid(getUuid());
        obj.setCreated(getCreated());
        obj.setModified(getModified());
        obj.setConcept(getConcept());
        obj.setValue(getValue_text());
        obj.setEncounter(getEncounter());
        return obj;
	}
	
	public static Uri getReferenceByEncounterAndId(ContentResolver resolver, 
			String encounter, String id){
		return ModelWrapper.getOneReferenceByFields(
				Observations.CONTENT_URI,
				new String[]{ Observations.Contract.ENCOUNTER, Observations.Contract.ID }, 
				new String[]{ encounter, id }, resolver);
	}
	
	/**
	 * Convenience wrapper to returns a single row matched by the uuid value.
	 * 
	 * @param resolver The resolver which will perform the query.
	 * @param uuid The uuid to select by.
 	 * @return A cursor with a single row.
 	 * @throws IllegalArgumentException if multiple objects are returned.
 	 */
	public static IObservation getOneByUuid(ContentResolver resolver, String uuid){
		ObservationWrapper wrapper = new ObservationWrapper(ModelWrapper.getOneByUuid(
				Observations.CONTENT_URI,resolver, uuid));
		IObservation object = null;
		if(wrapper != null)
		try{ 
			object = wrapper.next();
		} finally {
			wrapper.close();
		}
		return object;
	}
	
	/**
	 * Convenience wrapper to return a wrapped cursor which references all of 
	 * the entries ordered by {@link org.sana.android.provider.BaseContract#CREATED CREATED} 
	 * in ascending order, or, oldest first.
	 * 
	 * @param resolver The resolver which will perform the query.
	 * @return A cursor with the result or null.
	 */
	public static ObservationWrapper getAllByCreatedAsc(ContentResolver resolver)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByCreatedAsc(
				Observations.CONTENT_URI,resolver));
	}
	
	/**
	 * Convenience wrapper to return a wrapped cursor which references all of 
	 * the entries ordered by {@link org.sana.android.provider.BaseContract#CREATED CREATED} 
	 * in descending order, or, newest first.
	 *   
	 * @param resolver The resolver which will perform the query.
	 * @return A cursor with the result or null.
	 */
	public static ObservationWrapper getAllByCreatedDesc(ContentResolver resolver)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByCreatedDesc(
				Observations.CONTENT_URI,resolver));
	}
	
	/**
	 * Convenience wrapper to return a wrapped cursor which references all of 
	 * the entries ordered by {@link org.sana.android.provider.BaseContract#MODIFIED MODIFIED} 
	 * in ascending order, or, oldest first.
	 * 
	 * @param resolver The resolver which will perform the query.
	 * @return A cursor with the result or null.
	 */
	public static ObservationWrapper getAllByModifiedAsc(ContentResolver resolver)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByModifiedAsc(
				Observations.CONTENT_URI,resolver));
	}
	
	/**
	 * Convenience wrapper to return a wrapped cursor which references all of 
	 * the entries ordered by {@link org.sana.android.provider.BaseContract#MODIFIED MODIFIED} 
	 * in descending order, or, newest first.
	 * 
	 * @param resolver The resolver which will perform the query.
	 * @return A cursor with the result or null.
	 */
	public static ObservationWrapper getAllByModifiedDesc(ContentResolver resolver)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByModifiedDesc(
				Observations.CONTENT_URI,resolver));
	}
	
	/**
	 * Returns a single observation within an encounter.
	 * @param resolver A content resolver.
	 * @param encounter The encounter uuid.
	 * @param id The node id.
	 * @return The row values in an IObservation object. 
	 */
	public static IObservation getOneByEncounterAndId(ContentResolver resolver, 
			String encounter, String id)
	{		
		ObservationWrapper wrapper = new ObservationWrapper(ModelWrapper.getOneByFields(
				Observations.CONTENT_URI,resolver, 
				new String[]{ 	Observations.Contract.ENCOUNTER,
								Observations.Contract.ID },
				new String[]{	encounter, id}));
		IObservation object = null;
		if(wrapper != null)
		try{ 
			object = wrapper.next();
		} finally {
			wrapper.close();
		}
		return object;
	}

    public static Uri getByEncounterAndId(Context context, String encounter,
                                          String id)
    {
        Uri result = Uri.EMPTY;
        Cursor cursor = null;
        boolean multipleExist = false;
        try {
            final String[] projection = new String[]{ BaseContract._ID };
            final String selection = Observations.Contract.ENCOUNTER + "='"+encounter+"'"
                    + " AND " + Observations.Contract.ID + "='"+id+"'";
            String[] selectionArgs = new String[]{ id };
            cursor = context.getContentResolver().query(
                    Observations.CONTENT_URI, projection, selection,
                    null, null);
            if (cursor != null)
                if(cursor.getCount() == 1 && cursor.moveToFirst())
                    result = ContentUris.withAppendedId(Observations.CONTENT_URI, cursor.getLong(0));
                else if(cursor.getCount() > 1)
                    multipleExist = true;
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null) cursor.close();
        }
        if(multipleExist)
            throw new SQLiteDatabaseCorruptException("");
        return result;
    }

    public static Uri getUnique(Context context, Observation object)
    {
        return getUnique(context, object.getUuid(),
                    object.getEncounter().getUuid(),
                    object.getId());
    }


    public static Uri getUnique(Context context, String uuid,
                                String encounter, String node)
    {
        Uri result = Uri.EMPTY;
        Cursor cursor = null;
        boolean multipleExist = false;
        final String[] selectionArgs = new String[]{
                uuid,
                encounter,
                node
        };
        try {
            final String[] projection = new String[]{ BaseContract._ID };
            cursor = context.getContentResolver().query(
                    Observations.CONTENT_URI,
                    projection,
                    SELECTION_UNIQUE,
                    selectionArgs,
                    null);
            if (cursor != null)
                if(cursor.getCount() == 1 && cursor.moveToFirst())
                    result = ContentUris.withAppendedId(Observations.CONTENT_URI, cursor.getLong(0));
                else if(cursor.getCount() > 1)
                    multipleExist = true;
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null) cursor.close();
        }
        if(multipleExist)
            throw new SQLiteDatabaseCorruptException("Uniqueness on uuid or encounter and node violated!");
        return result;
    }

	/**
	 * Returns a all of the observations within an encounter.
	 * 
	 * @param resolver A content resolver.
	 * @param encounter The encounter uuid.
	 * @return
	 */
	public ObservationWrapper getAllByEncounter(ContentResolver resolver, String encounter)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByField(
				Observations.CONTENT_URI,resolver, Observations.Contract.ENCOUNTER, encounter));
	}

	/**
	 * Returns a all of the observations with the specified concept.
	 * 
	 * @param resolver A content resolver.
	 * @param concept The uuid of the concept.
	 * @return
	 */
	public static  ObservationWrapper getAllByConcept(ContentResolver resolver, String concept)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByField(
				Observations.CONTENT_URI,resolver, Observations.Contract.ENCOUNTER, concept));
	}

	/**
	 * Returns a all of the observations for a given subject.
	 * 
	 * @param resolver A content resolver.
	 * @param subject The uuid of the subject.
	 * @return
	 */
	public static  ObservationWrapper getAllBySubject(ContentResolver resolver, String subject)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByField(
				Observations.CONTENT_URI,resolver, Observations.Contract.ENCOUNTER, subject));
	}
	
	/**
	 * Returns a all of the observations for a given subject with a specified
	 * concept
	 * 
	 * @param resolver A content resolver.
	 * @param subject The uuid of the subject.
	 * @param concept The uuid of the concept.
	 * @return
	 */
	public static  ObservationWrapper getAllBySubjectAndConcept(
			ContentResolver resolver, String subject, String concept)
	{		
		return new ObservationWrapper(ModelWrapper.getAllByFields(
				Observations.CONTENT_URI,resolver, 
				new String[]{ 	Observations.Contract.SUBJECT,
								Observations.Contract.CONCEPT },
				new String[]{	subject,concept }));
	}
	
	public static File getComplexData(ContentResolver resolver, Uri obs){
		Cursor cursor = null;
		String path = null;
		try{
			String[] projection = new String[]{ Observations.Contract.VALUE };
			cursor = resolver.query(obs, projection, null,null,null);
			if(cursor != null && cursor.moveToFirst())
				path = cursor.getString(0);
		} finally {
			if(cursor != null) cursor.close();
		}
		return (TextUtils.isEmpty(path))? null: new File(path);
	}

    public static boolean existsByEncounterAndId(Context context,
                                                 String encounter, String id)
    {
        boolean exists = false;
        ObservationWrapper wrapper = null;
        try {
            wrapper = new ObservationWrapper(ModelWrapper.getOneByFields(
                    Observations.CONTENT_URI, context.getContentResolver(),
                    new String[]{Observations.Contract.ENCOUNTER,
                            Observations.Contract.ID},
                    new String[]{"'" + encounter + "'", id}));
            if (wrapper != null && wrapper.getCount() == 1)
                exists = true;
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            wrapper.close();
        }
        return exists;
    }

    public static int deleteCorrupted(Context context, String uuid,
                                      String encounter, String node) {
        Uri result = Uri.EMPTY;
        int deleted = 0;
        final String[] selectionArgs = new String[]{
                uuid,
                encounter,
                node
        };
        try {
            final String[] projection = new String[]{BaseContract._ID};
            deleted = context.getContentResolver().delete(
                    Observations.CONTENT_URI,
                    SELECTION_UNIQUE,
                    selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleted;
    }

    public static int deleteCorrupted(Context context, Observation object) {
        return deleteCorrupted(context, object.getUuid(),
                object.getEncounter().getUuid(),
                object.getId());
    }

    public static ContentValues toValues(Observation obj){
        ContentValues values = new ContentValues();
        values.put(BaseContract.UUID , obj.getUuid());
        values.put(BaseContract.CREATED ,
                DateUtil.format(obj.getCreated()));
        values.put(BaseContract.MODIFIED ,
                DateUtil.format(obj.getModified()));
        values.put(Observations.Contract.ENCOUNTER, obj.getEncounter().getUuid());
        values.put(Observations.Contract.ID, obj.getId());
        values.put(Observations.Contract.CONCEPT, obj.getConcept().getName());
        values.put(Observations.Contract.VALUE, obj.getValue_text());
        values.put(Observations.Contract.SUBJECT, obj.getEncounter().getSubject().getUuid());
        return values;
    }
}
