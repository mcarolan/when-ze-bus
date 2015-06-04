package net.mcarolan.whenzebus.api.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;

import android.util.Log;

import net.mcarolan.whenzebus.api.UnknownBusStop;
import net.mcarolan.whenzebus.api.request.Request;

public class Client {

	private static final String TAG = "PredictionClient";

	public static <T> ClientResult<Set<T>> getResponses(Request request, Function<Response, T> builder) {
		HttpURLConnection connection = openConnection(request);

		try {
			final int responseCode = getResponseCode(request, connection);

			if (responseCode == 416) {
				final String message = "Unknown bus stop " + request.getStopCode1().getValue();
				Log.w(TAG, message);
				throw new UnknownBusStop(message);
			}
			else if (responseCode != HttpURLConnection.HTTP_OK) {
				final String message = "Unexpected response code " + responseCode + ": " + request.toString();
				Log.e(TAG, message);
				throw new IllegalStateException(message);
			}

			final String httpResponse = readResponseBodyAsString(request, connection);

			final ResponseParser predictionResponseParser = new ResponseParser(httpResponse);
			final Set<Response> responses = predictionResponseParser.extractResponses(request.getFields());

			final Set<T> result = Sets.newHashSet();

			for (final Response response : responses) {
				result.add(builder.apply(response));
			}

			return new ClientResult<>(result);
		}
		catch (Throwable t) {
			return new ClientResult<>(t);
		}
		finally {
			connection.disconnect();
		}
	}

	public static <T> ClientResult<List<T>> getOrderedResponses(Request request, Function<Response, T> builder, final Comparator<T> comparator) {
		final ClientResult<Set<T>> responses = getResponses(request, builder);

		final Function<Set<T>, List<T>> sort = new Function<Set<T>, List<T>>() {
			@Override
			public List<T> apply(Set<T> input) {
				final List<T> list = Lists.newArrayList(input);
				Collections.sort(list, comparator);
				return list;
			}
		};

		return responses.map(sort);
	}

	private static final HttpURLConnection openConnection(Request request) {
		final URL url = request.buildURL();
		try {
			return (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			final String message = "Could not open connection: " + request.toString();
			Log.e(TAG, message);
			throw new IllegalStateException(message, e);
		}
	}

	private static final String readResponseBodyAsString(Request request, HttpURLConnection connection) {
		final InputStream inputStream;
		try {
			inputStream = new BufferedInputStream(connection.getInputStream());
		} catch (IOException e) {
			final String message = "Could not read response: " + request.toString();
			Log.e(TAG, message, e);
			throw new IllegalStateException(message, e);
		}

		final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		try {
			return CharStreams.toString(inputStreamReader);
		} catch (IOException e) {
			final String message = "Could not transform response to string";
			Log.e(TAG, message);
			throw new IllegalStateException(message, e);
		}
	}

	private static final int getResponseCode(Request request, HttpURLConnection connection) {
		try {
			return connection.getResponseCode();
		} catch (IOException e) {
			final String message = "Could not get response code: " + request.toString();
			Log.e(TAG, message, e);
			throw new IllegalStateException(message, e);
		}
	}

}
