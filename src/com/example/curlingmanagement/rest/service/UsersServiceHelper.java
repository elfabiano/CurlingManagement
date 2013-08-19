package com.example.curlingmanagement.rest.service;

import android.content.Context;
import android.os.Bundle;

public class UsersServiceHelper extends ServiceHelperBase{
	
	public UsersServiceHelper(Context context, String resultAction) {
		super(context, ProcessorService.Providers.USERS_PROVIDER, resultAction);
	}
	
	public void login(String username, String password) {
		Bundle extras = new Bundle();
		extras.putString(UsersServiceProvider.Methods.LOGIN_PARAM_USERNAME, username);
		extras.putString(UsersServiceProvider.Methods.LOGIN_PARAM_PASSWORD, password);
		RunMethod(UsersServiceProvider.Methods.LOGIN_METHOD, extras);
	}
	
	public void logout(String username, String auth_token) {
		Bundle extras = new Bundle();
		extras.putString(UsersServiceProvider.Methods.LOGOUT_PARAM_USERNAME, username);
		extras.putString(UsersServiceProvider.Methods.LOGOUT_PARAM_AUTH_TOKEN, auth_token);
		RunMethod(UsersServiceProvider.Methods.LOGOUT_METHOD, extras);
	}
	
	public void signup(String username, String password, String email) {
		Bundle extras = new Bundle();
		extras.putString(UsersServiceProvider.Methods.ADD_USER_PARAM_USERNAME, username);
		extras.putString(UsersServiceProvider.Methods.ADD_USER_PARAM_PASSWORD, password);
		extras.putString(UsersServiceProvider.Methods.ADD_USER_PARAM_EMAIL, email);
		RunMethod(UsersServiceProvider.Methods.ADD_USER_METHOD);
	}
	
	public void updateUser(String username, String password, String email) {
		Bundle extras = new Bundle();
		extras.putString(UsersServiceProvider.Methods.UPDATE_USER_PARAM_USERNAME, username);
		extras.putString(UsersServiceProvider.Methods.UPDATE_USER_PARAM_PASSWORD, password);
		extras.putString(UsersServiceProvider.Methods.UPDATE_USER_PARAM_EMAIL, email);
		RunMethod(UsersServiceProvider.Methods.UPDATE_USER_METHOD);
	}
	
	public void deleteUser(String username, String password) {
		Bundle extras = new Bundle();
		extras.putString(UsersServiceProvider.Methods.DELETE_USER_PARAM_USERNAME, username);
		extras.putString(UsersServiceProvider.Methods.DELETE_USER_PARAM_PASSWORD, password);
		RunMethod(UsersServiceProvider.Methods.DELETE_USER_METHOD, extras);
	}
}
