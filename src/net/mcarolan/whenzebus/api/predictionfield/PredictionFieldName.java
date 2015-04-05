package net.mcarolan.whenzebus.api.predictionfield;


import com.google.common.base.Function;

public class PredictionFieldName {
	
	public static final Function<PredictionField, String> transformer = new Function<PredictionField, String>() {
		
		public String apply(PredictionField predictionField) {
			return predictionField.getFieldName();
		}
		
	};

}
