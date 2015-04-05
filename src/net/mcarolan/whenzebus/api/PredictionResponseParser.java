package net.mcarolan.whenzebus.api;

import java.util.Set;

import net.mcarolan.whenzebus.api.predictionfield.PredictionField;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class PredictionResponseParser {
	
	private final String predictionResponse;
	private final PredictionParser predictionParser;
	
	private static final String TAG = "PredictionResponseParser";
	
	public PredictionResponseParser(String predictionResponse, PredictionParser predictionParser) {
		this.predictionResponse = predictionResponse;
		this.predictionParser = predictionParser;
	}
	
	public Set<Prediction> extractPredictions(final Set<? extends PredictionField> fields) {
		final Set<Prediction> predictions = Sets.newHashSet();
		for (final String arrayString : Splitter.on('\n').split(predictionResponse)) {
			final JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(arrayString);
			} catch (JSONException e) {
				final String message = "could not parse " + arrayString + " into a json array";
				Log.e(TAG, message, e);
				throw new IllegalStateException(message, e);
			}
			
			if (predictionParser.isPrediction(jsonArray, fields)) {
				predictions.add(predictionParser.parsePrediction(jsonArray, fields));
			}
		}
		return predictions;
	}

}
