package com.appspot.smartshop.ui.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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

import com.appspot.smartshop.MainActivity;
import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.map.DirectionListActivity;
import com.appspot.smartshop.map.MyLocation;
import com.appspot.smartshop.map.MyLocationListener;
import com.appspot.smartshop.map.MyLocation.LocationResult;
import com.appspot.smartshop.map.MyLocationListener.MyLocationCallback;
import com.appspot.smartshop.mock.MockUserInfo;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.ui.user.UserActivity;
import com.appspot.smartshop.ui.user.UserProfileActivity;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;

public class ViewBasicAttributeOfProduct extends Activity {
	public static final String TAG = "[ViewBasicAttributeOfProduct]";
	
	public TextView lblNameOfProduct;
	public TextView lblPriceOfProduct;
	public TextView lblQuantityOfProduct;
	public TextView lblWarrantyOfProduct;
	public TextView lblOriginOfProduct;
	public TextView lblAddressOfProduct;
	public TextView lblPageViewOfProduct;

	public EditText txtNameProduct;
	public EditText txtPriceOfProduct;
	public EditText txtQuantityOfProduct;
	public EditText txtWarrantyOfProduct;
	public EditText txtOriginOfProduct;
	public EditText txtAddressOfProduct;
	public EditText txtPageViewOfProduct;

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
		txtNameProduct.setFilters(Global.uneditableInputFilters);
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
		lblPageViewOfProduct = (TextView) findViewById(R.id.viewPageViewOfProduct);
		lblPageViewOfProduct.setWidth(labelWidth);
		txtPageViewOfProduct = (EditText) findViewById(R.id.txtViewPageViewOfProduct);

		// buttons
		btnViewComment = (Button) findViewById(R.id.viewComment);
		btnViewComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewBasicAttributeOfProduct.this, ViewCommentsActivity.class);
				intent.putExtra(Global.ID_OF_COMMENTS, productInfo.id);
				intent.putExtra(Global.TYPE_OF_COMMENTS, MainActivity.PRODUCT);
				startActivity(intent);
			}
		});
		
		btnViewUserInfo = (Button) findViewById(R.id.viewUserInfo);
		btnViewUserInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewBasicAttributeOfProduct.this, UserActivity.class);
				intent.putExtra(Global.USER_INFO, MockUserInfo.getUser(productInfo.username));
				intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
				
				startActivity(intent);
			}
		});
		
		// set up check box
		final CheckBox chVat = (CheckBox) findViewById(R.id.viewCheckBoxIsVAT);
		
		// setup data for text field if in edit/view product info mode
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
			
			boolean canEditProductInfo = bundle.getBoolean(Global.CAN_EDIT_PRODUCT_INFO);
			if (!canEditProductInfo) {
				txtNameProduct.setFilters(Global.uneditableInputFilters);
				txtPriceOfProduct.setFilters(Global.uneditableInputFilters);
				txtQuantityOfProduct.setFilters(Global.uneditableInputFilters);
				txtWarrantyOfProduct.setFilters(Global.uneditableInputFilters);
				txtOriginOfProduct.setFilters(Global.uneditableInputFilters);
				txtAddressOfProduct.setFilters(Global.uneditableInputFilters);
				txtPageViewOfProduct.setFilters(Global.uneditableInputFilters);
			}

		}
		
		btnViewUserInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewUserProfile();
			}
		});
		
		Button btnFindDirectionToProduct = (Button) findViewById(R.id.btnFindDirectionToProduct);
		btnFindDirectionToProduct.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findDirectionToProduct();
			}
		});
		
		TextView txtUsername = (TextView) findViewById(R.id.txtUsername);
		txtUsername.setText(productInfo.username);
	}

	protected void findDirectionToProduct() {
		// TODO loading when find direction
		Log.d(TAG, "find direction to product");
		final Intent intent = new Intent(this, DirectionListActivity.class);
//		MyLocation myLocation = MyLocation.getInstance();
		new MyLocationListener(this, new MyLocationCallback() {
			
			@Override
			public void onSuccess(GeoPoint point) {
				Log.d(TAG, "current location = " + point);
				
				intent.putExtra("lat1", (double) point.getLatitudeE6() / 1E6);
				intent.putExtra("lng1", (double) point.getLongitudeE6() / 1E6);
				intent.putExtra("lat2", (double)productInfo.lat);
				intent.putExtra("lng2", (double)productInfo.lng);
				
				startActivity(intent);
			}
			
			@Override
			public void onFailure() {
			}
		}).getCurrentLocation();
//		myLocation.getLocation(this, new LocationResult() {
//			
//			@Override
//			public void gotLocation(Location location) {
//				Log.d(TAG, "current location = " + location.getLatitude()+ ", " + location.getLongitude());
//				
//				intent.putExtra("lat1", location.getLatitude());
//				intent.putExtra("lng1", location.getLongitude());
//				intent.putExtra("lat2", (double)productInfo.lat / 1E6);
//				intent.putExtra("lng2", (double)productInfo.lng / 1E6);
//				
//				startActivity(intent);
//			}
//		});
	}

	protected void showComment() {
		Intent intent = new Intent(this, ViewCommentsActivity.class);
		intent.putExtra(Global.ID_OF_COMMENTS, Global.username);
		startActivity(intent);
	}

	protected void viewUserProfile() {
		Intent intent = new Intent(this, UserProfileActivity.class);
		intent.putExtra(Global.USER_NAME, productInfo.username);
		
		startActivity(intent);
	}
}
