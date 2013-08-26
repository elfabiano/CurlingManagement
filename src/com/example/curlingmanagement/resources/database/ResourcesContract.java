package com.example.curlingmanagement.resources.database;

import android.provider.BaseColumns;

public final class ResourcesContract {

	public ResourcesContract() {}

	public static abstract class GamesTable implements BaseColumns {
		public static final String TABLE_NAME = "games";
		public static final String COLUMN_NAME_SERVER_ID = "server_id";
		public static final String COLUMN_NAME_STATUS = "status";
		public static final String COLUMN_NAME_WAITING_FOR = "waiting_for";
		public static final String COLUMN_NAME_CURRENT_STATE_ID = "current_state_id";
		public static final String COLUMN_NAME_PREVIOUS_STATE_ID = "previous_state_id";
		public static final String COLUMN_NAME_HOME_SCORE = "home_score";
		public static final String COLUMN_NAME_AWAY_SCORE = "away_score";
		public static final String COLUMN_NAME_STONES_PLAYED = "stones_played";
		public static final String COLUMN_NAME_HOME_USERNAME = "home_username";
		public static final String COLUMN_NAME_AWAY_USERNAME = "away_username";
		public static final String COLUMN_NAME_MODIFIED = "modified";
	}	
	
	public static final String SQL_CREATE_GAMES = 
			"CREATE TABLE " + GamesTable.TABLE_NAME + " (" +
			GamesTable._ID + " INTEGER PRIMARY KEY," +
			GamesTable.COLUMN_NAME_SERVER_ID + " INTEGER," +
			GamesTable.COLUMN_NAME_STATUS + " TEXT," +
			GamesTable.COLUMN_NAME_WAITING_FOR + " TEXT," +
			GamesTable.COLUMN_NAME_CURRENT_STATE_ID + " INTEGER," +
			GamesTable.COLUMN_NAME_PREVIOUS_STATE_ID + " INTEGER," +
			GamesTable.COLUMN_NAME_HOME_SCORE + " INTEGER," +
			GamesTable.COLUMN_NAME_AWAY_SCORE + " INTEGER," +
			GamesTable.COLUMN_NAME_STONES_PLAYED + " INTEGER," +
			GamesTable.COLUMN_NAME_HOME_USERNAME + " TEXT," +
			GamesTable.COLUMN_NAME_AWAY_USERNAME + " TEXT," +
			GamesTable.COLUMN_NAME_MODIFIED + " TEXT)";
	
	public static final String SQL_DELETE_GAMES = 
			"DROP TABLE IF EXISTS " + GamesTable.TABLE_NAME; 
}
