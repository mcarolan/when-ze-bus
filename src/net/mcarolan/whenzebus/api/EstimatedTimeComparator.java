package net.mcarolan.whenzebus.api;

import java.util.Comparator;

import org.joda.time.DateTime;

import net.mcarolan.whenzebus.api.predictionfield.Fields;

public class EstimatedTimeComparator implements Comparator<Response> {

	@Override
	public int compare(Response lhs, Response rhs) {
		final DateTime estimatedTimeLeft = Fields.EstimatedTime.extract(lhs);
		final DateTime estimatedTimeRight = Fields.EstimatedTime.extract(rhs);
		return estimatedTimeLeft.compareTo(estimatedTimeRight);
	}

}
