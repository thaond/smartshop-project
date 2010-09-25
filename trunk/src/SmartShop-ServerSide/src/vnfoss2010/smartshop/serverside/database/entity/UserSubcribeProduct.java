package vnfoss2010.smartshop.serverside.database.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;
import com.google.gson.annotations.Exclude;

@PersistenceCapable
public class UserSubcribeProduct implements LocationCapable, Cloneable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private Double lat;

	@Persistent
	private Double lng;

	@Persistent
	private Double radius;

	@Persistent
	private String description;

	@Persistent
	private boolean isActive = true;

	@Persistent
	private Date date;

	@Persistent
	@Exclude
	private String username;

	@Persistent
	private List<String> categoryList;

	@Persistent
	private String q;
	
	@Persistent
	private int type_notification;
	
	@Persistent
	@Exclude
	private List<String> geocells;

	public UserSubcribeProduct() {
	}

	public UserSubcribeProduct(String userName, Double lat, Double lng, Double radius,  
			boolean isActive, Date date, String description, String keyword, int type) {
		this.lat = lat;
		this.lng = lng;
		this.radius = radius;
		this.description = description;
		this.isActive = isActive;
		this.date = date;
		this.username = userName;
		this.q = keyword;
		this.type_notification = type;
		categoryList = new ArrayList<String>();
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public void setGeocells(List<String> geocells) {
		this.geocells = geocells;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public List<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return username;
	}

	public void setUserID(String userName) {
		this.username = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public List<String> getGeocells() {
		return geocells;
	}

	@Override
	public String getKeyString() {
		return id + "";
	}

	@Override
	public Point getLocation() {
		return new Point(lat, lng);
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.q = keyword;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return q;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserSubcribeProduct [categoryList=" + categoryList + ", date="
				+ date + ", description=" + description + ", geocells="
				+ geocells + ", id=" + id + ", isActive=" + isActive
				+ ", keyword=" + q + ", lat=" + lat + ", lng=" + lng
				+ ", radius=" + radius + ", userName=" + username + "]";
	}

	/**
	 * @param type_notification the type_notification to set
	 */
	public void setType_notification(int type_notification) {
		this.type_notification = type_notification;
	}

	/**
	 * @return the type_notification
	 */
	public int getType_notification() {
		return type_notification;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
