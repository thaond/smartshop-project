package vnfoss2010.smartshop.serverside.database.dom;

import java.util.List;

import vnfoss2010.smartshop.serverside.database.entity.Product;

public class Subscribe_ListProduct {
	private long id;
	private List<Product> listProducts;

	public Subscribe_ListProduct() {
	}

	public Subscribe_ListProduct(long idSubscribe, List<Product> listProducts) {
		this.id = idSubscribe;
		this.listProducts = listProducts;
	}

	/**
	 * @param idSubscribe
	 *            the idSubscribe to set
	 */
	public void setIdSubscribe(long idSubscribe) {
		this.id = idSubscribe;
	}

	/**
	 * @return the idSubscribe
	 */
	public long getIdSubscribe() {
		return id;
	}

	/**
	 * @param listProducts
	 *            the listProducts to set
	 */
	public void setListProducts(List<Product> listProducts) {
		this.listProducts = listProducts;
	}

	/**
	 * @return the listProducts
	 */
	public List<Product> getListProducts() {
		return listProducts;
	}

}
