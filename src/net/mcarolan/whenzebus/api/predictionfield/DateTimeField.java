package net.mcarolan.whenzebus.api.predictionfield;

import net.mcarolan.whenzebus.api.Response;

import org.joda.time.DateTime;

public class DateTimeField implements Field {
	
	private final LongField longPredictionField;
	
	public DateTimeField(String fieldName, int sequenceNumber) {
		longPredictionField = new LongField(fieldName, sequenceNumber);
	}

	@Override
	public String getFieldName() {
		return longPredictionField.getFieldName();
	}

	@Override
	public int getSequenceNumber() {
		return longPredictionField.getSequenceNumber();
	}
	
	public DateTime extract(Response prediction) {
		final long timestamp = longPredictionField.extract(prediction);
		return new DateTime(timestamp);
	}
	
}
