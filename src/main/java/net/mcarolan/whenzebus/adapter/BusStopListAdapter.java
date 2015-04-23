package net.mcarolan.whenzebus.adapter;

import java.util.List;

import net.mcarolan.whenzebus.BusStop;
import net.mcarolan.whenzebus.ColorGenerator;
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

	static class ViewHolder {
		TextView busStopName;
		TextView towards;
		TextView stopPointIndicator;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_bus_stop, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.busStopName = (TextView) convertView.findViewById(R.id.stopPointName);
                        viewHolder.towards = (TextView) convertView.findViewById(R.id.towards);
                        viewHolder.stopPointIndicator = (TextView) convertView.findViewById(R.id.stopPointIndicator);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final BusStop item = getItem(position);
		
		viewHolder.busStopName.setText(item.getStopPointName().getValue());
		viewHolder.towards.setText("Towards " + item.getTowards().getValue());
		viewHolder.stopPointIndicator.setText(item.getStopPointIndicator().getValue());
		
		viewHolder.stopPointIndicator.setBackgroundColor(ColorGenerator.getBackgroundColorIntFor(item.getStopPointIndicator().getValue()));
		viewHolder.stopPointIndicator.setTextColor(ColorGenerator.getForegroundColorIntFor(item.getStopPointIndicator().getValue()));
		
		return convertView;
	}

}
