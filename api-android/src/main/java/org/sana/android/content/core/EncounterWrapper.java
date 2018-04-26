/**
 * 
 */
package org.sana.android.content.core;

import org.sana.android.content.Uris;
import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.Concepts;
import org.sana.android.provider.Encounters;
import org.sana.api.IConcept;
import org.sana.api.IEncounter;
import org.sana.api.IProcedure;
import org.sana.api.ISubject;
import org.sana.core.Concept;
import org.sana.core.Encounter;
import org.sana.core.Observer;
import org.sana.core.Procedure;
import org.sana.core.Subject;
import org.sana.util.DateUtil;
import org.sana.util.UUIDUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

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

	@Override
	public Observer getObserver() {
		return new Observer(getString(getColumnIndex(Encounters.Contract.PROCEDURE)));
	}

    @Override
    public IConcept getConcept() {
        Concept concept = new Concept();
        String val = getString(getColumnIndex(Encounters.Contract.CONCEPT));
        if(UUIDUtil.isValid(val)){
            concept.setUuid(val);
        } else {
            concept.setName(val);
        }
        return concept;
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
        e.concept = (Concept) getConcept();
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
        int descriptor = Uris.getDescriptor(uri);
        // Safety check
        switch(descriptor){
            case Uris.ENCOUNTER_ITEM:
            case Uris.ENCOUNTER_UUID:
                break;
            default:
                throw new IllegalArgumentException("Invalid Encounter Uri");
        }
        try{
            cursor = context.getContentResolver().query(
                    uri, null, null, null, null);
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
        if(obj.getConcept() != null && !TextUtils.isEmpty(obj.getConcept().getName())) {
            values.put(Encounters.Contract.CONCEPT, obj.getConcept().getName());
        }
        return values;
    }

}
