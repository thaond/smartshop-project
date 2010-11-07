package com.appspot.smartshop.facebook.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.appspot.smartshop.facebook.core.AsyncFacebookRunner;
import com.appspot.smartshop.facebook.core.DialogError;
import com.appspot.smartshop.facebook.core.Facebook;
import com.appspot.smartshop.facebook.core.FacebookError;
import com.appspot.smartshop.facebook.core.Util;
import com.appspot.smartshop.facebook.core.Facebook.DialogListener;
import com.appspot.smartshop.facebook.utils.SessionEvents.AuthListener;
import com.appspot.smartshop.facebook.utils.SessionEvents.LogoutListener;

/**
 * @author VoMinhTam
 */
public class FacebookUtils {

	private Activity activity;
	private static final String TAG = "[FacebookUtils]";
	// Variable for Facebook's connection
	private final String APP_ID = "168183433192672"; // SmartShop API Key on
	private final String[] PERMISSIONS = new String[] { "publish_stream",
			"read_stream", "offline_access" };
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;

	public FacebookUtils(Activity context) {
		this.activity = context;
		init();
	}

	private void init() {
		// Init for facebook
		if (APP_ID == null) {
			Util
					.showAlert(
							activity,
							"Warning",
							"Facebook Applicaton ID must be "
									+ "specified before running this example: see Example.java");
		}

		SessionListener mSessionListener = new SessionListener();
		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
		mFacebook = new Facebook();
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, activity);
	}

	public void logout() {
		// For logout action
		SessionEvents.onLogoutBegin();
		AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFacebook);
		asyncRunner.logout(activity, new LogoutRequestListener());
	}

	public void login() {
		mFacebook.authorize(activity, APP_ID, PERMISSIONS,
				new LoginDialogListener());
	}

	public boolean isLogin() {
		return mFacebook.isSessionValid();
	}

	public Activity getActivity() {
		return activity;
	}

	public void sendMessage(String message, String name, String pictureLink,
			String description, String link, BaseRequestListener listener) {
		System.out.println(mFacebook.isSessionValid());
		if (mFacebook.isSessionValid()) {
			// Send something
			// Bundle params = new Bundle();
			// params.putString("method", "photos.upload");
			//
			// URL uploadFileUrl = null;
			// try {
			// uploadFileUrl = new URL(
			// "http://www.facebook.com/images/devsite/iphone_connect_btn.jpg");
			// } catch (MalformedURLException e) {
			// e.printStackTrace();
			// }
			// try {
			// HttpURLConnection conn = (HttpURLConnection)
			// uploadFileUrl
			// .openConnection();
			// conn.setDoInput(true);
			// conn.connect();
			// int length = conn.getContentLength();
			//
			// byte[] imgData = new byte[length];
			// InputStream is = conn.getInputStream();
			// is.read(imgData);
			// params.putByteArray("picture", imgData);
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			//
			// mAsyncRunner.request(null, params, "POST",
			// new SampleUploadListener());

			// Send 3
			Bundle params = new Bundle();
			params.putString("message", message);
			params.putString("name", name);
			params.putString("picture", pictureLink);
			params.putString("description", description);
			params.putString("link", link);
			mAsyncRunner.request("me/feed", params, "POST", listener);
		} else {
			login();
		}
	}

	private final class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			Log.d(TAG, "onAuthSucceed");
			SessionStore.save(mFacebook, activity);
		}

		public void onAuthFail(String error) {
			Log.e(TAG, "onAuthFail: " + error);
		}

		public void onLogoutBegin() {
			Log.d(TAG, "onLogoutBegin");
		}

		public void onLogoutFinish() {
			Log.d(TAG, "onLogoutFinish");
			SessionStore.clear(activity);
		}
	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private Handler mHandler = new Handler();

	private class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response) {
			// callback should be run in the original thread,
			// not the background thread
			mHandler.post(new Runnable() {
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	public static class SimpleWallpostListener extends BaseRequestListener {

		private String message;
		private Activity activity;

		public SimpleWallpostListener(Activity activity, String message) {
			this.message = message;
			this.activity = activity;
		}

		public void onComplete(final String response) {
			Log.d(TAG, "Response: " + response.toString());
			try {
				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				activity.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(activity, message.toString(),
								Toast.LENGTH_LONG).show();
						Log.d(TAG, "Upload done");
					}
				});

				// process the response here: (executed in background thread)
				JSONObject json = Util.parseJson(response);
			} catch (JSONException e) {
				Log.w(TAG, "JSON Error in response");
			} catch (FacebookError e) {
				Log.w(TAG, "Facebook Error: " + e.getMessage());
			}
		}
	}
}
