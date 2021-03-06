package com.example.curlingmanagement.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.curlingmanagement.Constants;
import com.example.curlingmanagement.R;
import com.example.curlingmanagement.resources.model.Game;

/**
 * Controller class for the game menu page
 * 
 * @author Fabian
 *
 */
public class GameMenuActivity extends Activity {
	
	public static final String TAG = "GameMenuActivity";
	
	public Game mGame; 
	
	/**
	 * 
	 * @return m_game
	 */
	public Game getGame() {
		return mGame;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_menu);
		
		Log.i(TAG, "onCreate()");
		
		mGame = (Game) getIntent().getSerializableExtra(Constants.GAME);
		
		TextView homeUser = (TextView) findViewById(R.id.home_username);
		TextView awayUser = (TextView) findViewById(R.id.away_username);

		homeUser.setTextSize(16);
		awayUser.setTextSize(16);
		
		homeUser.setText(mGame.getHomeUsername());
		awayUser.setText(mGame.getAwayUsername());
		
		// Show the Up button in the action bar.
		//setupActionBar();
	}

	@SuppressWarnings("unused")
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    /** Called when the user clicks the "Play" button */
    public void goToGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        
        startActivity(intent);
    }
    
    public void resign(View view) {
    	Intent intent = new Intent(this, MainMenuActivity.class);
    	
    	//TODO: Resign game by changing status or deleting from database
    	
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	finish();
    }

}
