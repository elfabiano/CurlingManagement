package com.example.curlingmanagement.rest.client;

import java.util.ArrayList;

import com.example.curlingmanagement.resources.model.Game;

public interface IGamesApi {
	
	public ArrayList<Game> getGames(String username, String status, String authToken);
	
	public Game addGame(String status, String username, String authToken);
	
	public Game updateGame(Game game, String authToken);
	
	public boolean deleteGame(int id, String authToken);	
}
