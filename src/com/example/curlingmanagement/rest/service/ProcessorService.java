package com.example.curlingmanagement.rest.service;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * This service is for making asynchronous method calls on providers.
 * The fact that this is a service means the method calls
 * will continue to run even when the calling activity is killed.
 */
public class ProcessorService extends Service
{
	//private static final String TAG = "ProcessorService";
	
	private Integer lastStartId;
	private final Context mContext = this;

	/**
	 * The keys to be used for the required actions to start this service.
	 */
	public static class Extras
	{
		/**
		 * The provider which the called method is on.
		 */
		public static final String PROVIDER_EXTRA = "PROVIDER_EXTRA";

		/**
		 * The method to call.
		 */
		public static final String METHOD_EXTRA = "METHOD_EXTRA";

		/**
		 * The action to used for the result intent.
		 */
		public static final String RESULT_ACTION_EXTRA = "RESULT_ACTION_EXTRA";

		/**
		 * The extra used in the result intent to return the result.
		 */
		public static final String RESULT_EXTRA = "RESULT_EXTRA";
	}

	private final HashMap<String, AsyncServiceTask> mTasks = new HashMap<String, AsyncServiceTask>();

	/**
	 * Identifier for each supported provider.
	 * Cannot use 0 as Bundle.getInt(key) returns 0 when the key does not exist.
	 */
	public static class Providers
	{
		public static final int USERS_PROVIDER = 1;
		public static final int GAMES_PROVIDER = 2;
	}

	private IServiceProvider GetProvider(int providerId)
	{
		switch(providerId)
		{
		case Providers.USERS_PROVIDER:
			return new UsersServiceProvider(this);
		case Providers.GAMES_PROVIDER:
			return new GamesServiceProvider(this);
		}
		return null;
	}

	/**
	 * Builds a string identifier for this method call.
	 * The identifier will contain data about:
	 *   What processor was the method called on
	 *   What method was called
	 *   What parameters were passed
	 * This should be enough data to identify a task to detect if a similar task is already running.
	 */
	private String getTaskIdentifier(Bundle extras)
	{
		String[] keys = extras.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuilder identifier = new StringBuilder();

		for (int keyIndex = 0; keyIndex < keys.length; keyIndex++)
		{
			String key = keys[keyIndex];

			// The result action may be different for each call.
			if (key.equals(Extras.RESULT_ACTION_EXTRA))
			{
				continue;
			}

			identifier.append("{");
			identifier.append(key);
			identifier.append(":");
			if(extras.get(key) == null) {
				identifier.append("null");
			} else {
				identifier.append(extras.get(key).toString());
			}
			identifier.append("}");
		}

		return identifier.toString();
	}
	
	/*private boolean cancelLogin(Bundle extras) {
		int providerId = extras.getInt(Extras.PROVIDER_EXTRA);
		int methodId = extras.getInt(Extras.METHOD_EXTRA);
		
		if(providerId == Providers.USERS_PROVIDER && 
				methodId == UsersServiceProvider.Methods.CANCEL_LOGIN_METHOD) {
			for(HashMap.Entry<String, AsyncServiceTask> entry: mTasks.entrySet()) {
				if(entry.getKey().contains("{METHOD_EXTRA:1}")) {
					entry.getValue().cancel(true);
					return true;
				}
			}
		}
		return false;
	}*/

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// This must be synchronised so that service is not stopped while a new task is being added.
		synchronized (mTasks)
		{
			// stopSelf will be called later and if a new task is being added we do not want to stop the service.
			lastStartId = startId;

			Bundle extras = intent.getExtras();

			String taskIdentifier = getTaskIdentifier(extras);

			Log.i("ProcessorService", "starting " + taskIdentifier);

			// If a similar task is already running then lets use that task.
			AsyncServiceTask task = mTasks.get(taskIdentifier);

			if (task == null)
			{
				task = new AsyncServiceTask(taskIdentifier, extras);

				mTasks.put(taskIdentifier, task);

				// AsyncTasks are by default only run in serial (depending on the android version)
				// see android documentation for AsyncTask.execute()
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			}

			// Add this Result Action to the task so that the calling activity can be notified when the task is complete.
			String resultAction = extras.getString(Extras.RESULT_ACTION_EXTRA);
			if (resultAction != "")
			{
				task.addResultAction(extras.getString(Extras.RESULT_ACTION_EXTRA));
			}
		}


		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public class AsyncServiceTask extends AsyncTask<Void, Void, Boolean>
	{
		private final Bundle mExtras;
		private final ArrayList<String> mResultActions = new ArrayList<String>();

		private final String mTaskIdentifier;

		/**
		 * Constructor for AsyncServiceTask
		 * 
		 * @param taskIdentifier A string which describes the method being called.
		 * @param extras         The Extras from the Intent which was used to start this method call.
		 */
		public AsyncServiceTask(String taskIdentifier, Bundle extras)
		{
			mTaskIdentifier = taskIdentifier;
			mExtras = extras;
		}

		public void addResultAction(String resultAction)
		{
			if (!mResultActions.contains(resultAction))
			{
				mResultActions.add(resultAction);
			}
		}

		@Override
		protected Boolean doInBackground(Void... params)
		{

			Boolean result = false;
			Log.i("ProcessorService", "working " + mTaskIdentifier);				
			final int providerId = mExtras.getInt(Extras.PROVIDER_EXTRA);
			final int methodId = mExtras.getInt(Extras.METHOD_EXTRA);

			if (providerId != 0 && methodId != 0)
			{
				final IServiceProvider provider = GetProvider(providerId);

				if (provider != null)
				{
					try
					{
						result = provider.RunTask(methodId, mExtras);
					} catch (Exception e)
					{
						result = false;
					}
				}
			}			
			return result;			
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			// This must be synchronised so that service is not stopped while a new task is being added.
			synchronized (mTasks)
			{
				Log.i("ProcessorService", "finishing " + mTaskIdentifier);
				// Notify the caller(s) that the method has finished executing
				for (int i = 0; i < mResultActions.size(); i++)
				{
					Intent resultIntent = new Intent(mResultActions.get(i));

					resultIntent.putExtra(Extras.RESULT_EXTRA, result.booleanValue());
					resultIntent.putExtras(mExtras);

					resultIntent.setPackage(mContext.getPackageName());

					mContext.sendBroadcast(resultIntent);
				}

				// The task is complete so remove it from the running tasks list
				mTasks.remove(mTaskIdentifier);

				// If there are no other executing methods then stop the service
				if (mTasks.size() < 1)
				{
					stopSelf(lastStartId);
				}
			}
		}
	}
} 
