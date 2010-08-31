package com.appspot.smartshop.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.smartshop.R;

public class SimpleAsyncTask extends AsyncTask<Void, Void, Void> {
	public static final String TAG = "[SimpleAsyncTask]";
	
	private final ProgressDialog dialog;
	private DataLoader dataLoader;
	private Context context;
	
	public boolean hasData = true;
	public String message = null;

	public SimpleAsyncTask(Context context, DataLoader dataLoader) {
		this.dataLoader = dataLoader;
		this.context = context; 
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}

	protected void onPreExecute() {
		Log.d(TAG, "onPreExec");
		dialog.setMessage(Global.application.getString(R.string.loading));
		dialog.show();
	};

	@Override
	protected Void doInBackground(Void... params) {
		Log.d(TAG, "onInBackground");
		dataLoader.loadData();
		return null;
	}

	protected void onPostExecute(Void result) {
		Log.d(TAG, "onPostExec");
		if (!hasData) {
			dialog.dismiss();
			Toast.makeText(context, message, 
					Toast.LENGTH_SHORT).show();
			return;
		}
		dataLoader.updateUI();
		dialog.dismiss();
	};
	
	@Override
	protected void onCancelled() {
		dialog.dismiss();
		Toast.makeText(context, context.getString(R.string.cant_connect_network), 
				Toast.LENGTH_SHORT).show();
	}
}
