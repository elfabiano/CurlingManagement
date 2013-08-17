package com.example.curlingmanagement.rest.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

final public class NetworkUtilities {
	
    /** The tag used to log to adb console. **/
    private static final String TAG = "NetworkUtilities";
    
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
	
	private NetworkUtilities() {
	}
	
    /**
     * Configures the httpClient to connect to the URL provided.
     */
    public static HttpClient getHttpClient() {
    	Log.v(TAG, "configuring HTTP client");
        HttpClient httpClient = new DefaultHttpClient();
        final HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        return httpClient;
    }
	
    
}
