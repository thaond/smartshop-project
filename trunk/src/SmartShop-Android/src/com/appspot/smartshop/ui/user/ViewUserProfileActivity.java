package com.appspot.smartshop.ui.user;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.ui.page.PageActivity;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.product.PostProductActivity;
import com.appspot.smartshop.ui.user.subcribe.UserSubcribeListActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.MyArrayAdapter;
import com.appspot.smartshop.utils.ObjectPool;
import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.Utils;

public class ViewUserProfileActivity extends Activity {
	public static final String TAG = "[ViewUserProfileActivity]";
	
	private ListView listInfos;
	private ImageView imgAvatar;
	private Drawable drawableNoAvatar;
	private ProfileAdapter adapter;
	private UserInfo userInfo;
	private boolean isOwn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);

		// avatar and user basic info
		drawableNoAvatar = getResources().getDrawable(R.drawable.no_avatar);
		adapter = new ProfileAdapter(ViewUserProfileActivity.this,
				R.layout.view_profile_list_item);
		imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
		listInfos = (ListView) findViewById(R.id.listInfo);
		listInfos.setAdapter(adapter);
		
		// user function buttons
		Button btnPages = (Button) findViewById(R.id.btnPages);
		Button btnProducts = (Button) findViewById(R.id.btnProducts);
		Button btnAddNewPage = (Button) findViewById(R.id.btnAddNewPage);
		Button btnPostNewProduct = (Button) findViewById(R.id.btnPostNewProduct);
		Button btnSubcribe = (Button) findViewById(R.id.btnSubcribe);
		Button btnUserInfo = (Button) findViewById(R.id.btnEditUserInfo);

		// view user info
		Bundle tmp = getIntent().getExtras();
		if (tmp != null) {
			// don't show buttons
			btnPages.setVisibility(View.GONE);
			btnProducts.setVisibility(View.GONE);
			btnAddNewPage.setVisibility(View.GONE);
			btnPostNewProduct.setVisibility(View.GONE);
			btnSubcribe.setVisibility(View.GONE);
			btnUserInfo.setVisibility(View.GONE);
			
			// View other profile
			isOwn = false;
			//userInfo = (UserInfo) tmp.get(Global.USER_INFO); vanloi999 has replace this statement by the next one
			userInfo = Global.userInfo;
			viewProfile();
		} else if (Global.userInfo == null) {
			
			Utils.createOKDialog(
					this, getString(R.string.notice),
					getString(R.string.errMustLoginToViewProfile),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									ViewUserProfileActivity.this,
									LoginActivity.class);
							intent.putExtra(Global.LOGIN_LAST_ACTIVITY,
									Global.VIEW_PROFILE_ACTIVITY);
							startActivity(intent);
						}
					});
		} else {
			// View my profile
			userInfo = Global.userInfo;
			viewProfile();
			
			// set listener for buttons
			btnPages.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG, "[VIEW PAGES OF CURRENT USER] " + Global.username);
					Intent intent = new Intent(ViewUserProfileActivity.this, PagesListActivity.class);
					intent.putExtra(Global.PAGES_TYPE, PagesListActivity.PAGES_OF_USER);
					intent.putExtra(Global.PAGES_OF_USER, Global.username);
					startActivity(intent);
				}
			});
			
			btnProducts.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG, "[VIEW PRODUCTS OF CURRENT USER] " + Global.username);
					Intent intent = new Intent(ViewUserProfileActivity.this, UserProductListActivity.class);
					intent.putExtra(Global.PRODUCTS_OF_USER, Global.username);
					startActivity(intent);
				}
			});
			
			btnAddNewPage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG, "[CREATE NEW PAGE]");
					Intent intent = new Intent(ViewUserProfileActivity.this, PageActivity.class);
					startActivity(intent);
				}
			});
			
			btnPostNewProduct.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG, "[POST NEW PRODUCT]");
					Intent intent = new Intent(ViewUserProfileActivity.this, PostProductActivity.class);
					startActivity(intent);
				}
			});
			
			btnSubcribe.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO subcribe products
					Log.d(TAG, "[SUBCRIBE PRODUCTS]");
					showSubcribeList();
				}
			});
			
			btnUserInfo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG, "[EDIT INFO OF CURRENT USER]");
					Intent intent = new Intent(ViewUserProfileActivity.this, ViewUserInfoActivity.class);
					intent.putExtra(Global.USER_INFO, Global.userInfo);
					startActivity(intent);
				}
			});
		}
	}

	protected void showSubcribeList() {
		Intent intent = new Intent(this, UserSubcribeListActivity.class);
		startActivity(intent);
	}

	private void viewProfile() {
//		userInfo.avatarLink = "http://10.0.2.2/uploads/tam1234/a.jpg";
		Log.d(TAG, "fdsfdsfs");
		if (StringUtils.isEmptyOrNull(userInfo.avatarLink)) {
			imgAvatar.setBackgroundDrawable(drawableNoAvatar);
		} else {
			try {
				Bitmap bitmapAvatar = Utils
						.getBitmapFromURL(userInfo.avatarLink);
				bitmapAvatar = Bitmap.createScaledBitmap(bitmapAvatar,
						150, 150,
						true);
				imgAvatar.setImageBitmap(bitmapAvatar);
			} catch (Exception e) {
				e.printStackTrace();
				imgAvatar.setBackgroundDrawable(drawableNoAvatar);
			}
		}

		adapter.clear();
		ViewProfileItem item = ViewProfileItem.pool.borrow();
		item.name = getString(R.string.username);
		item.value = userInfo.username;
		adapter.add(item);

		item = ViewProfileItem.pool.borrow();
		item.name = getString(R.string.name);
		item.value = userInfo.first_name;
		adapter.add(item);

		if (!StringUtils.isEmptyOrNull(userInfo.last_name)) {
			item = ViewProfileItem.pool.borrow();
			item.name = getString(R.string.lastname);
			item.value = userInfo.last_name;
			adapter.add(item);
		}

		item = ViewProfileItem.pool.borrow();
		item.name = getString(R.string.email);
		item.value = userInfo.email;
		adapter.add(item);

		if (userInfo.birthday != null) {
			item = ViewProfileItem.pool.borrow();
			item.name = getString(R.string.birthday);
			item.value = Global.df.format(userInfo.birthday);
			adapter.add(item);
		}

		if (!StringUtils.isEmptyOrNull(userInfo.address)) {
			item = ViewProfileItem.pool.borrow();
			item.name = getString(R.string.address);
			item.value = userInfo.address;
			adapter.add(item);
		}

		if (StringUtils.isEmptyOrNull(userInfo.phone)) {
			item = ViewProfileItem.pool.borrow();
			item.name = getString(R.string.tel);
			item.value = userInfo.phone;
			adapter.add(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isOwn)
			menu.add(0, R.string.edit_profile, 0,
					getString(R.string.edit_profile));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.string.edit_profile:
			Intent intent = new Intent(ViewUserProfileActivity.this,
					ViewUserInfoActivity.class);
			intent.putExtra(Global.USER_INFO, Global.userInfo);
			startActivityForResult(intent, R.string.edit_profile);
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case R.string.edit_profile:
			switch (resultCode) {
			case RESULT_OK:
				viewProfile();
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	class ProfileAdapter extends MyArrayAdapter<ViewProfileItem> {
		public static final String TAG = "[CurrentUserProfileAdapter]";
		private LayoutInflater inflater;
		private int textViewResourceId;

		public ProfileAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			this.textViewResourceId = textViewResourceId;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(textViewResourceId, null);
				ImageView image = (ImageView) convertView
						.findViewById(R.id.image);
				TextView name = (TextView) convertView.findViewById(R.id.name);
				TextView value = (TextView) convertView
						.findViewById(R.id.value);

				ViewProfileItem item = getItem(position);
				if (item.icon != null) {
					image.setBackgroundDrawable(item.icon);
				}

				name.setText(item.name);
				value.setText(item.value);
			}

			return convertView;
		}

		public long getItemId(int position) {
			return position;
		}

	}
}

class ViewProfileItem {
	Drawable icon;
	String name;
	String value;

	static final ObjectPool<ViewProfileItem> pool = new ObjectPool<ViewProfileItem>() {

		@Override
		public ViewProfileItem createExpensiveObject() {
			return new ViewProfileItem();
		}
	};

	public ViewProfileItem(String name, String value, Drawable icon) {
		this.name = name;
		this.value = value;
		this.icon = icon;
	}

	public ViewProfileItem() {
	}
}
