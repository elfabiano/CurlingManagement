package com.example.curlingmanagement.rest.service;

import android.content.Context;
import android.os.Bundle;

import com.example.curlingmanagement.resources.model.Game;
import com.example.curlingmanagement.rest.processor.GamesProcessor;

public class GamesServiceProvider implements IServiceProvider {
	
	private Context mContext;
	
	public GamesServiceProvider(Context context) {
		mContext = context;
	}

	public static class Methods {
		public static final int GET_GAMES_METHOD = 1;
		public static final String GET_GAMES_PARAM_USERNAME = "username";
		public static final String GET_GAMES_PARAM_STATUS = "status";
		public static final String GET_GAMES_PARAM_AUTH_TOKEN = "authToken";
						
		public static final int ADD_GAME_METHOD = 2;
		public static final String ADD_GAME_PARAM_STATUS = "status";
		public static final String ADD_GAME_PARAM_WAITING_FOR = "waitingFor";
		public static final String ADD_GAME_PARAM_USERNAME = "username";
		public static final String ADD_GAME_PARAM_AUTH_TOKEN = "authToken";
				
		public static final int UPDATE_GAME_METHOD = 3;
		public static final String UPDATE_GAME_PARAM_GAME = "game";
		public static final String UPDATE_GAME_PARAM_AUTH_TOKEN = "authToken";
		
		public static final int DELETE_GAME_METHOD = 4;
		public static final String DELETE_GAME_PARAM_ID = "id";
		public static final String DELETE_GAME_PARAM_AUTH_TOKEN = "authToken";
	}
	
	@Override
	public boolean RunTask(int methodId, Bundle extras) {
		switch(methodId) {
		case Methods.GET_GAMES_METHOD:
			return getGames(extras);
		case Methods.ADD_GAME_METHOD:
			return addGame(extras);
		case Methods.UPDATE_GAME_METHOD:
			return updateGame(extras);
		case Methods.DELETE_GAME_METHOD:
			return deleteGame(extras);
		}
		return false;
	}

	private boolean getGames(Bundle extras) {
		String username = extras.getString(Methods.GET_GAMES_PARAM_USERNAME);
		String status = extras.getString(Methods.GET_GAMES_PARAM_STATUS);
		String authToken = extras.getString(Methods.GET_GAMES_PARAM_AUTH_TOKEN);
		return new GamesProcessor(mContext).getGames(username, status, authToken);
	}
	
	private boolean addGame(Bundle extras) {
		String status = extras.getString(Methods.ADD_GAME_PARAM_STATUS);
		String waitingFor = extras.getString(Methods.ADD_GAME_PARAM_WAITING_FOR);
		String username = extras.getString(Methods.ADD_GAME_PARAM_USERNAME);
		String authToken = extras.getString(Methods.ADD_GAME_PARAM_AUTH_TOKEN);
		return new GamesProcessor(mContext).addGame(status, waitingFor, username, authToken);
	}
	
	private boolean updateGame(Bundle extras) {
		Game game = (Game)extras.getSerializable(Methods.UPDATE_GAME_PARAM_GAME);
		String authToken = extras.getString(Methods.UPDATE_GAME_PARAM_AUTH_TOKEN);
		return new GamesProcessor(mContext).updateGame(game, authToken);
	}
	
	private boolean deleteGame(Bundle extras) {
		int id = extras.getInt(Methods.DELETE_GAME_PARAM_ID);
		String authToken = extras.getString(Methods.DELETE_GAME_PARAM_AUTH_TOKEN);
		return new GamesProcessor(mContext).deleteGame(id, authToken);
	}
}
