package com.example.curlingmanagement.rest.client;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.example.curlingmanagement.model.Game;

public class GamesApi implements IGamesApi {

	@Override
	public Game[] getGames(String username, String authToken) {

		return null;
	}

	@Override
	public Game[] getPendingGames(String username, String authToken) {

		return null;
	}

	@Override
	public Game[] getOngoingGames(String username, String authToken) {

		return null;
	}

	@Override
	public Game[] getFinishedGames(String username, String authToken) {

		return null;
	}

	@Override
	public boolean addGame(String authToken, ArrayList<NameValuePair> fields) {

		return false;
	}

	@Override
	public boolean updateGame(int id, String authToken, 
			ArrayList<NameValuePair> fieldsToChange) {

		return false;
	}

	@Override
	public boolean deleteGame(int id, String authToken) {

		return false;
	}

}
