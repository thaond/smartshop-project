package com.appspot.smartshop.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.smartshop.R;

public class SimpleAsyncTask extends AsyncTask<Void, Void, Void> {
	public static final String TAG = "[SimpleAsyncTask]";
	
	private final ProgressDialog dialog;
	private DataLoader dataLoader;

	public SimpleAsyncTask(Context context, DataLoader dataLoader) {
		this.dataLoader = dataLoader;
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
		dataLoader.updateUI();
		dialog.dismiss();
	};
}
