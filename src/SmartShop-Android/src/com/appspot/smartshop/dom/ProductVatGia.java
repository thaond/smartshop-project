package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.util.LinkedList;

public class ProductVatGia implements Serializable {
	public String name;
	public String thumbnail;
	public LinkedList<Company> listCo = new LinkedList<Company>();
	public LinkedList<Group> listGroup = new LinkedList<Group>();

	public ProductVatGia(String name, String thumbnail) {
		super();
		this.name = name;
		this.thumbnail = thumbnail;
	}

	public ProductVatGia() {
	}
}
