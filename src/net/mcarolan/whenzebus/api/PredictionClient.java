package net.mcarolan.whenzebus.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;

import android.util.Log;
import net.mcarolan.whenzebus.api.predictionfield.PredictionField;
import net.mcarolan.whenzebus.api.predictionfield.PredictionFields;

public class PredictionClient {
	
	private final String baseUri;
	private final Set<? extends PredictionField> fields;
	private final StopCode1 stopCode1;
	
	private final String TAG = "PredictionClient";
	
	public static final Set<? extends PredictionField> DEFAULT_PREDICTION_FIELDS = 
			Sets.newHashSet(PredictionFields.EstimatedTime, PredictionFields.ExpireTime, PredictionFields.DestinationText, PredictionFields.LineName);
	
	public PredictionClient(String baseUri,
			 StopCode1 stopCode1) {
		this(baseUri, stopCode1, DEFAULT_PREDICTION_FIELDS);
	}
	
	public PredictionClient(String baseUri,
			 StopCode1 stopCode1, Set<? extends PredictionField> fields) {
		this.baseUri = baseUri;
		this.fields = fields;
		this.stopCode1 = stopCode1;
	}
	
	public Set<Prediction> getPredictions() {
		final PredictionRequest predictionRequest = new PredictionRequest(fields, stopCode1);
		final String uri = baseUri + predictionRequest.getURI();
		final URL url;
		
		try {
			url = new URL(uri);
		} catch (MalformedURLException e) {
			final String message = uri + " is not a valid URL";
			Log.e(TAG, message, e);
			throw new IllegalStateException(message, e);
		}
		
		final HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			final String message = "Could not open connection to " + uri;
			Log.e(TAG, message);
			throw new IllegalStateException(message, e);
		}
		
		try {
			final int responseCode;
			try {
				responseCode = connection.getResponseCode();
			} catch (IOException e) {
				final String message = "Could not get response code from " + uri;
				Log.e(TAG, message, e);
				throw new IllegalStateException(message, e);
			}
			
			if (responseCode != HttpURLConnection.HTTP_OK) {
				final String message = "Unexpected response code " + responseCode + " from " + uri;
				Log.e(TAG, message);
				throw new IllegalStateException(message);
			}
			
			final InputStream inputStream;
			try {
				inputStream = new BufferedInputStream(connection.getInputStream());
			} catch (IOException e) {
				final String message = "Could not read response from " + uri;
				Log.e(TAG, message, e);
				throw new IllegalStateException(message, e);
			}
			
			final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			final String response;
			
			try {
				response = CharStreams.toString(inputStreamReader);
			} catch (IOException e) {
				final String message = "Could not transform response to string";
				Log.e(TAG, message);
				throw new IllegalStateException(message, e);
			}
			

			final PredictionResponseParser predictionResponseParser = new PredictionResponseParser(response, new PredictionParser());
			return predictionResponseParser.extractPredictions(fields);
		}
		finally {
			connection.disconnect();
		}
	}

}
