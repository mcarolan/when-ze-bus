package net.mcarolan.whenzebus.api.field;

import net.mcarolan.whenzebus.api.Response;

public class StringField implements Field {
	
	private final String fieldName;
	private final int sequenceNumber;

	public StringField(String fieldName, int sequenceNumber) {
		this.fieldName = fieldName;
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public String extract(Response prediction) {
		return prediction.getStringValueFromFieldName(fieldName);
	}

	@Override
	public String toString() {
		return getFieldName() + "/StringField";
	}
	
}
