package com.example.curlingmanagement.rest.processor;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.curlingmanagement.resources.database.ResourcesContract.GamesTable;
import com.example.curlingmanagement.resources.database.ResourcesDbHelper;
import com.example.curlingmanagement.resources.model.Game;
import com.example.curlingmanagement.rest.client.GamesApi;

public class GamesProcessor {
	
	private static final String TAG = "GamesProcessor";
	private Context mContext;
	private ResourcesDbHelper mDbHelper;
	
	public GamesProcessor(Context context) {
		mContext = context;
		mDbHelper = new ResourcesDbHelper(mContext);
	}
	
	public boolean getGames(String username, String status, String authToken) {
		Log.v(TAG, "getGames");
		
		ArrayList<Game> games = new GamesApi().getGames(username, status, authToken);
		
		if(games == null || games.size() == 0) {
			return false;
		}
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count = db.delete(GamesTable.TABLE_NAME, "1", null);
		
		Log.v(TAG, "getGames(), deleted " + count + " rows");
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		
		for(Game game : games) {
			values.clear();
			values.put(GamesTable.COLUMN_NAME_SERVER_ID, game.getServerId());
			values.put(GamesTable.COLUMN_NAME_STATUS, game.getStatus());
			values.put(GamesTable.COLUMN_NAME_WAITING_FOR, game.getWaitingFor());
			values.put(GamesTable.COLUMN_NAME_CURRENT_STATE_ID, game.getCurrentStateId());
			values.put(GamesTable.COLUMN_NAME_PREVIOUS_STATE_ID, game.getPreviousStateId());
			values.put(GamesTable.COLUMN_NAME_HOME_SCORE, game.getHomeScore());
			values.put(GamesTable.COLUMN_NAME_AWAY_SCORE, game.getAwayScore());
			values.put(GamesTable.COLUMN_NAME_STONES_PLAYED, game.getStonesPlayed());
			values.put(GamesTable.COLUMN_NAME_HOME_USERNAME, game.getHomeUsername());
			values.put(GamesTable.COLUMN_NAME_AWAY_USERNAME, game.getAwayUsername());
			values.put(GamesTable.COLUMN_NAME_MODIFIED, game.getModified());
			
			if(db.insert(GamesTable.TABLE_NAME, null, values) == -1) {
				return false;
			}
		}
		return true;
	}
	
	public boolean addGame(String username, String status, String waitingFor, String authToken) {
		Log.v(TAG, "addGame()");
		
		Game game = new GamesApi().addGame(username, status, waitingFor, authToken);
		
		if(game == null) {
			return false;
		}
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(GamesTable.COLUMN_NAME_SERVER_ID, game.getServerId());
		values.put(GamesTable.COLUMN_NAME_STATUS, game.getStatus());
		values.put(GamesTable.COLUMN_NAME_WAITING_FOR, game.getWaitingFor());
		values.put(GamesTable.COLUMN_NAME_CURRENT_STATE_ID, game.getCurrentStateId());
		values.put(GamesTable.COLUMN_NAME_PREVIOUS_STATE_ID, game.getPreviousStateId());
		values.put(GamesTable.COLUMN_NAME_HOME_SCORE, game.getHomeScore());
		values.put(GamesTable.COLUMN_NAME_AWAY_SCORE, game.getAwayScore());
		values.put(GamesTable.COLUMN_NAME_STONES_PLAYED, game.getStonesPlayed());
		values.put(GamesTable.COLUMN_NAME_HOME_USERNAME, game.getHomeUsername());
		values.put(GamesTable.COLUMN_NAME_AWAY_USERNAME, game.getAwayUsername());
		values.put(GamesTable.COLUMN_NAME_MODIFIED, game.getModified());
		
		if(db.insert(GamesTable.TABLE_NAME, null, values) == -1) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean updateGame(Game game, String authToken) {
		Log.v(TAG, "updateGame()");
		
		Game updatedGame = new GamesApi().updateGame(game, authToken);
		
		if (updatedGame == null) {
			return false;
		}
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(GamesTable.COLUMN_NAME_STATUS, updatedGame.getStatus());
		values.put(GamesTable.COLUMN_NAME_WAITING_FOR, updatedGame.getWaitingFor());
		values.put(GamesTable.COLUMN_NAME_CURRENT_STATE_ID, updatedGame.getCurrentStateId());
		values.put(GamesTable.COLUMN_NAME_PREVIOUS_STATE_ID, updatedGame.getPreviousStateId());
		values.put(GamesTable.COLUMN_NAME_HOME_SCORE, updatedGame.getHomeScore());
		values.put(GamesTable.COLUMN_NAME_AWAY_SCORE, updatedGame.getAwayScore());
		values.put(GamesTable.COLUMN_NAME_STONES_PLAYED, updatedGame.getStonesPlayed());
		values.put(GamesTable.COLUMN_NAME_HOME_USERNAME, updatedGame.getHomeUsername());
		values.put(GamesTable.COLUMN_NAME_AWAY_USERNAME, updatedGame.getAwayUsername());
		values.put(GamesTable.COLUMN_NAME_MODIFIED, updatedGame.getModified());
		
		String selection = GamesTable.COLUMN_NAME_SERVER_ID + " LIKE ?";
		String[] selectionArgs = {String.valueOf(updatedGame.getServerId())};
		
		if(db.update(GamesTable.TABLE_NAME, values, selection, selectionArgs) > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteGame(int id, String authToken) {	
		Log.v(TAG, "deleteGame()");
		
		if(new GamesApi().deleteGame(id, authToken) == true) {
			SQLiteDatabase db = mDbHelper.getWritableDatabase();

			String selection = GamesTable.COLUMN_NAME_SERVER_ID + " LIKE ?";
			String[] selectionArgs = {String.valueOf(id)};
			
			if(db.delete(GamesTable.TABLE_NAME, selection, selectionArgs) > 0) {
				return true;
			}
		}	
		return false;
	}
}
