package net.mcarolan.whenzebus.api;

import java.util.List;
import java.util.Set;

import net.mcarolan.whenzebus.api.field.Field;
import net.mcarolan.whenzebus.api.field.FieldName;
import net.mcarolan.whenzebus.api.field.Fields;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class Request {
	
	private final Set<? extends Field> fields;
	private final StopCode1 stopCode1;
	private final boolean showStop;
	
	public Request(final Set<? extends Field> fields, final StopCode1 stopCode1, final boolean showStop) {
		this.fields = fields;
		this.stopCode1 = stopCode1;
		this.showStop = showStop;
		
		if (fields.contains(Fields.EstimatedTime) && !fields.contains(Fields.ExpireTime)) {
			throw new IllegalArgumentException(fields.toString() + " contained EstimatedTime, but not ExpireTime");
		}
	}
	
	public String getURI() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("/interfaces/ura/instant_V1?ReturnList=");
		sb.append(createReturnList());
		sb.append("&StopCode1=");
		sb.append(stopCode1.getValue());
		
		if (showStop) {
			sb.append("&StopAlso=true");
		}
		
		return sb.toString();
	}

	private String createReturnList() {
		final List<String> fieldNames = Lists.transform(Lists.newArrayList(fields), FieldName.transformer);
		final Joiner joiner = Joiner.on(",");
		return joiner.join(fieldNames);
	}

}