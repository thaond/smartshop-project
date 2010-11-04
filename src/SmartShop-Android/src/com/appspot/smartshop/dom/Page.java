package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.URLConstant;
import com.google.gson.reflect.TypeToken;

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
	public Set<String> fts;
	public Set<Long> setProductIDs;

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
		setProductIDs = new HashSet<Long>();
		fts = new HashSet<String>();
	}

	public static Type getType() {
		return new TypeToken<List<Page>>() {}.getType();
	}
	
	public String getThumbImageURL() {
		if (StringUtils.isEmptyOrNull(link_thumbnail))
			return URLConstant.URL_NO_PAGE_IMG;
		else {
			return link_thumbnail;
		}
	}
	
	public String getShortDescription() {
		if (StringUtils.isEmptyOrNull(content))
			return content;
		int to = Math.min(60, content.length());
		return content.substring(0, Math.max(to, content.indexOf(" ",
				60)));
	}
}
