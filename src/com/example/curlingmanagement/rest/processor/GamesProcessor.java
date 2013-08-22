package com.example.curlingmanagement.rest.processor;

import com.example.curlingmanagement.resources.model.Game;
import com.example.curlingmanagement.rest.client.GamesApi;

import android.content.Context;

public class GamesProcessor {
private Context mContext;
	
	public GamesProcessor(Context context) {
		mContext = context;
	}
	
	public boolean getGames(String username, String status, String authToken) {
		return false;
	}
	
	public boolean addGame(String username, String status, String waitingFor, String authToken) {
		return false;
	}
	
	public boolean updateGame(Game game, String authToken) {
		return false;
	}
	
	public boolean deleteGame(int id, String authToken) {
		return new GamesApi().deleteGame(id, authToken);
	}
}
