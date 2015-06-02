package net.mcarolan.whenzebus.api.value;


import com.google.common.base.Objects;

public class Location {
	
	private final Latitude latitude;
	private final Longitude longitude;
	
	public Location(Latitude latitude, Longitude longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Latitude getLatitude() {
		return latitude;
	}

	public Longitude getLongitude() {
		return longitude;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return Objects.equal(latitude, location.latitude) &&
				Objects.equal(longitude, location.longitude);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(latitude, longitude);
	}

	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}

}
