package vnfoss2010.smartshop.serverside.services.parser.vatgia.dom;

import java.util.ArrayList;
import java.util.List;

public class Group {
	public String name;
	public List<AttributeVatGia> list = new ArrayList<AttributeVatGia>();

	public Group(String name) {
		this.name = name;
	}

	public Group() {
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", list=" + list + "]";
	}
}
