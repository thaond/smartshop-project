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
	private Date date;
	
	private long views;

	
	public APIKey() {
	}

	public APIKey(String source, Date date) {
		this.source = source;
		this.date = date;
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
}
