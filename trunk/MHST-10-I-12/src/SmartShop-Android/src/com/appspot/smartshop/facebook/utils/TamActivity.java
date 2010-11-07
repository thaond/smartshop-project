package com.appspot.smartshop.facebook.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.R;
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
public class TamActivity extends Activity {
	private static final String TAG = "[Tam]";
	public static final String APP_ID = "168183433192672";

	private static final String[] PERMISSIONS = new String[] {
			"publish_stream", "read_stream", "offline_access" };

	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	private SessionListener mSessionListener = new SessionListener();
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tam);

		if (APP_ID == null) {
			Util
					.showAlert(
							this,
							"Warning",
							"Facebook Applicaton ID must be "
									+ "specified before running this example: see Example.java");
		}

		initAuth();

		mFacebook = new Facebook();
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);

		Button btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mFacebook.isSessionValid()) {
					// For logout action
					// SessionEvents.onLogoutBegin();
					// AsyncFacebookRunner asyncRunner = new
					// AsyncFacebookRunner(
					// mFacebook);
					// asyncRunner.logout(TamActivity.this,
					// new LogoutRequestListener());

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

					// Send 2
					// Bundle params = new Bundle();
					//
					// params.putByteArray("message", "Test".getBytes());
					// params.putByteArray("name",
					// "American Virgin".getBytes());
					// params.putByteArray("link",
					// "http://www.google.com.vn".getBytes());
					// params.putByteArray("description",
					// "A Freshman College Girl on a scholarship from an ...".getBytes());
					// params.putByteArray("picture",
					// "http://www.facebook.com/images/devsite/iphone_connect_btn.jpg".getBytes());
					//
					// mAsyncRunner.request("me/feed", params, "POST", new
					// SampleUploadListener());

					// Send 3
					Bundle params = new Bundle();
					params.putString("message", "Smart Shop");
					params.putString("name", "Name");
					params
							.putString("picture",
									"http://hangxachtayusa.net/img/p/89-129-medium.jpg");
					params.putString("description", "Description");
					params.putString("link", "http://www.google.com.vn");
					mAsyncRunner.request("me/feed", params, "POST", new
							 SampleUploadListener());
				} else {
					mFacebook.authorize(TamActivity.this, APP_ID, PERMISSIONS,
							new LoginDialogListener());
				}
			}
		});
	}

	private void initAuth() {
		mHandler = new Handler();

		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			Log.d(TAG, "onAuthSucceed");
			SessionStore.save(mFacebook, TamActivity.this);
		}

		public void onAuthFail(String error) {
			Log.e(TAG, "onAuthFail: " + error);
		}

		public void onLogoutBegin() {
			Log.d(TAG, "onLogoutBegin");
		}

		public void onLogoutFinish() {
			Log.d(TAG, "onLogoutFinish");
			SessionStore.clear(TamActivity.this);
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

	public class SampleUploadListener extends BaseRequestListener {

		public void onComplete(final String response) {
			Log.d(TAG, "Response: " + response.toString());
			try {
				// process the response here: (executed in background thread)
				JSONObject json = Util.parseJson(response);
				final String src = json.getString("src");

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				TamActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						// mText
						// .setText("Hello there, photo has been uploaded at \n"
						// + src);
						Log.d(TAG, "Upload done");
					}
				});
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}
}
