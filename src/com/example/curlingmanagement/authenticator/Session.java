package com.example.curlingmanagement.authenticator;

import java.sql.Timestamp;

import com.example.curlingmanagement.model.User;

/**
 * Handles information about the current session.
 * 
 * This class belongs to the UserInterface. Communication with this class
 * from another module should go through the UserInterface interface.
 * 
 * @author Fabian
 *
 */
public class Session {

	public User mUser;
	public Timestamp mStartTime;
	/** Used to authenticate the user against the server */
	public String mAuthToken;
	
	/**
	 * Constructor
	 * 
	 * @param usrInt A UserInterFace controller object.
	 * @param timestamp The time the session was created.
	 */
	public Session(String username, String authToken, Timestamp timestamp) {
		mUser = new User(username);
		mAuthToken = authToken;
		mStartTime = timestamp;		
	}
	
	/**
	 * 
	 * @return the user for the session.
	 */
	public User getUser() {
		return mUser;
	}
	
	/**
	 * Used to simplify coding until authentification functionality is implemented
	 * 
	 * @param name
	 */
	public void changeUsername(String name) {
		mUser.setUsername(name);
	}
	
}
