package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


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
	
	public Button btnBack;
	public Button btnExit;
	
	public ProductInfo productInfo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_basic_attribute_of_product);
		//set up labelWidth and textWidth
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		int labelWidth = (int) (width * 0.25);
		//set up TextView and EditText
		lblNameOfProduct = (TextView) findViewById(R.id.viewNameOfProduct);
		lblNameOfProduct.setWidth(labelWidth);
		txtNameProduct = (EditText) findViewById(R.id.txtViewNameOfProduct);
		txtNameProduct.setEnabled(false);
		
		lblPriceOfProduct = (TextView) findViewById(R.id.viewPriceOfProduct);
		lblPriceOfProduct.setWidth(labelWidth);
		txtPriceOfProduct = (EditText) findViewById(R.id.txtViewPriceOfProduct);
		txtPriceOfProduct.setEnabled(false);
		
		lblQuantityOfProduct = (TextView) findViewById(R.id.viewQuantityOfProduct);
		lblQuantityOfProduct.setWidth(labelWidth);
		txtQuantityOfProduct = (EditText) findViewById(R.id.txtViewQuantityOfProduct);
		txtQuantityOfProduct.setFilters(Global.uneditableInputFilters);
		
		lblWarrantyOfProduct = (TextView) findViewById(R.id.viewWarrantyOfProduct);
		lblWarrantyOfProduct.setWidth(labelWidth);
		txtWarrantyOfProduct = (EditText) findViewById(R.id.txtViewWarrantyOfProduct);
		txtWarrantyOfProduct.setFilters(Global.uneditableInputFilters);
		
		lblOriginOfProduct = (TextView) findViewById(R.id.viewOriginOfProduct);
		lblOriginOfProduct.setWidth(labelWidth);
		txtOriginOfProduct = (EditText) findViewById(R.id.txtViewOriginOfProduct);
		txtOriginOfProduct.setFilters(Global.uneditableInputFilters);
		
		lblAddressOfProduct = (TextView) findViewById(R.id.viewAddressOfProduct);
		lblAddressOfProduct.setWidth(labelWidth);
		txtAddressOfProduct = (EditText) findViewById(R.id.txtViewAddressOfProduct);
		txtAddressOfProduct.setFilters(Global.uneditableInputFilters);
		//set up check box
		final CheckBox check1 = (CheckBox) findViewById(R.id.viewCheckBoxIsVAT);
		check1.setEnabled(false);
		//final TextView txtVAT = (TextView) findViewById(R.id.txtViewIsVATInPostProductForm);
		
//		check1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(check1.isChecked()) {
//					txtVAT.setText("clicked");
//				}
//				else {
//					txtVAT.setText("is not clicked");
//				}
//			}
//		});
		// setup data for text field if in edit/view product info mode
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			productInfo = (ProductInfo) bundle.get(Global.PRODUCT_INFO);
			txtNameProduct.setText(productInfo.name);
			txtPriceOfProduct.setText(""+productInfo.price);
			if(productInfo.isVAT==true){
				check1.setChecked(true);
			}else{
				check1.setChecked(false);
			}
			txtQuantityOfProduct.setText(""+productInfo.quantity);
			txtWarrantyOfProduct.setText(productInfo.warranty);
			txtOriginOfProduct.setText(productInfo.origin);
			txtAddressOfProduct.setText(productInfo.address);
		}
	}
}
