package com.appspot.smartshop.ui.product;

import java.util.Date;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.map.MapDialog.UserLocationListener;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

public class ProductBasicAttributeActivity extends MapActivity {
	public static final String TAG = "[PostProductActivityBasicAttribute]";
	
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

	public ProductInfo productInfo = new ProductInfo();;
	private CheckBox chVat;
	private double lat, lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		
		// buttons
		btnChooseCategory = (Button) findViewById(R.id.btnChooseCategory);
		btnChooseCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseCategories();
			}
		});
		
		btnTagOnMap = (Button) findViewById(R.id.btnTagOnMap);
		btnTagOnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tagOnMap();
			}
		});
		
		btnOK = (Button) findViewById(R.id.btnXong);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postNewProduct();
			}
		});
		
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// vat check box
		chVat = (CheckBox) findViewById(R.id.checkBoxIsVAT);
	}
	
	private SimpleAsyncTask task;
	private void postNewProduct() {
		// setup product info
		productInfo.datePost = new Date();
		productInfo.description = txtDescriptionOfProduct.getText().toString();
		productInfo.isVAT = chVat.isChecked();
		productInfo.lat = lat;
		productInfo.lng = lng;
		productInfo.name = txtNameProduct.getText().toString();
		productInfo.origin = txtOriginOfProduct.getText().toString();
		productInfo.price = Double.parseDouble(txtPriceOfProduct
				.getText().toString());
		productInfo.quantity = Integer.parseInt(txtQuantityOfProduct
				.getText().toString());
		productInfo.lat = lat;
		productInfo.lng = lng;
		productInfo.datePost = new Date();
		productInfo.description = txtDescriptionOfProduct.getText().toString();
		productInfo.username = Global.username;
		productInfo.warranty = txtWarrantyOfProduct.getText().toString();
		productInfo.address = txtAddressOfProduct.getText().toString();
		// TODO attribute of categories
		productInfo.setAttributes = ProductUserDefinedAttributeActivity.setAttributes;
		
		// post new product
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
			}
			
			@Override
			public void loadData() {
				String param = Global.gsonWithHour.toJson(productInfo);
				Log.d(TAG, param);
				
				RestClient.postData(URLConstant.POST_PRODUCT, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
					}
					
					@Override
					public void onFailure(String message) {
						task.cancel(true);
					}
				});
			}
		});
		task.execute();
	}

	protected void chooseCategories() {
		CategoriesDialog.showCategoriesDialog(this, new CategoriesDialogListener() {
			
			@Override
			public void onCategoriesDialogClose(Set<String> categories) {
				productInfo.setCategoryKeys = categories;
			}
		});
	}

	protected void tagOnMap() {
		String location = txtAddressOfProduct.getText().toString();

		MapDialog.createLocationDialog(this,
				MapService.locationToGeopoint(location),
				new UserLocationListener() {

					@Override
					public void processUserLocation(GeoPoint point) {
						if (point != null) {
							lat = (double)point.getLatitudeE6() / 1E6;
							lng = (double)point.getLongitudeE6() / 1E6;
						}
						Log.d(TAG, "product location = " + lat + ", " + lng);
					}
				}).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
