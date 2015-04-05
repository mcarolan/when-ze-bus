package net.mcarolan.whenzebus.api.predictionfield;

import net.mcarolan.whenzebus.api.Prediction;

import org.joda.time.DateTime;

public class DateTimePredictionField implements PredictionField {
	
	private final LongPredictionField longPredictionField;
	
	public DateTimePredictionField(String fieldName, int sequenceNumber) {
		longPredictionField = new LongPredictionField(fieldName, sequenceNumber);
	}

	@Override
	public String getFieldName() {
		return longPredictionField.getFieldName();
	}

	@Override
	public int getSequenceNumber() {
		return longPredictionField.getSequenceNumber();
	}
	
	public DateTime extract(Prediction prediction) {
		final long timestamp = longPredictionField.extract(prediction);
		return new DateTime(timestamp);
	}
	
}
