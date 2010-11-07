package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.SmartshopNotification;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<SmartshopNotification> {
	public static final String TAG = "[NotificationAdapter]";
	private LayoutInflater inflater;
	public AlertDialog dialog = null;
	public String detail;
	public SmartshopNotification notification;
	public ViewHolder holder;
	public NotificationAdapter(Context context, int resource,
			List<SmartshopNotification> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.notification_item, null);
			holder = new ViewHolder();
			holder.chBoxIsRead = (CheckBox) convertView
					.findViewById(R.id.checkBoxIsRead);
			holder.txtContent = (TextView) convertView
					.findViewById(R.id.txtContentOfSubcribe);
			holder.txtDate = (TextView) convertView
					.findViewById(R.id.txtDateOfSubcribe);
			holder.btnDetail = (Button) convertView
					.findViewById(R.id.btnDetail);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		notification = getItem(position);
		detail = notification.content;
		holder.txtDate.setText(notification.getTitle());
		holder.chBoxIsRead.setChecked(notification.isNew);
		holder.txtContent.setText(notification.content.substring(0, notification.content.length()/2)+"...");
		holder.btnDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (inflater == null) {
					inflater = LayoutInflater.from(getContext());
				}
				View view = inflater
						.inflate(R.layout.view_detail_of_notification, null);
				notification = getItem(position);
				// comment content
				EditText txtNotification = (EditText) view
						.findViewById(R.id.txtNotification);
				txtNotification.setText(notification.content);
				Button btnOk = (Button) view.findViewById(R.id.btnOut);
				
				btnOk.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public CheckBox chBoxIsRead;
		public TextView txtContent;
		public TextView txtDate;
		public Button btnDetail;

		public ViewHolder() {

		}

	}
}
