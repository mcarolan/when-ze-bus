package net.mcarolan.whenzebus.api;

import java.util.Comparator;

import org.joda.time.DateTime;

import net.mcarolan.whenzebus.api.predictionfield.PredictionFields;

public class EstimatedTimeComparator implements Comparator<Prediction> {

	@Override
	public int compare(Prediction lhs, Prediction rhs) {
		final DateTime estimatedTimeLeft = PredictionFields.EstimatedTime.extract(lhs);
		final DateTime estimatedTimeRight = PredictionFields.EstimatedTime.extract(rhs);
		return estimatedTimeLeft.compareTo(estimatedTimeRight);
	}

}
