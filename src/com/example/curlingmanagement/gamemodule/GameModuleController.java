package com.example.curlingmanagement.gamemodule;

/**
 * The controller of the game module, implemented as a singelton class. 
 * Keeps a static instance of itself in order to be reachable from anywhere
 * in the program. 
 * 
 * @author Fabian
 *
 */
public class GameModuleController implements GameModule{

	private static GameModuleController mInstance;
	
	/**
	 * Default constructor
	 */
	private GameModuleController() {
	}
	
	/**
	 * Initializes the class
	 */
	public static void initialize() {
		if(mInstance == null) {
			mInstance = new GameModuleController();
		}
	}
	
	/**
	 * Used to get an instance of this class.
	 * 
	 * @return instance of the class
	 */
	public static GameModuleController getInstance() {
		return mInstance;
	}
}
