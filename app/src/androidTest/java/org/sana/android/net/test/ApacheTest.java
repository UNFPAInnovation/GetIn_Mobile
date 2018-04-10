package org.sana.android.net.test;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.VersionInfo;
import org.sana.R;
import org.sana.android.net.HttpRequestFactory;
import org.sana.android.net.MDSInterface2;
import org.sana.net.http.HttpTaskFactory;

import android.test.AndroidTestCase;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ApacheTest extends AndroidTestCase {

	
	public void testVersion(){
		VersionInfo vi = VersionInfo.loadVersionInfo("org.apache.http.client",getClass().getClassLoader());  
		String version = vi.getRelease();  
		Log.d("org.apache.http.client", version);
	}

    public void testJsonResponse(){
        String username = getContext().getString(R.string.debug_user);
        String password = getContext().getString(R.string.debug_password);
        String url = MDSInterface2.getMDSUrl(getContext(), getContext().getString(R.string.path_session));
        URI uri = null;
        try {
            uri = MDSInterface2.getURI(getContext(), getContext().getString(R.string.path_session));

        List<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("username", username));
        postData.add(new BasicNameValuePair("password", password));
        HttpPost post = HttpRequestFactory.getPostRequest(uri, postData);
        //HttpPost post = new HttpPost(uri);
        post.setHeader("Accept", "application/json");
        UrlEncodedFormEntity entity = null;
            entity = new UrlEncodedFormEntity(postData, "UTF-8");
            post.setEntity(entity);
            HttpResponse response;
            String responseString;
            HttpClient client = HttpTaskFactory.CLIENT_FACTORY.produce();
            HttpParams httpParams = client.getParams();
            int timeout = -1;
            if(timeout > 0){
                HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
                HttpConnectionParams.setSoTimeout(httpParams, timeout);
            }
            response = client.execute(post);
            responseString = EntityUtils.toString(response.getEntity());
            Log.d("ApacheTest", responseString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
