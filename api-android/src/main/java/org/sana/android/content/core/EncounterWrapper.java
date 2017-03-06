/**
 * 
 */
package org.sana.android.content.core;

import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.Concepts;
import org.sana.android.provider.Encounters;
import org.sana.api.IConcept;
import org.sana.api.IEncounter;
import org.sana.api.IProcedure;
import org.sana.api.ISubject;
import org.sana.core.Encounter;
import org.sana.core.Observer;
import org.sana.core.Procedure;
import org.sana.core.Subject;
import org.sana.util.DateUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author ewinkler
 *
 */
public class EncounterWrapper extends ModelWrapper<IEncounter> implements IEncounter {

	public EncounterWrapper(Cursor cursor) {
		super(cursor);
	}

	@Override
	public ISubject getSubject() {
		Subject s = new Subject();
		s.setUuid(getString(getColumnIndex(Encounters.Contract.SUBJECT)));
		return s;
	}

	@Override
	public IProcedure getProcedure() {
		return new Procedure(getString(getColumnIndex(Encounters.Contract.PROCEDURE)));
	}
 //changed from Contract.PROCEDURE to Contract.Observer
	@Override
	public Observer getObserver() {
		return new Observer(getString(getColumnIndex(Encounters.Contract.PROCEDURE)));
	}

	@Override
	public IEncounter getObject() {
		Encounter e = new Encounter();
        e.setUuid(getUuid());
		e.created = getCreated();
		e.modified = getModified();
		e.subject = (Subject) getSubject();
		e.observer = getObserver();
		e.procedure = (Procedure) getProcedure();
		return e;
	}

	public static IEncounter getOneByUuid(ContentResolver resolver, String uuid){
		EncounterWrapper wrapper = new EncounterWrapper(ModelWrapper.getOneByUuid(
				Concepts.CONTENT_URI,resolver, uuid));
		IEncounter object = null;
		if(wrapper != null)
		try{ 
			object = wrapper.next();
		} finally {
			wrapper.close();
		}
		return object;
	}

    /**
     * Gets a single Encounter representation.
     *
     * @param context The context
     * @param uri The encounter Uri
     * @return
     * @throws IllegalArgumentException If the Uri returns more than one
     *  Encounter reference or none.
     */
    public static IEncounter get(Context context, Uri uri){
        Cursor cursor = null;
        EncounterWrapper wrapper = null;
        IEncounter object = null;
            try{
                cursor = context.getContentResolver().query(
                        Encounters.CONTENT_URI, null, null, null, null);
                if(cursor != null){
                    wrapper = new EncounterWrapper(cursor);
                    if(wrapper.moveToFirst() && wrapper.getCount() == 1){
                        object = wrapper.getObject();
                    } else {
                        throw new IllegalArgumentException("Invalid Encounter Uri");
                    }
                }
            } finally {
                if(wrapper != null) wrapper.close();
            }
        return object;
    }

    public static ContentValues toValues(Encounter obj){
        ContentValues values = new ContentValues();
        values.put(BaseContract.UUID , obj.getUuid());
        values.put(BaseContract.CREATED ,
                DateUtil.format(obj.getCreated()));
        values.put(BaseContract.MODIFIED ,
                DateUtil.format(obj.getModified()));
        values.put(Encounters.Contract.PROCEDURE , obj.getProcedure().getUuid());
        values.put(Encounters.Contract.SUBJECT , obj.getSubject().getUuid() );
        values.put(Encounters.Contract.OBSERVER , obj.getObserver().getUuid());
        return values;
    }

}
