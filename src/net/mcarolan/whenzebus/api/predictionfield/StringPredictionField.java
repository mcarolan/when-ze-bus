package net.mcarolan.whenzebus.api.predictionfield;

import net.mcarolan.whenzebus.api.Prediction;

public class StringPredictionField implements PredictionField {
	
	private final String fieldName;
	private final int sequenceNumber;

	public StringPredictionField(String fieldName, int sequenceNumber) {
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
	
	public String extract(Prediction prediction) {
		return prediction.getStringValueFromFieldName(fieldName);
	}
	
}
