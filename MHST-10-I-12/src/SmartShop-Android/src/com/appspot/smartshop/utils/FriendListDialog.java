package com.appspot.smartshop.utils;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.appspot.smartshop.R;

public class FriendListDialog {
	
	public static final int PRODUCT = 0;
	public static final int PAGE = 1;
	
	public static final int TAG = 0;
	public static final int UN_TAG = 1;
	
	public static int tagType;

	private static AlertDialog dialog;
	private static Context context;

	public static void showTagFriendDialog(Context context, final long productId) {
		FriendListDialog.context = context;
		
		final LinkedList<String> friends = new LinkedList<String>(Global.userInfo.setFriendsUsername); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_list_item_single_choice, 
				friends);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(com.appspot.smartshop.R.layout.friend_list_dialog, null);
		
		final ListView list = (ListView) view.findViewById(R.id.listFriend);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String taggedFriends = "";
				for (long position : list.getCheckItemIds()) {
					taggedFriends += friends.get((int) position) + ",";
				}
				
				tagFriendToProduct(productId, taggedFriends);
				
				dialog.dismiss();
			}
		});
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setView(view);
		dialog = dialogBuilder.create();
		
		dialog.show();
	}
	
	public static void showUntagFriendDialog(Context context, final long productId) {
		FriendListDialog.context = context;
		
		final LinkedList<String> friends = new LinkedList<String>(Global.userInfo.setFriendsUsername); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_list_item_single_choice, 
				friends);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(com.appspot.smartshop.R.layout.friend_list_dialog, null);
		
		final ListView list = (ListView) view.findViewById(R.id.listFriend);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setBackgroundResource(R.drawable.un_tag);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String taggedFriends = "";
				for (long position : list.getCheckItemIds()) {
					taggedFriends += friends.get((int) position) + ",";
				}
				
				untagFriendToProduct(productId, taggedFriends);
				
				dialog.dismiss();
			}
		});
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setView(view);
		dialog = dialogBuilder.create();
		
		dialog.show();
	}

	protected static void untagFriendToProduct(long productId, String friends) {
		friends = URLEncoder.encode(friends);
		String url = null;
		if (tagType == PRODUCT) {
			url = String.format(URLConstant.UNTAG_FRIEND_FROM_PRODUCT, Global.getSession(), 
					productId, friends);
		} else {
			url = String.format(URLConstant.UNTAG_FRIEND_FROM_PAGE, Global.getSession(), 
					productId, friends);
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

	protected static void tagFriendToProduct(long productId, String taggedFriend) {
		taggedFriend = URLEncoder.encode(taggedFriend);
		String url = null;
		if (tagType == PRODUCT) {
			url = String.format(URLConstant.TAG_FRIEND_TO_PRODUCT, Global.getSession(), 
					productId, taggedFriend);
		} else {
			url = String.format(URLConstant.TAG_FRIEND_TO_PAGE, Global.getSession(), 
					productId, taggedFriend);
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
