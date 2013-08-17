package com.example.curlingmanagement.userinterface;

import java.util.ArrayList;

import com.example.curlingmanagement.R;

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
			holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
			
			row.setTag(holder);
		}
		else
		{
			holder = (GameHolder)row.getTag();
		}
		
		Game game = mOngoingGames.get(position);
		holder.txtTitle.setText(game.getOpponent().getUsername());
		
		return row;
	}
	
	static class GameHolder {
		TextView txtTitle; 
	}
	
}
