package com.appspot.smartshop.ui.product;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Media;
import com.appspot.smartshop.utils.StringUtils;

public class ProductGalleryActivity extends Activity implements ViewFactory {
	private static final String TAG = "[ProductGallery]";
	private ImageSwitcher imageView;
	public static List<Media> listMediaBitmap = new ArrayList<Media>();
	private TextView lblName;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_gallery);

		// initMockTest();

		Gallery ga = (Gallery) findViewById(R.id.Gallery01);
		BaseAdapter adapter = new ImageAdapter(this);
		ga.setAdapter(adapter);

		imageView = (ImageSwitcher) findViewById(R.id.ImageView01);
		imageView.setFactory(this);
		imageView.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		imageView.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		lblName = (TextView) findViewById(R.id.lblName);
		// if (!listMediaBitmap.isEmpty()){
		// if (!StringUtils
		// .isEmptyOrNull(listMediaBitmap.get(0).description))
		// Toast.makeText(getBaseContext(),
		// listMediaBitmap.get(0).description,
		// Toast.LENGTH_SHORT).show();
		// imageView.setImageDrawable(listMediaBitmap.get(0)
		// .getDrawable());
		// lblName.setText(listMediaBitmap.get(0).name);
		// }

		ga.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (!StringUtils
						.isEmptyOrNull(listMediaBitmap.get(position).description))
					Toast.makeText(getBaseContext(),
							listMediaBitmap.get(position).description,
							Toast.LENGTH_SHORT).show();
				imageView.setImageDrawable(listMediaBitmap.get(position)
						.getDrawable());
				lblName.setText(listMediaBitmap.get(position).name);
			}
		});

		if (!listMediaBitmap.isEmpty()) {
			if (!StringUtils
					.isEmptyOrNull(listMediaBitmap.get(0).description))
				Toast.makeText(getBaseContext(),
						listMediaBitmap.get(0).description,
						Toast.LENGTH_SHORT).show();
			imageView.setImageDrawable(listMediaBitmap.get(0)
					.getDrawable());
			lblName.setText(listMediaBitmap.get(0).name);
		}
	}

	public View makeView() {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundColor(0xFF000000);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return imageView;
	}

	private void initMockTest() {
		listMediaBitmap.add(new Media("Amage",
				"http://10.0.2.2:8888/image_host/product/img1.jpg", "jpg",
				"Desc"));
		listMediaBitmap.add(new Media("Amage",
				"http://10.0.2.2:8888/image_host/product/img2.jpg", "jpg",
				"Desc"));
	}

	public class ImageAdapter extends BaseAdapter {

		private Context context;
		private int itemBackground;

		public ImageAdapter(Context c) {
			context = c;

			// ---setting the style---
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			itemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		// ---returns the number of images---
		public int getCount() {
			return listMediaBitmap.size();
		}

		// ---returns the ID of an item---
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// ---returns an ImageView view---
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(context);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setImageDrawable(listMediaBitmap.get(position)
					.getDrawable());
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
			imageView.setBackgroundResource(itemBackground);

			return imageView;
		}

	}
}
