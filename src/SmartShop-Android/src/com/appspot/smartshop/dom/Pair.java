package com.appspot.smartshop.dom;

public class Pair {
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
