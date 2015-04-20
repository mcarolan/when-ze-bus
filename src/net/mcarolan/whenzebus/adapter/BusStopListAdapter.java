package net.mcarolan.whenzebus.adapter;

import java.util.List;

import net.mcarolan.whenzebus.BusStop;
import net.mcarolan.whenzebus.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BusStopListAdapter extends ArrayAdapter<BusStop> {
	
	public BusStopListAdapter(Context context, List<BusStop> busStops) {
		super(context, R.layout.listitem_bus_stop, busStops);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.listitem_bus_stop, parent, false);
		final BusStop item = getItem(position);
		
		final TextView busStopName = (TextView) rowView.findViewById(R.id.busStopName);
		
		busStopName.setText(item.getStopPointName().getValue());
		
		return rowView;
	}

}
