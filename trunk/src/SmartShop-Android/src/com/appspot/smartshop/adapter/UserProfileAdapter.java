package com.appspot.smartshop.adapter;

import java.util.ArrayList;

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
import com.appspot.smartshop.mock.MockUserInfo;
import com.appspot.smartshop.ui.page.PageActivity;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.product.PostProductActivity;
import com.appspot.smartshop.ui.user.UserActivity;
import com.appspot.smartshop.ui.user.UserProductListActivity;
import com.appspot.smartshop.utils.Global;

public class UserProfileAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private Context context;
	
	private Drawable[] icons;
	private String[] text;
	
	public UserProfileAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		
		icons = new Drawable[] {
				context.getResources().getDrawable(R.drawable.user_profile),
				context.getResources().getDrawable(R.drawable.user_pages_list),
				context.getResources().getDrawable(R.drawable.user_products_list),
				context.getResources().getDrawable(R.drawable.add_new_page),
				context.getResources().getDrawable(R.drawable.post_new_product),
		};
		text = new String[] {
				context.getString(R.string.user_profile),
				context.getString(R.string.user_pages_list),
				context.getString(R.string.user_products_list),
				context.getString(R.string.add_new_page),
				context.getString(R.string.post_new_product),
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
						intent = new Intent(context, UserActivity.class);
						// TODO (condorhero01): request info of user based on Global.username
						intent.putExtra(Global.USER_INFO, MockUserInfo.getInstance());
						intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
						break;
						
					case 1:
						intent = new Intent(context, PagesListActivity.class);
						intent.putExtra(Global.PAGES_LIST_TYPE, PagesListActivity.PAGES_OF_USER);
						break;
						
					case 2:
						intent = new Intent(context, UserProductListActivity.class);
						break;
						
					case 3:
						intent = new Intent(context, PageActivity.class);
						break;
						
					case 4:
						intent = new Intent(context, PostProductActivity.class);
						break;
					}
					
					context.startActivity(intent);
				}
			});
			
		}
		
		return convertView;
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
