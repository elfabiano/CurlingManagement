package com.example.curlingmanagement;

import android.app.Application;

import com.example.curlingmanagement.gamemodule.GameModuleController;
import com.example.curlingmanagement.managermodule.ManagerModuleController;
import com.example.curlingmanagement.userinterface.UserInterfaceController;

/**
 * Application class. Lives as long as the application is running. Can be reached via the
 * Context.getApplication() function. Initializes the module controllers.
 * 
 * @author Fabian
 *
 */
public class CurlingManagement extends Application {
	
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