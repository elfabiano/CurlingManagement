package com.example.curlingmanagement;

import android.app.Application;

import com.example.curlingmanagement.authenticator.Session;
import com.example.curlingmanagement.controller.GameModuleController;
import com.example.curlingmanagement.controller.ManagerModuleController;
import com.example.curlingmanagement.controller.UserInterfaceController;

/**
 * Application class. Lives as long as the application is running. Can be reached via the
 * Context.getApplication() function. Initializes the module controllers.
 * 
 * @author Fabian
 *
 */
public class CurlingManagement extends Application {
	
	public static Session session;
	
	public static Session getSession() {
		return session;
	}

	public static void setSession(Session session) {
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
		UserInterfaceController.initialize();
		ManagerModuleController.initialize();
		GameModuleController.initialize();
	}
}
