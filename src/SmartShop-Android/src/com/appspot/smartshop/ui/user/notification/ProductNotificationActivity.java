package com.appspot.smartshop.ui.user.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;
import com.xtify.android.sdk.NotificationActivity;

/**
 * This is a sample Activity that responds to a custom notification action. 
 * The data is retrieved from the Intent and in this case, it is displayed in a text box.
 * To launch this Activity, your notification would set the action to "com.acmelabs.DISPLAY_CUSTOM_DATA" in order 
 * to match the intent filter defined in AndroidManifest.xml.
 */
public class ProductNotificationActivity extends NotificationActivity {
	private static final String TAG = "ProductNotificationActivity";
	
	private static ProductAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "Running");
		setContentView(R.layout.product_subscribe_list);
		
		Intent intent = getIntent();
		String data = decodeUriData(intent.getData());
		if (data != null) {
			ProductInfo productInfo = Global.gsonWithHour.fromJson(data, ProductInfo.class);
			Global.listSubscribeProduct.add(productInfo);
		}

		ListView listView = (ListView) findViewById(R.id.listView);
		adapter = new ProductAdapter(this, R.layout.product_list_item, Global.listSubscribeProduct);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
	}
};