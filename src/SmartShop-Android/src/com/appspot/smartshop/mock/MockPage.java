package com.appspot.smartshop.mock;

import java.util.Date;

import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.Utils;

public class MockPage {
	private static final int NUM_OF_PAGES = 20;

	public static Page getInstance() {
		Page page = new Page();
		page.id = 1L;
		page.name = "Foo page";
		page.content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
		page.page_view = 100;
		page.username = Global.username;
		page.date_post = new Date();
		
		return page;
	}
	
	public static Page[] getPages() {
		Page[] pages = new Page[NUM_OF_PAGES];
		for (int i = 0; i < NUM_OF_PAGES; ++i) {
			pages[i] = new Page();
			
			pages[i].id = (long) i;
			pages[i].name = "page " + (i + 1);
			pages[i].content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
			pages[i].page_view = Utils.random(1000);
			pages[i].username = MockUserInfo.getUsers()[Utils.random(4)].username;
			pages[i].date_post = new Date(2010 - 1900, Utils.random(12), Utils.random(31));
		}
		
		return pages;
	}
}
