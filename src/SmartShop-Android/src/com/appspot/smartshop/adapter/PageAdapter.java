package com.appspot.smartshop.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.facebook.utils.FacebookUtils;
import com.appspot.smartshop.ui.page.PageActivity;
import com.appspot.smartshop.ui.page.ViewPageActivity;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.URLConstant;

public class PageAdapter extends ArrayAdapter<Page> {
	public static final String TAG = "[PageAdapter]";
	
	public static final int PAGES_OF_USER = 1;
	public static final int NORMAL_PAGES = 0;
	public static int pageType = NORMAL_PAGES;
	
	private LayoutInflater inflater;
	private Context context;
	private FacebookUtils facebook;

	public PageAdapter(Context context, int textViewResourceId, Page[] objects, FacebookUtils facebook) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.facebook = facebook;
	}
	
	public PageAdapter(Context context, int textViewResourceId, List<Page> objects, FacebookUtils facebook) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.facebook = facebook;
	}
	
	public PageAdapter(Context context, int textViewResourceId, FacebookUtils facebook) {
		this(context, textViewResourceId, new Page[] {}, facebook);
	}

	private Intent intent;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.page_list_item, null);
			
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtPageView = (TextView) convertView.findViewById(R.id.txtPageView);
			holder.txtPostDate = (TextView) convertView.findViewById(R.id.txtPostDate);
			holder.btnDetail = (Button) convertView.findViewById(R.id.btnDetail);
			holder.btnPostFb = (ImageView) convertView.findViewById(R.id.btnPostFb);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Page page = (Page) getItem(position);
		holder.txtName.setText(page.name);
		holder.txtPageView.setText(context.getString(R.string.page_view) + " " + page.page_view);
		holder.txtPostDate.setText(Global.dfFull.format(page.date_post));
		
		// listener for view page
		OnClickListener onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if (Global.isLogin && page.username.equals(Global.userInfo.username)) {
//					Log.d(TAG, "edit page of " + Global.userInfo.username);
//					intent = new Intent(context, PageActivity.class);
//				} else {
//					Log.d(TAG, "view page");
//					intent = new Intent(context, ViewPageActivity.class);
//				}
				
				Log.d(TAG, "view page");
				if (pageType == NORMAL_PAGES) {
					intent = new Intent(context, ViewPageActivity.class);
					intent.putExtra(Global.IS_NORMAL_PAGE, true);
				} else {
					intent = new Intent(context, PageActivity.class);
					intent.putExtra("can_edit_page", true);
				}
				
				String url = String.format(URLConstant.GET_PAGE_BY_ID, page.id);
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						Page newPage = Global.gsonWithHour.fromJson(
								json.getString("page"), Page.class);
						intent.putExtra(Global.PAGE, newPage);
						context.startActivity(intent);
					}
					
					@Override
					public void onFailure(String message) {
					}
				});
			}
		};
		holder.btnDetail.setOnClickListener(onClickListener);
		convertView.setOnClickListener(onClickListener);
		
		holder.btnPostFb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (facebook != null)
					facebook
							.sendMessage(
									"SmartShop - Trang thông tin",
									page.name,
									page.getThumbImageURL(),
									page.getShortDescription(),
									String.format(
											URLConstant.URL_WEBBASED_PAGE,
											page.id),
									new FacebookUtils.SimpleWallpostListener(
											facebook.getActivity(),
											Global.application
													.getString(R.string.postPageOnFacebookSuccess)));
			}
		});
		return convertView;
	}
	
	static class ViewHolder {
		TextView txtName;
		TextView txtPageView;
		TextView txtPostDate;
		Button btnDetail;
		ImageView btnPostFb;
	}
}
