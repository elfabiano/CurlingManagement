package com.example.curlingmanagement.controller;

import java.util.ArrayList;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.curlingmanagement.CurlingManagement;
import com.example.curlingmanagement.resources.database.ResourcesContract.GamesTable;
import com.example.curlingmanagement.resources.database.ResourcesDbHelper;
import com.example.curlingmanagement.resources.model.Game;

public class OngoingGamesLoader extends AsyncTaskLoader<ArrayList<Game>> {
	
	private static final String TAG = "OngoingGamesLoader";
	
	public ArrayList<Game> mData;
	private GameIntentReceiver mObserver;

	public OngoingGamesLoader(Context context) {
		super(context);
	}

	@Override
	public ArrayList<Game> loadInBackground() {
		Log.v(TAG, "loadInBackground()");
		
		ArrayList<Game> data = new ArrayList<Game>();
		
		SQLiteDatabase db = new ResourcesDbHelper(getContext()).getReadableDatabase();
		
		String[] projection = {
			GamesTable.COLUMN_NAME_SERVER_ID,
			GamesTable.COLUMN_NAME_STATUS,
			GamesTable.COLUMN_NAME_WAITING_FOR,
			GamesTable.COLUMN_NAME_CURRENT_STATE_ID,
			GamesTable.COLUMN_NAME_PREVIOUS_STATE_ID,
			GamesTable.COLUMN_NAME_HOME_SCORE,
			GamesTable.COLUMN_NAME_AWAY_SCORE,
			GamesTable.COLUMN_NAME_STONES_PLAYED,
			GamesTable.COLUMN_NAME_HOME_USERNAME,
			GamesTable.COLUMN_NAME_AWAY_USERNAME,
			GamesTable.COLUMN_NAME_MODIFIED
		};
		
		String sortOrder = GamesTable.COLUMN_NAME_MODIFIED + " DESC";
		
		String selection = 
				GamesTable.COLUMN_NAME_HOME_USERNAME + " LIKE ? OR " +
				GamesTable.COLUMN_NAME_AWAY_USERNAME + " LIKE ? OR " +
				GamesTable.COLUMN_NAME_WAITING_FOR + " LIKE ?";
		
		String username = CurlingManagement.session.getUsername();
		String[] selectionArgs = {
				username,
				username,
				username
		};
		
		Cursor c = db.query(
				GamesTable.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder
				);
		
		Log.v(TAG, "after query");
		
		while(c.moveToNext()) {
			Log.v(TAG, "fetching query results");
			data.add(new Game(
						c.getInt(0),
						c.getString(1),
						c.getString(2),
						c.getInt(3),
						c.getInt(4),
						c.getInt(5),
						c.getInt(6),
						c.getInt(7),
						c.getString(8),
						c.getString(9),
						c.getString(10)
						));
		}
		
		c.close();
		db.close();
		
		return data;
	}
	
	@Override
	public void deliverResult(ArrayList<Game> data) {
		Log.v(TAG, "deliverResult()");
		
		if(isReset()) {
			// The Loader has been reset; ignore the result and invalidate the data.
			releaseResources(data);
			return;
		}

		// Hold a reference to the old data so it doesn't get garbage collected.
		// We must protect it until the new data has been delivered.
		ArrayList<Game> oldData = mData;
		mData = data;

		if (isStarted()) {
			// If the Loader is in a started state, deliver the results to the
			// client. The superclass method does this for us.
			super.deliverResult(data);
		}

		// Invalidate the old data as we don't need it any more.
		if (oldData != null && oldData != data) {
			releaseResources(oldData);
		}	  
	}

	@Override
	protected void onStartLoading() {
		Log.v(TAG, "onStartLoading()");
		
		if(mData != null) {
			deliverResult(mData);
		}

		if(mObserver == null) {
			mObserver = new GameIntentReceiver(this);
		}

		if(takeContentChanged() || mData == null) {
			forceLoad();
		}
	}
	
	@Override
	protected void onStopLoading() {
		Log.v(TAG, "onStopLoading()");
		// The Loader is in a stopped state, so we should attempt to cancel the 
	    // current load (if there is one).
	    cancelLoad();	    
	    // Note that we leave the observer as is. Loaders in a stopped state
	    // should still monitor the data source for changes so that the Loader
	    // will know to force a new load if it is ever started again.
	}
	
	 @Override
	  protected void onReset() {
		 Log.v(TAG, "onReset()");
	    // Ensure the loader has been stopped.
	    onStopLoading();
	 
	    // At this point we can release the resources associated with 'mData'.
	    if (mData != null) {
	      releaseResources(mData);
	      mData = null;
	    }
	 
	    // The Loader is being reset, so we should stop monitoring for changes.
	    if (mObserver != null) {
	      getContext().unregisterReceiver(mObserver);
	      mObserver = null;
	    }
	  }
	 
	 @Override
	  public void onCanceled(ArrayList<Game> data) {
		 Log.v(TAG, "onCanceled()");
	    // Attempt to cancel the current asynchronous load.
	    super.onCanceled(data);
	 
	    // The load has been canceled, so we should release the resources
	    // associated with 'data'.
	    releaseResources(data);
	  }

	private void releaseResources(ArrayList<Game> data) {
		Log.v(TAG, "releaseResources()");
		// For a simple List, there is nothing to do. For something like a Cursor, we 
		// would close it in this method. All resources associated with the Loader
		// should be released here.
	}

}
