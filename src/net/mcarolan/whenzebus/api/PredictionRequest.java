package net.mcarolan.whenzebus.api;

import java.util.List;
import java.util.Set;

import net.mcarolan.whenzebus.api.predictionfield.PredictionField;
import net.mcarolan.whenzebus.api.predictionfield.PredictionFieldName;
import net.mcarolan.whenzebus.api.predictionfield.PredictionFields;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class PredictionRequest {
	
	private final Set<? extends PredictionField> fields;
	private final StopCode1 stopCode1;
	
	public PredictionRequest(final Set<? extends PredictionField> fields, final StopCode1 stopCode1) {
		this.fields = fields;
		this.stopCode1 = stopCode1;
		
		if (fields.contains(PredictionFields.EstimatedTime) && !fields.contains(PredictionFields.ExpireTime)) {
			throw new IllegalArgumentException(fields.toString() + " contained EstimatedTime, but not ExpireTime");
		}
	}
	
	public String getURI() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("/interfaces/ura/instant_V1?ReturnList=");
		sb.append(createReturnList());
		sb.append("&StopCode1=");
		sb.append(stopCode1.getValue());
		
		return sb.toString();
	}

	private String createReturnList() {
		final List<String> fieldNames = Lists.transform(Lists.newArrayList(fields), PredictionFieldName.transformer);
		final Joiner joiner = Joiner.on(",");
		return joiner.join(fieldNames);
	}

}