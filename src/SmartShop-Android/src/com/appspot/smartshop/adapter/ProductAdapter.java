package com.appspot.smartshop.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.ui.product.ViewProductActivity;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;

public class ProductAdapter extends ArrayAdapter<ProductInfo> {
	public static final String TAG = "[ProductAdapter]";

	private Context context;
	private LayoutInflater inflater;

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
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.product_list_item, null);

			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.btnMap = (Button) convertView.findViewById(R.id.btnMap);
			holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtProductName);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
			holder.txtDatePost = (TextView) convertView.findViewById(R.id.txtDatePost);

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

		return convertView;
	}

	static class ViewHolder {
		ImageView image;
		TextView txtName;
		TextView txtPrice;
		TextView txtDescription;
		TextView txtDatePost;
		Button btnMap;
	}
}