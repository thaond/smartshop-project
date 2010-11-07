package vnfoss2010.smartshop.serverside.database.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import vnfoss2010.smartshop.webbased.share.WMedia;

@PersistenceCapable
public class Media {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String name;

	@Persistent
	private String link;

	@Persistent
	private String mime_type;

	@Persistent
	private String description;

	public Media() {
	}

	// public Media(String name, String link, String mimeType, String
	// description,
	// Product product) {
	// this.name = name;
	// this.link = link;
	// mime_type = mimeType;
	// this.description = description;
	// }

	public Media(String name, String link, String mimeType, String description) {
		this.name = name;
		this.link = link;
		mime_type = mimeType;
		this.description = description;
	}
	
	public Media(String name, String link) {
		this.name = name;
		this.link = link;
		this.description = name;
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
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the mime_type
	 */
	public String getMime_type() {
		return mime_type;
	}

	/**
	 * @param mimeType
	 *            the mime_type to set
	 */
	public void setMime_type(String mimeType) {
		mime_type = mimeType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		Media other = (Media) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
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
		return "Media [id=" + getId() + ", link=" + link + ", mime_type="
				+ mime_type + ", name=" + name + "]";
	}

	// /**
	// * @param product
	// * the product to set
	// */
	// public void setProduct(Product product) {
	// this.product = product;
	// }
	//
	// /**
	// * @return the product
	// */
	// public Product getProduct() {
	// return product;
	// }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public WMedia cloneObject() {
		WMedia wm = new WMedia();
		wm.name = this.name;
		wm.link = this.link;
		wm.mime_type = this.mime_type;
		wm.description = this.description;
		return wm;
	}
}
