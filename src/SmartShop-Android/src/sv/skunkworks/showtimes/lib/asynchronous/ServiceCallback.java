/**
 * Copyright 2010 Hieu Rocker & Tien Trum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sv.skunkworks.showtimes.lib.asynchronous;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.appspot.smartshop.MainActivity;
import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.URLConstant;
import com.google.gson.JsonObject;

/**
 * Interface definition for a callback to be invoked when a service request has
 * been done.
 * 
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public abstract class ServiceCallback {
	private Context context;
	private ProgressDialog dialog = null;

	public ServiceCallback() {
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}

	public ServiceCallback(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * Called when service request success.
	 */
	public abstract void onSuccess(JsonObject jsonObject);
	
	public boolean handleMessage(JsonObject jsonObject) {
		int errorCode = jsonObject.get(URLConstant.ERROR_CODE).getAsInt();
		if (errorCode == 1) {
			String message = jsonObject.get(URLConstant.MESSAGE).getAsString();
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			
			return false;
		} 
		
		return true;
	}

	/**
	 * Called when service request failed.
	 */
	public void onFailure(Exception ex){
    	if (context!=null){
    		Log.e(context.getPackageName(), ex.getMessage());
        	Toast.makeText(context, context.getString(R.string.cant_connect_network), Toast.LENGTH_SHORT).show();	
    	}
    	else{
    		ex.printStackTrace();
    	}
    }

	public void onUpdating() {
		dialog.setMessage(Global.application.getString(R.string.loading));
		dialog.show();
	}

	public void onEndUpdating() {
		dialog.dismiss();
	}
}
