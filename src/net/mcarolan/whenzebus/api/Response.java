package net.mcarolan.whenzebus.api;

import java.util.Map;

import net.mcarolan.whenzebus.api.field.Field;

import com.google.common.collect.ImmutableMap;

public class Response {
	
	private final ImmutableMap<String, String> fieldNameToStringValue;
	
	public Response(final Map<Field, String> fieldToValue) {
		final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		
		for (Map.Entry<Field, String> entry : fieldToValue.entrySet()) {
			builder.put(entry.getKey().getFieldName(), entry.getValue());
		}
		
		fieldNameToStringValue = builder.build();
	}
	
	public String getStringValueFromFieldName(String fieldName) {
		return fieldNameToStringValue.get(fieldName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((fieldNameToStringValue == null) ? 0
						: fieldNameToStringValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Response other = (Response) obj;
		if (fieldNameToStringValue == null) {
			if (other.fieldNameToStringValue != null)
				return false;
		} else if (!fieldNameToStringValue.equals(other.fieldNameToStringValue))
			return false;
		return true;
	}

}