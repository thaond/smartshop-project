package vnfoss2010.smartshop.serverside.map.direction;

import vnfoss2010.smartshop.serverside.net.HttpRequest;

import com.google.gson.Gson;

public class TestDirection {
	public static final String GET_DIRECTION_URL = "http://maps.google.com/maps/api/directions/json?"
			+ "origin=%s"
			+ "&destination=%s"
			+ "&sensor=true&language=vi&units=metric";

	public static void main(String[] args) {
		String result = HttpRequest.get(String.format(GET_DIRECTION_URL, "Chicago,IL", "Los+Angeles,CA")).content;
		
		Gson gson = new Gson();
		Direction direction = gson.fromJson(result, Direction.class);
		
		System.out.println(gson.toJson(direction));
	}
}
