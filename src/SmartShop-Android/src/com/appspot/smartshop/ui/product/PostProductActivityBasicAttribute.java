package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;
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
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PostProductActivityBasicAttribute extends Activity {

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
	public Button btnOK;
	public Button btnCancel;

	public ProductInfo productInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.basic_product_attribute);
		// set up labelWidth and textWidth
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();
		int labelWidth = (int) (width * 0.25);
		// set up TextView and EditText
		lblNameOfProduct = (TextView) findViewById(R.id.nameOfProduct);
		lblNameOfProduct.setWidth(labelWidth);
		txtNameProduct = (EditText) findViewById(R.id.txtNameOfProduct);

		lblPriceOfProduct = (TextView) findViewById(R.id.priceOfProduct);
		lblPriceOfProduct.setWidth(labelWidth);
		txtPriceOfProduct = (EditText) findViewById(R.id.txtPriceOfProduct);

		lblQuantityOfProduct = (TextView) findViewById(R.id.quantityOfProduct);
		lblQuantityOfProduct.setWidth(labelWidth);
		txtQuantityOfProduct = (EditText) findViewById(R.id.txtQuantityOfProduct);

		lblWarrantyOfProduct = (TextView) findViewById(R.id.warrantyOfProduct);
		lblWarrantyOfProduct.setWidth(labelWidth);
		txtWarrantyOfProduct = (EditText) findViewById(R.id.txtWarrantyOfProduct);

		lblOriginOfProduct = (TextView) findViewById(R.id.originOfProduct);
		lblOriginOfProduct.setWidth(labelWidth);
		txtOriginOfProduct = (EditText) findViewById(R.id.txtOriginOfProduct);

		lblAddressOfProduct = (TextView) findViewById(R.id.addressOfProduct);
		lblAddressOfProduct.setWidth(labelWidth);
		txtAddressOfProduct = (EditText) findViewById(R.id.txtAddressOfProduct);
		// set up check box
		CheckBox check1 = (CheckBox) findViewById(R.id.checkBoxIsVAT);
		TextView txtVAT = (TextView) findViewById(R.id.isVATInPostProductForm);
		btnOK = (Button) findViewById(R.id.btnXong);
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO condohero01: user has finished posting product, put it in database
				// remember to get currently time (post date)
				
			}
		});
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO condohero01
				
			}
		});

		// setup data for text field if in edit/view product info mode
		// TODO:(condohero01) check whether the user has logined or not to post
		// product
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			productInfo = (ProductInfo) bundle.get(Global.PRODUCT_INFO);
			txtNameProduct.setText(productInfo.name);
			txtPriceOfProduct.setText("" + productInfo.price);
			if (productInfo.isVAT == true) {
				check1.setChecked(true);
			} else {
				check1.setChecked(false);
			}
			txtQuantityOfProduct.setText("" + productInfo.quantity);
			txtWarrantyOfProduct.setText(productInfo.warranty);
			txtOriginOfProduct.setText(productInfo.origin);
			txtAddressOfProduct.setText(productInfo.address);
		}
	}
}