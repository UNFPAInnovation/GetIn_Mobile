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
package org.sana.android.db.impl;

import java.util.Date;

import org.sana.android.db.TableHelper;
import org.sana.android.provider.BaseContract;
import org.sana.android.provider.EncounterTasks.Contract;
import org.sana.android.provider.Models;
import org.sana.android.util.Dates;
import org.sana.api.task.EncounterTask;
import org.sana.api.task.Status;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

/**
 * @author Sana Development
 *
 */
public class EncounterTasksHelper extends TableHelper<EncounterTask>{
    public static final String TAG = EncounterTasksHelper.class.getSimpleName();

	private EncounterTasksHelper(){
		super(EncounterTask.class);
	}

	@Override
	public ContentValues onInsert(ContentValues values) {
		ContentValues vals = new ContentValues();
        // Default to ASSIGNED
        vals.put(Contract.STATUS, Status.ASSIGNED.name());
        // Default to NEW(value=2) if not included.
        vals.put(BaseContract.SYNCH, Models.Synch.NEW);
        // Default to empty string
        vals.put( Contract.OBSERVER, "");
        vals.put(Contract.ENCOUNTER, "");
        vals.put( Contract.PROCEDURE, "");
        vals.put( Contract.SUBJECT, "");

        // Default for the date fields
        Date now = new Date();
        /*
        String dueStr = values.getAsString(Contract.DUE_DATE);
		Date dueDate = new Date();
		try {
			dueDate = (dueStr != null)? Dates.fromSQL(dueStr): new Date();

            Date checkDate;
        
            String completed = values.getAsString(Contract.COMPLETED);
            if(!TextUtils.isEmpty(completed)){
                checkDate = Dates.fromSQL(completed);
                vals.put( Contract.COMPLETED, Dates.toSQL(checkDate));
            }
            String started = values.getAsString(Contract.STARTED);
            if(!TextUtils.isEmpty(started)){
                checkDate = Dates.fromSQL(started);
                vals.put( Contract.STARTED, Dates.toSQL(checkDate));
            }
        } catch (ParseException e) {
		    e.printStackTrace();
	    }
	    */
        vals.put( Contract.DUE_DATE, Dates.toSQL(now));
        vals.putAll(values);
		return super.onInsert(vals);
	}

	@Override
	public ContentValues onUpdate(Uri uri, ContentValues values) {
        if(!values.containsKey(BaseContract.SYNCH)){
            values.put(BaseContract.SYNCH, Models.Synch.MODIFIED);
        }
		return super.onUpdate(uri, values);
	}

	@Override
	public String onCreate() {
		Log.i(TAG, "onCreate()");
		return "CREATE TABLE " + getTable() + " ("
				+ Contract._ID + " INTEGER PRIMARY KEY,"
				+ Contract.UUID + " TEXT, "
				+ Contract.OBSERVER + " TEXT, "
				+ Contract.STATUS + " TEXT, "
				+ Contract.DUE_DATE + " DATE, "
				+ Contract.COMPLETED + " DATE, "
				+ Contract.STARTED + " DATE, "
				+ Contract.ENCOUNTER + " TEXT, "
				+ Contract.PROCEDURE + " TEXT, "
				+ Contract.SUBJECT + " TEXT, "
                + Contract.CREATED + " DATE,"
                + Contract.MODIFIED + " DATE,"
                + Contract.CONCEPT + " TEXT, "
                + BaseContract.SYNCH + " INTEGER DEFAULT '-1'"
				+ ");";
		
	}

	/* (non-Javadoc)
	 * @see org.sana.android.db.UpgradeHelper#onUpgrade(int, int)
	 */
	@Override
	public String onUpgrade(int oldVersion, int newVersion) {
        String sql = null;
		if(oldVersion < newVersion){
            StringBuilder sqlBuilder = new StringBuilder();
            if (newVersion == 9 || (oldVersion < 9 && newVersion > 9)){
                //sqlBuilder.append("ALTER TABLE " + getTable() + " ADD COLUMN " +
                //    BaseContract.SYNCH + " INTEGER DEFAULT '-1';");
                sqlBuilder.append("ALTER TABLE " + getTable() + " ADD COLUMN " +
                        Contract.CONCEPT + " TEXT;");
            }
            sql = sqlBuilder.toString();
        }
        return sql;
	}
	

	private static final EncounterTasksHelper HELPER = new EncounterTasksHelper();
	
	public static EncounterTasksHelper getInstance(){
		return HELPER;
	}
}
