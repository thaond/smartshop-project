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
import com.google.gson.annotations.Exclude;

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
	private Date date_post;

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
	private String description;
	
	@Persistent
	private int product_view;

	@Persistent
	private String username;

	@Persistent
	private List<String> listBuyers;

	@Exclude
	@Persistent
	private Set<String> fts;
	
	@Persistent
	private Set<Long> setPagesID;
	@Persistent
	private Set<String> setCategoryKeys;

	@Persistent(mappedBy = "product")
	@Element(dependent = "true")
	private List<Attribute> attributeSets;

	@Exclude
	@Persistent
	private List<String> geocells;

	public Set<Long> getSetPagesID() {
		return setPagesID;
	}

	public void setSetPages(Set<Long> setPages) {
		this.setPagesID = setPages;
	}

	public Boolean getIs_vat() {
		return is_vat;
	}

	public Product() {
		this("", 0, false, 0, "", "", "", 0, 0, "", 0, "");
		ProductServiceImpl.updateFTSStuffForProduct(this);
	}

	public Product(String name, double price, boolean isVat, int quantity,
			String warranty, String origin, String address, double lat,
			double lng, String description, int view, String username) {
		this.name = name;
		this.price = price;
		is_vat = isVat;
		this.quantity = quantity;
		this.warranty = warranty;
		this.origin = origin;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.description = description;
		this.product_view = view;
		this.username = username;

		setCategoryKeys = new HashSet<String>();
		attributeSets = new ArrayList<Attribute>();
		setPagesID = new HashSet<Long>();
		listBuyers = new ArrayList<String>();
		this.fts = new HashSet<String>();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Product [address=" + address + ", attributeSets="
				+ attributeSets + ", geocells=" + geocells + ", id=" + id
				+ ", is_vat=" + is_vat + ", lat=" + lat + ", lng=" + lng
				+ ", name=" + name + ", origin=" + origin + ", price=" + price
				+ ", quantity=" + quantity + ", setCategoryKeys="
				+ setCategoryKeys + ", setPagesId=" + getSetPagesID()
				+ ", username=" + username + ", warranty=" + warranty + "]";
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

	public static void main(String[] args) {
		Product product = new Product();
		product = new Product("Nec Monitor", 123, false, 2, "12 month",
				"China", "Binh Tan", 10.11, 106.123, "Man hình rất xịn" , 3, "tamvo");

		product.getSetCategoryKeys().add("comp");

		Gson gson = new Gson();
		String json = gson.toJson(product);
		System.out.println(json);
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

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the product_view
	 */
	public int getProduct_view() {
		return product_view;
	}

	/**
	 * @param productView the product_view to set
	 */
	public void setProduct_view(int productView) {
		product_view = productView;
	}

	/**
	 * @param setPagesID the setPagesID to set
	 */
	public void setSetPagesID(Set<Long> setPagesID) {
		this.setPagesID = setPagesID;
	}

	/**
	 * @param listBuyers the listBuyers to set
	 */
	public void setListBuyers(List<String> listBuyers) {
		this.listBuyers = listBuyers;
	}

	/**
	 * @return the listBuyers
	 */
	public List<String> getListBuyers() {
		return listBuyers;
	}
}
