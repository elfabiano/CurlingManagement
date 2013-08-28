package com.example.curlingmanagement.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.curlingmanagement.view.MainMenuActivity;

public class GameIntentReceiver extends BroadcastReceiver {
	
	private final OngoingGamesLoader mLoader;
	
	public GameIntentReceiver (OngoingGamesLoader loader) {
		mLoader = loader;
		IntentFilter filter = new IntentFilter(MainMenuActivity.ACTION_CHANGE_GAMES);
		mLoader.getContext().registerReceiver(this, filter);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		mLoader.onContentChanged();
	}

}
