package com.example.curlingmanagement.authenticator;

import com.example.curlingmanagement.CurlingManagement;

public class Session {
	private String mUsername;
	private String mAuthToken;
	
	public Session(String username, String authToken) {
		mUsername = username;
		mAuthToken = authToken;
		CurlingManagement.setSession(this);
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		mUsername = username;
	}

	public String getAuthToken() {
		return mAuthToken;
	}

	public void setAuthToken(String authToken) {
		mAuthToken = authToken;
	}	
}
