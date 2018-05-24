package org.sana.net.http.handler;

import com.google.gson.reflect.TypeToken;

import org.sana.core.Subcounty;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

;

public class SubcountyResponseHandler extends ApiResponseHandler<Response<Collection<Subcounty>>>{

	@Override
	public Type getType() {
		Type typeOf = new TypeToken<Response<Collection<Subcounty>>>(){}.getType();
		return typeOf;
	}
}
