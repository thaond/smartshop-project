package vnfoss2010.smartshop.serverside.map.direction;

import java.util.List;

public class Leg {
	List<Step> steps;
	Pair distance;
	Pair duration;
	GeoPoint start_location;
	GeoPoint end_location;
	String start_address;
	String end_address;
}
