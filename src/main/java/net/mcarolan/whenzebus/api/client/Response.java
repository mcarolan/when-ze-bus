package net.mcarolan.whenzebus.api.client;

import java.util.Map;

import net.mcarolan.whenzebus.api.field.Field;

import com.google.common.base.Objects;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Response response = (Response) o;
		return Objects.equal(fieldNameToStringValue, response.fieldNameToStringValue);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(fieldNameToStringValue);
	}
}
