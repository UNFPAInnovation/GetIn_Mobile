package org.sana.net.http.handler;

import com.google.common.reflect.TypeToken;

import org.sana.core.VHT;
import org.sana.net.Response;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by Marting on 21/11/2016.
 */

public class VHTResponseHandler extends ApiResponseHandler <Response<Collection<VHT>>> {

    public static final String TAG = VHTResponseHandler.class.getName();
    @Override
    public Type getType() {
        return new TypeToken<Response<Collection<VHT>>>(){}.getType();
    }
}
