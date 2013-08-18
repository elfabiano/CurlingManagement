package com.example.curlingmanagement.controller;

import com.example.curlingmanagement.model.Team;

/**
 * The controller of the manager module, implemented as a singelton class. 
 * Keeps a static instance of itself in order to be reachable from anywhere
 * in the program.
 * 
 * @author Fabian
 *
 */
public class ManagerModuleController implements IManagerModule {
	
	private static ManagerModuleController m_instance;
	
	public Team mCurrentTeam;
	
	/**
	 * Default constructor
	 */
	private ManagerModuleController() {
	}

	/**
	 * Initializes the class
	 */
	public static void initialize() {
		if(m_instance == null) {
			m_instance = new ManagerModuleController();
		}
	}
	
	/**
	 * Used to get an instance of this class.
	 * 
	 * @return instance of the class
	 */
	public static ManagerModuleController getInstance() {
		return m_instance;
	}

//	/**
//	 * @return Team
//	 */
//	@Override
//	public Team getCurrentTeam() {
//		return m_currentTeam;
//	}

}
