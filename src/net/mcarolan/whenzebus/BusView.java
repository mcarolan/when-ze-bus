package net.mcarolan.whenzebus;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import net.mcarolan.whenzebus.adapter.PredictionModelAdapter;
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

public class BusView extends ActionBarActivity {
	
	private static final String TAG = "BusView";
    final Client client = new Client("http://countdown.api.tfl.gov.uk");
    
    private TextView messageTextView;
    private ListView listView;
    
    private BusStop selectedBusStop = null;
    Timer timer = null;
    
    private static final long RESPONSE_REFRESH_MILLIS = TimeUnit.SECONDS.toMillis(30);
    private static final long LISTVIEW_REFRESH_MILLIS = 500;
    
    private class DisplayBusTimesResult {
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
	
	class DisplayBusTimesTask extends AsyncTask<String, Void, DisplayBusTimesResult> {
		
		final BusStop busStop = selectedBusStop;

		@Override
		protected DisplayBusTimesResult doInBackground(String... params) {
			try {
				final Set<Response> responses = client.getResponses(busStop.getStopCode1(), true, Client.DEFAULT_PREDICTION_FIELDS);
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
			if (result.isSuccess && result.responses.size() > 0) {
				Log.i(TAG, "Received " + result.responses.size() + " responses for " + busStop.getStopCode1());
		        final ArrayAdapter<PredictionModel> arrayAdapter = new PredictionModelAdapter(BusView.this, result.responses);
		        final ListView listView = (ListView) findViewById(R.id.listview);
		        listView.setAdapter(arrayAdapter);
		        messageTextView.setVisibility(View.GONE);
		        listView.setVisibility(View.VISIBLE);
			}
			else if (result.isSuccess) {
				Log.i(TAG, "0 responses for " + busStop.getStopCode1());
				messageTextView.setText(getResources().getString(R.string.busview_no_buses) + busStop.getStopPointName().getValue());
				listView.setVisibility(View.GONE);
				messageTextView.setVisibility(View.VISIBLE);
			}
			else {
				messageTextView.setText(getResources().getString(R.string.busview_error));
				listView.setVisibility(View.GONE);
				messageTextView.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	class RefreshTimesTask extends TimerTask {

		@Override
		public void run() {
			if (selectedBusStop != null && WhenZeBusApplication.isInForeground()) {
				new DisplayBusTimesTask().execute();	
			}
		}
		
	}
	
	class RefreshListViewTask extends TimerTask {

		@Override
		public void run() {
			final ActionBarActivity that = BusView.this;
			that.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (listView.getAdapter() != null) {
						final PredictionModelAdapter adapter = (PredictionModelAdapter) listView.getAdapter();
						adapter.notifyDataSetChanged();
					}
				}
				
			});
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
		setTitle(stopPointName);
    	messageTextView.setVisibility(View.VISIBLE);
    	listView.setVisibility(View.GONE);
    	messageTextView.setText(getResources().getString(R.string.busview_loading_before) + stopPointName + getResources().getString(R.string.busview_loading_after));
	}
	
    @Override
	protected void onPause() {
    	super.onPause();
    	Log.i(TAG, "stopping timer due to pause");
		if (timer != null) {
			timer.cancel();
		}
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	displayTimes();
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedBusStop = BusStop.readFrom(getIntent());
        setContentView(R.layout.activity_bus_view);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

	/**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bus_view, container, false);
            messageTextView = (TextView) rootView.findViewById(R.id.message);
            listView = (ListView) rootView.findViewById(R.id.listview);
            BusView.this.displayTimes();
            return rootView;
        }
    }
}
