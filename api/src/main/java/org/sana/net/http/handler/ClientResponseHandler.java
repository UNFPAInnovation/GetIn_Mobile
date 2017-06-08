package org.sana.net.http.handler;

import com.google.gson.reflect.TypeToken;

import org.sana.clients.Client;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by winkler.em@gmail.com, on 06/05/2017.
 */
public class ClientResponseHandler  extends ApiResponseHandler<Response<Collection<Client>>>{

    @Override
    public Type getType() {
        return new TypeToken<Response<Collection<Client>>>(){}.getType();
    }
}
