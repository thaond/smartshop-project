 package vnfoss2010.smartshop.serverside.database.entity;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gson.annotations.Exclude;

@PersistenceCapable
public class Notification {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String type;
	
	@Persistent
	private Long type_id;

	@Persistent
	private String content;

	@Persistent
	private Date date; 
 
	@Persistent
	private boolean isNew;
 
	@Persistent
	@Exclude 
	private String username; 

	public Notification() {
	}

	public Notification(String content, Date date, String username) {
		this.content = content;
		this.date = date;
		this.setUsername(username);
		this.isNew = false;
	}
	
	/**
	 * Full constructor
	 */
	public Notification(String content, Date date, boolean isNew,
			String type, Long typeId, String username) {
		this.content = content;
		this.date = date;
		this.isNew = isNew;
		this.type = type;
		this.type_id = typeId;
		this.username = username;
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
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
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

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.type_id = typeId;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return type_id;
	}
}
