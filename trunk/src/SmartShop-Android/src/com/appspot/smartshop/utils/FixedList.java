package com.appspot.smartshop.utils;

import java.util.LinkedList;

public class FixedList<T> extends LinkedList<T> {
	private T removedItem = null;
	
	public boolean add(T e) {
		if (size() == 2) {
			removedItem = remove(0);
			super.add(e);
			return true;
		} else if (size() < 2) {
			removedItem = null;
			super.add(e);
			return true;
		}
		
		removedItem = null;
		return false;
	};
	
	public T getRemovedItem() {
		return removedItem;
	}
	
	public void showList() {
		int len = size();
		for (int i = 0; i < len; ++i) {
			System.out.println(get(i));
		}
	}
}
