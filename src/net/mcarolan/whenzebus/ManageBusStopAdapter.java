package net.mcarolan.whenzebus;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ManageBusStopAdapter extends ArrayAdapter<BusStop> {
	
	public ManageBusStopAdapter(Context context, List<BusStop> busStops) {
		super(context, R.layout.listitem_manage, busStops);
	}
	
	private final Set<BusStop> checkedItems = Sets.newHashSet();

	public Set<BusStop> getCheckedItems() {
		return checkedItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.listitem_manage, parent, false);
		final BusStop item = getItem(position);
		
		final CheckBox busStopName = (CheckBox) rowView.findViewById(R.id.manageListItemStopName);
		
		busStopName.setText(item.getStopPointName().getValue());
		
		final OnCheckedChangeListener listener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					checkedItems.add(item);
				}
				else {
					checkedItems.remove(item);
				}
			}
			
		};
		busStopName.setOnCheckedChangeListener(listener);
		return rowView;
	}

}
