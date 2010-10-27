package com.appspot.smartshop.utils;

import java.net.URLEncoder;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appspot.smartshop.R;

public class FriendListDialog {
	
	public static final int TAG_PRODUCT = 0;
	public static final int TAG_PAGE = 1;
	
	public static int tagType;

	private static AlertDialog dialog;
	private static Context context;

	public static void showDialog(Context context, final long productId) {
		FriendListDialog.context = context;
		
		final LinkedList<String> friends = new LinkedList<String>(Global.userInfo.setFriendsUsername); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_list_item_single_choice, 
				friends);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(com.appspot.smartshop.R.layout.friend_list_dialog, null);
		
		ListView list = (ListView) view.findViewById(R.id.listFriend);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				String taggedFriend = friends.get(position);
				tagFriendToProduct(productId, taggedFriend);
				
				dialog.dismiss();
			}
		});
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setView(view);
		dialog = dialogBuilder.create();
		
		dialog.show();
	}

	protected static void tagFriendToProduct(long productId, String taggedFriend) {
		taggedFriend = URLEncoder.encode(taggedFriend);
		String url = null;
		if (tagType == TAG_PRODUCT) {
			url = String.format(URLConstant.TAG_FRIEND_TO_PRODUCT, productId, taggedFriend);
		} else {
			// TODO: tag page to friend url
		}
		
		RestClient.getData(url, new JSONParser() {
			
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
			}
			
			@Override
			public void onFailure(String message) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
