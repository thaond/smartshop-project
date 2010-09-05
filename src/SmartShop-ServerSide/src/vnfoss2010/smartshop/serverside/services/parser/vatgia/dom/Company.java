package vnfoss2010.smartshop.serverside.services.parser.vatgia.dom;

import java.util.ArrayList;
import java.util.List;

public class Company {
	public String name;
	public String vnd;
	public String usd;
	public String comment;
	public List<Pair> listProInfos = new ArrayList<Pair>();
	public List<Pair> listCoInfos = new ArrayList<Pair>();

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
}
