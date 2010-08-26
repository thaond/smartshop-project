package com.appspot.smartshop.ui.product;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;

public class ViewBasicAttributeOfProduct extends Activity {
	public TextView lblNameOfProduct;
	public TextView lblPriceOfProduct;
	public TextView lblQuantityOfProduct;
	public TextView lblWarrantyOfProduct;
	public TextView lblOriginOfProduct;
	public TextView lblAddressOfProduct;

	public EditText txtNameProduct;
	public EditText txtPriceOfProduct;
	public EditText txtQuantityOfProduct;
	public EditText txtWarrantyOfProduct;
	public EditText txtOriginOfProduct;
	public EditText txtAddressOfProduct;

	public Button btnViewComment;
	public Button btnViewUserInfo;

	public ProductInfo productInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_basic_attribute_of_product);
		// set up labelWidth and textWidth
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();
		int labelWidth = (int) (width * 0.25);
		// set up TextView and EditText
		lblNameOfProduct = (TextView) findViewById(R.id.viewNameOfProduct);
		lblNameOfProduct.setWidth(labelWidth);
		txtNameProduct = (EditText) findViewById(R.id.txtViewNameOfProduct);

		lblPriceOfProduct = (TextView) findViewById(R.id.viewPriceOfProduct);
		lblPriceOfProduct.setWidth(labelWidth);
		txtPriceOfProduct = (EditText) findViewById(R.id.txtViewPriceOfProduct);

		lblQuantityOfProduct = (TextView) findViewById(R.id.viewQuantityOfProduct);
		lblQuantityOfProduct.setWidth(labelWidth);
		txtQuantityOfProduct = (EditText) findViewById(R.id.txtViewQuantityOfProduct);

		lblWarrantyOfProduct = (TextView) findViewById(R.id.viewWarrantyOfProduct);
		lblWarrantyOfProduct.setWidth(labelWidth);
		txtWarrantyOfProduct = (EditText) findViewById(R.id.txtViewWarrantyOfProduct);

		lblOriginOfProduct = (TextView) findViewById(R.id.viewOriginOfProduct);
		lblOriginOfProduct.setWidth(labelWidth);
		txtOriginOfProduct = (EditText) findViewById(R.id.txtViewOriginOfProduct);

		lblAddressOfProduct = (TextView) findViewById(R.id.viewAddressOfProduct);
		lblAddressOfProduct.setWidth(labelWidth);
		txtAddressOfProduct = (EditText) findViewById(R.id.txtViewAddressOfProduct);

		btnViewComment = (Button) findViewById(R.id.viewComment);
		btnViewUserInfo = (Button) findViewById(R.id.viewUserInfo);
		btnViewComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: vanloi999 xu li comment

			}
		});
		btnViewUserInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: vanloi999 xu li view User info

			}
		});
		// set up check box
		final CheckBox check1 = (CheckBox) findViewById(R.id.viewCheckBoxIsVAT);
		check1.setEnabled(false);
		// final TextView txtVAT = (TextView)
		// findViewById(R.id.txtViewIsVATInPostProductForm);
		// setup data for text field if in edit/view product info mode
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			productInfo = (ProductInfo) bundle.get(Global.PRODUCT_INFO);
			boolean edit = bundle.getBoolean(Global.CAN_EDIT_PRODUCT_INFO);
			txtNameProduct.setText(productInfo.name);
			txtPriceOfProduct.setText("" + productInfo.price);
			txtQuantityOfProduct.setText("" + productInfo.quantity);
			txtWarrantyOfProduct.setText(productInfo.warranty);
			txtOriginOfProduct.setText(productInfo.origin);
			txtAddressOfProduct.setText(productInfo.address);
			if (productInfo.isVAT == true) {
				check1.setChecked(true);
			} else {
				check1.setChecked(false);
			}
			if (edit == false) {
				txtNameProduct.setFilters(Global.uneditableInputFilters);
				txtPriceOfProduct.setFilters(Global.uneditableInputFilters);
				txtQuantityOfProduct.setFilters(Global.uneditableInputFilters);
				txtWarrantyOfProduct.setFilters(Global.uneditableInputFilters);
				txtOriginOfProduct.setFilters(Global.uneditableInputFilters);
				txtAddressOfProduct.setFilters(Global.uneditableInputFilters);
			}

		}
	}
}
