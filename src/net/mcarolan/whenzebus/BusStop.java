package net.mcarolan.whenzebus;

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

	@Override
	public String toString() {
		return "BusStop [" + stopCode1 + ", " + stopPointName + "]";
	}
	
}
