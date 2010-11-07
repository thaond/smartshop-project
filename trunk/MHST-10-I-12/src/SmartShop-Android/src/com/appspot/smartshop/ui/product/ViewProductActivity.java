package com.appspot.smartshop.ui.product;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.Utils;

public class ViewProductActivity extends TabActivity {
	public static final String TAG = "[ViewProduct]";
	public static final String PRODUCT_BASIC_INFO = "basic";
	public static final String PRODUCT_USER_DEFINED_INFO = "user_defined";
	public static final String PRODUCT_GALLERY = "gallery";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view product");
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();

		// get product info from intent
		Bundle bundle = getIntent().getExtras();
		productInfo = (ProductInfo) bundle.get(Global.PRODUCT_INFO);
		SmartshopNotification notification = (SmartshopNotification) bundle.get(Global.NOTIFICATION);
		if (notification != null) {
			Log.d(TAG, "remove notification " + notification.id);
			Global.notificationManager.cancel(notification.id);
			Global.notifications.remove(notification);
			Utils.markNotificationAsRead(notification.id);
		}

		// two tabs
		Intent intent = new Intent(this,
				ViewProductBasicAttributeActivity.class);
		intent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, false);
		ViewProductBasicAttributeActivity.productInfo = productInfo;
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_BASIC_INFO).setIndicator(
				getString(R.string.product_basic_info)).setContent(intent));

//		intent = new Intent(this, ViewProductUserDefinedAttributeActivity.class);
//		intent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, false);
//		// ViewProductUserDefinedAttributeActivity.setAttributes.clear();
//		ViewProductUserDefinedAttributeActivity.setAttributes = productInfo.attributeSets;
//		Log.d(TAG, "attribute set = "
//				+ ViewProductUserDefinedAttributeActivity.setAttributes);
//		tabHost.addTab(tabHost.newTabSpec(PRODUCT_USER_DEFINED_INFO)
//				.setIndicator(getString(R.string.product_user_defined_info))
//				.setContent(intent));

		intent = new Intent(this, ProductGalleryActivity.class);
		intent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, false);
		if (productInfo.setMedias != null)
			ProductGalleryActivity.listMediaBitmap
					.addAll(productInfo.setMedias);
		Log.d(TAG, ProductGalleryActivity.listMediaBitmap + "");
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_GALLERY).setIndicator(
				getString(R.string.gallery)).setContent(intent));

		// set up size for tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}

		// Remove Notification
		SmartshopNotification sNotification = (SmartshopNotification) getIntent()
				.getSerializableExtra(Global.NOTIFICATION);
		if (sNotification != null) {
			Global.notifications.remove(sNotification);
			Global.notificationManager.cancel(sNotification.id);
		}
	}
	
	private static final int MENU_VIEW_RELATED_RRODUCTS = 2;
	private ProductInfo productInfo;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_VIEW_RELATED_RRODUCTS, 0,
				getString(R.string.view_related_products));
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_VIEW_RELATED_RRODUCTS) {
			Intent intent = new Intent(this, ViewRelatedProductsActivity.class);
			intent.putExtra(Global.RELATED_PRODUCT_ID, productInfo.id);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
