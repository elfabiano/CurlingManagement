package com.example.curlingmanagement.rest.service;

import android.content.Context;
import android.os.Bundle;

import com.example.curlingmanagement.rest.processor.UsersProcessor;

public class UsersServiceProvider implements IServiceProvider {
	
	private final Context mContext;
	
	public UsersServiceProvider(Context context) {
		mContext = context;
	}
	
	public static class Methods {
		public static final int LOGIN_METHOD = 1;
		public static final String LOGIN_PARAM_USERNAME = "username";
		public static final String LOGIN_PARAM_PASSWORD = "password";
				
		public static final int LOGOUT_METHOD = 2;
		public static final String LOGOUT_PARAM_USERNAME = "username";
		public static final String LOGOUT_PARAM_AUTH_TOKEN = "authToken";
		
		public static final int ADD_USER_METHOD = 3;
		public static final String ADD_USER_PARAM_USERNAME = "username";
		public static final String ADD_USER_PARAM_PASSWORD = "password";
		public static final String ADD_USER_PARAM_EMAIL = "email";
		
		public static final int UPDATE_USER_METHOD = 4;
		public static final String UPDATE_USER_PARAM_USERNAME = "username";
		public static final String UPDATE_USER_PARAM_PASSWORD = "password";
		public static final String UPDATE_USER_PARAM_EMAIL = "email";
		
		public static final int DELETE_USER_METHOD = 5;
		public static final String DELETE_USER_PARAM_USERNAME = "username";
		public static final String DELETE_USER_PARAM_PASSWORD = "password";
	}

	@Override
	public boolean RunTask(int methodId, Bundle extras) {
		switch(methodId) {
		case Methods.LOGIN_METHOD:
			return login(extras);
		case Methods.LOGOUT_METHOD:
			return logout(extras);
		case Methods.ADD_USER_METHOD:
			return addUser(extras);
		case Methods.UPDATE_USER_METHOD:
			return updateUser(extras);
		case Methods.DELETE_USER_METHOD:
			return deleteUser(extras);
		}
		return false;
	}

	private boolean login(Bundle extras) {
		String username = extras.getString(Methods.LOGIN_PARAM_USERNAME);
		String auth_token = extras.getString(Methods.LOGIN_PARAM_PASSWORD);
		return new UsersProcessor(mContext).login(username, auth_token);
	}
	
	private boolean logout(Bundle extras) {
		String username = extras.getString(Methods.LOGOUT_PARAM_USERNAME);
		String authToken = extras.getString(Methods.LOGOUT_PARAM_AUTH_TOKEN);
		return new UsersProcessor(mContext).logout(username, authToken);
	}
	
	private boolean addUser(Bundle extras) {
		String username = extras.getString(Methods.ADD_USER_PARAM_USERNAME);
		String password = extras.getString(Methods.ADD_USER_PARAM_PASSWORD);
		String email = extras.getString(Methods.ADD_USER_PARAM_EMAIL);
		return new UsersProcessor(mContext).addUser(username, password, email);
	}
	
	private boolean updateUser(Bundle extras) {
		String username = extras.getString(Methods.UPDATE_USER_PARAM_USERNAME);
		String password = extras.getString(Methods.UPDATE_USER_PARAM_PASSWORD);
		String email = extras.getString(Methods.UPDATE_USER_PARAM_EMAIL);
		return new UsersProcessor(mContext).updateUser(username, password, email);
	}
	
	private boolean deleteUser(Bundle extras) {
		String username = extras.getString(Methods.DELETE_USER_PARAM_USERNAME);
		String password = extras.getString(Methods.DELETE_USER_PARAM_PASSWORD);
		return new UsersProcessor(mContext).deleteUser(username, password);
	}
}
