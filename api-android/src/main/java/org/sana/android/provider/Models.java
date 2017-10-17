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
package org.sana.android.provider;

import org.sana.android.content.Uris;
import org.sana.core.Concept;
import org.sana.core.Encounter;
import org.sana.core.Event;
import org.sana.core.Instruction;
import org.sana.core.Notification;
import org.sana.core.Observation;
import org.sana.core.Observer;
import org.sana.core.Procedure;
import org.sana.core.Subject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Sana Development
 *
 */
public class Models {
	public static final String TAG = Models.class.getSimpleName();
	
	public static final String AUTHORITY = "org.sana.provider";
	
	public static final Uri CONTENT_URI = Uri.fromParts(
			ContentResolver.SCHEME_CONTENT, AUTHORITY, null);
	
	public static final Class<?>[] MODELS = new Class<?>[]{
		Concept.class,
		Encounter.class,
		Event.class,
		Instruction.class,
		Notification.class,
		Observation.class,
		Observer.class,
		Procedure.class,
		Subject.class,
	};

    public static final Uri[] SYNCHABLE = new Uri[]{
            Subjects.CONTENT_URI,
            Encounters.CONTENT_URI,
            EncounterTasks.CONTENT_URI
    };

    public interface Synch {
        int MODIFIED = -1;
        int UP_TO_DATE = 0;
        int PENDING = 1;
        int NEW = 2;
        int DELAY = 4;
    }

    public static void markModified(Context context, Collection<Uri> list) {
        for (Uri uri : list) {
            markPending(context, uri);
        }
    }

    public static void markModified(Context context, Uri uri) {
        ContentValues values = new ContentValues();
        markModified(values);
        context.getContentResolver().update(uri, values, null, null);
    }

    public static void markModified(ContentValues values) {
        if (!values.containsKey(BaseContract.SYNCH))
            values.put(BaseContract.SYNCH, Synch.MODIFIED);
    }

    public static void markNew(Context context, Uri uri) {
        ContentValues values = new ContentValues();
        markNew(values);
        context.getContentResolver().update(uri, values, null, null);
    }

    public static void markNew(ContentValues values) {
        if (!values.containsKey(BaseContract.SYNCH))
            values.put(BaseContract.SYNCH, Synch.NEW);
    }

    public static void markUpToDate(Context context, Uri uri) {
        ContentValues values = new ContentValues();
        markUpToDate(values);
        context.getContentResolver().update(uri, values, null, null);
    }

    public static void markUpToDate(ContentValues values) {
        if (!values.containsKey(BaseContract.SYNCH))
            values.put(BaseContract.SYNCH, Synch.UP_TO_DATE);
    }

    public static void markPending(Context context, Collection<Uri> list) {
        for (Uri uri : list) {
            markPending(context, uri);
        }
    }

    public static void markPending(Context context, Uri uri) {
        ContentValues values = new ContentValues();
        markPending(values);
        context.getContentResolver().update(uri, values, null, null);
    }

    public static void markPending(ContentValues values) {
        if (!values.containsKey(BaseContract.SYNCH))
            values.put(BaseContract.SYNCH, Synch.PENDING);
    }

    public static void markDelay(Context context, Collection<Uri> list) {
        for (Uri uri : list) {
            markDelay(context, uri);
        }
    }

    public static void markDelay(Context context, Uri uri) {
        ContentValues values = new ContentValues();
        markDelay(values);
        context.getContentResolver().update(uri, values, null, null);
    }

    public static void markDelay(ContentValues values) {
        if (!values.containsKey(BaseContract.SYNCH))
            values.put(BaseContract.SYNCH, Synch.DELAY);
    }

    public static final List<Uri> getReadyToSynch(Context context) {
        List<Uri> list = new ArrayList<>();
        for(Uri uri:SYNCHABLE) {
            list.addAll(Models.getWaitingToSynch(context, uri));
        }
        return list;
    }

    public static final List<Uri> getReadyToSynchNew(Context context) {
        List<Uri> list = new ArrayList<>();
        for(Uri uri:SYNCHABLE) {
            list.addAll(Models.getWaitingToSynchNew(context,uri));
        }
        return list;
    }

    public static final List<Uri> getReadyToSynchPending(Context context) {
        List<Uri> list = new ArrayList<>();
        for(Uri uri:SYNCHABLE) {
            list.addAll(Models.getWaitingToSynchPending(context,uri));
        }
        return list;
    }


    public static List<Uri> getWaitingToSynch(Context context, Uri uri, int state) {
        List<Uri> list = new ArrayList<>();
        Cursor cursor = null;
        int descriptor = Uris.getDescriptor(uri);

        String selection = null;
        String[] projection= null;
        switch (descriptor) {
            case Uris.SUBJECT_DIR:
            case Uris.ENCOUNTER_DIR:
            case Uris.ENCOUNTER_TASK:
                selection = BaseContract.SYNCH + " = ?";
                projection = new String[]{
                        String.valueOf(state)
                };
                break;
            default:
                return list;
        }
        try {
            cursor = context.getContentResolver().query(uri,
                    new String[]{BaseContract.UUID}, selection,
                    projection, null);
            while (cursor.moveToNext()) {
                String uuid = cursor.getString(0);
                list.add(Uris.withAppendedUuid(uri, uuid));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    public static List<Uri> getWaitingToSynch(Context context, Uri uri) {
        List<Uri> list = new ArrayList<>();
        list.addAll(getWaitingToSynch(context, uri, Models.Synch.MODIFIED));
        return list;
    }


    public static List<Uri> getWaitingToSynchNew(Context context, Uri uri) {
        List<Uri> list = new ArrayList<>();
        list.addAll(getWaitingToSynch(context, uri, Models.Synch.NEW));
        return list;
    }


    public static List<Uri> getWaitingToSynchPending(Context context, Uri uri) {
        List<Uri> list = new ArrayList<>();
        list.addAll(getWaitingToSynch(context, uri, Models.Synch.PENDING));
        return list;
    }
}
