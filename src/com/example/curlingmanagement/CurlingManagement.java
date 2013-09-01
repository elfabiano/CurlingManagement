package com.example.curlingmanagement;

import android.app.Application;
import android.util.Log;

import com.example.curlingmanagement.authenticator.Session;
import com.example.curlingmanagement.controller.GameModuleController;
import com.example.curlingmanagement.controller.ManagerModuleController;

/**
 * Application class. Lives as long as the application is running. Can be reached via the
 * Context.getApplication() function. Initializes the module controllers.
 * 
 * @author Fabian
 *
 */
public class CurlingManagement extends Application {
	
	private static final String TAG = "CurlingManagement";
	
	public static Session session;
	
	public static Session getSession() {		
		return session;
	}

	public static void setSession(Session session) {
		Log.v(TAG, "setSession()");
		
		CurlingManagement.session = session;
	}

	/**
	 * Is only run once, when the app is started.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		initModules();
	}
	
	/**
	 * Initializes the module controllers.
	 */
	protected void initModules() {
		ManagerModuleController.initialize();
		GameModuleController.initialize();
	}
}
