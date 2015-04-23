package net.mcarolan.whenzebus.api;

public class UnknownBusStop extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UnknownBusStop(String message) {
		super(message);
	}

}
