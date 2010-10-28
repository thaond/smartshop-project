package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.ui.user.AddFriendActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.SimpleAsyncTask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddFriendAdapter extends ArrayAdapter<UserInfo>{
	public static final String TAG = "[AddFriendAdapter]";
	private Context context;
	private LayoutInflater inflater;
	private SimpleAsyncTask task;
	public AddFriendAdapter(Context context, int textViewResourceId,
			List<UserInfo> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.add_friend_item, null);
			holder.friendAvatar = (ImageView) convertView.findViewById(R.id.friend_avatar);
			holder.friendAddress = (TextView) convertView.findViewById(R.id.txtFriendAddress);
			holder.friendEmail = (TextView) convertView.findViewById(R.id.txtFriendEmail);
			holder.friendName = (TextView) convertView.findViewById(R.id.txtFriendName);
			holder.chBxSelectFriend = (CheckBox) convertView.findViewById(R.id.chBxSelectFriendToAdd);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final UserInfo friendInfo = getItem(position);
		//TODO: VANLOI999 remember set up avatar for friend
		holder.friendAddress.setText(friendInfo.address);
		holder.friendName.setText(friendInfo.first_name + friendInfo.last_name);
		holder.friendEmail.setText(friendInfo.email);
		holder.chBxSelectFriend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					Log.d(TAG, AddFriendActivity.friendsToAdd);
					if(AddFriendActivity.friendsToAdd.length()==0){
						AddFriendActivity.friendsToAdd += friendInfo.username;
					}else if(!AddFriendActivity.friendsToAdd.contains(friendInfo.username)){
						AddFriendActivity.friendsToAdd += ","+ friendInfo.username;
					}
				}
			}
		});
		
		return convertView;
	}
	static class ViewHolder{
		ImageView friendAvatar;
		TextView friendName;
		TextView friendEmail;
		TextView friendAddress;
		CheckBox chBxSelectFriend;
	}
}
