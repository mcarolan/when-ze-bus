package net.mcarolan.whenzebus.api;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.mcarolan.whenzebus.api.predictionfield.Field;
import net.mcarolan.whenzebus.api.predictionfield.FieldComparator;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ResponseParser {
	
	private final String predictionResponse;
	
	private static final String TAG = "PredictionResponseParser";
	
	public ResponseParser(String predictionResponse) {
		this.predictionResponse = predictionResponse;
	}
	
	private boolean isPrediction(final JSONArray array, final Set<? extends Field> fields) {
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
	
	private Response parsePrediction(final JSONArray array, final Set<? extends Field> fields) {
		if (!isPrediction(array, fields)) {
			throw new IllegalStateException("Could not parse prediction for " + array.toString() + " as isPrediction returns false for " + fields.toString());
		}
		
		final ImmutableMap.Builder<Field, String> builder = ImmutableMap.builder();
		final List<Field> fieldList = Lists.newArrayList(fields);

		Collections.sort(fieldList, new FieldComparator());

		for (int i = 0; i < fieldList.size(); ++i) {
			final String value;
			try {
				value = array.getString(1 + i);
			} catch (JSONException e) {
				final String message = "Unable to parse prediction " + array.toString() + " for fields " + fields.toString();
				Log.e(TAG, message, e);
				throw new IllegalStateException(message, e);
			}

			final Field field = fieldList.get(i);
			builder.put(field, value);
		}
		
		final ImmutableMap<Field, String> fieldToValue = builder.build();
		return new Response(fieldToValue);
	}
	
	public Set<Response> extractPredictions(final Set<? extends Field> fields) {
		final Set<Response> predictions = Sets.newHashSet();
		for (final String arrayString : Splitter.on('\n').split(predictionResponse)) {
			final JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(arrayString);
			} catch (JSONException e) {
				final String message = "could not parse " + arrayString + " into a json array";
				Log.e(TAG, message, e);
				throw new IllegalStateException(message, e);
			}
			
			if (isPrediction(jsonArray, fields)) {
				predictions.add(parsePrediction(jsonArray, fields));
			}
		}
		return predictions;
	}

}
