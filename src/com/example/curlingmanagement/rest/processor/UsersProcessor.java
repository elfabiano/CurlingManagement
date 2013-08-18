package com.example.curlingmanagement.rest.processor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.curlingmanagement.Constants;
import com.example.curlingmanagement.rest.client.UsersApi;

public class UsersProcessor {
	private Context mContext;
	
	public UsersProcessor(Context context) {
		mContext = context;
	}
	
	public boolean login(String username, String password) {
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
	
	public boolean logout(String username, String auth_token) {
		return false;
	}
	
	public boolean addUser(String username, String password, String email) {
		return false;
	}
	
}
