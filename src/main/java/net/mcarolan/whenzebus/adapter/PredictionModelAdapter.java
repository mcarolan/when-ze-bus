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

	static class ViewHolder {
		TextView destinationText;
		TextView lineName;
		TextView estimatedTime;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.destinationText = (TextView) convertView.findViewById(R.id.destinationtext);
			viewHolder.lineName = (TextView) convertView.findViewById(R.id.linename);
			viewHolder.estimatedTime = (TextView) convertView.findViewById(R.id.estimatedtime);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final PredictionModel item = getItem(position);
		
		viewHolder.destinationText.setText(item.getDestinationText());
		viewHolder.lineName.setText(item.getLineName());

		viewHolder.lineName.setBackgroundColor(ColorGenerator.getBackgroundColorIntFor(item.getLineName()));
		viewHolder.lineName.setTextColor(ColorGenerator.getForegroundColorIntFor(item.getLineName()));
		
		final TimeRemaining timeRemaining = calculator.getTimeRemaining(item.getEstimatedTime());
		
		
		if (timeRemaining.isInPast()) {
			viewHolder.estimatedTime.setText(convertView.getResources().getString(R.string.prediction_due));
		}
		else {
			viewHolder.estimatedTime.setText(convertView.getResources().getString(R.string.prediction_duein) + timeRemaining.getTimeRemainingString());
		}
		
		return convertView;
	}

}
