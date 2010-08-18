package vnfoss2010.smartshop.serverside.database.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;

@PersistenceCapable
public class Product implements LocationCapable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String name;

	@Persistent
	private double price;

	@Persistent
	private boolean is_vat;

	@Persistent
	private int quantity;

	@Persistent
	private String warranty;

	@Persistent
	private String origin;

	@Persistent
	private String address;

	@Persistent
	private double lat;

	@Persistent
	private double lng;

	@Persistent
	private String username;

	@Persistent
	private Set<Long> setPagesId;

	@Persistent
	private Set<String> setCategoryKeys;

	@Persistent
	private Set<Attribute> setAttributes;
	
	@Persistent
	private List<String> geocells;

	public Product() {
		setSetPagesId(new HashSet<Long>());
		setCategoryKeys = new HashSet<String>();
		setAttributes = new HashSet<Attribute>();
	}

	public Product(String name, double price, boolean isVat, int quantity,
			String warranty, String origin, String address, double lat,
			double lng, String username) {
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
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the is_vat
	 */
	public boolean isIs_vat() {
		return is_vat;
	}

	/**
	 * @param isVat
	 *            the is_vat to set
	 */
	public void setIs_vat(boolean isVat) {
		is_vat = isVat;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
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
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(double lng) {
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

	/**
	 * @param setPagesId
	 *            the setPagesId to set
	 */
	public void setSetPagesId(Set<Long> setPagesId) {
		this.setPagesId = setPagesId;
	}

	/**
	 * @return the setPagesId
	 */
	public Set<Long> getSetPagesId() {
		return setPagesId;
	}

	/**
	 * @param setAttributes
	 *            the setAttributes to set
	 */
	public void setSetAttributes(Set<Attribute> setAttributes) {
		this.setAttributes = setAttributes;
	}

	/**
	 * @return the setAttributes
	 */
	public Set<Attribute> getSetAttributes() {
		return setAttributes;
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
}
