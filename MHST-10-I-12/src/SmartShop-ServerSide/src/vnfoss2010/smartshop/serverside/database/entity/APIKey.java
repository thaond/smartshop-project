package vnfoss2010.smartshop.serverside.database.entity;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * 
 * @author VoMinhTam
 */
@PersistenceCapable
public class APIKey {
	
	@PrimaryKey
	@Persistent
	private String source;
	
	@Persistent
	private String email;
	
	@Persistent 
	private Date date;
	
	@Persistent
	private long views;

	
	public APIKey() {
	}

	public APIKey(String source, String email, Date date) {
		this.source = source;
		this.date = date;
		this.email = email;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param date the date to set
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
	 * @param views the views to set
	 */
	public void setViews(long views) {
		this.views = views;
	}

	/**
	 * @return the views
	 */
	public long getViews() {
		return views;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
}
