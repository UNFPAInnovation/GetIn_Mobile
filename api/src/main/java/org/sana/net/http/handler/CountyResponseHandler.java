package org.sana.net.http.handler;

import com.google.gson.reflect.TypeToken;

import org.sana.core.County;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

;

public class CountyResponseHandler extends ApiResponseHandler<Response<Collection<County>>>{

	@Override
	public Type getType() {
		Type typeOf = new TypeToken<Response<Collection<County>>>(){}.getType();
		return typeOf;
	}
}
