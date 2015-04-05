package net.mcarolan.whenzebus.api.predictionfield;

import net.mcarolan.whenzebus.api.Response;
import android.util.Log;

public class DoubleField implements Field {
	
	private static final String TAG = "DoublePredictionField";
	
	private final String fieldName;
	private final int sequenceNumber;
	
	public DoubleField(String fieldName, int sequenceNumber) {
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
	
	public double extract(Response prediction) {
		final String stringValue = prediction.getStringValueFromFieldName(fieldName);
		final double doubleValue;
		
		try {
			doubleValue = Double.parseDouble(stringValue);
		}
		catch (NumberFormatException e) {
			final String message = stringValue + " was not a valid double value in field " + fieldName;
			Log.e(TAG, message, e);
			throw new IllegalStateException(message, e);
		}
		
		return doubleValue;
	}
	
}