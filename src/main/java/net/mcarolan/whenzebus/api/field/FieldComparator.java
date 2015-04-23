package net.mcarolan.whenzebus.api.field;

import java.util.Comparator;


public class FieldComparator implements Comparator<Field> {

	@Override
	public int compare(Field lhs, Field rhs) {
		return lhs.getSequenceNumber() - rhs.getSequenceNumber();
	}
	

}
