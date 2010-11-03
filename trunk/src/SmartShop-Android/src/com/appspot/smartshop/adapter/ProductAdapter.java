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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.facebook.core.FacebookError;
import com.appspot.smartshop.facebook.core.Util;
import com.appspot.smartshop.facebook.utils.BaseRequestListener;
import com.appspot.smartshop.facebook.utils.FacebookUtils;
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

	public boolean isNormalProductList = true;

	private int textViewResourceId;
	private FacebookUtils facebook;

	public ProductAdapter(Context context, int textViewResourceId,
			List<ProductInfo> objects, FacebookUtils facebook) {
		super(context, textViewResourceId, objects);
		this.textViewResourceId = textViewResourceId;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.facebook = facebook;
	}

	public ProductAdapter(Context context, int textViewResourceId,
			List<ProductInfo> objects, boolean isNormalProductList,
			FacebookUtils facebook) {
		this(context, textViewResourceId, objects, facebook);
		this.isNormalProductList = isNormalProductList;
	}

	public ProductAdapter(Context context, int textViewResourceId,
			ProductInfo[] objects, FacebookUtils facebook) {
		super(context, textViewResourceId, objects);
		this.textViewResourceId = textViewResourceId;

		inflater = LayoutInflater.from(context);
		this.context = context;
		this.facebook = facebook;
	}

	public ProductAdapter(Context context, int textViewResourceId,
			FacebookUtils facebook) {
		this(context, textViewResourceId, new ProductInfo[] {}, facebook);
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
			holder.chLike = (CheckBox) convertView.findViewById(R.id.chLike);
			if (!isNormalProductList) {
				holder.chLike.setVisibility(View.GONE);
			} else if (!Global.isLogin) {
				holder.chLike.setVisibility(View.GONE);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// display product info
		final ProductInfo productInfo = (ProductInfo) getItem(position);
		// general info
		holder.txtName.setText(productInfo.name);
		holder.txtPrice.setText("" + productInfo.price);
		holder.txtDescription.setText(productInfo.getShortDescription());
		if (productInfo.date_post != null) {
			holder.txtDatePost.setText(Global.dfFull
					.format(productInfo.date_post));
		}

		// listener
		// map
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

		// facebook
		holder.postFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "Posting on Facebook...");
				if (facebook != null)
					facebook
							.sendMessage(
									"SmartShop - Giới thiệu sản phẩm",
									productInfo.name,
									productInfo.getRandomThumbImageURL(),
									productInfo.getShortDescription(),
									String.format(
											URLConstant.URL_WEBBASED_PRODUCT,
											productInfo.id),
									new FacebookUtils.SimpleWallpostListener(
											facebook.getActivity(),
											Global.application
													.getString(R.string.postOnFacebookSuccess)));
			}
		});

		// avatar
		if (productInfo.setMedias == null || productInfo.setMedias.isEmpty())
			holder.image.setBackgroundResource(R.drawable.product_unknown);
		else {
			Drawable productDrawable = productInfo.setMedias.get(
					(int) (Math.random() * productInfo.setMedias.size()))
					.getDrawable();
			if (productDrawable != null)
				holder.image.setBackgroundDrawable(productDrawable);
			else
				holder.image.setBackgroundResource(R.drawable.product_unknown);
		}
//		holder.image.setBackgroundDrawable(productInfo.getRandomThumbImage());

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

		// mark/unmark product as interest
		if (Global.isLogin) {
			// holder.chLike.setOnClickListener(new OnClickListener() {
			//				
			// @Override
			// public void onClick(View v) {
			// Log.d(TAG, "checkbox = " + holder.chLike.isChecked());
			// if (holder.chLike.isChecked()) {
			// markProductAsInterest(productInfo.id);
			// } else {
			// unmarkProductAsInterest(productInfo.id);
			// }
			// }
			// });
			holder.chLike
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								Log.d(TAG, "[MARK PRODUCT AS INTEREST]");
								markProductAsInterest(productInfo.id);
							} else {
								Log.d(TAG, "[UNMARK PRODUCT AS INTEREST]");
								unmarkProductAsInterest(productInfo.id);
							}
						}
					});
		}

		return convertView;
	}

	private void markProductAsInterest(long productId) {
		String url = String.format(URLConstant.MARK_PRODUCT_AS_INTEREST, Global
				.getSession(), productId);
		RestClient.getData(url, new JSONParser() {

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
			}

			@Override
			public void onFailure(String message) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void unmarkProductAsInterest(long productId) {
		String url = String.format(URLConstant.UNMARK_PRODUCT_AS_INTEREST,
				Global.getSession(), productId);
		RestClient.getData(url, new JSONParser() {

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
			}

			@Override
			public void onFailure(String message) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	static class ViewHolder {
		ImageView image;
		TextView txtName;
		TextView txtPrice;
		TextView txtDescription;
		TextView txtDatePost;
		Button btnMap;
		ImageView postFacebook;
		CheckBox chLike;
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