package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;

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
	public static final int POST_PRODUCT =0;
	public static final int VIEW_PRODUCT = 1;
	public static final int EDIT_PRODUCT = 2;
	public int mode = POST_PRODUCT;
	
	
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
	
	public ProductInfo productInfo = null;
	
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
		//set up check box
		final CheckBox check1 = (CheckBox) findViewById(R.id.checkBoxIsVAT);
		final TextView txtVAT = (TextView) findViewById(R.id.isVATInPostProductForm);
		
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
			mode = EDIT_PRODUCT;
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
			Boolean canEditProductInfo = bundle.getBoolean(Global.CAN_EDIT_PRODUCT_INFO);
			if(canEditProductInfo==null || canEditProductInfo == false){
				mode = VIEW_PRODUCT;
				txtNameProduct.setEnabled(false);
			}
		}
	}

}
