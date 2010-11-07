package vnfoss2010.smartshop.webbased.share;

import java.util.Date;

public class WComment {
	public Long id;
	public String content;
	public String type;
	public long type_id;
	public String username;
	public Date date_post;

	public WComment() {
	}

	public WComment(String content, String type, int typeId, String username) {
		this.content = content;
		this.type = type;
		type_id = typeId;
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WComment [content=" + content + ", id=" + id + ", type=" + type
				+ ", type_id=" + type_id + ", username=" + username + "]";
	}
}
