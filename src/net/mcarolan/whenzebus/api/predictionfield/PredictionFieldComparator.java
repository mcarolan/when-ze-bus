package net.mcarolan.whenzebus.api.predictionfield;

import java.util.Comparator;


public class PredictionFieldComparator implements Comparator<PredictionField> {

	@Override
	public int compare(PredictionField lhs, PredictionField rhs) {
		return lhs.getSequenceNumber() - rhs.getSequenceNumber();
	}
	

}
