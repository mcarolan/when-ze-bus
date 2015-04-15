package net.mcarolan.whenzebus;

import java.util.List;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ManageStops extends ActionBarActivity {
	
	private final WhenZeBusDAL dal = new WhenZeBusDAL(this);
	private ListView listView;
	private TextView noBusStops;
	private Button deleteButton;
	private ManageBusStopAdapter adapter;
	
	private static final String TAG = "ManageStops";
	
	private View.OnClickListener onAddClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final Intent intent = new Intent(ManageStops.this, AddView.class);
			Log.i(TAG, "launch add activity");
			ManageStops.this.startActivityForResult(intent, 0);
			ManageStops.this.loadStops();
		}
		
	};
	
	private View.OnClickListener onDeleteClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			for (BusStop busStop : adapter.getCheckedItems()) {
				Log.i(TAG, "remove " + busStop.toString());
				dal.removeBusStop(busStop.getStopCode1());
			}
			ManageStops.this.loadStops();
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_stops);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.manageContainer, new PlaceholderFragment())
                    .commit();
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		 Log.i(TAG, "reload stops after activity result");
		 this.loadStops();
	}
	
	private void loadStops() {
		final List<BusStop> busStops = dal.getBusStops();
		
		Log.i(TAG, "Found " + busStops.size() + " stops");
		
		if (busStops.isEmpty()) {
			noBusStops.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			deleteButton.setVisibility(View.GONE);
		}
		else {
			noBusStops.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			deleteButton.setVisibility(View.VISIBLE);
			
			adapter = new ManageBusStopAdapter(this, busStops);
			listView.setAdapter(adapter);
		}
	}
	
	public class PlaceholderFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_manage_view, container, false);
			listView = (ListView) rootView.findViewById(R.id.manageListview);
			noBusStops = (TextView) rootView.findViewById(R.id.noBusStops);
			deleteButton = (Button) rootView.findViewById(R.id.deleteSelectedBusStop);
			deleteButton.setOnClickListener(onDeleteClick);
			final Button addButton = (Button) rootView.findViewById(R.id.manageAddBusStop);
			addButton.setOnClickListener(onAddClick);
	        loadStops();
            return rootView;
		}
		
		
	}

}
