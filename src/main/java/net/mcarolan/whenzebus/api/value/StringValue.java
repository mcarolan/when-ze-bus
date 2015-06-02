package net.mcarolan.whenzebus.api.value;

import com.google.common.base.Objects;

abstract class StringValue {
	
	private final String value;

	public StringValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StringValue that = (StringValue) o;
		return Objects.equal(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return value;
	}

}
