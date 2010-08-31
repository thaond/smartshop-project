package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class CategoryInfo implements Serializable{
	public String[] parentCategory;
	public String [][]childrenCategory;
	public CategoryInfo(String [] parentCategory, String [][]childrenCategory){
		this.parentCategory = parentCategory;
		this.childrenCategory = childrenCategory;
	}
	
	public static Type getType() {
		return new TypeToken<List<CategoryInfo>>() {}.getType();
	}
}
