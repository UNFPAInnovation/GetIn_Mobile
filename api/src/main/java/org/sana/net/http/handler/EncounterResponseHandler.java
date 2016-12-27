package org.sana.net.http.handler;

import java.lang.reflect.Type;
import java.util.Collection;

import org.sana.core.Encounter;
import org.sana.net.Response;

import com.google.gson.reflect.TypeToken;

public class EncounterResponseHandler extends ApiResponseHandler<Response<Collection<Encounter>>>{

	@Override
	public Type getType() {
		Type typeOf = new TypeToken<Response<Collection<Encounter>>>(){}.getType();
		return typeOf;
	}
}
