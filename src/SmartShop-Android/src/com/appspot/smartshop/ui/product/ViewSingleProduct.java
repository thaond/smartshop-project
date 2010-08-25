package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.mock.MockProductInfo;
import com.appspot.smartshop.utils.Global;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class ViewSingleProduct extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();
		// TODO (vanloi999): load data of product from server and put them into
		// basic attribute and advance attribute
		Intent basicIntent = new Intent(this, ViewBasicAttributeOfProduct.class);
		basicIntent
				.putExtra(Global.PRODUCT_INFO, MockProductInfo.getInstance());
		basicIntent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, false);
		// ProductInfo productInfo = (ProductInfo)
		// getIntent().getExtras().get(Global.PRODUCT_INFO);
		// basicIntent
		// .putExtra(Global.PRODUCT_INFO, productInfo);
		Log.d("Test", "Tab is not created");

		tabHost.addTab(tabHost.newTabSpec("Basic").setIndicator(
				"Basic Features").setContent(basicIntent));
		tabHost.addTab(tabHost.newTabSpec("User Define").setIndicator(
				"Advanced Fearture").setContent(
				new Intent(this, ViewAdvanceAttributeOfProduct.class)));
		//set up size for tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height=40;
		}

	}
}
