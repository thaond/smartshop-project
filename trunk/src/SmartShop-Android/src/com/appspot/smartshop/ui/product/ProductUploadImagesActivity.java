package com.appspot.smartshop.ui.product;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.anddev.android.filebrowser.AndroidFileBrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Media;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.MyArrayAdapter;
import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.capture.ImageCaptureActivity;

/**
 * @author VoMinhTam
 */
public class ProductUploadImagesActivity extends Activity {
	private static final String TAG = "[ProductUploadImages]";  
	private static final int FILE_BROWSER_ID = 0;
	private static final int IMAGE_CAPTURE_ID = 1;
	
	public static List<Media> setMedias = new ArrayList<Media>();

	private InputStream inputStream;
	private EditText txtPath;
	private ImageAdapter adapterImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_upload_thumbs);
		
		txtPath = (EditText) findViewById(R.id.txtPath);

		Button btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isEmptyOrNull(txtPath.getText().toString())
						&& inputStream != null) {
					Media imageInfo = new Media();
					imageInfo.name = txtPath.getText().toString();
					imageInfo.inputStream = inputStream;
					
					setMedias.add(imageInfo);
					
					Drawable drawable = Drawable.createFromStream(inputStream,
							"thumb");
					adapterImage.add(drawable);

					inputStream = null;
					txtPath.setText("");
				} else {
					Toast.makeText(ProductUploadImagesActivity.this,
							getString(R.string.please_add_image),
							Toast.LENGTH_SHORT).show();
				}
				
				Log.d(TAG, setMedias + "");
			}
		});

		// Browser sdcard button
		Button btnBrowser = (Button) findViewById(R.id.btnBrowser);
		btnBrowser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFileBrowserActivity();
			}
		});

		// photo button
		Button btnPhoto = (Button) findViewById(R.id.btnPhoto);
		btnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startImageCaptureActivity();
			}
		});
		adapterImage = new ImageAdapter(this);

		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(adapterImage);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
			}
		});

	}

	private void startFileBrowserActivity() {
		Intent intent = new Intent(ProductUploadImagesActivity.this,
				AndroidFileBrowser.class);
		intent.setAction(Global.FILE_BROWSER_ACTIVITY);
		intent.putExtra(Global.FILTER_FILE, Global.IMAGE_FILTER_EXTENSION);
		startActivityForResult(intent, FILE_BROWSER_ID);
	}

	private void startImageCaptureActivity() {
		Intent intent = new Intent(ProductUploadImagesActivity.this,
				ImageCaptureActivity.class);
		intent.setAction(Global.IMAGE_CAPURE_ACTIVITY);
		startActivityForResult(intent, IMAGE_CAPTURE_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case FILE_BROWSER_ID:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					File file = (File) data
							.getSerializableExtra(Global.FILE_INTENT_ID);
					if (file != null) {
						try {
							inputStream = new FileInputStream(file);
							txtPath.setText(file.getName());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
			break;

		case IMAGE_CAPTURE_ID:
			// Return array of byte
			if (resultCode == RESULT_OK) {
				if (data != null) {
					byte[] arrBytes = data
							.getByteArrayExtra(Global.BYTE_ARRAY_INTENT_ID);
					if (arrBytes != null) {
						inputStream = new ByteArrayInputStream(arrBytes);
						String fileName = Global.dfTimeStamp.format(new Date())
								+ ".jpg";

						txtPath.setText(fileName);
					}
				}
			}
			break;

		default:
			break;
		}
	}

	class ImageAdapter extends MyArrayAdapter<Drawable> {

		public ImageAdapter(Context c) {
			super(c);
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
				// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			Drawable drawable = getItem(position);
			imageView.setBackgroundDrawable(drawable);
			return imageView;
		}
	}
}
