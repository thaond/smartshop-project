package vnfoss2010.smartshop.webbased.share;

import java.io.Serializable;

public class WMedia implements Serializable {
	private static final long serialVersionUID = 1L;
	public String name;
	public String link;
	public String mime_type;
	public String description;

	public WMedia(String name, String link, String mimeType, String description) {
		this.name = name;
		this.link = link;
		mime_type = mimeType;
		this.description = description;
	}

	public WMedia() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WMedia [description=" + description + ", link=" + link
				+ ", mime_type=" + mime_type + ", name=" + name + "]";
	}
}
