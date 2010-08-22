package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;


public class ViewBasicAttributeOfProduct extends Activity {
	public TextView lblName;
	public TextView lblPrice;
	public TextView lblQuantity;
	public TextView lblDatePost;
	public TextView lblWarranty;
	public TextView lblOrigin;
	public TextView lblAddress;
	public TextView lblUserName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_basic_attribute_of_product);
		// label width
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		int labelWidth = (int) (width * 0.4);
		//set up width of attribute of product
		lblName = (TextView) findViewById(R.id.txtViewName);
		lblName.setWidth(labelWidth);
		lblPrice = (TextView) findViewById(R.id.txtViewPrice);
		lblPrice.setWidth(labelWidth);
		lblQuantity = (TextView) findViewById(R.id.txtViewQuantity);
		lblQuantity.setWidth(labelWidth);
		lblDatePost = (TextView) findViewById(R.id.txtViewQuantity);
		lblDatePost.setWidth(labelWidth);
		lblWarranty = (TextView) findViewById(R.id.txtViewWarranty);
		lblWarranty.setWidth(labelWidth);
		lblOrigin = (TextView) findViewById(R.id.txtViewOrigin);
		lblOrigin.setWidth(labelWidth);
		lblAddress = (TextView) findViewById(R.id.txtViewAddress);
		lblAddress.setWidth(labelWidth);
		lblUserName= (TextView) findViewById(R.id.txtViewUserName);
		lblUserName.setWidth(labelWidth);
		//TODO (condohero01) 
	}
}
