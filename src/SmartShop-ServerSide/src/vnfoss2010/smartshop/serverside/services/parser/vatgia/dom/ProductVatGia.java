package vnfoss2010.smartshop.serverside.services.parser.vatgia.dom;

import java.util.ArrayList;

public class ProductVatGia {
	public String name;
	public String thumbnail;
	public ArrayList<Company> listCo = new ArrayList<Company>();
	public ArrayList<Group> listGroup = new ArrayList<Group>();

	public ProductVatGia(String name, String thumbnail) {
		super();
		this.name = name;
		this.thumbnail = thumbnail;
	}

	public ProductVatGia() {
	}
}
