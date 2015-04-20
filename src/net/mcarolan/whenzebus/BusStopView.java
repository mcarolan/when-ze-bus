package net.mcarolan.whenzebus;

import java.util.List;

import net.mcarolan.whenzebus.adapter.BusStopListAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class BusStopView extends ActionBarActivity {
	
	private final WhenZeBusDAL dal = new WhenZeBusDAL(this);
	private ListView listview;
	private View layoutBusStops;
	private View layoutNoBusStops;
	private static final String TAG = "BusStopView";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busstopview);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.busStopViewContainer, new PlaceholderFragment())
                    .commit();
        }
	}
	
	private final AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final BusStop item = (BusStop) parent.getItemAtPosition(position);
			final Intent intent = new Intent(BusStopView.this, BusView.class);
			item.writeTo(intent);
			BusStopView.this.startActivity(intent);
		}
		
	};
	
	private final AdapterView.OnItemLongClickListener longPress = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			final BusStop busStop = (BusStop) listview.getItemAtPosition(position);
			new AlertDialog.Builder(BusStopView.this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Remove " + busStop.getStopPointName().getValue())
	        .setMessage("Would you like to remove " + busStop.getStopPointName().getValue() + " ?")
	        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dal.removeBusStop(busStop.getStopCode1());
					BusStopView.this.loadBusStops();
				}
				
			})
	        .setNegativeButton("Cancel", null)
	        .show();
			return true;
		}
		
	};
	
	private final OnClickListener onAddClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Intent intent = new Intent(BusStopView.this, AddView.class);
			BusStopView.this.startActivityForResult(intent, 0);
		}
		
	};
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		loadBusStops();
	}

	private void loadBusStops() {
		final List<BusStop> stops = dal.getBusStops();
		
		if (stops.isEmpty()) {
			layoutBusStops.setVisibility(View.GONE);
			layoutNoBusStops.setVisibility(View.VISIBLE);
		}
		else {
			final BusStopListAdapter adapter = new BusStopListAdapter(BusStopView.this, stops);
			listview.setAdapter(adapter);
			layoutBusStops.setVisibility(View.VISIBLE);
			layoutNoBusStops.setVisibility(View.GONE);
		}
		
	}
	
	private class PlaceholderFragment extends Fragment {
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_bus_stop_view, container, false);
			listview = (ListView) rootView.findViewById(R.id.busStopListView);
			final Button add = (Button) rootView.findViewById(R.id.addRemoveBusStop);
			final Button noStopsAdd = (Button) rootView.findViewById(R.id.noStopsAdd);
			layoutBusStops = rootView.findViewById(R.id.layoutBusStops);
			layoutNoBusStops = rootView.findViewById(R.id.layoutNoBusStops);
			add.setOnClickListener(onAddClick);
			noStopsAdd.setOnClickListener(onAddClick);
			listview.setOnItemClickListener(itemClick);
			listview.setOnItemLongClickListener(longPress);
			BusStopView.this.loadBusStops();
			BusStopView.this.registerForContextMenu(listview);
			return rootView;
		}
		
	}

}
