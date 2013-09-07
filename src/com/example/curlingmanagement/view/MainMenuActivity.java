package com.example.curlingmanagement.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.curlingmanagement.Constants;
import com.example.curlingmanagement.CurlingManagement;
import com.example.curlingmanagement.R;
import com.example.curlingmanagement.authenticator.AuthenticatorActivity;
import com.example.curlingmanagement.authenticator.Session;
import com.example.curlingmanagement.controller.OngoingGamesAdapter;
import com.example.curlingmanagement.controller.OngoingGamesLoader;
import com.example.curlingmanagement.resources.model.Game;
import com.example.curlingmanagement.rest.service.GamesServiceHelper;
import com.example.curlingmanagement.rest.service.ProcessorService;
import com.example.curlingmanagement.rest.service.UsersServiceHelper;

/**
 * Controller class for the main menu. As of now the main activity.
 * 
 * @author Fabian
 *
 */
public class MainMenuActivity extends Activity 
implements LoaderManager.LoaderCallbacks<ArrayList<Game>> {

	public static final String TAG = "MainMenuActivity";

	public static final int REQUEST_AUTHENTICATE = 1;
	
	public static final int LOADER_ID = 1;

	public static final String ACTION_LOGOUT = 
			"com.example.curlingmanagement.view.MainMenuActivity.action_logout";
	
	public static final String ACTION_CHANGE_GAMES =
			"com.example.curlingmanagement.view.MainMenuActivity.action_games_refreshed";

	private final IntentFilter mFilter = new IntentFilter(ACTION_LOGOUT);
	
	private final ScheduledExecutorService mScheduler = Executors.newSingleThreadScheduledExecutor();
	
	private LoaderManager.LoaderCallbacks<ArrayList<Game>> mCallbacks;

	private UsersServiceHelper mLogoutServiceHelper;
	private GamesServiceHelper mUpdateGamesHelper;

	/** Keep track of the progress dialog so we can dismiss it */
	private ProgressDialog mProgressDialog = null;
	private SharedPreferences mAccountPrefs;
	//private UserInterfaceController mController;
	private String mUsername;
	private String mAuthToken;
	/** Flags if the activity is created for the first time */
	private Boolean mLoggedIn;
	private OngoingGamesAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);                 
		Log.v(TAG, "onCreate()");

		setContentView(R.layout.activity_main_menu);
		
		mCallbacks = this;
		
		mAccountPrefs = getApplicationContext().getSharedPreferences(Constants.ACCOUNT_PREFS_NAME, 0);
        
		if(mLoggedIn = findCredentials()) {		
			Log.v(TAG, "onCreate(), logged in");
			CurlingManagement.setSession(new Session(mUsername, mAuthToken));
			Log.v(TAG, "initializing loader");
			LoaderManager lm = getLoaderManager();
			lm.initLoader(LOADER_ID, null, mCallbacks);			
		}
	}

	/*
	 * {@inheritDoc}
	 */
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getText(R.string.ui_activity_logging_out));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		// We save off the progress dialog in a field so that we can dismiss
		// it later. We can't just call dismissDialog(0) because the system
		// can lose track of our dialog if there's an orientation change.
		mProgressDialog = dialog;
		return dialog;
	}

	@Override
	protected void onStart() {
		super.onStart();    	
		Log.v(TAG, "onStart()");
		registerReceiver(mBroadcastReceiver, mFilter);
		mLogoutServiceHelper = new UsersServiceHelper(this, ACTION_LOGOUT);
		mUpdateGamesHelper = new GamesServiceHelper(getApplicationContext(), ACTION_CHANGE_GAMES);
		if(mLoggedIn) {
			scheduleSync();
		}
	}    

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume()");
	}

	@Override
	public Loader<ArrayList<Game>> onCreateLoader(int id, Bundle args) {
		Log.v(TAG, "onCreateLoader()");
		
		return new OngoingGamesLoader(getApplicationContext());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Game>> loader, ArrayList<Game> data) {
		Log.v(TAG, "onLoadFinished()");
		
		switch(loader.getId()) {
		case LOADER_ID:
			mAdapter = 
			new OngoingGamesAdapter(this, R.layout.listview_item_row, data);
			ListView listView = (ListView) findViewById(R.id.listview_ongoing_games);
			listView.setAdapter(mAdapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, final View view, 
						int position, long id) {
					Game game = (Game) parent.getItemAtPosition(position);
					goToGameMenu(view, game);
				}
			});
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Game>> arg0) {
		Log.v(TAG, "onLoaderReset)");
		
		mAdapter.clear();
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

				Log.v(TAG, "mUsername: " + mUsername + ", mAuthToken: " + mAuthToken);
				
				CurlingManagement.setSession(new Session(mUsername, mAuthToken));
				
				Log.v(TAG, "initializing loader");
				LoaderManager lm = getLoaderManager();
				lm.initLoader(LOADER_ID, null, mCallbacks);
				scheduleSync();
				
				mLoggedIn = true;
			} else if(resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}

	/*private void start() {
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
	}*/

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop()");

		unregisterReceiver(mBroadcastReceiver);
		hideProgress();
		
		/*if(mLoggedIn) {
			UserInterfaceController.getInstance().saveOngoingGames(this); 
		}*/
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
		//mController.destroySession();
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
	
	private void scheduleSync() {
		mScheduler.scheduleAtFixedRate(
				new Runnable() {
					public void run() {
						mUpdateGamesHelper.getGames(mUsername, null, mAuthToken);
					}
				}, 0, Constants.SYNC_INTERVAL, TimeUnit.SECONDS);
	}

	public void logout(View view) {
		if(mLoggedIn) {
			showProgress();
			mLogoutServiceHelper.logout(mUsername, mAuthToken);
		}
	}

	/**
	 * Shows the progress UI for a lengthy operation.
	 */
	@SuppressWarnings("deprecation")
	private void showProgress() {
		showDialog(0);
	}

	/**
	 * Hides the progress UI for a lengthy operation.
	 */
	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
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
			intent.putExtra(Constants.GAME, (Serializable) game);
			startActivity(intent);
		}
	}

	private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();

			String action = intent.getAction();
			if(action.equals(ACTION_LOGOUT)) {

				boolean success = extras.getBoolean(ProcessorService.Extras.RESULT_EXTRA);

				hideProgress();

				if(success) {
					finish();
				} else {
					Log.e(TAG, "onAuthenticationResult: not logged out on server");
					finish();
				}		
			}		
		}
	};

}
