package com.appspot.smartshop.adapter;

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

import com.appspot.smartshop.MainActivity;
import com.appspot.smartshop.R;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.product.ProductsListActivity;
import com.appspot.smartshop.ui.product.SearchProductsTabActivity;
import com.appspot.smartshop.ui.user.LoginActivity;
import com.appspot.smartshop.ui.user.UserActivity;
import com.appspot.smartshop.ui.user.UserProfileActivity;
import com.appspot.smartshop.ui.user.ViewNotificationsActivity;
import com.appspot.smartshop.utils.Global;

public class MainAdapter extends BaseAdapter {
	public static final String TAG = "[MainAdapter]";
	
	private Context context;
	private LayoutInflater inflater;
	
	private int[] icons;
	private String[] text;
	
	public MainAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		
		icons = new int[] {
				R.drawable.user_products_list,	
				R.drawable.user_pages_list,
				R.drawable.login,
				R.drawable.register,
				R.drawable.user_profile,
				R.drawable.notifications
		};
		
		text = new String[] {
				context.getString(R.string.user_products_list),
				context.getString(R.string.user_pages_list),
				context.getString(R.string.lblLogin),
				context.getString(R.string.lblRegister),
				context.getString(R.string.user_profile),
				context.getString(R.string.view_notifications)
		};
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
			convertView = inflater.inflate(R.layout.user_profile_list_item, null);
			
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

	protected void onMainMenuClick(int position) {
		Intent intent = null;
		
		switch (icons[position]) {
		case R.drawable.user_pages_list:
			Log.d(TAG, "view pages list ");
			intent = new Intent(context, PagesListActivity.class);
			break;
			
		case R.drawable.user_products_list:
			Log.d(TAG, "view products list");
			intent = new Intent(context, SearchProductsTabActivity.class);
			break;
			
		case R.drawable.login:
			Log.d(TAG, "login");
			intent = new Intent(context, LoginActivity.class);
			break;
			
		case R.drawable.register:
			Log.d(TAG, "register");
			intent = new Intent(context, UserActivity.class);
			break;
			
		case R.drawable.user_profile:
//			if (!Global.isLogin) {
//				Toast.makeText(context,
//						context.getString(R.string.errMustLoginToViewProfile),
//						Toast.LENGTH_SHORT).show();
//			} else {
//				intent = new Intent(context, UserProfileActivity.class);
//				intent.putExtra(Global.USER_NAME, Global.username);
//				if (Global.isLogin) {
//					intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
//				}
//			}
			
			if (!Global.isLogin) {
				Toast.makeText(context,
						context.getString(R.string.errMustLoginToViewProfile),
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			intent = new Intent(context, UserProfileActivity.class);
			intent.putExtra(Global.USER_NAME, Global.username);
			intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
			break;
		case R.drawable.notifications:
			Log.d(TAG, "notifications");
			intent = new Intent(context,ViewNotificationsActivity.class);
			break;
		}
		
		context.startActivity(intent);
	}

}
