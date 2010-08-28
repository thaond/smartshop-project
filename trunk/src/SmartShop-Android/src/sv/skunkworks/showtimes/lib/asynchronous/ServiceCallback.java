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

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.appspot.smartshop.R;

/**
 * Interface definition for a callback to be invoked when a service request has
 * been done.
 * 
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public abstract class ServiceCallback<T> {
	private Context context;

	public ServiceCallback() {
	}

	public ServiceCallback(Context context) {
		this.context = context;
	}

	/**
	 * Called when service request success.
	 */
	public abstract void onSuccess(T result);

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
	}

	public void onEndUpdating() {
	};
}
