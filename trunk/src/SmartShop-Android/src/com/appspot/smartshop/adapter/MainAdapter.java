package com.appspot.smartshop.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.SmartShopActivity;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.product.SearchProductsTabActivity;
import com.appspot.smartshop.ui.user.LoginActivity;
import com.appspot.smartshop.ui.user.RegisterUserActivity;
import com.appspot.smartshop.ui.user.ViewUserProfileActivity;
import com.appspot.smartshop.ui.user.email.SendEmailToAdminActivity;
import com.appspot.smartshop.ui.user.notification.SmartShopNotificationService;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class MainAdapter extends BaseAdapter {
	public static final String TAG = "[MainAdapter]";

	private Context context;
	private LayoutInflater inflater;

	private int[] icons;
	private String[] text;

	public MainAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		if (!Global.isLogin) {
			icons = new int[] { 
					R.drawable.user_products_list, 
					R.drawable.user_pages_list,
					R.drawable.login, 
					R.drawable.register,
					R.drawable.exit,
			};

			text = new String[] {
					context.getString(R.string.user_products_list),
					context.getString(R.string.user_pages_list),
					context.getString(R.string.lblLogin),
					context.getString(R.string.lblRegister),
					context.getString(R.string.exit),
			};
		} else {
			icons = new int[] { 
					R.drawable.user_products_list, 
					R.drawable.user_pages_list,
					R.drawable.smartshop_logout,
					R.drawable.user_profile,
					R.drawable.contact,
					R.drawable.exit,
			};

			text = new String[] {
					context.getString(R.string.user_products_list),
					context.getString(R.string.user_pages_list),
					context.getString(R.string.logout),
					context.getString(R.string.user_profile), 
					context.getString(R.string.contact_us),
					context.getString(R.string.exit),
			};
		}
	}

	public int getCount() {
		return icons.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.user_profile_list_item,
					null);

			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			image.setBackgroundResource(icons[position]);
			TextView txt = (TextView) convertView.findViewById(R.id.txt);
			txt.setText(text[position]);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onMainMenuClick(position);
			}
		});

		return convertView;
	}

	private Intent intent;
	protected void onMainMenuClick(int position) {

		switch (icons[position]) {
		case R.drawable.user_pages_list:
			Log.d(TAG, "[VIEW PAGES LIST]");
			intent = new Intent(context, PagesListActivity.class);
			break;

		case R.drawable.user_products_list:
			Log.d(TAG, "[VIEW PRODUCTS LIST]");
			intent = new Intent(context, SearchProductsTabActivity.class);
			break;

		case R.drawable.login:
			Log.d(TAG, "[LOGIN]");
			intent = new Intent(context, LoginActivity.class);
			break;

		case R.drawable.register:
			Log.d(TAG, "[REGISTER]");
			intent = new Intent(context, RegisterUserActivity.class);
			break;

		case R.drawable.user_profile:
			Log.d(TAG, "[VIEW USER PROFILE]");
			if (!Global.isLogin) {
				Toast.makeText(context,
						context.getString(R.string.errMustLoginToViewProfile),
						Toast.LENGTH_SHORT).show();
				return;
			}

			intent = new Intent(context, ViewUserProfileActivity.class);
			intent.putExtra(Global.USER_NAME, Global.userInfo.username);
			intent.putExtra(Global.CAN_EDIT_USER_INFO, true);
			break;
			
		case R.drawable.exit:
			System.exit(0);
			break;
			
		case R.drawable.smartshop_logout:
			Log.d(TAG, "[LOGOUT]");
			
			String url = String.format(URLConstant.LOGOUT, Global.getSession());
			RestClient.getData(url, new JSONParser() {
				
				@Override
				public void onSuccess(JSONObject json) throws JSONException {
					Global.isLogin = false;
					Global.userInfo = null;
					intent = new Intent(context, SmartShopActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					
					Utils.clearAllNotifications();
					Global.isWaitingForNotifications = false;
					Global.application.stopService(new Intent(Global.application, 
							SmartShopNotificationService.class));
				}
				
				@Override
				public void onFailure(String message) {
					Global.isLogin = false;
					Global.userInfo = null;
					// TODO: process when logout fail (because of exprired session)
					intent = new Intent(context, SmartShopActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}
			});
			break;
			
		case R.drawable.contact:
			intent = new Intent(context, SendEmailToAdminActivity.class);
			if (Global.userInfo != null) {
				String sender = Global.userInfo.email;
				if (sender != null && !sender.trim().equals("")) {
					intent.putExtra(Global.SENDER, sender);
				}
			}
			break;
		}

		context.startActivity(intent);
	}

}
