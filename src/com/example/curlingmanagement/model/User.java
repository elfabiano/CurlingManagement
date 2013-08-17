package com.example.curlingmanagement.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * The user of the app
 * 
 * This class belongs to the UserInterface. Communication with this class
 * from another module should go through the UserInterface interface.
 * 
 * @author Fabian
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String mUsername;
	public ArrayList<Game> mOngoingGames;
	
	/**
	 * Constructor
	 * 
	 * @param username
	 */
	public User(String username) {		
		mUsername = username;
		mOngoingGames = new ArrayList<Game>();
	}

	/**
	 * 
	 * @return the name of the user
	 */
	public String getUsername() {
		return mUsername;
	}

	/**
	 * Sets the username
	 *  
	 * @param m_username
	 */
	public void setUsername(String m_username) {
		this.mUsername = m_username;
	}

	/**
	 * 
	 * @return a list of ongoing games
	 */
	public ArrayList<Game> getOngoingGames() {
		return mOngoingGames;
	}

	/**
	 * Sets the list of ongoing games.
	 * NOTE: This will erase the previous list. Use addOngoingGame
	 * to add a game to the list.
	 * 
	 * @param m_ongoingGames
	 */
	public void setOngoingGames(ArrayList<Game> m_ongoingGames) {
		this.mOngoingGames = m_ongoingGames;
	}
	
	/**
	 * Adds a game to the list of ongoing games
	 * 
	 * @param ongoingGame
	 */
	public void addOngoingGame(Game ongoingGame) {
		mOngoingGames.add(ongoingGame);
	}
	
	/**
	 * Removes a game from the list of ongoing games
	 * 
	 * @param ongoingGame
	 */
	public void removeOngoingGame(Game ongoingGame) {
		mOngoingGames.remove(ongoingGame);
	}

}
