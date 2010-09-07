package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.util.LinkedList;

public class Group implements Serializable  {
	public String name;
	public LinkedList<Pair> listAtt = new LinkedList<Pair>();

	public Group(String name) {
		this.name = name;
	}

	public Group() {
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", list=" + listAtt + "]";
	}
}
