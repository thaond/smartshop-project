package com.appspot.smartshop.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.facebook.AsyncFacebookRunner;
import com.appspot.smartshop.facebook.BaseRequestListener;
import com.appspot.smartshop.facebook.Facebook;
import com.appspot.smartshop.facebook.FacebookError;
import com.appspot.smartshop.facebook.SessionStore;
import com.appspot.smartshop.facebook.Util;
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.ui.product.ViewProductActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.URLConstant;
import com.google.android.maps.GeoPoint;

public class ProductAdapter extends ArrayAdapter<ProductInfo> {
	public static final String TAG = "[ProductAdapter]";

	private Context context;
	private LayoutInflater inflater;
	public ViewHolder holder;
	private Bundle params = new Bundle();// contain information for post product
	// to facebook

	public static final int IMAGE_WIDTH = 50;
	public static final int IMAGE_HEIGHT = 50;

	private int textViewResourceId;

	public ProductAdapter(Context context, int textViewResourceId,
			List<ProductInfo> objects) {
		super(context, textViewResourceId, objects);
		this.textViewResourceId = textViewResourceId;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public ProductAdapter(Context context, int textViewResourceId,
			ProductInfo[] objects) {
		super(context, textViewResourceId, objects);
		this.textViewResourceId = textViewResourceId;

		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public ProductAdapter(Context context, int textViewResourceId) {
		this(context, textViewResourceId, new ProductInfo[] {});
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(textViewResourceId, null);

			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.btnMap = (Button) convertView.findViewById(R.id.btnMap);
			holder.txtDescription = (TextView) convertView
					.findViewById(R.id.txtDescription);
			holder.txtName = (TextView) convertView
					.findViewById(R.id.txtProductName);
			holder.txtPrice = (TextView) convertView
					.findViewById(R.id.txtProductPrice);
			holder.txtDatePost = (TextView) convertView
					.findViewById(R.id.txtDatePost);
			holder.postFacebook = (ImageView) convertView
					.findViewById(R.id.btnPostFb);
			if (Global.mFacebook != null && !Global.mFacebook.isSessionValid()) {
				holder.postFacebook.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ProductInfo productInfo = (ProductInfo) getItem(position);
		holder.txtName.setText(productInfo.name);
		holder.txtPrice.setText("" + productInfo.price);
		holder.txtDescription.setText(productInfo.description);
		if (productInfo.date_post != null) {
			holder.txtDatePost.setText(Global.dfFull
					.format(productInfo.date_post));
		}
		holder.btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (productInfo.lat == 0 && productInfo.lng == 0) {
					Log.d(TAG, "product has no lat, long");
					Toast
							.makeText(
									context,
									context
											.getString(R.string.warnProductHasNoAddress),
									Toast.LENGTH_SHORT).show();
				} else {
					Log.d(TAG, "show location of product");
					MapDialog.createProductLocationDialog(
							productInfo.address,
							context,
							new GeoPoint((int) (productInfo.lat * 1E6),
									(int) (productInfo.lng * 1E6))).show();
				}
			}
		});
		holder.postFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postFacebookSmartShop();
			}
		});

		holder.image.setBackgroundDrawable(productInfo.getRandomThumbImage());

		// go to product detail
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "view detail of product");

				// get product info from server
				String url = String.format(URLConstant.GET_PRODUCT_BY_ID,
						productInfo.id);
				RestClient.getData(url, new JSONParser() {

					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						ProductInfo newProductInfo = Global.gsonWithHour
								.fromJson(json.getString("product"),
										ProductInfo.class);
						Intent intent = new Intent(context,
								ViewProductActivity.class);
						intent.putExtra(Global.PRODUCT_INFO, newProductInfo);
						if (Global.isLogin
								&& productInfo.username
										.equals(Global.userInfo.username)) {
							Log.d(TAG, "can edit product profile");
							intent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, true);
						}

						context.startActivity(intent);
					}

					@Override
					public void onFailure(String message) {
						Toast
								.makeText(
										context,
										context
												.getString(R.string.err_cant_get_product_info),
										Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

		// set up information to post on Facebook
		// TODO
		params.putString("message", "Smart Shop");
		params.putString("name", productInfo.name);
		params.putString("picture",
				"http://hangxachtayusa.net/img/p/89-129-medium.jpg");
		params.putString("description", productInfo.description);
		params.putString("link",
				"http://www.hangxachtayusa.net/product.php?id_product=195");

		return convertView;
	}

	protected void postFacebookSmartShop() {
		AsyncFacebookRunner mAsyncRunner;
		if (Global.mFacebook == null)
			Global.mFacebook = new Facebook();
		SessionStore.restore(Global.mFacebook, context);
		mAsyncRunner = new AsyncFacebookRunner(Global.mFacebook);
		if (!Global.mFacebook.isSessionValid()) {
			Toast.makeText(context,
					context.getString(R.string.alertLoginFacebook),
					Toast.LENGTH_SHORT).show();
		} else {
			mAsyncRunner.request("me/feed", params, "POST",
					new WallPostRequestListener());
			Toast.makeText(context,
					context.getString(R.string.postOnFacebookSuccess),
					Toast.LENGTH_LONG).show();
			holder.postFacebook
					.setImageResource(R.drawable.facebook_share_nonactive);
			holder.postFacebook.setClickable(false);
		}
	}

	static class ViewHolder {
		ImageView image;
		TextView txtName;
		TextView txtPrice;
		TextView txtDescription;
		TextView txtDatePost;
		Button btnMap;
		ImageView postFacebook;
	}

	public class WallPostRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(String response) {
			Log.d("Facebook-Example", "Got response: " + response);
			String message = "<empty>";
			try {
				JSONObject json = Util.parseJson(response);
				message = json.getString("message");
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}
}