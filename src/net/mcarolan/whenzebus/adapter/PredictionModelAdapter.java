package net.mcarolan.whenzebus.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.mcarolan.whenzebus.ColorGenerator;
import net.mcarolan.whenzebus.PredictionModel;
import net.mcarolan.whenzebus.R;
import net.mcarolan.whenzebus.TimeRemainingCalculator;
import net.mcarolan.whenzebus.R.id;
import net.mcarolan.whenzebus.R.layout;
import net.mcarolan.whenzebus.R.string;
import net.mcarolan.whenzebus.TimeRemainingCalculator.TimeRemaining;
import net.mcarolan.whenzebus.api.EstimatedTimeComparator;
import net.mcarolan.whenzebus.api.Response;
import net.mcarolan.whenzebus.api.field.Fields;

public class PredictionModelAdapter extends ArrayAdapter<PredictionModel> {
	
	private final TimeRemainingCalculator calculator;
	
	public PredictionModelAdapter(Context context, Set<Response> predictions) {
		super(context, R.layout.listitem, toPredictionModelList(predictions));
		calculator = new TimeRemainingCalculator(context);
	}
	
	private static List<PredictionModel> toPredictionModelList(Set<Response> predictions) {
		List<Response> predictionList = Lists.newArrayList(predictions);
		Collections.sort(predictionList, new EstimatedTimeComparator());
		return Lists.transform(predictionList, transform);
	}
	
	private final static Function<Response, PredictionModel> transform = new Function<Response, PredictionModel>() {
		
		public PredictionModel apply(Response prediction) {
			return new PredictionModel(Fields.LineName.extract(prediction),
					Fields.DestinationText.extract(prediction),
					Fields.EstimatedTime.extract(prediction),
					Fields.ExpireTime.extract(prediction));
		}
		
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) getContext()
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.listitem, parent, false);
		final PredictionModel item = getItem(position);
		
		final TextView destinationText = (TextView) rowView.findViewById(R.id.destinationtext);
		final TextView lineName = (TextView) rowView.findViewById(R.id.linename);
		final TextView estimatedTime = (TextView) rowView.findViewById(R.id.estimatedtime);
		
		destinationText.setText(item.getDestinationText());
		lineName.setText(item.getLineName());
		
		lineName.setBackgroundColor(ColorGenerator.getBackgroundColorIntFor(item.getLineName()));
		lineName.setTextColor(ColorGenerator.getForegroundColorIntFor(item.getLineName()));
		
		final TimeRemaining timeRemaining = calculator.getTimeRemaining(item.getEstimatedTime());
		
		
		if (timeRemaining.isInPast()) {
			estimatedTime.setText(rowView.getResources().getString(R.string.prediction_due));
		}
		else {
			estimatedTime.setText(rowView.getResources().getString(R.string.prediction_duein) + timeRemaining.getTimeRemainingString());
		}
		
		return rowView;
	}

}
