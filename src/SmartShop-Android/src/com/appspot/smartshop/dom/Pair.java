package com.appspot.smartshop.dom;

import java.io.Serializable;

public class Pair implements Serializable {
	public String name;
	public String value;

	public Pair(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public Pair() {
	}

	@Override
	public String toString() {
		return "Pair [name=" + name + ", value=" + value + "]";
	}

}
