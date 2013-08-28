package com.example.curlingmanagement.rest.processor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.example.curlingmanagement.Constants;
import com.example.curlingmanagement.rest.client.UsersApi;

public class UsersProcessor {
	
	private static final String TAG = "UsersProcessor";
	
	private Context mContext;
	
	public UsersProcessor(Context context) {
		mContext = context;
	}
	
	public boolean login(String username, String password) {
		Log.v(TAG, "login()");
		
		boolean success = false;
		String authToken = new UsersApi().login(username, password);
		if(authToken != null && authToken.length() > 0) {
			success = true;
		}
		
		SharedPreferences accountPrefs = mContext.getApplicationContext().
				getSharedPreferences(Constants.ACCOUNT_PREFS_NAME, 0);
		Editor editor = accountPrefs.edit();
		editor.clear();
        editor.putString(Constants.PREFS_KEY_USERNAME, username);
        editor.putString(Constants.PREFS_KEY_AUTH_TOKEN, authToken);
        editor.commit();
		
		return success;
	}
	
	public boolean logout(String username, String authToken) {
		Log.v(TAG, "logout()");
		
		boolean success = new UsersApi().logout(username, authToken);
		if(success) {
			SharedPreferences accountPrefs = mContext.getApplicationContext().
					getSharedPreferences(Constants.ACCOUNT_PREFS_NAME, 0);
			Editor editor = accountPrefs.edit();
			editor.clear();
			editor.commit();
		}
		
		return success;
	}
	
	public boolean addUser(String username, String password, String email) {
		Log.v(TAG, "addUser()");
		
		return new UsersApi().addUser(username, password, email);
	}
	
	public boolean updateUser(String username, String password, String email) {
		Log.v(TAG, "updateUser()");
		
		return new UsersApi().addUser(username, password, email);
	}
	
	public boolean deleteUser(String username, String password) {
		Log.v(TAG, "deleteUser()");
		
		return new UsersApi().deleteUser(username, password);
	}
	
}
