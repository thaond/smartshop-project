package com.appspot.smartshop.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.appspot.smartshop.adapter.NProductVatgiaAdapter.WallPostRequestListener;
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
import com.google.android.maps.GeoPoint;

public class ProductAdapter extends ArrayAdapter<ProductInfo> {
	public static final String TAG = "[ProductAdapter]";

	private Context context;
	private LayoutInflater inflater;
	public Bundle params = new Bundle();//contain information for post product to facebook
	public ProductAdapter(Context context, int textViewResourceId,
			List<ProductInfo> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public ProductAdapter(Context context, int textViewResourceId,
			ProductInfo[] objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public ProductAdapter(Context context, int textViewResourceId) {
		this(context, textViewResourceId, new ProductInfo[] {});
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.product_list_item, null);

			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.btnMap = (Button) convertView.findViewById(R.id.btnMap);
			holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtProductName);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
			holder.txtDatePost = (TextView) convertView.findViewById(R.id.txtDatePost);
			holder.postFacebook = (ImageView) convertView.findViewById(R.id.btnPostFb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ProductInfo productInfo = (ProductInfo) getItem(position);
		holder.txtName.setText(productInfo.name);
		holder.txtPrice.setText("" + productInfo.price);
		holder.txtDescription.setText(productInfo.description);
		if (productInfo.date_post != null) {
			holder.txtDatePost.setText(Global.dfFull.format(productInfo.date_post));
		}
		holder.btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (productInfo.lat == 0 && productInfo.lng == 0) {
					Log.d(TAG, "product has no lat, long");
					Toast.makeText(context, context.getString(R.string.warnProductHasNoAddress), 
							Toast.LENGTH_SHORT).show();
				} else {
					Log.d(TAG, "show location of product");
					MapDialog.createProductLocationDialog(productInfo.address,
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
				holder.postFacebook.setImageResource(R.drawable.facebook_share_nonactive);
				holder.postFacebook.setClickable(false);
			}
		});
		
//		// TODO Load image of product from internet
//		String url = "http://hangxachtayusa.net/img/p/89-129-medium.jpg";
//		Bitmap imageOfProduct = getBitmapFromURL(url);
//		imageOfProduct = Bitmap.createScaledBitmap(imageOfProduct,imageOfProduct.getWidth(), imageOfProduct.getHeight(), true);
//		holder.image.setImageBitmap(imageOfProduct);
		// TODO sample image
		holder.image.setBackgroundResource(R.drawable.icon);
		
		// go to product detail
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "view detail of product");
				Intent intent = new Intent(context, ViewProductActivity.class);
				intent.putExtra(Global.PRODUCT_INFO, productInfo);
				if (Global.isLogin && productInfo.username.equals(Global.username)) {
					Log.d(TAG, "can edit product profile");
					intent.putExtra(Global.CAN_EDIT_PRODUCT_INFO, true);
				}

				context.startActivity(intent);
			}
		});
		//set up information to post on Facebook
		params.putString("message", "Smart Shop");
		params.putString("name", productInfo.name);
		params.putString("picture", "http://hangxachtayusa.net/img/p/89-129-medium.jpg" );
		params.putString("description",
				productInfo.description);
		params.putString("link",
				"http://www.hangxachtayusa.net/product.php?id_product=195");

		return convertView;
	}

	protected void postFacebookSmartShop() {
		AsyncFacebookRunner mAsyncRunner;
		Global.mFacebook = new Facebook();
		SessionStore.restore(Global.mFacebook, context);
		mAsyncRunner = new AsyncFacebookRunner(Global.mFacebook);
		if (!Global.mFacebook.isSessionValid()) {
			Toast.makeText(context,
					context.getString(R.string.alertLogin),
					Toast.LENGTH_SHORT).show();
		} else {
			mAsyncRunner.request("me/feed", params, "POST",
					new WallPostRequestListener());
			Toast.makeText(context,
					context.getString(R.string.postOnFacebookSuccess),
					Toast.LENGTH_LONG).show();
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