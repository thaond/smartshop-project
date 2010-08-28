package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.mock.MockProductInfo;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.Utils;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class ViewSingleProduct extends TabActivity {
	public static final String TAG = "[ViewSingleProduct]";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();
		
		// get product info from intent
		ProductInfo productInfo = (ProductInfo) getIntent().getExtras().get(Global.PRODUCT_INFO);
		Log.d(TAG, Utils.gson.toJson(productInfo));
		
		// TODO (vanloi999): load data of product from server and put them into
		// basic attribute and advance attribute
		Intent intent = new Intent(this, ViewBasicAttributeOfProduct.class);
		intent.putExtra(Global.PRODUCT_INFO, productInfo);
		intent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, false);

		tabHost.addTab(tabHost.newTabSpec("Basic").setIndicator(
				"Thông tin cơ bản").setContent(intent));
		tabHost.addTab(tabHost.newTabSpec("User Define").setIndicator(
				"Thông tin chi tiết").setContent(
				new Intent(this, ViewAdvanceAttributeOfProduct.class)));
		
		// set up size for tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height=40;
		}
	}
}
