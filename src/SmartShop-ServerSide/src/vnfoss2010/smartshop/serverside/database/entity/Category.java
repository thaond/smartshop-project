package vnfoss2010.smartshop.serverside.database.entity;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable 
public class Category {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String key_cat;
	
	@Persistent
	private String name;
	
	@Persistent
	private int parent_id;
	
	@Persistent
	private Set<Long> setPages;
	
	public Category(){
		setPages = new HashSet<Long>();
	}

	public Category(String name, int parentId) {
		this();
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
	public int getParent_id() {
		return parent_id;
	}

	/**
	 * @param parentId the parent_id to set
	 */
	public void setParent_id(int parentId) {
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
	 * @param setPages the setPages to set
	 */
	public void setSetPages(Set<Long> setPages) {
		this.setPages = setPages;
	}

	/**
	 * @return the setPages
	 */
	public Set<Long> getSetPages() {
		return setPages;
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
	
}
