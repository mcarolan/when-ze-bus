package net.mcarolan.whenzebus.api.field;


import com.google.common.base.Function;

public class FieldName {
	
	public static final Function<Field, String> transformer = new Function<Field, String>() {
		
		public String apply(Field predictionField) {
			return predictionField.getFieldName();
		}
		
	};

}
