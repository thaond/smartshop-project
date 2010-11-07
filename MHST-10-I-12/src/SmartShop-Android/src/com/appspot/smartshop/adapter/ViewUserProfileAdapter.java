package com.appspot.smartshop.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.user.UserProductListActivity;
import com.appspot.smartshop.ui.user.ViewUserProfileActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

public class ViewUserProfileAdapter extends BaseAdapter {
	public static final String TAG = "[ViewUserProfileAdapter]";
	
	private LayoutInflater inflater;
	private Context context;
	
	private Drawable[] icons;
	private String[] text;
	
	private String username;
	
	public ViewUserProfileAdapter(Context context, String username) {
		this.context = context;
		this.username = username;
		inflater = LayoutInflater.from(context);
		
		icons = new Drawable[] {
				context.getResources().getDrawable(R.drawable.user_profile),
				context.getResources().getDrawable(R.drawable.user_pages_list),
				context.getResources().getDrawable(R.drawable.user_products_list),
		};
		text = new String[] {
				context.getString(R.string.user_profile),
				context.getString(R.string.user_pages_list),
				context.getString(R.string.user_products_list),
		};
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.user_profile_list_item, null);
			
			ImageView image = (ImageView) convertView.findViewById(R.id.image);
			image.setBackgroundDrawable(icons[position]);
			TextView txt = (TextView) convertView.findViewById(R.id.txt);
			txt.setText(text[position]);
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = null;
					switch (position) {
					case 0:
						Log.d(TAG, "view info of " + username);
						getUserProfile();
						break;
						
					case 1:
						Log.d(TAG, "view page of user " + username);
						intent = new Intent(context, PagesListActivity.class);
						intent.putExtra(Global.PAGES_TYPE, PagesListActivity.PAGES_OF_USER);
						intent.putExtra(Global.PAGES_OF_USER, username);
						context.startActivity(intent);
						break;
						
					case 2:
						Log.d(TAG, "view products of " + username);
						intent = new Intent(context, UserProductListActivity.class);
						intent.putExtra(Global.PRODUCTS_OF_USER, username);
						context.startActivity(intent);
						break;
					}
				}
			});
			
		}
		
		return convertView;
	}

	private SimpleAsyncTask task;
	protected void getUserProfile() {
		// view profile of other user
		System.out.println("context = null "+ (context == null));
		task = new SimpleAsyncTask(context, new DataLoader() {
			
			@Override
			public void updateUI() {
			}
			
			@Override
			public void loadData() {
				String url = String.format(URLConstant.GET_USER_INFO, username);
				
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						UserInfo userInfo = Global.gsonDateWithoutHour.fromJson(
								json.get("userinfo").toString(), UserInfo.class);
						
						Intent intent = new Intent(context, ViewUserProfileActivity.class);
						intent.putExtra(Global.USER_INFO, userInfo);
						context.startActivity(intent);
					}
					 
					@Override
					public void onFailure(String message) {
					}
				});
			}
		});
		task.execute();
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
}

