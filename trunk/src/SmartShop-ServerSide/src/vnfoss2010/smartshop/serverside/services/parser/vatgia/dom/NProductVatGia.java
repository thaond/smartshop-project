package vnfoss2010.smartshop.serverside.services.parser.vatgia.dom;

public class NProductVatGia {

	int numOfResults;
	int numOfPages;
	String imageThumbnail, imageBlankThumbnail, urlListShop, productName,
			priceVND, numOfStore, categoryPageURL, categoryName;
	public int getNumOfResults() {
		return numOfResults;
	}
	public void setNumOfResults(int numOfResults) {
		this.numOfResults = numOfResults;
	}
	public int getNumOfPages() {
		return numOfPages;
	}
	public void setNumOfPages(int numOfPages) {
		this.numOfPages = numOfPages;
	}
	public String getImageThumbnail() {
		return imageThumbnail;
	}
	public void setImageThumbnail(String imageThumbnail) {
		this.imageThumbnail = imageThumbnail;
	}
	public String getImageBlankThumbnail() {
		return imageBlankThumbnail;
	}
	public void setImageBlankThumbnail(String imageBlankThumbnail) {
		this.imageBlankThumbnail = imageBlankThumbnail;
	}
	public String getUrlListShop() {
		return urlListShop;
	}
	public void setUrlListShop(String urlListShop) {
		this.urlListShop = urlListShop;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPriceVND() {
		return priceVND;
	}
	public void setPriceVND(String priceVND) {
		this.priceVND = priceVND;
	}
	public String getNumOfStore() {
		return numOfStore;
	}
	public void setNumOfStore(String numOfStore) {
		this.numOfStore = numOfStore;
	}
	public String getCategoryPageURL() {
		return categoryPageURL;
	}
	public void setCategoryPageURL(String categoryPageURL) {
		this.categoryPageURL = categoryPageURL;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	}
