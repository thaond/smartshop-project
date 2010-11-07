package vnfoss2010.smartshop.serverside.database.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gson.annotations.Exclude;

@PersistenceCapable
public class Notification {
	public static final int ADD_FRIEND = 1;
	public static final int TAG_PRODUCT = 2;
	public static final int UNTAG_PRODUCT = 3;
	public static final int TAG_PAGE = 4;
	public static final int UNTAG_PAGE = 5;
	public static final int ADD_COMMENT_PRODUCT = 6;
	public static final int ADD_COMMENT_PAGE = 7;
	public static final int TAG_PRODUCT_TO_PAGE = 8;
	public static final int SUBSCRIBE_HAS_NEW_PRODUCT = 9;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private int type;

	@Persistent
	private String content;

	@Persistent
	private long timestamp;

	@Persistent
	private boolean isNew;

	@Persistent
	@Exclude
	private String username;

	@Persistent
	private String detail;

	@NotPersistent
	private String jsonOutput;

	public Notification() {
	}

	public Notification(String content, long date, String username) {
		this.content = content;
		this.timestamp = date;
		this.setUsername(username);
		this.isNew = true;
	}

	/**
	 * Full constructor
	 */
	public Notification(String content, long date, boolean isNew,
			String username, int type, String detail) {
		this.type = type;
		this.content = content;
		this.timestamp = date;
		this.isNew = isNew;
		// this.type_id = typeId;
		this.username = username;
		this.detail = detail;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param isNew
	 *            the isNew to set
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	// /**
	// * @param typeId the typeId to set
	// */
	// public void setTypeId(int typeId) {
	// this.type_id = typeId;
	// }
	//
	// /**
	// * @return the typeId
	// */
	// public int getTypeId() {
	// return type_id;
	// }

	/**
	 * @param detail
	 *            the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Notification [content=" + content + ", detail=" + detail
				+ ", id=" + id + ", isNew=" + isNew + ", timestamp="
				+ timestamp + ", type=" + type + ", username=" + username + "]";
	}

	/**
	 * @param jsonOutput
	 *            the jsonOutput to set
	 */
	public void setJsonOutput(String jsonOutput) {
		this.jsonOutput = jsonOutput;
	}

	/**
	 * @return the jsonOutput
	 */
	public String getJsonOutput() {
		return jsonOutput;
	}

}
