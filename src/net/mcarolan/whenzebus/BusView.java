package net.mcarolan.whenzebus;

import java.util.Set;

import net.mcarolan.whenzebus.api.Response;
import net.mcarolan.whenzebus.api.Client;
import net.mcarolan.whenzebus.api.StopCode1;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BusView extends ActionBarActivity {
	
	private final WhenZeBusDAL dal = new WhenZeBusDAL(this);
    final Client client = new Client("http://countdown.api.tfl.gov.uk");
	
	class DisplayBusTimesTask extends AsyncTask<String, Void, Set<Response>> {

		@Override
		protected Set<Response> doInBackground(String... params) {
			return client.getResponses(new StopCode1("76458"), true, Client.DEFAULT_PREDICTION_FIELDS);
		}
		
		@Override
		protected void onPostExecute(Set<Response> result) {
			super.onPostExecute(result);
	        final ArrayAdapter<PredictionModel> arrayAdapter = new PredictionModelAdapter(BusView.this, result);
	        final ListView listView = (ListView) findViewById(R.id.listview);
	        listView.setAdapter(arrayAdapter);
		}
		
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_view);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
//        final DisplayBusTimesTask displayBusTimes = new DisplayBusTimesTask();
//        displayBusTimes.execute();
    }

    

    @Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
    	menu.clear();
        getMenuInflater().inflate(R.menu.bus_view, menu);
        
        for (final BusStop busStop : dal.getBusStops()) {
        	menu.add(Menu.NONE, Menu.NONE, 0, busStop.getStopPointName().getValue());
        }
        
        return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
        	final AddDialog addDialog = new AddDialog(this, dal, client);
        	addDialog.show(getFragmentManager(), null);
        	this.invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bus_view, container, false);
            return rootView;
        }
    }
}
