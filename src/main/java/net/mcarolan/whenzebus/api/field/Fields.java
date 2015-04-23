package net.mcarolan.whenzebus.api.field;


public class Fields {
	
	public static final StringField StopPointName = new StringField("StopPointName", 1);
	public static final StringField StopID = new StringField("StopID", 2);
	public static final StringField StopCode1 = new StringField("StopCode1", 3);
	public static final StringField StopCode2 = new StringField("StopCode2", 4);
	public static final StringField StopPointType = new StringField("StopPointType", 5);
	public static final LongField StopPointState = new LongField("StopPointState", 9);
	public static final StringField StopPointIndicator = new StringField("StopPointIndicator", 8);
	public static final StringField Towards = new StringField("Towards", 6);
	public static final LongField Bearing = new LongField("Bearing", 7);
	public static final DoubleField Latitude = new DoubleField("Latitude", 10);
	public static final DoubleField Longitude = new DoubleField("Longitude", 11);
	public static final LongField VisitNumber = new LongField("VisitNumber", 12);
	public static final StringField TripID = new StringField("TripID", 19);
	public static final StringField VehicleID = new StringField("VehicleID", 18);
	public static final StringField RegistrationNumber = new StringField("RegistrationNumber", 20);
	public static final StringField LineID = new StringField("LineID", 13);
	public static final StringField LineName = new StringField("LineName", 14);
	public static final LongField DestinationID = new LongField("DestinationID", 15);
	public static final StringField DestinationText = new StringField("DestinationText", 16);
	public static final StringField DestinationName = new StringField("DestinationName", 17);
	public static final DateTimeField EstimatedTime = new DateTimeField("EstimatedTime", 21);
	public static final DateTimeField ExpireTime = new DateTimeField("ExpireTime", 22);
	
}
