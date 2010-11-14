package com.smartshop.docs.client;

import com.google.gwt.user.client.ui.Frame;

/**
 * @author VoMinhTam
 */
public class FullServicesPanel extends Frame{
	private static FullServicesPanel instance = null;

	public static FullServicesPanel getInstance() {
		if (instance == null) {
			instance = new FullServicesPanel();
		}

		return instance;
	}

	private FullServicesPanel() {
		this.setSize("100%", "92%");
		this.setUrl("https://spreadsheets.google.com/ccc?key=0AlA9GeZqMuOAdE53bTdJN2E5SUpraEI1VVBNYUE5RGc&hl=en#gid=0");
	}
}
