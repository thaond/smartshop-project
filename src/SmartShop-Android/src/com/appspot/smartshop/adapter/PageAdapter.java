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
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Page;
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

	public PageAdapter(Context context, int textViewResourceId, Page[] objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public PageAdapter(Context context, int textViewResourceId, List<Page> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public PageAdapter(Context context, int textViewResourceId) {
		this(context, textViewResourceId, new Page[] {});
	}

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
				final Intent intent = new Intent(context, ViewPageActivity.class);
				if (pageType == NORMAL_PAGES) {
					intent.putExtra(Global.IS_NORMAL_PAGE, true);
				}
				
				// TODO: get page info from service find page by id
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
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView txtName;
		TextView txtPageView;
		TextView txtPostDate;
		Button btnDetail;
	}
}
