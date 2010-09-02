package vnfoss2010.smartshop.serverside.database.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import vnfoss2010.smartshop.serverside.utils.SearchCapable;

import com.google.gson.annotations.Exclude;

@PersistenceCapable
public class Page implements SearchCapable{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String name;
 
	@Persistent
	private String content;

	@Persistent
	private String link_thumbnail;

	@Persistent
	private int page_view;

	@Persistent
	private Date date_post;

	@Persistent
	private Date last_modified;

	@Persistent
	private String username;

	@Exclude
	@Persistent
	private Set<String> fts;

	@Persistent
	private Set<String> setCategoryKeys;
	@Persistent
	private Set<Long> setProductIDs;

	public Set<Long> getSetProduct() {
		return setProductIDs;
	}

	public void setSetProduct(Set<Long> setProduct) {
		this.setProductIDs = setProduct;
	}

	public Page(String name, String content, String linkThumbnail,
			int pageView, Date datePost, Date lastModified, String username) {
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

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the link_thumbnail
	 */
	public String getLink_thumbnail() {
		return link_thumbnail;
	}

	/**
	 * @param linkThumbnail
	 *            the link_thumbnail to set
	 */
	public void setLink_thumbnail(String linkThumbnail) {
		link_thumbnail = linkThumbnail;
	}

	/**
	 * @return the page_view
	 */
	public int getPage_view() {
		return page_view;
	}

	/**
	 * @param pageView
	 *            the page_view to set
	 */
	public void setPage_view(int pageView) {
		page_view = pageView;
	}

	/**
	 * @return the date_post
	 */
	public Date getDate_post() {
		return date_post;
	}

	/**
	 * @param datePost
	 *            the date_post to set
	 */
	public void setDate_post(Date datePost) {
		date_post = datePost;
	}

	/**
	 * @return the last_modified
	 */
	public Date getLast_modified() {
		return last_modified;
	}

	/**
	 * @param lastModified
	 *            the last_modified to set
	 */
	public void setLast_modified(Date lastModified) {
		last_modified = lastModified;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @param setCategoryKeys
	 *            the setCategoryKeys to set
	 */
	public void setSetCategoryKeys(Set<String> setCategoryKeys) {
		this.setCategoryKeys = setCategoryKeys;
	}

	/**
	 * @return the setCategoryKeys
	 */
	public Set<String> getSetCategoryKeys() {
		return setCategoryKeys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page [id=" + id + ", date_post=" + date_post + ", content="
				+ content + ", name=" + name + ", page_view=" + page_view
				+ ", setCategoryKeys=" + setCategoryKeys + ", username="
				+ username + "]";
	}

	/**
	 * @param fts
	 *            the fts to set
	 */
	public void setFts(Set<String> fts) {
		this.fts = fts;
	}

	/**
	 * @return the fts
	 */
	public Set<String> getFts() {
		return fts;
	}
}
