package vnfoss2010.smartshop.webbased.share;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vnfoss2010.smartshop.webbased.client.utils.WebbasedUtils;

public class WProduct implements Serializable {
	private static final String URL_NO_PRODUCT_IMG = "./webbased/product_unknown.png";
	private static final long serialVersionUID = 4327456927331765617L;
	public Long id;
	public int product_view;
	public String name = null;
	public double price;
	public String description = null;
	public boolean isVAT;
	public int quantity;
	public Date date_post = null;
	public String warranty = null;
	public String origin = null;
	public String address = null;
	public double lat;
	public double lng;
	public int sum_star;
	public int count_vote;
	public String username = null;
	public List<WMedia> setMedias = new ArrayList<WMedia>();
	public Set<Long> setPagesId = new HashSet<Long>();
	public Set<String> setCategoryKeys = new HashSet<String>();
	public Set<WAttribute> attributeSets = new HashSet<WAttribute>();

	public WUserInfo userInfo;
	public List<WComment> listComments = new ArrayList<WComment>();
	public List<WProduct> listRelatedProduct = new ArrayList<WProduct>();

	public WProduct(String name, double price, String description) {
		this.name = name;
		this.price = price;
		this.description = description;
	}

	public WProduct() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WProduct [address=" + address + ", attributeSets="
				+ attributeSets + ", count_vote=" + count_vote + ", date_post="
				+ date_post + ", description=" + description + ", id=" + id
				+ ", isVAT=" + isVAT + ", lat=" + lat + ", listComments="
				+ listComments + ", lng=" + lng + ", name=" + name
				+ ", origin=" + origin + ", price=" + price + ", product_view="
				+ product_view + ", quantity=" + quantity
				+ ", setCategoryKeys=" + setCategoryKeys + ", setMedias="
				+ setMedias + ", setPagesId=" + setPagesId + ", sum_star="
				+ sum_star + ", userInfo=" + userInfo + ", username="
				+ username + ", warranty=" + warranty + "]";
	}

	public String getRandomThumbImage() {
		if (setMedias == null || setMedias.isEmpty())
			return URL_NO_PRODUCT_IMG;
		else {
			return setMedias.get((int) (Math.random() * setMedias.size())).link;
		}
	}

	public String getShortDescription() {
		if (WebbasedUtils.isEmptyOrNull(description))
			return description;
		int to = Math.min(60, description.length());
		return description.substring(0, Math.max(to, description.indexOf(" ",
				60)));
	}
}
