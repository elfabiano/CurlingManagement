package com.example.curlingmanagement.model;

import java.util.ArrayList;

/**
 * This class belongs to the ManagerModule. Communication with this class
 * from another module should go through the ManagerModule interface.
 * 
 * @author Fabian
 *
 */
public class Team {
	
	public ArrayList<Player> mPlayers;

	/**
	 * Default constructor
	 */
	public Team() {
	
	}
	
	/**
	 * 
	 * @return an arraylist of the players of the team
	 */
	public ArrayList<Player> getPlayers() {
		return mPlayers;
	}

	/**
	 * Assigns a new set of players to the team
	 * 
	 * @param players an arraylist of players
	 */
	public void setPlayers(ArrayList<Player> players) {
		mPlayers = players;
	}
}
