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
public class UserSubcribeProduct implements LocationCapable {
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
	private String userName;
	
	@Persistent
	private List<String> categoryList;
	
	@Exclude
	@Persistent
	private List<String> geocells;

	public UserSubcribeProduct() {
	}

	public UserSubcribeProduct(String userName, Double lat, Double lng,
			boolean isActive, String description) {
		this.lat = lat;
		this.lng = lng;
		this.description = description;
		this.isActive = isActive;
		this.userName = userName;
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
		return userName;
	}

	public void setUserID(String userName) {
		this.userName = userName;
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
}
