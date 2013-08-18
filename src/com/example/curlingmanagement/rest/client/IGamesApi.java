package com.example.curlingmanagement.rest.client;

import java.util.ArrayList;
import org.apache.http.NameValuePair;

import com.example.curlingmanagement.model.Game;

public interface IGamesApi {
	
	public static final String FIELD_STATUS = "status";	
	public static final String FIELD_HOME_SCORE = "home_score";	
	public static final String FIELD_AWAY_SCORE = "away_score";	
	public static final String FIELD_STONES_PLAYED = "stones_played";	
	public static final String PARAM_USERNAME = "username";
	public static final String FIELD_HOME_USERNAME = "home_username";	
	public static final String FIELD_AWAY_USERNAME = "away_username";
	
	public Game[] getPendingGames(String username, String authToken);
	
	public Game[] getOngoingGames(String username, String authToken);
	
	public Game[] getFinishedGames(String username, String authToken);
	
	public boolean addGame(String authToken, ArrayList<NameValuePair> fields);
	
	public boolean updateGame(int id, String authToken, ArrayList<NameValuePair> fieldsToChange);
	
	public boolean deleteGame(int id, String authToken);	
}
