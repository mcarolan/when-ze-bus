package net.mcarolan.whenzebus.api.predictionfield;

import net.mcarolan.whenzebus.api.Response;
import android.util.Log;

public class LongField implements Field {
	
	private static final String TAG = "LongPredictionField";
	
	private final String fieldName;
	private final int sequenceNumber;
	
	public LongField(String fieldName, int sequenceNumber) {
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
	
	public long extract(Response prediction) {
		final String stringValue = prediction.getStringValueFromFieldName(fieldName);
		final long longValue;
		
		try {
			longValue = Long.parseLong(stringValue);
		}
		catch (NumberFormatException e) {
			final String message = stringValue + " was not a valid long value in field " + fieldName;
			Log.e(TAG, message, e);
			throw new IllegalStateException(message, e);
		}
		
		return longValue;
	}
	
}