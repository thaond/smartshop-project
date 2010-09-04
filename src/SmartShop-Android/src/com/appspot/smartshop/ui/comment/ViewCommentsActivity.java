package com.appspot.smartshop.ui.comment;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sv.skunkworks.showtimes.lib.asynchronous.HttpService;
import sv.skunkworks.showtimes.lib.asynchronous.ServiceCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.CommentAdapter;
import com.appspot.smartshop.dom.Comment;
import com.appspot.smartshop.mock.MockComments;
import com.appspot.smartshop.ui.user.UserActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ViewCommentsActivity extends Activity {
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
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view comment");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_comments);
		
		// get type_id and type
		id = getIntent().getExtras().getLong(Global.ID_OF_COMMENTS);
		type = getIntent().getExtras().getString(Global.TYPE_OF_COMMENTS);
		Log.d(TAG, "id of comments = " + id);
		Log.d(TAG, "type of comments = " + type);
		
		// add new comment
		Button btnAddComment = (Button) findViewById(R.id.btnAddComment);
		if (!Global.isLogin) {
			btnAddComment.setVisibility(View.GONE);
		}
		btnAddComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAddNewCommentDialog();
			}
		});
		
		// list comments
		adapter = new CommentAdapter(this, 0, new LinkedList<Comment>());
		listComments = (ListView) findViewById(R.id.listComments);
		
		// load comments
		loadComments();
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
						if (arr == null || arr.length() == 0) {
							Log.d(TAG, "no comment found");
							task.hasData = false;
							task.message = json.getString(URLConstant.MESSAGE);
							return;
						}
						
						comments = Global.gsonWithHour.fromJson(arr.toString(), Comment.getType());
						Log.d(TAG, "found " + comments.size() + " comment(s)");
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
				dialog.cancel();
			}
			
			@Override
			public void loadData() {
				comment = new Comment();
				comment.content = content;
				comment.username = Global.username;
				comment.type_id = id;
				comment.type = type;
				
				String param = Global.gsonWithHour.toJson(comment);
				
				RestClient.postData(URLConstant.ADD_NEW_COMMENT, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						// TODO add comment fail
					}
					
					@Override
					public void onFailure(String message) {
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
