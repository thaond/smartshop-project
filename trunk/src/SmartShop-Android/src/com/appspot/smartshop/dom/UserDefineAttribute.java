package com.appspot.smartshop.dom;


public class UserDefineAttribute {
	public String newAttribute;
	public String valueOfNewAttribute;
	public UserDefineAttribute(String newAttribute, String valueOfNewAttribute){
		this.newAttribute = newAttribute;
		this.valueOfNewAttribute = valueOfNewAttribute;
	}
	public String getNewAttribute(){
		return this.newAttribute;
	}
	public String getValueOfNewAttribute(){
		return this.valueOfNewAttribute;
	}
}
