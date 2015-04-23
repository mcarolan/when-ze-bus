package net.mcarolan.whenzebus.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;

import android.util.Log;
import net.mcarolan.whenzebus.api.field.Field;
import net.mcarolan.whenzebus.api.field.Fields;
import net.mcarolan.whenzebus.api.value.StopCode1;

public class Client {
	
	private final String baseUri;
	
	private final String TAG = "PredictionClient";
	
	public static final Set<? extends Field> DEFAULT_PREDICTION_FIELDS = 
			Sets.newHashSet(Fields.EstimatedTime, Fields.ExpireTime, Fields.DestinationText, Fields.LineName);
	
	public static final Set<? extends Field> BUS_INFORMATION_FIELDS =
			Sets.newHashSet(Fields.StopPointIndicator, Fields.StopPointName, Fields.StopCode1, Fields.Towards, Fields.Latitude, Fields.Longitude);
	
	public Client(String baseUri) {
		this.baseUri = baseUri;
	}
	
	public Set<Response> getResponses(StopCode1 stopCode1, boolean stopInformation, Set<? extends Field> fields) {
		final Request predictionRequest = new Request(fields, stopCode1, stopInformation);
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
			
			if (responseCode == 416) {
				final String message = "Unknown bus stop " + stopCode1.toString();
				Log.e(TAG, message);
				throw new UnknownBusStop(message);
			}
			else if (responseCode != HttpURLConnection.HTTP_OK) {
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
			
			final ResponseParser predictionResponseParser = new ResponseParser(response);
			return predictionResponseParser.extractResponses(fields);
		}
		finally {
			connection.disconnect();
		}
	}

}
