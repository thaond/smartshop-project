package com.appspot.smartshop.ui.product;

import java.util.Date;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.AddFriendAdapter;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.facebook.AsyncFacebookRunner;
import com.appspot.smartshop.facebook.BaseRequestListener;
import com.appspot.smartshop.facebook.Facebook;
import com.appspot.smartshop.facebook.FacebookError;
import com.appspot.smartshop.facebook.LoginButton;
import com.appspot.smartshop.facebook.SessionEvents;
import com.appspot.smartshop.facebook.SessionStore;
import com.appspot.smartshop.facebook.Util;
import com.appspot.smartshop.facebook.SessionEvents.AuthListener;
import com.appspot.smartshop.facebook.SessionEvents.LogoutListener;
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.map.MapDialog.UserLocationListener;
import com.appspot.smartshop.mock.MockAddFriend;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

public class ProductBasicAttributeActivity extends MapActivity {
	public static final String TAG = "[ProductBasicAttributeActivity]";
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
	public Button btnTagFriends;
	public ProductInfo productInfo = new ProductInfo();;
	private CheckBox chVat;
	private double lat, lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.post_basic_product_attribute);
		// set up labelWidth and textWidth
		int width = Utils.getScreenWidth();
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
		// Tag Friends to Product
		btnTagFriends = (Button) findViewById(R.id.btnTagFriends);

		btnTagFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogTagFriends();

			}
		});
	}


	public LayoutInflater inflater;
	public View view;
	public static final String PARAM_SEARCH_FRIENDS = "{username:\"%s\"}";
	public static final String PARAM_ADD_FRIENDS = "{username:\"%s\",friends:%s}";
	public static String friendsToAdd = "";
	public AddFriendAdapter adapter;
	public ListView friendList;
	public LinkedList<UserInfo> friends;
	public Button btnFriendSearch;
	public TextView txtFriendSearch;
	public Button btnAddFriend;
	private Builder dialogBuilder;
	private AlertDialog dialog;

	protected void showDialogTagFriends() {
		// setup view
		if (inflater == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (view == null) {
			view = inflater.inflate(R.layout.add_friend, null);
		}
		friends = new LinkedList<UserInfo>();
		friendList = (ListView) view.findViewById(R.id.listFriend);
		adapter = new AddFriendAdapter(this, 0, MockAddFriend.getInstance());
		friendList.setAdapter(adapter);
		// create dialog
		dialogBuilder = new AlertDialog.Builder(
				ProductBasicAttributeActivity.this);
		ViewGroup parent = ((ViewGroup) view.getParent());
		if (parent != null) {
			parent.removeView(view);
		}
		dialogBuilder.setView(view);
		dialog = dialogBuilder.create();
		dialog.show();

	}

	public static SimpleAsyncTask taskAddFriend;
	boolean isSuccess = false;

	protected void tagFriends() {
		// TODO (condohero01) call service to tag friends (similar addFriends in
		// AddFriendActivity)

	}

	protected void loadFriendList() {
		// TODO (condohero01) call service to load friends list (similar to
		// loadFriendList in AddFriendActivity)

	}

	private SimpleAsyncTask task;

	private void getProductInfo() {
		// setup product info
		productInfo.date_post = new Date();
		productInfo.description = txtDescriptionOfProduct.getText().toString();
		productInfo.isVAT = chVat.isChecked();
		productInfo.lat = lat;
		productInfo.lng = lng;
		productInfo.name = txtNameProduct.getText().toString();
		productInfo.origin = txtOriginOfProduct.getText().toString();
		productInfo.price = Double.parseDouble(txtPriceOfProduct.getText()
				.toString());
		productInfo.quantity = Integer.parseInt(txtQuantityOfProduct.getText()
				.toString());
		productInfo.lat = lat;
		productInfo.lng = lng;
		productInfo.date_post = new Date();
		productInfo.description = txtDescriptionOfProduct.getText().toString();
		productInfo.username = Global.userInfo.username;
		productInfo.warranty = txtWarrantyOfProduct.getText().toString();
		productInfo.address = txtAddressOfProduct.getText().toString();
		productInfo.attributeSets = ProductUserDefinedAttributeActivity.setAttributes;
		productInfo.setMedias = ProductUploadImagesActivity.setMedias;
	}

	private void doImagesUpload() {
		RestClient.getData(URLConstant.URL_UPLOAD_IMG_PRODUCT,
				new JSONParser() {

					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						Toast.makeText(ProductBasicAttributeActivity.this,
								json.getString("message"), Toast.LENGTH_SHORT).show();
						finish();
					}

					@Override
					public void onFailure(String message) {
						Toast.makeText(ProductBasicAttributeActivity.this,
								message, Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void postNewProduct() {
		// setup product info
		getProductInfo();

		String images = URLConstant.HOST + "/image_host/product/img%s.jpg";
		for (int i = 0; i < Math.min(productInfo.setMedias.size(), 7); i++) {
			productInfo.setMedias.get(i).link = String.format(images, i + 1);
		}

		// post new product
		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
			}

			@Override
			public void loadData() {
				String param = Global.gsonWithHour.toJson(productInfo);
				Log.d(TAG, param);

				RestClient.postData(URLConstant.POST_PRODUCT, param,
						new JSONParser() {

							@Override
							public void onSuccess(JSONObject json)
									throws JSONException {
								int errCode = Integer.parseInt(""
										+ json.get("errCode"));
								String message = json.getString("message");
								switch (errCode) {
								case 0:
									//TODO
//									Toast.makeText(
//											ProductBasicAttributeActivity.this,
//											message, Toast.LENGTH_SHORT).show();
//									finish();
									break;

								default:
									//TODO
//									Toast.makeText(
//											ProductBasicAttributeActivity.this,
//											message, Toast.LENGTH_SHORT).show();
									break;
								}
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
		CategoriesDialog.showCategoriesDialog(this,
				new CategoriesDialogListener() {

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
							lat = (double) point.getLatitudeE6() / 1E6;
							lng = (double) point.getLongitudeE6() / 1E6;
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
