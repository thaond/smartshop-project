package vnfoss2010.smartshop.serverside.services.parser.vatgia.dom;

import java.util.Set;

public class NCompany {
	private String name;
	private String vnd;
	private String usd;
	private String vatStatus, comparePrice, status, warranty, address;
	private Set<String> phones;

	public NCompany() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVnd() {
		return vnd;
	}

	public void setVnd(String vnd) {
		this.vnd = vnd;
	}

	public String getUsd() {
		return usd;
	}

	public void setUsd(String usd) {
		this.usd = usd;
	}

	public String getVatStatus() {
		return vatStatus;
	}

	public void setVatStatus(String vatStatus) {
		this.vatStatus = vatStatus;
	}

	public String getComparePrice() {
		return comparePrice;
	}

	public void setComparePrice(String comparePrice) {
		this.comparePrice = comparePrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<String> getPhones() {
		return phones;
	}

	public void setPhones(Set<String> phones) {
		this.phones = phones;
	}
}
