package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.basic_product_attribute);
		//set up labelWidth and textWidth
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		int labelWidth = (int) (width * 0.25);
		//set up TextView and EditText
		lblNameOfProduct = (TextView) findViewById(R.id.nameOfProduct);
		lblNameOfProduct.setWidth(labelWidth);
		lblPriceOfProduct = (TextView) findViewById(R.id.priceOfProduct);
		lblPriceOfProduct.setWidth(labelWidth);
		lblQuantityOfProduct = (TextView) findViewById(R.id.quantityOfProduct);
		lblQuantityOfProduct.setWidth(labelWidth);
		lblWarrantyOfProduct = (TextView) findViewById(R.id.warrantyOfProduct);
		lblWarrantyOfProduct.setWidth(labelWidth);
		lblOriginOfProduct = (TextView) findViewById(R.id.originOfProduct);
		lblOriginOfProduct.setWidth(labelWidth);
		lblAddressOfProduct = (TextView) findViewById(R.id.addressOfProduct);
		lblAddressOfProduct.setWidth(labelWidth);
		
		//set up check box
		final CheckBox check1 = (CheckBox) findViewById(R.id.checkBoxIsVAT);
		final TextView txtVAT = (TextView) findViewById(R.id.isVATInPostProductForm);

		check1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(check1.isChecked()) {
					txtVAT.setText("clicked");
				}
				else {
					txtVAT.setText("is not clicked");
				}
			}
		});
	}

}
