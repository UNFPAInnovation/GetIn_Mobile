package org.sana.net.http.handler;

import com.google.gson.reflect.TypeToken;

import org.sana.core.District;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

public class DistrictResponseHandler extends ApiResponseHandler<Response<Collection<District>>>{

	@Override
	public Type getType() {
		Type typeOf = new TypeToken<Response<Collection<District>>>(){}.getType();
		return typeOf;
	}
}
