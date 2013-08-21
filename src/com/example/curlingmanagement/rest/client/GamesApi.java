package com.example.curlingmanagement.rest.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.curlingmanagement.resources.model.Game;

public class GamesApi implements IGamesApi {
	
	public static final String BASE_URL = "http://curlingmanagement.com";
	
    public static final String GET_GAMES_URI = BASE_URL + "/gamesapi.php?request=getgames";
    
    public static final String ADD_GAME_URI = BASE_URL + "/gamesapi.php?request=addgame";
    
    public static final String UPDATE_GAME_URI = BASE_URL + "/gamesapi.php?request=updategame";
    
    public static final String DELETE_GAME_URI = BASE_URL + "gamesapi.php?request=deletegame";
	
	private static final String PARAM_USERNAME = "username";
	private static final String PARAM_AUTH_TOKEN = "auth_token";
	private static final String PARAM_ID = "id";
	private static final String PARAM_STATUS = "status";
	private static final String PARAM_HOME_SCORE = "home_score";
	private static final String PARAM_AWAY_SCORE = "away_score";
	private static final String PARAM_STONES_PLAYED = "stones_played";
	private static final String PARAM_HOME_USERNAME = "home_username";
	private static final String PARAM_AWAY_USERNAME = "away_username";

	private static final String TAG = "GamesApi";

	@Override
	public ArrayList<Game> getGames(String username, String status, String authToken) {
		Log.v(TAG, "getGames()");

		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_USERNAME, username));
		params.add(new BasicNameValuePair(PARAM_STATUS, status));
		params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authToken));

		//Creates an entity to send to the server
		final HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params);
		} catch (final UnsupportedEncodingException e) {
			// this should never happen.
			throw new IllegalStateException(e);
		}

		//Creates a post to send the entity with
		final HttpPost post = new HttpPost(GET_GAMES_URI);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);

		//The server response
		final HttpResponse resp;

		try {
			resp = NetworkUtilities.getHttpClient().execute(post);

			HttpEntity respEntity = resp.getEntity();

			InputStream inputStream = respEntity.getContent();   		

			try {
				// json is UTF-8 by default
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = reader.readLine()) != null)
				{
					sb.append(line + "\n");
				}
				String result = sb.toString(); 

				JSONArray array = new JSONArray(result);
				
				ArrayList<Game> games = new ArrayList<Game>();				

				for(int i = 0; i < array.length(); i++) {
					JSONObject row = array.getJSONObject(i);
					games.add(new Game(row.getInt("id"), 
							row.getString("status"),
							row.getInt("current_state_id"),
							row.getInt("previous_state_id"),
							row.getInt("home_score"),
							row.getInt("away_score"),
							row.getInt("stones_played"),
							row.getString("home_username"),
							row.getString("away_username"),
							row.getString("modified")));				
				}
				return games;
			} 
			finally {				
				if(inputStream != null) {
					inputStream.close(); 
				}				
			}
		} catch (ClientProtocolException e) {
    		Log.e(TAG, "ClientProtocolException when getting games", e);
    		return null;
    	} catch (IOException e) {
    		Log.e(TAG, "IOException when getting games", e);
    		return null;
    	}catch (JSONException e) {
    		Log.e(TAG, "JSONException when getting games", e);
    		return null;
		}
		}

	@Override
	public Game addGame(String authToken, Game game) {
		Log.v(TAG, "addGame()");
		
		return null;
	}

	@Override
	public Game updateGame(int id, String authToken, Game game) {
		Log.v(TAG, "updateGame()");
		
		return null;
	}

	@Override
	public boolean deleteGame(int id, String authToken) {
		Log.v(TAG, "deleteGame()");
		
		return false;
	}

}
