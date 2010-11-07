package vnfoss2010.smartshop.webbased.share;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vnfoss2010.smartshop.webbased.client.utils.WebbasedUtils;

public class WPage implements Serializable {
	private static final long serialVersionUID = -8219862630220273676L;
	public String name = null;
	public String content = null;
	public String link_thumbnail = null;
	public Date date_post = null;
	public Date last_modified = null;
	public String username = null;
	public Long id = null;
	public int page_view;
	
	public Set<String> setCategoryKeys = new HashSet<String>();
	public Set<Long> setProductIDs = new HashSet<Long>();
	
	public WUserInfo userInfo;
	public List<WComment> listComments = new ArrayList<WComment>();

	public WPage(String name, String content, String linkThumbnail,
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

	public WPage() {
		setCategoryKeys = new HashSet<String>();
		setProductIDs = new HashSet<Long>();
	}
	
	public String getShortDescription() {
		if (WebbasedUtils.isEmptyOrNull(content))
			return content;
		int to = Math.min(60, content.length());
		return content.substring(0, Math.max(to, content.indexOf(" ",
				60)));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WPage [content=" + content + ", date_post=" + date_post
				+ ", id=" + id + ", last_modified="
				+ last_modified + ", link_thumbnail=" + link_thumbnail
				+ ", name=" + name + ", page_view=" + page_view
				+ ", setCategoryKeys=" + setCategoryKeys + ", setProductIDs="
				+ setProductIDs + ", username=" + username + "]";
	}
}
