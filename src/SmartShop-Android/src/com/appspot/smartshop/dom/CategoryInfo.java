package com.appspot.smartshop.dom;

import java.io.Serializable;

public class CategoryInfo implements Serializable{
	public String[] parentCategory;
	public String [][]childrenCategory;
	public CategoryInfo(String [] parentCategory, String [][]childrenCategory){
		this.parentCategory = parentCategory;
		this.childrenCategory = childrenCategory;
	}
}
