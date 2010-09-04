package com.appspot.smartshop.ui.product;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;

public class ViewProductActivity extends TabActivity {
	public static final String TAG = "[ViewProduct]";
	public static final String PRODUCT_BASIC_INFO = "basic";
	public static final String PRODUCT_USER_DEFINED_INFO = "user_defined";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view product");
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();
		
		// get product info from intent
		ProductInfo productInfo = (ProductInfo) getIntent().getExtras().get(Global.PRODUCT_INFO);
		Log.d(TAG, Global.gsonWithHour.toJson(productInfo));
		
		// two tabs
		Intent intent = new Intent(this, ViewProductBasicAttributeActivity.class);
		ViewProductBasicAttributeActivity.productInfo = productInfo;
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_BASIC_INFO).setIndicator(
				getString(R.string.product_basic_info)).setContent(intent));
		
		intent = new Intent(this, ViewProductUserDefinedAttributeActivity.class);
		ViewProductUserDefinedAttributeActivity.setAttributes = productInfo.setAttributes;
		Log.d(TAG, "attribute set = " + ViewProductUserDefinedAttributeActivity.setAttributes);
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_USER_DEFINED_INFO).setIndicator(
				getString(R.string.product_user_defined_info)).setContent(intent));
		
		// set up size for tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}
	}
}
