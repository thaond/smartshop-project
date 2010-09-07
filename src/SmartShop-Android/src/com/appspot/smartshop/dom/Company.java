package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Company implements Serializable {
	public String name;
	public String vnd;
	public String usd;
	public String comment;
	public LinkedList<Pair> listProInfos = new LinkedList<Pair>();
	public LinkedList<Pair> listCoInfos = new LinkedList<Pair>();

	public Company(String name, String vnd, String usd, String comment) {
		this.name = name;
		this.vnd = vnd;
		this.usd = usd;
		this.comment = comment;
	}

	public Company() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Company [name=" + name + ", vnd=" + vnd + ", comment="
				+ comment + ", usd=" + usd + ", listProInfos=" + listProInfos
				+ ", listCoInfos=" + listCoInfos + "]";
	}
	
	public String getExtraInfo() {
		String extraInfo = "";
		for (Pair pair : listProInfos) {
			extraInfo += String.format("<b>%s</br> %s<br />", pair.name, pair.value);
		}
		for (Pair pair : listCoInfos) {
			extraInfo += String.format("<b>%s</br> %s<br />", pair.name, pair.value);
		}
		
		return extraInfo;
	}
}
