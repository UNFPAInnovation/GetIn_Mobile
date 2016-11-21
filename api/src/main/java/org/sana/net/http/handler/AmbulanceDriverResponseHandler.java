package org.sana.net.http.handler;

import com.google.gson.reflect.TypeToken;

import org.sana.core.AmbulanceDriver;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by winkler.em@gmail.com, on 11/18/2016.
 */
public class AmbulanceDriverResponseHandler  extends ApiResponseHandler<Response<Collection<AmbulanceDriver>>>{
    public static final String TAG = AmbulanceDriverResponseHandler.class.getSimpleName();

        @Override
        public Type getType() {
            return new TypeToken<Response<Collection<AmbulanceDriver>>>(){}.getType();
        }
}
