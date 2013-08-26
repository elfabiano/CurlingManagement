package com.example.curlingmanagement.rest.processor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.curlingmanagement.resources.database.ResourcesContract.GamesTable;
import com.example.curlingmanagement.resources.database.ResourcesDbHelper;
import com.example.curlingmanagement.resources.model.Game;
import com.example.curlingmanagement.rest.client.GamesApi;

public class GamesProcessor {
	private Context mContext;
	private ResourcesDbHelper mDbHelper;
	
	public GamesProcessor(Context context) {
		mContext = context;
		mDbHelper = new ResourcesDbHelper(mContext);
	}
	
	public boolean getGames(String username, String status, String authToken) {
		return false;
	}
	
	public boolean addGame(String username, String status, String waitingFor, String authToken) {
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
		return false;
	}
	
	public boolean deleteGame(int id, String authToken) {
		return new GamesApi().deleteGame(id, authToken);
	}
}
