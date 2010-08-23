package vnfoss2010.smartshop.serverside.map.geocoder;

public class Point {
	public double lat;
	public double lng;
	public double alt;

	public Point() {
	}

	public Point(double lat, double lng, double alt) {
		this.lat = lat;
		this.lng = lng;
		this.alt = alt;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * @param alt the alt to set
	 */
	public void setAlt(double alt) {
		this.alt = alt;
	}

	/**
	 * @return the alt
	 */
	public double getAlt() {
		return alt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Point [lat=" + lat + ", lng=" + lng + ", alt=" + alt + "]";
	}

	
}
