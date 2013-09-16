package com.example.curlingmanagement.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.curlingmanagement.CurlingManagement;
import com.example.curlingmanagement.R;
import com.example.curlingmanagement.rest.service.GamesServiceHelper;

/**
 * Controller class for the new game page
 * 
 * @author Fabian
 *
 */
public class NewGameActivity extends Activity {
//	public final static String GAME = "com.example.curlingmanagement.NEW_GAME";
	public static final String TAG = "NewGameActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		// Show the Up button in the action bar.
		//setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
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
	
    /** 
     * Called when the user clicks the "Find random opponent" button
     */
	public void findRandomOpponent(View view) {

		GamesServiceHelper randomGameHelper = 
				new GamesServiceHelper(getApplicationContext(), MainMenuActivity.ACTION_CHANGE_GAMES);
		randomGameHelper.addGame(
				CurlingManagement.session.getUsername(), 
				"random", 
				null, 
				CurlingManagement.session.getAuthToken());
		
		Log.v(TAG, "authToken: " + CurlingManagement.session.getAuthToken());
		
		String toastMessage = "Searching for an opponent. A new game will be started shortly.";
		
		Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
		toast.show();
		
		Intent intent = new Intent(this, MainMenuActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	/**
	 * Called when user clicks the "Friends list" button
	 * 
	 * @param view the view that was clicked
	 */
	public void goToFriendsList(View view) {
		Intent intent = new Intent(this, FriendsListActivity.class);
		
		startActivity(intent);
	}
	
	/**
	 * Called when user clicks the "Search by user name" button
	 * 
	 * @param view the view that was clicked
	 */
	public void goToSearchOpponent(View view) {
		Intent intent = new Intent(this, SearchOpponentActivity.class);
		
		startActivity(intent);
	}
}
