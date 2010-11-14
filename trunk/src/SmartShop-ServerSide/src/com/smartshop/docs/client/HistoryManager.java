package com.smartshop.docs.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.smartshop.docs.client.utils.ClientUtil;

public class HistoryManager implements ValueChangeHandler<String> {
	private static HistoryManager instance = null;
	private static final String INIT_STATE = "intro";

	/**
	 * You should use this method to create an instance of this class.<br/>
	 * <b>Note:</b> <code>Singleton</code> is used here
	 * 
	 * @return
	 */
	public static HistoryManager getInstance() {
		if (instance == null)
			instance = new HistoryManager();
		return instance;
	}

	private HistoryManager() {
	}

	private void onHistoryChange(String token) {
		if (token.equals(""))
			token = "intro";
		if (token.equals("dev-full-services")) {
			MainPanel.getInstance().setRightWidget(FullServicesPanel.getInstance());
		} else if (ClientUtil.mapNode.containsKey(token)) {
			ItemNode itemNode = ClientUtil.mapNode.get(token);
			ContentPanel.getInstance().showData(itemNode);
			MainPanel.getInstance().setRightWidget(ContentPanel.getInstance());
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		System.out.println(event.getValue());
		onHistoryChange(event.getValue());
	}

	public void initHistoryState() {

		// Get the history token. You can use
		// History.getToken() throughout your
		// web application to determine what
		// state your application should be in.
		String token = History.getToken();

		// determine if there is a token in the history
		// stack, and if there is not a token then you
		// can pass through any string you want,
		// denoting that your web application is in its
		// initial state.
		if (token.length() == 0) {
			onHistoryChange(INIT_STATE);
		} else {
			onHistoryChange(token);
		}
	}
}
