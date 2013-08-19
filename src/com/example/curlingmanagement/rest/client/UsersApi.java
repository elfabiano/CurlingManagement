package com.example.curlingmanagement.rest.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

final public class UsersApi implements IUsersApi{
	
    public static final String PARAM_USERNAME = "username";
    
    public static final String PARAM_PASSWORD = "password";
    
    public static final String PARAM_EMAIL = "email";
    
    public static final String PARAM_AUTH_TOKEN = "auth_token";
    
    public static final String BASE_URL = "http://curlingmanagement.com";
    
    public static final String LOGIN_URI = BASE_URL + "/usersapi.php?request=login";
    
    public static final String LOGOUT_URI = BASE_URL + "/usersapi.php?request=logout";
    
    public static final String ADD_USER_URI = BASE_URL + "/usersapi.php?request=adduser";
    
    public static final String UPDATE_USER_URI = BASE_URL + "/usersapi.php?request=updateuser";
    
    public static final String DELETE_USER_URI = BASE_URL + "/usersapi.php?request=deleteuser";

	private static final String TAG = "UsersApi";
	
	@Override
	public String login(String username, String password) {
    	Log.v(TAG, "login()");

    	final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair(PARAM_USERNAME, username));
    	params.add(new BasicNameValuePair(PARAM_PASSWORD, password));

    	//Creates an entity to send to the server
    	final HttpEntity entity;
    	try {
    		entity = new UrlEncodedFormEntity(params);
    	} catch (final UnsupportedEncodingException e) {
    		// this should never happen.
    		throw new IllegalStateException(e);
    	}

    	//Creates a post to send the entity with
    	final HttpPost post = new HttpPost(LOGIN_URI);
    	post.addHeader(entity.getContentType());
    	post.setEntity(entity);

    	//The server response
    	final HttpResponse resp;

    	try {
    		resp = NetworkUtilities.getHttpClient().execute(post);

    		HttpEntity respEntity = resp.getEntity();

    		InputStream inputStream = respEntity.getContent();
    		
    		try {
    			// json is UTF-8 by default
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
    			StringBuilder sb = new StringBuilder();

    			String line = null;
    			while ((line = reader.readLine()) != null)
    			{
    				sb.append(line + "\n");
    			}
    			String result = sb.toString(); 
    			
    			JSONObject obj = new JSONObject(result);
    			
    			String authToken = obj.getString("auth_token");
    			
    			if ((authToken != null) && (authToken.length() > 0)) {
        			Log.v(TAG, "Successful authentication");
        			Log.v(TAG, "authToken value: " + authToken);
        			return authToken;
        		} else {
        			Log.e(TAG, "Error authenticating" + resp.getStatusLine());
        			return null;
        		}    			
    		} 
    		finally {
    			Log.v(TAG, "getAuthtoken completing");
    			if(inputStream != null) 
    				inputStream.close(); 
    		}
    	} catch (ClientProtocolException e) {
    		Log.e(TAG, "ClientProtocolException when getting authtoken", e);
    		return null;
    	} catch (IOException e) {
    		Log.e(TAG, "IOException when getting authtoken", e);
    		return null;
    	}catch (JSONException e) {
    		Log.e(TAG, "JSONException when getting authtoken", e);
    		return null;
		}
    }

	@Override
	public boolean logout(String username, String authToken) {
		Log.v(TAG, "logout()");

    	final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair(PARAM_USERNAME, username));
    	params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authToken));
    	
    	//Creates an entity to send to the server
    	final HttpEntity entity;
    	try {
    		entity = new UrlEncodedFormEntity(params);
    	} catch (final UnsupportedEncodingException e) {
    		// this should never happen.
    		throw new IllegalStateException(e);
    	}
    	
    	//Creates a post to send the entity with
    	final HttpPost post = new HttpPost(LOGOUT_URI);
    	post.addHeader(entity.getContentType());
    	post.setEntity(entity);
    	
    	//The server response
    	final HttpResponse resp;
    	
    	try {
    		resp = NetworkUtilities.getHttpClient().execute(post);
    		if(resp.getStatusLine().getStatusCode() == 200) {
    			return true;
    		} else {
    			return false;
    		}
    	} catch(Exception e) {
    		Log.e(TAG, "Exception when logging out", e);
    		return false;
    	}

	}

	@Override
	public boolean addUser(String username, String password, String email) {

		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_USERNAME, username));
		params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
		params.add(new BasicNameValuePair(PARAM_EMAIL, email));

		//Creates an entity to send to the server
		final HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params);
		} catch (final UnsupportedEncodingException e) {
			// this should never happen.
			throw new IllegalStateException(e);
		}

		//Creates a post to send the entity with
		final HttpPost post = new HttpPost(ADD_USER_URI);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);

		//The server response
		final HttpResponse resp;

		try {
			resp = NetworkUtilities.getHttpClient().execute(post);
			if(resp.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			Log.e(TAG, "Exception when adding user", e);
			return false;
		}
	}

	@Override
	public boolean updateUser(String username, String password, String email) {
		
		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_USERNAME, username));
		params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
		params.add(new BasicNameValuePair(PARAM_EMAIL, email));

		//Creates an entity to send to the server
		final HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params);
		} catch (final UnsupportedEncodingException e) {
			// this should never happen.
			throw new IllegalStateException(e);
		}

		//Creates a post to send the entity with
		final HttpPost post = new HttpPost(UPDATE_USER_URI);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);

		//The server response
		final HttpResponse resp;

		try {
			resp = NetworkUtilities.getHttpClient().execute(post);
			if(resp.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			Log.e(TAG, "Exception when updating user", e);
			return false;
		}
	}

	@Override
	public boolean deleteUser(String username, String password) {
		
		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_USERNAME, username));
		params.add(new BasicNameValuePair(PARAM_PASSWORD, password));

		//Creates an entity to send to the server
		final HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params);
		} catch (final UnsupportedEncodingException e) {
			// this should never happen.
			throw new IllegalStateException(e);
		}

		//Creates a post to send the entity with
		final HttpPost post = new HttpPost(DELETE_USER_URI);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);

		//The server response
		final HttpResponse resp;

		try {
			resp = NetworkUtilities.getHttpClient().execute(post);
			if(resp.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			Log.e(TAG, "Exception when deleting user", e);
			return false;
		}
	}

}
