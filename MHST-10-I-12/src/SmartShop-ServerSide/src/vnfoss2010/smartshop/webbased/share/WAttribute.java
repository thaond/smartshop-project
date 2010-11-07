package vnfoss2010.smartshop.webbased.share;

import java.io.Serializable;

public class WAttribute implements Serializable {
	private static final long serialVersionUID = 1L;
	public String key_cat;
	public String name;
	public String value;
	public String username;

	public WAttribute() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WAttribute [key_cat=" + key_cat + ", name=" + name
				+ ", username=" + username + ", value=" + value + "]";
	}
}
