package com.appspot.smartshop.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.NProductVatGia;
import com.appspot.smartshop.facebook.utils.FacebookUtils;
import com.appspot.smartshop.ui.product.vatgia.VatgiaTabActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class NProductVatgiaAdapter extends ArrayAdapter<NProductVatGia> {
	public ViewHolder holder;
	public static final int IMAGE_WIDTH = 50;
	public static final int IMAGE_HEIGHT = 50;
	private LayoutInflater inflater;
	private Context context;
	String webUrlOfProduct = "";
	// to facebook
	private FacebookUtils facebookUtils;

	public NProductVatgiaAdapter(Context context, int textViewResourceId,
			List<NProductVatGia> objects, FacebookUtils facebookUtils) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.facebookUtils = facebookUtils;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.nproductvatgia_list_item,
					null);

			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.txtName = (TextView) convertView
					.findViewById(R.id.txtProductName);
			holder.txtPrice = (TextView) convertView
					.findViewById(R.id.txtProductPrice);
			holder.btnListShop = (Button) convertView
					.findViewById(R.id.btnListShop);
			holder.txtNumOfStore = (TextView) convertView
					.findViewById(R.id.txtNumOfStore);
			holder.postFacebook = (ImageView) convertView
					.findViewById(R.id.btnPostFacebookVatGia);
			// if(!Global.mFacebook.isSessionValid()){
			// holder.postFacebook.setVisibility(View.GONE);
			// }

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// fill data to list item
		final NProductVatGia item = (NProductVatGia) getItem(position);

		Bitmap imageOfProduct = Utils.getBitmapFromURL(item.imageThumbnail);

		imageOfProduct = Bitmap.createScaledBitmap(imageOfProduct, IMAGE_WIDTH,
				IMAGE_HEIGHT, true);

		holder.image.setImageBitmap(imageOfProduct);

		holder.txtName.setText(item.productName);
		String numOfStore = String.format(Global.application
				.getString(R.string.num_of_stores), item.numOfStore);
		holder.txtNumOfStore.setText(numOfStore);
		holder.txtPrice.setText(item.priceVND);
		holder.btnListShop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, VatgiaTabActivity.class);
				String url = String.format(
						URLConstant.GET_DETAIL_OF_VATGIA_PRODUCT,
						item.urlListShop);
				intent.putExtra(Global.VATGIA_URL_LIST_SHOP, url);
				context.startActivity(intent);
			}
		});

		holder.postFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postFacebookVatGia(item);
			}
		});

		return convertView;
	}
	
	protected void postFacebookVatGia(NProductVatGia item) {
		// set up information to post on Facebook
		webUrlOfProduct = item.urlListShop.substring(0, 7) + item.urlListShop.substring(9, item.urlListShop.length());
		facebookUtils.sendMessage("SmartShop - Giới thiệu sản phẩm",
				item.productName, webUrlOfProduct, item.priceVND,
				webUrlOfProduct, new FacebookUtils.SimpleWallpostListener(
						facebookUtils.getActivity(), Global.application
								.getString(R.string.postOnFacebookSuccess)));
		// AsyncFacebookRunner mAsyncRunner;
		// Global.mFacebook = new Facebook();
		// SessionStore.restore(Global.mFacebook, context);
		// mAsyncRunner = new AsyncFacebookRunner(Global.mFacebook);
		// if (!Global.mFacebook.isSessionValid()) {
		// Toast.makeText(context,
		// context.getString(R.string.alertLoginFacebook),
		// Toast.LENGTH_SHORT).show();
		// } else {
		//			
		// Log.d("TAG", webUrlOfProduct);
		// mAsyncRunner.request("me/feed", params, "POST",
		// new WallPostRequestListener());
		// Toast.makeText(context,
		// context.getString(R.string.postOnFacebookSuccess),
		// Toast.LENGTH_LONG).show();
		 holder.postFacebook.setImageResource(R.drawable.facebook_share_nonactive);
		 holder.postFacebook.setClickable(false);
		// }

	}

	static class ViewHolder {
		ImageView image;
		TextView txtName;
		TextView txtPrice;
		TextView txtNumOfStore;
		Button btnListShop;
		ImageView postFacebook;
	}

}
