package org.sana.net.http.handler;

import com.google.gson.reflect.TypeToken;

import org.sana.core.Parish;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

;

public class ParishResponseHandler extends ApiResponseHandler<Response<Collection<Parish>>>{

	@Override
	public Type getType() {
		Type typeOf = new TypeToken<Response<Collection<Parish>>>(){}.getType();
		return typeOf;
	}
}
