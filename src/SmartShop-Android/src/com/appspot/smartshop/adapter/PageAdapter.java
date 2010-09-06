package com.appspot.smartshop.adapter;

import java.util.List;

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

public class PageAdapter extends ArrayAdapter<Page> {
	public static final String TAG = "[PageAdapter]";
	
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
				Intent intent = null;
				if (Global.isLogin && page.username.equals(Global.username)) {
					Log.d(TAG, "edit page of " + Global.username);
					intent = new Intent(context, PageActivity.class);
				} else {
					Log.d(TAG, "view page");
					intent = new Intent(context, ViewPageActivity.class);
				}
				
				intent.putExtra(Global.PAGE, page);
				context.startActivity(intent);
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
