package com.example.curlingmanagement.controller;

import java.util.ArrayList;

import com.example.curlingmanagement.R;
import com.example.curlingmanagement.resources.model.Game;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Custom adapter class for a list of ongoing games
 * 
 * @author Fabian
 *
 */
public class OngoingGamesAdapter extends ArrayAdapter<Game> {

	public Context mContext;
	public int mLayoutResourceId;
	ArrayList<Game> mOngoingGames;
	
	public OngoingGamesAdapter(Context context, int layoutResourceId,
			ArrayList<Game> ongoingGames) {
		super(context, layoutResourceId, ongoingGames);
		mContext = context;
		mLayoutResourceId = layoutResourceId;
		mOngoingGames = ongoingGames;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		GameHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
			
			holder = new GameHolder();
			holder.txtHomeUsername = (TextView)row.findViewById(R.id.txt_home_username);
			holder.txtAwayUsername = (TextView)row.findViewById(R.id.txt_away_username);
			
			row.setTag(holder);
		}
		else
		{
			holder = (GameHolder)row.getTag();
		}
		
		Game game = mOngoingGames.get(position);
		holder.txtHomeUsername.setText(game.getHomeUsername());
		holder.txtAwayUsername.setText(game.getAwayUsername());
		
		return row;
	}
	
	static class GameHolder {
		TextView txtHomeUsername; 
		TextView txtAwayUsername;
	}
	
}
