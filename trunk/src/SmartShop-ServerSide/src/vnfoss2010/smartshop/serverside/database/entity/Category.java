package vnfoss2010.smartshop.serverside.database.entity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gson.annotations.Exclude;

@PersistenceCapable 
public class Category { 

	@PrimaryKey
	private String key_cat;
	
	@Persistent 
	private String name;
	
	@Persistent 
	private String parent_id;  

	@Exclude
	@Persistent 
	private List<Long> listPages;  
	
	@Exclude
	@Persistent
	private List<Long> listProducts;
	 
	public Category(){
		listPages = new ArrayList<Long>();
		listProducts = new ArrayList<Long>();
	}

	public Category(String key_cat,String name, String parentId) {
		this();
		this.key_cat = key_cat;
		this.name = name;
		parent_id = parentId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent_id
	 */
	public String getParent_id() {
		return parent_id;
	}

	/**
	 * @param parentId the parent_id to set
	 */
	public void setParent_id(String parentId) {
		parent_id = parentId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getKey_cat() == null) ? 0 : getKey_cat().hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		Category other = (Category) obj;
		if (getKey_cat() == null) {
			if (other.getKey_cat() != null)
				return false;
		} else if (!getKey_cat().equals(other.getKey_cat()))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Category [id=" + getKey_cat() + ", name=" + name + ", parent_id="
				+ parent_id + "]";
	}

	/**
	 * @param key_cat the key_cat to set
	 */
	public void setKey_cat(String key_cat) {
		this.key_cat = key_cat;
	}

	/**
	 * @return the key_cat
	 */
	public String getKey_cat() {
		return key_cat;
	}

	/**
	 * @param listPages the listPages to set
	 */
	public void setListPages(List<Long> listPages) {
		this.listPages = listPages;
	}

	/**
	 * @return the listPages
	 */
	public List<Long> getListPages() {
		return listPages;
	}

	/**
	 * @param listProducts the listProducts to set
	 */
	public void setListProducts(List<Long> listProducts) {
		this.listProducts = listProducts;
	}

	/**
	 * @return the listProducts
	 */
	public List<Long> getListProducts() {
		return listProducts;
	}
	
}
