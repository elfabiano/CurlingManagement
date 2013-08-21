package com.example.curlingmanagement.rest.service;

import android.content.Context;
import android.os.Bundle;

import com.example.curlingmanagement.resources.model.Game;

public class GamesServiceHelper extends ServiceHelperBase {

	public GamesServiceHelper(Context context, String resultAction) {
		super(context, ProcessorService.Providers.GAMES_PROVIDER, resultAction);
	}
	
	public void getGames(String username, String status, String authToken) {
		Bundle extras = new Bundle();
		extras.putString(GamesServiceProvider.Methods.GET_GAMES_PARAM_USERNAME, username);
		extras.putString(GamesServiceProvider.Methods.GET_GAMES_PARAM_STATUS, status);
		extras.putString(GamesServiceProvider.Methods.GET_GAMES_PARAM_AUTH_TOKEN, authToken);
		RunMethod(GamesServiceProvider.Methods.GET_GAMES_METHOD, extras);
	}
	
	public void addGame(String username, String status, String authToken) {
		Bundle extras = new Bundle();
		extras.putString(GamesServiceProvider.Methods.ADD_GAME_PARAM_USERNAME, username);
		extras.putString(GamesServiceProvider.Methods.ADD_GAME_PARAM_STATUS, status);
		extras.putString(GamesServiceProvider.Methods.ADD_GAME_PARAM_AUTH_TOKEN, authToken);
		RunMethod(GamesServiceProvider.Methods.ADD_GAME_METHOD, extras);
	}
	
	public void updateGame(Game game, String authToken) {
		Bundle extras = new Bundle();
		extras.putSerializable(GamesServiceProvider.Methods.UPDATE_GAME_PARAM_GAME, game);
		extras.putString(GamesServiceProvider.Methods.UPDATE_GAME_PARAM_AUTH_TOKEN, authToken);
		RunMethod(GamesServiceProvider.Methods.UPDATE_GAME_METHOD, extras);
	}
	
	public void deleteGame(int id, String authToken) {
		Bundle extras = new Bundle();
		extras.putInt(GamesServiceProvider.Methods.DELETE_GAME_PARAM_ID, id);
		extras.putString(GamesServiceProvider.Methods.DELETE_GAME_PARAM_AUTH_TOKEN, authToken);
		RunMethod(GamesServiceProvider.Methods.DELETE_GAME_METHOD, extras);
	}
}
