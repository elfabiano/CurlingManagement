package com.example.curlingmanagement.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.curlingmanagement.Constants;
import com.example.curlingmanagement.R;
import com.example.curlingmanagement.authenticator.AuthenticatorActivity;
import com.example.curlingmanagement.controller.UserInterfaceController;
import com.example.curlingmanagement.model.Game;

/**
 * Controller class for the main menu. As of now the main activity.
 * 
 * @author Fabian
 *
 */
public class MainMenuActivity extends Activity {
	
	public static final String TAG = "MainMenuActivity";
	
	public static final int REQUEST_AUTHENTICATE = 1;

	private SharedPreferences mAccountPrefs;
	private UserInterfaceController mController;
	private String mUsername;
	private String mAuthToken;
	/** Flags if the activity is created for the first time */
	private Boolean mLoggedIn;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                 
        Log.v(TAG, "onCreate()");
        
        setContentView(R.layout.activity_main_menu);
        
        mAccountPrefs = getApplicationContext().getSharedPreferences(Constants.ACCOUNT_PREFS_NAME, 0);
        /*Editor editor = mAccountPrefs.edit();
        editor.clear();
        editor.commit();*/        
        mController = UserInterfaceController.getInstance();
        if(mLoggedIn = findCredentials() && mController.getCurrentSession() == null) {
        	Log.v(TAG, "onCreate(), initSession");
        	mController.initSession(mUsername, mAuthToken, this);
        }
    }

    @Override
    protected void onStart() {
    	super.onStart();    	
    	Log.v(TAG, "onStart()");    	
    }    
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.v(TAG, "onResume()");
    	if(mLoggedIn) 
    	{    		
    		Log.v(TAG, "in the if");
    		start();    
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.v(TAG, "onActivityResult()");
    	if(requestCode == REQUEST_AUTHENTICATE) {
    		if(resultCode == RESULT_OK) {    			
    			//Retrieve the username and auth token from the login activity
    			mUsername = data.getStringExtra(Constants.PARAM_USERNAME);
    			mAuthToken = data.getStringExtra(Constants.PARAM_AUTH_TOKEN);
    			
    			//Store credentials in the shared preferences
    			Editor editor = mAccountPrefs.edit();
    	        editor.clear();
    	        editor.putString(Constants.PREFS_KEY_USERNAME, mUsername);
    	        editor.putString(Constants.PREFS_KEY_AUTH_TOKEN, mAuthToken);
    	        editor.commit();
    			
    	        //mUsername = mAccountPrefs.getString(Constants.PREFS_KEY_USERNAME, null);
    	        //mAuthToken = mAccountPrefs.getString(Constants.PREFS_KEY_AUTH_TOKEN, null);
    	        
    			Log.v(TAG, "mUsername: " + mUsername + ", mAuthToken: " + mAuthToken);    
    			
    			if(mController.getCurrentSession() == null) {
    	        	Log.v(TAG, "onActivityResult(), initSession");
    				mController.initSession(mUsername, mAuthToken, this);
    			}
        		mLoggedIn = true;
    		} else if(resultCode == RESULT_CANCELED) {
    			finish();
    		}
    	}
    }
    
    private void start() {
    	ListView listView = (ListView) findViewById(R.id.listview_ongoing_games);

		Log.v(TAG, "fetching ongoing games");
		ArrayList<Game> ongoingGames = 
				UserInterfaceController.getInstance().getCurrentSession().
				getUser().getOngoingGames();

		OngoingGamesAdapter adapter = 
				new OngoingGamesAdapter(this, R.layout.listview_item_row, ongoingGames);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, 
					int position, long id) {
				Game game = (Game) parent.getItemAtPosition(position);
				goToGameMenu(view, game);
			}
		});
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.v(TAG, "onStop()");
    	if(mLoggedIn)
        	UserInterfaceController.getInstance().saveOngoingGames(this);    	
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.v(TAG, "onDestroy()");
    	mController.destroySession();
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Checks the shared preferences to see if a user is logged in. Starts the login activity
     * if no credentials were found.
     * 
     * @return true if credentials were found, false if the login activity was started.
     */
    public Boolean findCredentials() {        
    	Log.v(TAG, "findCredentials()");
    	
        mUsername = mAccountPrefs.getString(Constants.PREFS_KEY_USERNAME, null);
        mAuthToken = mAccountPrefs.getString(Constants.PREFS_KEY_AUTH_TOKEN, null);

        if (mUsername == null) {
        	Intent intent = new Intent(this, AuthenticatorActivity.class);
        	intent.putExtra(Constants.PARAM_REQUEST_NEW_ACCOUNT, false);
        	startActivityForResult(intent, REQUEST_AUTHENTICATE);
        	return false;
        }
        else {
        	return true;
        }
    }
    
    public void logout(View view) {
    	Editor editor = mAccountPrefs.edit();
    	editor.clear();
    	editor.commit();
    	finish();
    }
     
    /** Called when the user clicks the "New game" button */
    public void goToNewGame(View view) {
        Intent intent = new Intent(this, NewGameActivity.class);
        
        startActivity(intent);
    }    
    
    /** Called when the user clicks the "Manage team" button */
    public void goToManagementMain(View view) {
        Intent intent = new Intent(this, ManagementMainActivity.class);
        
        startActivity(intent);
    }
    
    /** Called when the user clicks an ongoing game */
    public void goToGameMenu(View view, Game game) {
    	if(game != null) {
    		Intent intent = new Intent(this, GameMenuActivity.class);
    		intent.putExtra(Constants.GAME, (Parcelable) game);
    		startActivity(intent);
    	}
    }
}
