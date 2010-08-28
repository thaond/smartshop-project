package com.appspot.smartshop.ui.comment;

import java.util.List;

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
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ViewCommentsActivity extends Activity {
	public static final String TAG = "[ViewCommentsActivity]";

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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_comments);
		
		// get type_id and type
		id = getIntent().getExtras().getLong(Global.ID_OF_COMMENTS);
		type = getIntent().getExtras().getString(Global.TYPE_OF_COMMENTS);
		Log.d(TAG, "id of comments = " + id);
		Log.d(TAG, "type of comments = " + type);
		
		// add new comment
		Button btnAddComment = (Button) findViewById(R.id.btnAddComment);
		btnAddComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAddNewCommentDialog();
			}
		});
		
		// list comments
		listComments = (ListView) findViewById(R.id.listComments);
		
		// load comments
		new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void loadData() {
				// TODO (condorhero01): load list comments based on id of page or product
				comments = MockComments.getInstance();
				adapter = new CommentAdapter(ViewCommentsActivity.this, 0, comments);
			}

			@Override
			public void updateUI() {
				listComments.setAdapter(adapter);
			}
		}).execute();
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

	protected void addNewComment() {
		String url = URLConstant.ADD_NEW_COMMENT;
		// create comment
		String content = txtComment.getText().toString();
		if (content.trim().equals("")) {
			onAddNewCommentFailure();
			return;
		}
		
		final Comment comment = new Comment();
		comment.content = content;
		comment.username = Global.username;
		comment.type_id = id;
		comment.type = type;
		Log.d(TAG, "user " + comment.username + ", comment = " + comment.content
				+ " type_id = " + comment.type_id + ", type = " + type);
		
		String param = Utils.gson.toJson(comment);
		HttpService.postResource(url, param, false, new ServiceCallback() {
			
			@Override
			public void onSuccess(JsonObject result) {
				
				adapter.addNewComment(comment);
				dialog.cancel();
			}
		});
	}
	
	private void onAddNewCommentFailure() {
		Toast.makeText(ViewCommentsActivity.this, getString(R.string.errEmptyComment), 
				Toast.LENGTH_SHORT).show();
		dialog.cancel();
	}
}
