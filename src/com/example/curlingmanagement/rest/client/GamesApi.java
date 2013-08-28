package com.example.curlingmanagement.rest.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.curlingmanagement.resources.model.Game;

public class GamesApi implements IGamesApi {
	
	private static final String TAG = "GamesApi";
	
	public static final String BASE_URL = "http://curlingmanagement.com";
	
    public static final String GET_GAMES_URI = BASE_URL + "/gamesapi.php?request=getgames";
    
    public static final String ADD_GAME_URI = BASE_URL + "/gamesapi.php?request=addgame";
    
    public static final String UPDATE_GAME_URI = BASE_URL + "/gamesapi.php?request=updategame";
    
    public static final String DELETE_GAME_URI = BASE_URL + "gamesapi.php?request=deletegame";
	
	private static final String PARAM_USERNAME = "username";
	private static final String PARAM_AUTH_TOKEN = "auth_token";
	private static final String PARAM_ID = "id";
	private static final String PARAM_STATUS = "status";
	private static final String PARAM_WAITING_FOR = "waiting_for";
	private static final String PARAM_CURRENT_STATE_ID = "current_state_id";
	private static final String PARAM_PREVIOUS_STATE_ID = "previous_state_id";
	private static final String PARAM_HOME_SCORE = "home_score";
	private static final String PARAM_AWAY_SCORE = "away_score";
	private static final String PARAM_STONES_PLAYED = "stones_played";
	private static final String PARAM_HOME_USERNAME = "home_username";
	private static final String PARAM_AWAY_USERNAME = "away_username";
	private static final String PARAM_MODIFIED = "modified";

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
					Game game = new Game();
					if(!row.isNull(PARAM_ID)) {
						game.setServerId(row.getInt(PARAM_ID));
					}
					if(!row.isNull(PARAM_STATUS)) {
						game.setStatus(row.getString(PARAM_STATUS));
					}
					if(!row.isNull(PARAM_WAITING_FOR)) {
						game.setWaitingFor(row.getString(PARAM_WAITING_FOR));
					}
					if(!row.isNull(PARAM_CURRENT_STATE_ID)) {
						game.setCurrentStateId(row.getInt(PARAM_CURRENT_STATE_ID));
					}
					if(!row.isNull(PARAM_PREVIOUS_STATE_ID)) {
						game.setPreviousStateId(row.getInt(PARAM_PREVIOUS_STATE_ID));
					}
					if(!row.isNull(PARAM_HOME_SCORE)) {
						game.setHomeScore(row.getInt(PARAM_HOME_SCORE));
					}
					if(!row.isNull(PARAM_AWAY_SCORE)) {
						game.setAwayScore(row.getInt(PARAM_AWAY_SCORE));
					}
					if(!row.isNull(PARAM_STONES_PLAYED)) {
						game.setStonesPlayed(row.getInt(PARAM_STONES_PLAYED));
					}
					if(!row.isNull(PARAM_HOME_USERNAME)) {
						game.setHomeUsername(row.getString(PARAM_HOME_USERNAME));
					}
					if(!row.isNull(PARAM_AWAY_USERNAME)) {
						game.setAwayUsername(row.getString(PARAM_AWAY_USERNAME));
					}
					if(!row.isNull(PARAM_MODIFIED)) {
						game.setModified(row.getString(PARAM_MODIFIED));
					}
					games.add(game);
				}
				Log.v(TAG, "getGames() returning " + String.valueOf(games.size()) + " games");
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
	public Game addGame(String username, String status, String waitingFor, String authToken) {
		Log.v(TAG, "addGame()");

		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_STATUS, status));
		params.add(new BasicNameValuePair(PARAM_WAITING_FOR, waitingFor));		
		params.add(new BasicNameValuePair(PARAM_USERNAME, username));
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
		final HttpPost post = new HttpPost(ADD_GAME_URI);
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

				JSONObject obj = new JSONObject(result);

				Game game = (new Game(obj.getInt("id"), 
						obj.getString("status"),
						obj.getString("waiting_for"),
						obj.getInt("current_state_id"),
						obj.getInt("previous_state_id"),
						obj.getInt("home_score"),
						obj.getInt("away_score"),
						obj.getInt("stones_played"),
						obj.getString("home_username"),
						obj.getString("away_username"),
						obj.getString("modified")));				
				
				return game;
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
	public Game updateGame(Game game, String authToken) {
		Log.v(TAG, "updateGame()");

		JSONObject obj = new JSONObject();

		try {
			obj.put(PARAM_AUTH_TOKEN, authToken);
			obj.put(PARAM_ID, game.getServerId());
			obj.put(PARAM_STATUS, game.getStatus());
			obj.put(PARAM_WAITING_FOR, game.getWaitingFor());
			obj.put(PARAM_HOME_SCORE, game.getHomeScore());
			obj.put(PARAM_AWAY_SCORE, game.getAwayScore());
			obj.put(PARAM_STONES_PLAYED, game.getStonesPlayed());
			obj.put(PARAM_HOME_USERNAME, game.getHomeUsername());
			obj.put(PARAM_AWAY_USERNAME, game.getAwayUsername());
		}
		catch (JSONException e) {
			Log.e(TAG, "JSONException when updating game", e);
			return null;
		}

		//Creates an entity to send to the server
		final HttpEntity entity;

		try {
			entity = new ByteArrayEntity(obj.toString().getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}	

		//Creates a post to send the entity with
		final HttpPost post = new HttpPost(UPDATE_GAME_URI);
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

				JSONObject json = new JSONObject(result);

				game.setModified(json.getString("modified"));

				return game;
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
	public boolean deleteGame(int id, String authToken) {
		Log.v(TAG, "deleteGame()");
		
		JSONObject obj = new JSONObject();

		try {
			obj.put(PARAM_ID, id);
			obj.put(PARAM_AUTH_TOKEN, authToken);
		}
		catch (JSONException e) {
			Log.e(TAG, "JSONException when deleting game");
			return false;
		}

		//Creates an entity to send to the server
		final HttpEntity entity;

		try {
			entity = new ByteArrayEntity(obj.toString().getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}	

		//Creates a post to send the entity with
		final HttpPost post = new HttpPost(DELETE_GAME_URI);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);

		//The server response
		final HttpResponse resp;

		try {
			resp = NetworkUtilities.getHttpClient().execute(post);
			if(resp.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			Log.e(TAG, "Exception when deleting user", e);
			return false;
		}
	}
}
