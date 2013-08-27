package com.example.curlingmanagement.view;

import java.util.ArrayList;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.curlingmanagement.resources.database.ResourcesDbHelper;
import com.example.curlingmanagement.resources.model.Game;

public class OngoingGamesLoader extends AsyncTaskLoader<ArrayList<Game>> {
	
	ArrayList<Game> mOngoingGames;

	public OngoingGamesLoader(Context context) {
		super(context);
	}

	@Override
	public ArrayList<Game> loadInBackground() {
		ArrayList<Game> data = new ArrayList<Game>();
		
		SQLiteDatabase db = new ResourcesDbHelper(getContext()).getReadableDatabase();
		
		String[] projection = {
			
		};
		return null;
	}

}
