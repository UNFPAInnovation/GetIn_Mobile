/**
 * Copyright (c) 2014, Sana
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
package org.sana.android.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.sana.core.Model;
import org.sana.net.Response;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Sana Development
 *
 */
public final class ModelEntity {

	public static class NamedContentValues{
		public final Uri uri;
		public final ContentValues values;	
		public NamedContentValues(Uri uri, ContentValues values){
			this.uri = uri;
			this.values = values;
		}
	}

	ContentValues values;
	Uri uri;
	public ModelEntity(Uri uri, ContentValues values){
		this.uri = uri;
		this.values = new ContentValues(values);
	}
	
	public ContentValues getEntityValues(){
		return values;
	}
	
	public Uri getUri(){
		return uri;
	}

    public static Map<String, String> toMap(Bundle form){
        Map<String, String> data = new HashMap<String, String>();
        // Should have at least one field that need to be updated
        if(form != null){
            Iterator<String> keys = form.keySet().iterator();
            while(keys.hasNext()){
                String key = keys.next();
                Object obj = form.get(key);
                if(obj == null)
                    data.put(key, "");
                else if(obj instanceof Model)
                    data.put(key, ((Model)obj).getUuid());
                else
                    data.put(key, String.valueOf(obj));
            }
        }
        return data;
    }
}
