package com.appspot.smartshop.ui.user.notification;

import android.app.Activity;
import android.os.Bundle;

import com.appspot.smartshop.adapter.ProductAdapter;

/**
 * This is a sample Activity that responds to a custom notification action. 
 * The data is retrieved from the Intent and in this case, it is displayed in a text box.
 * To launch this Activity, your notification would set the action to "com.acmelabs.DISPLAY_CUSTOM_DATA" in order 
 * to match the intent filter defined in AndroidManifest.xml.
 */
public class ProductNotificationActivity extends Activity {
	private static final String TAG = "ProductNotificationActivity";
	
	private static ProductAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		Log.d(TAG, "Running");
//		setContentView(R.layout.product_subscribe_list);
//		
//		Intent intent = getIntent();
//		String data = decodeUriData(intent.getData());
//		if (data != null) {
//			ProductInfo productInfo = Global.gsonWithHour.fromJson(data, ProductInfo.class);
//			Global.listSubscribeProduct.add(productInfo);
//		}
//
//		ListView listView = (ListView) findViewById(R.id.listView);
//		adapter = new ProductAdapter(this, R.layout.product_list_item, Global.listSubscribeProduct);
//		listView.setAdapter(adapter);
//		
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				
//			}
//		});
	}
};