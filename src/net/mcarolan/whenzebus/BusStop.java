package net.mcarolan.whenzebus;

import android.content.Intent;
import net.mcarolan.whenzebus.api.Response;
import net.mcarolan.whenzebus.api.field.Fields;
import net.mcarolan.whenzebus.api.value.Latitude;
import net.mcarolan.whenzebus.api.value.Location;
import net.mcarolan.whenzebus.api.value.Longitude;
import net.mcarolan.whenzebus.api.value.StopCode1;
import net.mcarolan.whenzebus.api.value.StopPointIndicator;
import net.mcarolan.whenzebus.api.value.StopPointName;
import net.mcarolan.whenzebus.api.value.Towards;

public class BusStop {
	
	private final StopCode1 stopCode1;
	private final StopPointIndicator stopPointIndicator;
	private final StopPointName stopPointName;
	private final Towards towards;
	private final Location location;
	
	public BusStop(StopCode1 stopCode1, StopPointIndicator stopPointIndicator,
			StopPointName stopPointName, Towards towards, Location location) {
		this.stopCode1 = stopCode1;
		this.stopPointIndicator = stopPointIndicator;
		this.stopPointName = stopPointName;
		this.towards = towards;
		this.location = location;
	}

	public StopPointIndicator getStopPointIndicator() {
		return stopPointIndicator;
	}

	public StopCode1 getStopCode1() {
		return stopCode1;
	}
	
	public StopPointName getStopPointName() {
		return stopPointName;
	}
	
	public Towards getTowards() {
		return towards;
	}

	public Location getLocation() {
		return location;
	}

	public void writeTo(Intent intent) {
		intent.putExtra("stopcode1", stopCode1.getValue());
		intent.putExtra("stoppointindicator", stopPointIndicator.getValue());
		intent.putExtra("stoppointname", stopPointName.getValue());
		intent.putExtra("towards", towards.getValue());
		intent.putExtra("latitude", location.getLatitude().getValue());
		intent.putExtra("longitude", location.getLongitude().getValue());
	}
	
	public static BusStop readFrom(Intent intent) {
		final String stopCode1 = intent.getStringExtra("stopcode1");
		final String stopPointIndicator = intent.getStringExtra("stoppointindicator");
		final String stopPointName = intent.getStringExtra("stoppointname");
		final String towards = intent.getStringExtra("towards");
		final double latitude = intent.getDoubleExtra("latitude", 0.0);
		final double longitude = intent.getDoubleExtra("longitude", 0.0);
		return new BusStop(new StopCode1(stopCode1), new StopPointIndicator(stopPointIndicator), new StopPointName(stopPointName), new Towards(towards), new Location(new Latitude(latitude), new Longitude(longitude)));
	}
	
	public static BusStop fromResponse(Response response) {
		final String stopCode1 = Fields.StopCode1.extract(response);
		final String stopPointIndicator = Fields.StopPointIndicator.extract(response);
		final String stopPointName = Fields.StopPointName.extract(response);
		final String towards = Fields.Towards.extract(response);
		final double latitude = Fields.Latitude.extract(response);
		final double longitude = Fields.Longitude.extract(response);
		return new BusStop(new StopCode1(stopCode1), new StopPointIndicator(stopPointIndicator), new StopPointName(stopPointName), new Towards(towards), new Location(new Latitude(latitude), new Longitude(longitude)));
	}

	@Override
	public String toString() {
		return "BusStop [stopCode1=" + stopCode1 + ", stopPointIndicator="
				+ stopPointIndicator + ", stopPointName=" + stopPointName
				+ ", towards=" + towards + ", location=" + location + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((stopCode1 == null) ? 0 : stopCode1.hashCode());
		result = prime
				* result
				+ ((stopPointIndicator == null) ? 0 : stopPointIndicator
						.hashCode());
		result = prime * result
				+ ((stopPointName == null) ? 0 : stopPointName.hashCode());
		result = prime * result + ((towards == null) ? 0 : towards.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusStop other = (BusStop) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (stopCode1 == null) {
			if (other.stopCode1 != null)
				return false;
		} else if (!stopCode1.equals(other.stopCode1))
			return false;
		if (stopPointIndicator == null) {
			if (other.stopPointIndicator != null)
				return false;
		} else if (!stopPointIndicator.equals(other.stopPointIndicator))
			return false;
		if (stopPointName == null) {
			if (other.stopPointName != null)
				return false;
		} else if (!stopPointName.equals(other.stopPointName))
			return false;
		if (towards == null) {
			if (other.towards != null)
				return false;
		} else if (!towards.equals(other.towards))
			return false;
		return true;
	}
	
}
