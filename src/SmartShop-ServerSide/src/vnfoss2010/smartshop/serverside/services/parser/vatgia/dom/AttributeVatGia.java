package vnfoss2010.smartshop.serverside.services.parser.vatgia.dom;

public class AttributeVatGia {
	public String name;
	public String value;

	public AttributeVatGia(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public AttributeVatGia() {
	}

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", value=" + value + "]";
	}
}
