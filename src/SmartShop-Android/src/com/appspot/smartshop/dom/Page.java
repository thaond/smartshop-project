package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.entity.SerializableEntity;

public class Page implements Serializable {
	public String name = null;
	public String content = null;
	public String link_thumbnail = null;
	public Date date_post = null;
	public Date last_modified = null;
	public String username = null;
	public Long id = null;
	public int page_view;
	
	public Set<String> setCategoryKeys;

	public Page(String name, String content, String linkThumbnail,
			int pageView, Date datePost, Date lastModified, String username,
			String categoryId) {
		this();
		this.name = name;
		this.content = content;
		link_thumbnail = linkThumbnail;
		page_view = pageView;
		date_post = datePost;
		last_modified = lastModified;
		this.username = username;
	}

	public Page() {
		setCategoryKeys = new HashSet<String>();
	}

}
