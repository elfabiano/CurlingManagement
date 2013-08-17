package com.example.curlingmanagement.rest.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * The service helpers are a facade for starting a task on the ProcessorService.
 * The purpose of the helpers is to give a simple interface to the upper layers to make asynchronous method calls in the service.
 */
public abstract class ServiceHelperBase
{
	private final Context mContext;
	private final int mProviderId;
	private final String mResultAction;

	public ServiceHelperBase(Context context, int providerId, String resultAction)
	{
		mContext = context;
		mProviderId = providerId;
		mResultAction = resultAction;
	}

	/**
	 * Starts the specified methodId with no parameters
	 * @param methodId The method to start
	 */
	protected void RunMethod(int methodId)
	{
		RunMethod(methodId, null);
	}

	/**
	 * Starts the specified methodId with the parameters given in Bundle
	 * @param methodId The method to start
	 * @param bundle   The parameters to pass to the method
	 */
	protected void RunMethod(int methodId, Bundle bundle)
	{
		Intent service = new Intent(mContext, ProcessorService.class);

		service.putExtra(ProcessorService.Extras.PROVIDER_EXTRA, mProviderId);
		service.putExtra(ProcessorService.Extras.METHOD_EXTRA, methodId);
		service.putExtra(ProcessorService.Extras.RESULT_ACTION_EXTRA, mResultAction);

		if (bundle != null)
		{
			service.putExtras(bundle);
		}

		mContext.startService(service);
	}

} 
