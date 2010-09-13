package com.appspot.smartshop.adapter;
import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Notification;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<Notification> {
	private LayoutInflater inflater;
	public AlertDialog dialog = null;
	public Notification notification;
	public NotificationAdapter(Context context, int resource,
			List<Notification> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if(convertView==null){
			convertView = inflater.inflate(R.layout.notification_item, null);
			holder.chBoxIsRead = (CheckBox) convertView.findViewById(R.id.checkBoxIsRead);
			holder.txtContent = (TextView) convertView.findViewById(R.id.txtContentOfSubcribe);
			holder.txtDate = (TextView) convertView.findViewById(R.id.txtDateOfSubcribe);
			holder.btnDetail = (Button) convertView.findViewById(R.id.btnDetail);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		notification = getItem(position);
		holder.txtDate.setText(notification.date.toString());
		holder.chBoxIsRead.setChecked(notification.isNew);
		holder.txtContent.setText(notification.content.substring(0, 25));
		holder.btnDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDetailOfNotification();
			}
		});
		return convertView;
	}
	protected void showDetailOfNotification() {
		// inflater
		if (inflater == null) {
			inflater = LayoutInflater.from(getContext());
		}
		View view = inflater.inflate(R.layout.view_detail_of_notification, null);
		
		// comment content
		EditText txtNotification = (EditText) view.findViewById(R.id.txtNotification);
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
	static class ViewHolder {
		CheckBox chBoxIsRead;
		TextView txtContent;
		TextView txtDate;
		Button btnDetail;
		public ViewHolder(){
			
		}
		
	}
}
