package com.appspot.smartshop.ui.product;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.appspot.smartshop.map.DirectionListActivity;
import com.appspot.smartshop.map.DirectionResult;
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.map.SearchProductsOnMapActivity;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;

public class PostProductActivityBasicAttribute extends Activity {
	public static final int PICK_CATEGORIES = 0;
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
	public EditText txtDescriptionOfProduct;
	public Button btnOK;
	public Button btnCancel;
	public Button btnChooseCategory;
	public Button btnTagOnMap;

	public ProductInfo productInfo = null;
	private CheckBox chVat;
	public ArrayList<String> childSelected;
	private EditText txtDescription;
	private int lat, lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		childSelected = new ArrayList<String>();
		setContentView(R.layout.post_basic_product_attribute);

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
		txtDescriptionOfProduct = (EditText) findViewById(R.id.txtDescription);
		txtDescriptionOfProduct.setHeight(labelWidth);
		btnChooseCategory = (Button) findViewById(R.id.btnChooseCategory);
		btnChooseCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendRequestToCategoryDialog();
			}
		});
		btnTagOnMap = (Button) findViewById(R.id.btnTagOnMap);
		btnTagOnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tagOnMap();
			}
		});

		// set up check box

		chVat = (CheckBox) findViewById(R.id.checkBoxIsVAT);
		btnOK = (Button) findViewById(R.id.btnXong);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postNewProduct();
			}

			private void postNewProduct() {
				// TODO condohero01: user has finished posting product, put it
				// in database
				// remember to get currently time (post date)
				productInfo = new ProductInfo();
				productInfo.datePost = new Date();
				productInfo.description = txtDescription.getText().toString();
				productInfo.isVAT = chVat.isChecked();
				productInfo.lat = lat;
				productInfo.lng = lng;
				productInfo.name = txtNameProduct.getText().toString();
				productInfo.origin = txtOriginOfProduct.getText().toString();
				productInfo.price = Double.parseDouble(txtPriceOfProduct
						.getText().toString());
				productInfo.quantity = Integer.parseInt(txtQuantityOfProduct
						.getText().toString());
			}
		});
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// TODO get lat, lng of product

		// TODO get description of product

		// setup data for text field if in edit/view product info mode
		// TODO:(condohero01) check whether the user has logined or not to post
		// product
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			productInfo = (ProductInfo) bundle.get(Global.PRODUCT_INFO);

			txtNameProduct.setText(productInfo.name);
			txtPriceOfProduct.setText("" + productInfo.price);
			txtQuantityOfProduct.setText("" + productInfo.quantity);
			txtWarrantyOfProduct.setText(productInfo.warranty);
			txtOriginOfProduct.setText(productInfo.origin);
			txtAddressOfProduct.setText(productInfo.address);
			if (productInfo.isVAT == true) {
				chVat.setChecked(true);
			} else {
				chVat.setChecked(false);
			}
		}
	}

	protected void tagOnMap() {
		//TODO:(condohero01) show Map Dialog
		
	}

	protected void sendRequestToCategoryDialog() {
		Intent intent = new Intent(PostProductActivityBasicAttribute.this,CategoryDialogActivity.class);
		intent.putExtra(Global.SELECTED_CATEGORIES,childSelected);
		startActivityForResult(intent, PICK_CATEGORIES);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_CATEGORIES) {
			if (resultCode == RESULT_OK) {
					childSelected = data.getStringArrayListExtra(Global.SELECTED_CATEGORIES);
					for(int i =0;i<childSelected.size();i++){
						Log.d("TAG",childSelected.get(i));
					}
					TextView txtChoose = (TextView) findViewById(R.id.txtChoose);
					txtChoose.setText("Đã chọn xong");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	};
}
