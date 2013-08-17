/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.curlingmanagement.authenticator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.curlingmanagement.Constants;
import com.example.curlingmanagement.R;
import com.example.curlingmanagement.rest.client.UsersApi;

/**
 * Activity which displays login screen to the user.
 */
public class AuthenticatorActivity extends Activity {
	
    /** The tag used to log to adb console. */
    private static final String TAG = "AuthenticatorActivity";
	
    public static final String ACTION_LOGIN = 
    		"com.example.curlingmanagement.authenticator.actionlogin";
    
    //private final IntentFilter mFilter = new IntentFilter(ACTION_LOGIN);
    
    //private UsersServiceHelper mLoginServiceHelper;

    /** Keep track of the login task so can cancel it if requested */
    private UserLoginTask mAuthTask = null;

    /** Keep track of the progress dialog so we can dismiss it */
    private ProgressDialog mProgressDialog = null;

    private TextView mMessage;

    private String mPassword;

    private EditText mPasswordEdit;
    
    private TextView mEmailLabel;
    
    private EditText mEmailEdit;

    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;

    private String mUsername;

    private EditText mUsernameEdit;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "onCreate(" + savedInstanceState + ")");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "loading data from Intent");
        final Intent intent = getIntent();
        mRequestNewAccount = intent.getBooleanExtra(Constants.PARAM_REQUEST_NEW_ACCOUNT, false);
        Log.i(TAG, "    request new: " + mRequestNewAccount);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.login_activity);
        getWindow().setFeatureDrawableResource(
                Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
        mMessage = (TextView) findViewById(R.id.message);
        mUsernameEdit = (EditText) findViewById(R.id.username_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);
        mEmailLabel = (TextView) findViewById(R.id.email_label);
        mEmailEdit = (EditText) findViewById(R.id.email_edit);
        if(!mRequestNewAccount) {
        	mEmailLabel.setVisibility(View.GONE);
        	mEmailEdit.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mUsername)) mUsernameEdit.setText(mUsername);
        mMessage.setText(getMessage());
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	Log.v(TAG, "onStart()");
    	
    	//registerReceiver(mBroadcastReceiver, mFilter);
    	//mLoginServiceHelper = new UsersServiceHelper(this, ACTION_LOGIN);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.v(TAG, "onStop()");
    	
    	//unregisterReceiver(mBroadcastReceiver);
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getText(R.string.ui_activity_authenticating));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "user cancelling authentication");
                //mLoginServiceHelper.cancelLogin();
                mAuthTask.cancel(true);
            }
        });
        // We save off the progress dialog in a field so that we can dismiss
        // it later. We can't just call dismissDialog(0) because the system
        // can lose track of our dialog if there's an orientation change.
        mProgressDialog = dialog;
        return dialog;
    }

    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication. The button is configured to call
     * handleLogin() in the layout XML.
     *
     * @param view The Submit button for which this method is invoked
     */
    public void handleLogin(View view) {
    	Log.v(TAG, "handleLogin()");
    	mUsername = mUsernameEdit.getText().toString();
    	mPassword = mPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            mMessage.setText(getMessage());
        } else {
            // Show a progress dialog, and kick off a background task to perform
            // the user login attempt.
            showProgress();
            mAuthTask = new UserLoginTask();
            mAuthTask.execute();
            //mLoginServiceHelper.login(mUsername, mPassword);
        }
    }

    /**
     * Called when response is received from the server for confirm credentials
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller.
     *
     * @param result the confirmCredentials result.
     */
    /*private void finishConfirmCredentials(boolean result, String authToken) {
        Log.i(TAG, "finishConfirmCredentials()");
        final Intent intent = new Intent();
        intent.putExtra(Constants.PARAM_USERNAME, mUsername);
        intent.putExtra(Constants.PARAM_AUTH_TOKEN, authToken);
        setResult(RESULT_OK, intent);
        finish();
    }*/

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. We store the
     * authToken that's returned from the server as the 'password' for this
     * account - so we're never storing the user's actual password locally.
     *
     * @param result the confirmCredentials result.
     */
    private void finishLogin(String authToken) {

        Log.i(TAG, "finishLogin()");
        final Intent intent = new Intent();
        intent.putExtra(Constants.PARAM_USERNAME, mUsername);
        intent.putExtra(Constants.PARAM_AUTH_TOKEN, authToken);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param authToken the authentication token returned by the server, or NULL if
     *            authentication failed.
     */
    public void onAuthenticationResult(String authToken) {

        boolean success = ((authToken != null) && (authToken.length() > 0));
        Log.i(TAG, "onAuthenticationResult(" + success + ")");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();

        if (success) {
        	finishLogin(authToken);
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
            if (mRequestNewAccount) {
                // "Please enter a valid username/password.
                mMessage.setText(getText(R.string.login_activity_loginfail_text_both));
            } else {
                // "Please enter a valid password." (Used when the
                // account is already in the database but the password
                // doesn't work.)
                mMessage.setText(getText(R.string.login_activity_loginfail_text_pwonly));
            }
        }
    }

    public void onAuthenticationCancel() {
        Log.i(TAG, "onAuthenticationCancel()");

        // Our task is complete, so clear it out
        mAuthTask = null;

        // Hide the progress dialog
        hideProgress();
    }

    /**
     * Returns the message to be displayed at the top of the login dialog box.
     */
    private CharSequence getMessage() {
        getString(R.string.app_name);
        if (TextUtils.isEmpty(mUsername)) {
            // If no username, then we ask the user to log in using an
            // appropriate service.
            final CharSequence msg = getText(R.string.login_activity_newaccount_text);
            return msg;
        }
        if (TextUtils.isEmpty(mPassword)) {
            // We have an account but no password
            return getText(R.string.login_activity_loginfail_text_pwmissing);
        }
        return null;
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
    
    /*private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

    	@Override
    	public void onReceive(Context context, Intent intent) {
    		Bundle extras = intent.getExtras();
    		
    		String action = intent.getAction();
    		if(action.equals(ACTION_LOGIN)) {
    		
    			hideProgress();
    			boolean success = extras.getBoolean(ProcessorService.Extras.RESULT_EXTRA);
    			
    			if(success) {
    				Intent result = new Intent();
    				AuthenticatorActivity.this.setResult(RESULT_OK, result);
    		        finish();
    			} else {
    	            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
    	            if (mRequestNewAccount) {
    	                // "Please enter a valid username/password.
    	                mMessage.setText(getText(R.string.login_activity_loginfail_text_both));
    	            } else {
    	                // "Please enter a valid password." (Used when the
    	                // account is already in the database but the password
    	                // doesn't work.)
    	                mMessage.setText(getText(R.string.login_activity_loginfail_text_pwonly));
    	            }
    	        }
    		
    		}
    		
    	}
    };*/

    /**
     * Represents an asynchronous task used to authenticate a user against the
     * Curling Management Service
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // We do the actual work of authenticating the user
            // in the NetworkUtilities class.
            try {
                return UsersApi.login(mUsername, mPassword);
            } catch (Exception ex) {
                Log.e(TAG, "UserLoginTask.doInBackground: failed to authenticate");
                Log.i(TAG, ex.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String authToken) {
            // On a successful authentication, call back into the Activity to
            // communicate the authToken (or null for an error).
            onAuthenticationResult(authToken);
        }

        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            onAuthenticationCancel();
        }
    }
}
