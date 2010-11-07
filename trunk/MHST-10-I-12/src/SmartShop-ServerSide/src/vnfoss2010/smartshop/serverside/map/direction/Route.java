package vnfoss2010.smartshop.serverside.map.direction;

import java.util.List;

public class Route {
	String summary;
	List<Leg> legs;
	int[] waypoint_order;
	Polyline overview_polyline;
	String copyrights;
	String[] warnings;
}
