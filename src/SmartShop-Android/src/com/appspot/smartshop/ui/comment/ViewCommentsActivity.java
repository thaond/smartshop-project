package com.appspot.smartshop.ui.comment;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.CommentAdapter;
import com.appspot.smartshop.dom.Comment;
import com.appspot.smartshop.dom.SmartshopNotification;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class ViewCommentsActivity extends BaseUIActivity {
	public static final String TAG = "[ViewCommentsActivity]";
	
	public static final String COMMENTS_PARAM = "{type:\"%s\",type_id:%d}";

	private CommentAdapter adapter;
	private ListView listComments;
	
	private long id;
	private String type;
	
	private List<Comment> comments;

	private LayoutInflater inflater;

	private AlertDialog dialog;

	private EditText txtComment;
	
	@Override
	protected void onCreatePre() {
		setContentView(R.layout.view_comments);
	}
	
	@Override
	protected void onCreatePost(Bundle savedInstanceState) {
		Log.d(TAG, "view comment");
		
		// get type_id and type
		Bundle bundle = getIntent().getExtras();
		id = bundle.getLong(Global.ID_OF_COMMENTS);
		type = bundle.getString(Global.TYPE_OF_COMMENTS);
		Log.d(TAG, "id of comments = " + id);
		Log.d(TAG, "type of comments = " + type);
		SmartshopNotification notification = (SmartshopNotification) bundle.get(Global.NOTIFICATION);
		if (notification != null) {
			Log.d(TAG, "remove notification " + notification.id);
			Global.notificationManager.cancel(notification.id);
			Global.notifications.remove(notification);
			Utils.markNotificationAsRead(notification.id);
		}
		
		// list comments
		adapter = new CommentAdapter(this, 0, new LinkedList<Comment>());
		listComments = (ListView) findViewById(R.id.listComments);
		
		// load comments
		loadComments();
		
		SmartshopNotification sNotification = (SmartshopNotification) getIntent().getSerializableExtra(Global.NOTIFICATION);
		if (sNotification!=null){
			Global.notifications.remove(sNotification);
			Global.notificationManager.cancel(sNotification.id);
			Utils.markNotificationAsRead(sNotification.id);
		}
	}

	private static final int MENU_ADD_COMMENT = 0;
	private static final int MENU_RETURN_TO_HOME = 1;
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD_COMMENT, 0,
				getString(R.string.add_new_comment)).setIcon(R.drawable.new_comment);
		menu.add(0, MENU_RETURN_TO_HOME, 0, getString(R.string.return_to_home)).setIcon(R.drawable.home);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD_COMMENT:
			if (!Global.isLogin) {
				Toast.makeText(ViewCommentsActivity.this,
						getString(R.string.warn_must_login_to_post_comment), Toast.LENGTH_SHORT).show();
			} else {
				showAddNewCommentDialog();
			}
			break;
			
		case MENU_RETURN_TO_HOME:
			Utils.returnHomeActivity(this);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private SimpleAsyncTask task;
	private void loadComments() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void loadData() {
				String param = String.format(COMMENTS_PARAM, type, id);
				
				RestClient.postData(URLConstant.GET_COMMENTS, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("comments");
						comments = Global.gsonWithHour.fromJson(arr.toString(), Comment.getType());
						Log.d(TAG, "found " + comments.size() + " comment(s)");
						if (comments.size() == 0) {
							task.hasData = false;
							task.message = getString(R.string.warn_has_no_comment);
						}
					}	
					
					@Override
					public void onFailure(String message) {
						task.cancel(true);
					}
				});
			}

			@Override
			public void updateUI() {
				adapter = new CommentAdapter(ViewCommentsActivity.this, 0, comments);
				listComments.setAdapter(adapter);
			}
		});
		task.execute();
	}

	protected void showAddNewCommentDialog() {
		// inflater
		if (inflater == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		View view = inflater.inflate(R.layout.add_comment_dialog, null);
		
		// comment content
		txtComment = (EditText) view.findViewById(R.id.txtComment);
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addNewComment();
				dialog.cancel();
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
	}

	private Comment comment;
	protected void addNewComment() {
		// create comment
		final String content = txtComment.getText().toString();
		if (content.trim().equals("")) {
			onAddNewCommentFailure();
			return;
		}
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapter.addNewComment(comment);
				listComments.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				comment = new Comment();
				comment.content = content;
				comment.username = Global.userInfo.username;
				comment.type_id = id;
				comment.type = type;
				
				String param = Global.gsonWithHour.toJson(comment);
				String url = String.format(URLConstant.ADD_NEW_COMMENT, Global.getSession());
				RestClient.postData(url, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
					}
					
					@Override
					public void onFailure(String message) {
						task.hasData = false;
						task.message = message;
						task.cancel(true);
					}
				});
			}
		});
		task.execute();
	}
	
	private void onAddNewCommentFailure() {
		Toast.makeText(ViewCommentsActivity.this, getString(R.string.errEmptyComment), 
				Toast.LENGTH_SHORT).show();
		dialog.cancel();
	}
}
