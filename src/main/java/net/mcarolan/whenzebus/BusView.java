package net.mcarolan.whenzebus;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import net.mcarolan.whenzebus.adapter.GenericListAdapter;
import net.mcarolan.whenzebus.adapter.item.PredictionModelItem;
import net.mcarolan.whenzebus.api.PredictionModel;
import net.mcarolan.whenzebus.api.Response;
import net.mcarolan.whenzebus.api.Client;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;

public class BusView extends ActionBarActivity {
	
	private static final String TAG = "BusView";
	private static final Client client = new Client("http://countdown.api.tfl.gov.uk");
        
	private static BusStop selectedBusStop = null;
    
    private static final long RESPONSE_REFRESH_MILLIS = TimeUnit.SECONDS.toMillis(30);
    private static final long LISTVIEW_REFRESH_MILLIS = 500;
    
    private static class DisplayBusTimesResult {
    	final boolean isSuccess;
    	final Set<Response> responses;
    	final Throwable error;
    	
		public DisplayBusTimesResult(boolean isSuccess, Set<Response> responses,
				Throwable error) {
			this.isSuccess = isSuccess;
			this.responses = responses;
			this.error = error;
		}
    }
	
    @Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "stopping timer due to pause");
		final BusViewFragment fragment = (BusViewFragment) getSupportFragmentManager().findFragmentById(R.id.container);
		fragment.clearTimer();
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
	final BusViewFragment fragment = (BusViewFragment) getSupportFragmentManager().findFragmentById(R.id.container);
    	fragment.displayTimes();
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedBusStop = BusStop.readFrom(getIntent());
        setContentView(R.layout.activity_bus_view);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BusViewFragment())
                    .commit();
        }
    }

	private static List<PredictionModelItem> toPredictionModelItems(Iterable<Response> responses) {
		final List<PredictionModel> predictionModels = Lists.newArrayList();

		for (final Response response : responses) {
			predictionModels.add(PredictionModel.fromResponse(response));
		}

		Collections.sort(predictionModels, PredictionModel.comparator);

		final List<PredictionModelItem> predictionModelItems = Lists.newArrayList();

		for (final PredictionModel predictionModel : predictionModels) {
			predictionModelItems.add(new PredictionModelItem(predictionModel));
		}

		return predictionModelItems;
	}

    public static class BusViewFragment extends Fragment {
	private Timer timer = null;

	private void clearTimer() {
		if (timer != null) {
			timer.cancel();
		}
		timer = null;
	}

	class RefreshListViewTask extends TimerTask {

		@Override
		public void run() {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					final ListView listview = (ListView) getActivity().findViewById(R.id.listview);
					if (listview.getAdapter() != null) {
						final GenericListAdapter<?> adapter = (GenericListAdapter<?>) listview.getAdapter();
						adapter.notifyDataSetChanged();
					}
				}
			});
		}
		
	}
		
	class DisplayBusTimesTask extends AsyncTask<String, Void, DisplayBusTimesResult> {
		
		@Override
		protected DisplayBusTimesResult doInBackground(String... params) {
			try {
				final Set<Response> responses = client.getResponses(selectedBusStop.getStopCode1(), true, Client.DEFAULT_PREDICTION_FIELDS);
				return new DisplayBusTimesResult(true, responses, null);
			}
			catch (Exception e) {
				Log.e(TAG, "Unable to get bus times", e);
				return new DisplayBusTimesResult(false, null, e);
			}
		}
		
		@Override
		protected void onPostExecute(DisplayBusTimesResult result) {
			super.onPostExecute(result);
			final ListView listView = (ListView) getActivity().findViewById(R.id.listview);
			final TextView messageTextView = (TextView) getActivity().findViewById(R.id.message);

			if (result.isSuccess && result.responses.size() > 0) {
				Log.i(TAG, "Received " + result.responses.size() + " responses for " + selectedBusStop.getStopCode1());
				final List predictionModelItems = toPredictionModelItems(result.responses);

				final GenericListAdapter<PredictionModelItem> arrayAdapter = new GenericListAdapter<>(getActivity(), predictionModelItems);
				listView.setAdapter(arrayAdapter);
				messageTextView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
			}
			else if (result.isSuccess) {
				Log.i(TAG, "0 responses for " + selectedBusStop.getStopCode1());
				messageTextView.setText(WhenZeBusApplication.getResourceString(R.string.busview_no_buses) + selectedBusStop.getStopPointName().getValue());
				listView.setVisibility(View.GONE);
				messageTextView.setVisibility(View.VISIBLE);
			}
			else {
				messageTextView.setText(WhenZeBusApplication.getResourceString(R.string.busview_error));
				listView.setVisibility(View.GONE);
				messageTextView.setVisibility(View.VISIBLE);
			}
		}
		
	}

	class RefreshTimesTask extends TimerTask {

		@Override
		public void run() {
			if (WhenZeBusApplication.isInForeground()) {
				new DisplayBusTimesTask().execute();	
			}
		}

	}

	private void displayTimes() {
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new RefreshTimesTask(), 0, RESPONSE_REFRESH_MILLIS);
		timer.schedule(new RefreshListViewTask(), 0, LISTVIEW_REFRESH_MILLIS);
		
		final String stopPointName = selectedBusStop.getStopPointName().getValue();
		getActivity().setTitle(stopPointName);
		final TextView messageTextView = (TextView) getActivity().findViewById(R.id.message);
		final ListView listview = (ListView) getActivity().findViewById(R.id.listview);

		messageTextView.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
		messageTextView.setText(WhenZeBusApplication.getResourceString(R.string.busview_loading_before) + stopPointName + WhenZeBusApplication.getResourceString(R.string.busview_loading_after));
	}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bus_view, container, false);
            return rootView;
        }
    }
}
