package net.mcarolan.whenzebus;

import android.content.Intent;
import net.mcarolan.whenzebus.api.StopCode1;
import net.mcarolan.whenzebus.api.StopPointName;

public class BusStop {
	
	private final StopCode1 stopCode1;
	private final StopPointName stopPointName;
	
	public BusStop(StopCode1 stopCode1, StopPointName stopPointName) {
		this.stopCode1 = stopCode1;
		this.stopPointName = stopPointName;
	}
	
	public StopCode1 getStopCode1() {
		return stopCode1;
	}
	
	public StopPointName getStopPointName() {
		return stopPointName;
	}
	
	public void writeTo(Intent intent) {
		intent.putExtra("stopcode1", stopCode1.getValue());
		intent.putExtra("stoppointname", stopPointName.getValue());
	}
	
	public static BusStop readFrom(Intent intent) {
		final String stopCode1 = intent.getStringExtra("stopcode1");
		final String stopPointName = intent.getStringExtra("stoppointname");
		return new BusStop(new StopCode1(stopCode1), new StopPointName(stopPointName));
	}

	@Override
	public String toString() {
		return "BusStop [" + stopCode1 + ", " + stopPointName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((stopCode1 == null) ? 0 : stopCode1.hashCode());
		result = prime * result
				+ ((stopPointName == null) ? 0 : stopPointName.hashCode());
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
		if (stopCode1 == null) {
			if (other.stopCode1 != null)
				return false;
		} else if (!stopCode1.equals(other.stopCode1))
			return false;
		if (stopPointName == null) {
			if (other.stopPointName != null)
				return false;
		} else if (!stopPointName.equals(other.stopPointName))
			return false;
		return true;
	}
	
}
