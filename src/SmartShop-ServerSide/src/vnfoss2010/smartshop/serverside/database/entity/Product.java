package vnfoss2010.smartshop.serverside.database.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

@PersistenceCapable
public class Product implements LocationCapable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String name;

	@Persistent
	private Double price;

	@Persistent
	private Boolean is_vat;

	@Persistent
	private Integer quantity;

	@Persistent
	private Date date_post;

	@Persistent
	private String warranty;

	@Persistent
	private String origin;

	@Persistent
	private String address;

	@Persistent
	private Double lat;

	@Persistent
	private Double lng;

	@Persistent
	private String username;

	@Persistent
	private Set<String> fts;

	@Persistent
	private List<Long> listPagesId;

	@Persistent
	private Set<String> setCategoryKeys;

	@Expose
	@Persistent(mappedBy = "product")
	@Element(dependent = "true")
	private List<Attribute> attributeSets;

	@Persistent
	private List<String> geocells;

	public Product() {
		setCategoryKeys = new HashSet<String>();
		attributeSets = new ArrayList<Attribute>();
		listPagesId = new ArrayList<Long>();

		this.fts = new HashSet<String>();
		ProductServiceImpl.updateFTSStuffForUserInfo(this);
	}

	public Product(String name, Double price, Boolean isVat, Integer quantity,
			String warranty, String origin, String address, Double lat,
			Double lng, String username) {
		this();
		this.name = name;
		this.price = price;
		is_vat = isVat;
		this.quantity = quantity;
		this.warranty = warranty;
		this.origin = origin;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.username = username;

		this.fts = new HashSet<String>();
		ProductServiceImpl.updateFTSStuffForUserInfo(this);
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
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the is_vat
	 */
	public Boolean isIs_vat() {
		return is_vat;
	}

	/**
	 * @param isVat
	 *            the is_vat to set
	 */
	public void setIs_vat(Boolean isVat) {
		is_vat = isVat;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the warranty
	 */
	public String getWarranty() {
		return warranty;
	}

	/**
	 * @param warranty
	 *            the warranty to set
	 */
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public Double getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(Double lng) {
		this.lng = lng;
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
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", quantity="
				+ quantity + ", warranty=" + warranty + ", price=" + price
				+ ", is_vat=" + is_vat + ", address=" + address + ", lat="
				+ lat + ", lng=" + lng + ", origin=" + origin + ", username="
				+ username + "]";
	}

	public List<Attribute> getAttributeSets() {
		return attributeSets;
	}

	public void setAttributeSets(List<Attribute> attributeSets) {
		this.attributeSets = attributeSets;
	}

	/**
	 * @param setCategoryKeys
	 *            the setCategoryKeys to set
	 */

	public void setSetCategoryKeys(Set<String> setCategoryKeys) {
		this.setCategoryKeys = setCategoryKeys;
	}

	public void setGeocells(List<String> geocell) {
		this.geocells = geocell;
	}

	/**
	 * @return the setCategoryKeys
	 */
	public Set<String> getSetCategoryKeys() {
		return setCategoryKeys;
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
	 * @param listPagesId
	 *            the listPagesId to set
	 */
	public void setListPagesId(List<Long> listPagesId) {
		this.listPagesId = listPagesId;
	}

	/**
	 * @return the listPagesId
	 */
	public List<Long> getListPagesId() {
		return listPagesId;
	}

	/**
	 * @param date_post
	 *            the date_post to set
	 */
	public void setDate_post(Date date_post) {
		this.date_post = date_post;
	}

	/**
	 * @return the date_post
	 */
	public Date getDate_post() {
		return date_post;
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
