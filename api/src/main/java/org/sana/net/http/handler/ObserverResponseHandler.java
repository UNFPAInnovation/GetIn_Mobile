package org.sana.net.http.handler;

import com.google.gson.reflect.TypeToken;

import org.sana.core.Observer;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by winkler.em@gmail.com, on 12/23/2016.
 */
public class ObserverResponseHandler extends ApiResponseHandler<Response<Collection<Observer>>> {
    public static final String TAG = ObserverResponseHandler.class.getSimpleName();

    @Override
    public Type getType() {
        return new TypeToken<Response<Collection<Observer>>>(){}.getType();
    }
}
