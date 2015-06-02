package net.mcarolan.whenzebus;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.common.collect.Lists;

import net.mcarolan.whenzebus.adapter.GenericListAdapter;
import net.mcarolan.whenzebus.adapter.item.BusStopItem;

public class BusStopView extends ActionBarActivity {
	
	private static final String TAG = "BusStopView";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busstopview);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.busStopViewContainer, new BusStopViewFragment())
                    .commit();
        }
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		BusStopViewFragment fragment = (BusStopViewFragment) getSupportFragmentManager().findFragmentById(R.id.busStopViewContainer);
		fragment.loadBusStops();
	}


	public static class BusStopViewFragment extends Fragment {
	
		private void loadBusStops() {
			final WhenZeBusDAL dal = new WhenZeBusDAL(getActivity());
			final List<BusStop> stops = dal.getBusStops();
			
			final View layoutBusStops = getActivity().findViewById(R.id.layoutBusStops);
			final View layoutNoBusStops = getActivity().findViewById(R.id.layoutNoBusStops);
			final ListView listview = (ListView) getActivity().findViewById(R.id.busStopListView);

			if (stops.isEmpty()) {
				layoutBusStops.setVisibility(View.GONE);
				layoutNoBusStops.setVisibility(View.VISIBLE);
			}
			else {
				final List<BusStopItem> busStopItems = Lists.newArrayList();
				for (final BusStop busStop : stops) {
					busStopItems.add(new BusStopItem(busStop));
				}
				final GenericListAdapter<BusStopItem> adapter = new GenericListAdapter<>(getActivity(), busStopItems);
				listview.setAdapter(adapter);
				layoutBusStops.setVisibility(View.VISIBLE);
				layoutNoBusStops.setVisibility(View.GONE);
			}
		}	

		
		private final AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				final BusStop item = ((BusStopItem) parent.getItemAtPosition(position)).getBusStop();
				final Intent intent = new Intent(getActivity(), BusView.class);
				item.writeTo(intent);
				getActivity().startActivity(intent);
			}
			
		};
		
		private final AdapterView.OnItemLongClickListener longPress = new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final WhenZeBusDAL dal = new WhenZeBusDAL(getActivity());
				final BusStop busStop = (BusStop) parent.getItemAtPosition(position);
				new AlertDialog.Builder(getActivity())
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Remove " + busStop.getStopPointName().getValue())
			.setMessage("Would you like to remove " + busStop.getStopPointName().getValue() + " ?")
			.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dal.removeBusStop(busStop.getStopCode1());
						loadBusStops();
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
				final Intent intent = new Intent(getActivity(), AddView.class);
				getActivity().startActivityForResult(intent, 0);
			}
			
		};

		@Override
		public void onStart() {
			super.onStart();
			loadBusStops();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_bus_stop_view, container, false);
			final Button add = (Button) rootView.findViewById(R.id.addRemoveBusStop);
			final Button noStopsAdd = (Button) rootView.findViewById(R.id.noStopsAdd);
			final ListView listview = (ListView) rootView.findViewById(R.id.busStopListView);
			add.setOnClickListener(onAddClick);
			noStopsAdd.setOnClickListener(onAddClick);
			listview.setOnItemClickListener(itemClick);
			listview.setOnItemLongClickListener(longPress);
			
			getActivity().registerForContextMenu(listview);
			return rootView;
		}
		
	}

}
