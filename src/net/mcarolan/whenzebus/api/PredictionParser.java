package net.mcarolan.whenzebus.api;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.mcarolan.whenzebus.api.predictionfield.PredictionField;
import net.mcarolan.whenzebus.api.predictionfield.PredictionFieldComparator;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class PredictionParser {

	private static final String TAG = "PredictionParser";
	
	public boolean isPrediction(final JSONArray array, final Set<? extends PredictionField> fields) {
		final int expected = fields.size() + 1;
		if (array.length() == expected) {
			final int responseType;
			try {
				responseType = array.getInt(0);
			} catch (JSONException e) {
				Log.e(TAG, "Unable to check whether " + array.toString() + " is a prediction", e);
				return false;
			}
			return responseType == 1;
		}
		else {
			Log.w(TAG, array.toString() + " was not a prediction, as its length was " + array.length() + " and a valid prediction for " + fields.toString() + " should have length " + fields);
			return false;
		}
	}
	
	public Prediction parsePrediction(final JSONArray array, final Set<? extends PredictionField> fields) {
		if (!isPrediction(array, fields)) {
			throw new IllegalStateException("Could not parse prediction for " + array.toString() + " as isPrediction returns false for " + fields.toString());
		}
		
		final ImmutableMap.Builder<PredictionField, String> builder = ImmutableMap.builder();
		final List<PredictionField> fieldList = Lists.newArrayList(fields);

		Collections.sort(fieldList, new PredictionFieldComparator());

		for (int i = 0; i < fieldList.size(); ++i) {
			final String value;
			try {
				value = array.getString(1 + i);
			} catch (JSONException e) {
				final String message = "Unable to parse prediction " + array.toString() + " for fields " + fields.toString();
				Log.e(TAG, message, e);
				throw new IllegalStateException(message, e);
			}

			final PredictionField field = fieldList.get(i);
			builder.put(field, value);
		}
		
		final ImmutableMap<PredictionField, String> fieldToValue = builder.build();
		return new Prediction(fieldToValue);
	}
	
}
