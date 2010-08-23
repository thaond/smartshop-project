package com.appspot.smartshop.mock;

import java.util.ArrayList;
import java.util.List;

import com.appspot.smartshop.dom.Comment;

public class MockComments {
	
	private static final int NUM_OF_COMMENTS = 10;	

	public static List<Comment> getInstance() {
		List<Comment> comments = new ArrayList<Comment>();
		
		for (int i = 0; i < NUM_OF_COMMENTS; ++i) {
			Comment c = new Comment();
			c.content = "this is comment number " + (i + 1);
			comments.add(c);
		}
		
		return comments;
	}
}
