package net.mcarolan.whenzebus.api.value;

import com.google.common.base.Objects;

abstract class DoubleValue {

	private final double value;

	public DoubleValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DoubleValue that = (DoubleValue) o;
		return Objects.equal(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}
	
}
