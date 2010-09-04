package com.appspot.smartshop.ui.product;

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
import android.widget.Toast;

import com.appspot.smartshop.MainActivity;
import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.map.DirectionListActivity;
import com.appspot.smartshop.map.MyLocationListener;
import com.appspot.smartshop.map.MyLocationListener.MyLocationCallback;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.ui.user.UserProfileActivity;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;

public class ViewProductBasicAttributeActivity extends Activity {
	public static final String TAG = "[ViewBasicAttributeOfProduct]";
	
	private TextView lblNameOfProduct;
	private TextView lblPriceOfProduct;
	private TextView lblQuantityOfProduct;
	private TextView lblWarrantyOfProduct;
	private TextView lblOriginOfProduct;
	private TextView lblAddressOfProduct;
	private TextView lblPageViewOfProduct;

	private EditText txtNameProduct;
	private EditText txtPriceOfProduct;
	private EditText txtQuantityOfProduct;
	private EditText txtWarrantyOfProduct;
	private EditText txtOriginOfProduct;
	private EditText txtAddressOfProduct;
	private EditText txtPageViewOfProduct;

	private Button btnViewComment;
	private Button btnViewUserInfo;

	protected static ProductInfo productInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view basic info of product");
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
		lblPageViewOfProduct = (TextView) findViewById(R.id.viewPageViewOfProduct);
		lblPageViewOfProduct.setWidth(labelWidth);
		txtPageViewOfProduct = (EditText) findViewById(R.id.txtViewPageViewOfProduct);

		// buttons
		btnViewComment = (Button) findViewById(R.id.viewComment);
		btnViewComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showComment();
			}
		});
		
		btnViewUserInfo = (Button) findViewById(R.id.viewUserInfo);
		btnViewUserInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewUserProfile();
			}
		});
		
		// set up check box
		final CheckBox chVat = (CheckBox) findViewById(R.id.viewCheckBoxIsVAT);
		
		// fill data of product to form
		txtNameProduct.setText(productInfo.name);
		txtPriceOfProduct.setText("" + productInfo.price);
		txtQuantityOfProduct.setText("" + productInfo.quantity);
		txtWarrantyOfProduct.setText(productInfo.warranty);
		txtOriginOfProduct.setText(productInfo.origin);
		txtAddressOfProduct.setText(productInfo.address);
		txtPageViewOfProduct.setText(productInfo.product_view + "");
		chVat.setChecked(productInfo.isVAT);
		
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
		new MyLocationListener(this, new MyLocationCallback() {
			
			@Override
			public void onSuccess(GeoPoint point) {
				Log.d(TAG, "current location = " + point);
				if (point == null) {
					Toast.makeText(ViewProductBasicAttributeActivity.this, 
							getString(R.string.errCannotFindCurrentLocation),
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				Intent intent = new Intent(ViewProductBasicAttributeActivity.this, DirectionListActivity.class);
				intent.putExtra("lat1", (double) point.getLatitudeE6() / 1E6);
				intent.putExtra("lng1", (double) point.getLongitudeE6() / 1E6);
				intent.putExtra("lat2", (double)productInfo.lat);
				intent.putExtra("lng2", (double)productInfo.lng);
				
				startActivity(intent);
			}
			
			@Override
			public void onFailure() {
			}
		}).findCurrentLocation();
	}

	protected void showComment() {
		Intent intent = new Intent(ViewProductBasicAttributeActivity.this, ViewCommentsActivity.class);
		intent.putExtra(Global.ID_OF_COMMENTS, productInfo.id);
		intent.putExtra(Global.TYPE_OF_COMMENTS, MainActivity.PRODUCT);
		startActivity(intent);
	}

	protected void viewUserProfile() {
		Intent intent = new Intent(this, UserProfileActivity.class);
		intent.putExtra(Global.USER_NAME, productInfo.username);
		if (Global.isLogin && Global.username.equals(productInfo.username)) {
			intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
		}
		
		startActivity(intent);
	}
}
