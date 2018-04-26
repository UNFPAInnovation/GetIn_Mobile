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

import org.sana.android.content.Uris;
import org.sana.android.db.ModelWrapper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.Procedures;
import org.sana.api.IConcept;
import org.sana.api.IProcedure;
import org.sana.core.Concept;
import org.sana.core.Procedure;
import org.sana.util.DateUtil;
import org.sana.util.UUIDUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

/**
 * @author Sana Development
 *
 */
public class ProcedureWrapper extends ModelWrapper<IProcedure> implements
		IProcedure {
	public static final String TAG = ProcedureWrapper.class.getSimpleName();

	/**
	 * @param cursor
	 */
	public ProcedureWrapper(Cursor cursor) {
		super(cursor);
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IProcedure#getAuthor()
	 */
	@Override
	public String getAuthor() {
        return getString(getColumnIndex(Procedures.Contract.AUTHOR));
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IProcedure#getVersion()
	 */
	@Override
	public String getVersion() {
        return getString(getColumnIndex(Procedures.Contract.VERSION));
	}

	/* (non-Javadoc)
	 * @see org.sana.api.IProcedure#getDescription()
	 */
	@Override
	public String getDescription() {
        return getString(getColumnIndex(Procedures.Contract.DESCRIPTION));
    }

	/* (non-Javadoc)
	 * @see org.sana.api.IProcedure#getSrc()
	 */
	@Override
	public String getSrc() {
		return getString(getColumnIndex(Procedures.Contract.PROCEDURE));
	}

    @Override
    public String getTitle() {
        return getString(getColumnIndex(Procedures.Contract.TITLE));
    }

    @Override
    public IConcept getConcept() {
        Concept concept = new Concept();
        String val = getString(getColumnIndex(Procedures.Contract.CONCEPT));
        if(UUIDUtil.isValid(val)){
            concept.setUuid(val);
        } else {
            concept.setName(val);
        }
        return concept;
    }

    /* (non-Javadoc)
     * @see org.sana.android.db.ModelWrapper#getObject()
     */
	@Override
	public IProcedure getObject() {
        Procedure obj = new Procedure();
        obj.setAuthor(getAuthor());
        obj.setTitle(getTitle());
        obj.setVersion(getVersion());
        obj.setConcept((Concept) getConcept());
        obj.setDescription(getDescription());
        obj.setSrc(getSrc());
		return obj;
	}

    public static IProcedure get(Context context, Uri uri){
        Cursor cursor = null;
        ProcedureWrapper wrapper = null;
        IProcedure object = null;
        int descriptor = Uris.getDescriptor(uri);
        // Safety check
        switch(descriptor){
            case Uris.PROCEDURE_ITEM:
            case Uris.PROCEDURE_UUID:
                break;
            default:
                throw new IllegalArgumentException("Invalid Procedure Uri");
        }
        try{
            cursor = context.getContentResolver().query(
                    uri, null, null, null, null);
            if(cursor != null){
                wrapper = new ProcedureWrapper(cursor);
                if(wrapper.moveToFirst() && wrapper.getCount() == 1){
                    object = wrapper.getObject();
                } else {
                    throw new IllegalArgumentException("Invalid Procedure Uri");
                }
            }
        } finally {
            if(wrapper != null) wrapper.close();
        }
        return object;
    }


    public static ContentValues toValues(Procedure obj){
        ContentValues values = new ContentValues();
        values.put(BaseContract.UUID , obj.getUuid());
        values.put(BaseContract.CREATED ,
                DateUtil.format(obj.getCreated()));
        values.put(BaseContract.MODIFIED ,
                DateUtil.format(obj.getModified()));
        values.put(Procedures.Contract.TITLE, obj.getTitle());
        values.put(Procedures.Contract.AUTHOR, obj.getAuthor());
        values.put(Procedures.Contract.DESCRIPTION, obj.getDescription());
        values.put(Procedures.Contract.VERSION, obj.getVersion());
        if(obj.getConcept() != null && !TextUtils.isEmpty(obj.getConcept().getName())) {
            values.put(Procedures.Contract.CONCEPT, obj.getConcept().getName());
        }
        return values;
    }
}
