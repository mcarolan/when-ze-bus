package net.mcarolan.whenzebus.api.predictionfield;


public class PredictionFields {
	
	public static final StringPredictionField StopPointName = new StringPredictionField("StopPointName", 1);
	public static final StringPredictionField StopID = new StringPredictionField("StopID", 2);
	public static final StringPredictionField StopCode1 = new StringPredictionField("StopCode1", 3);
	public static final StringPredictionField StopCode2 = new StringPredictionField("StopCode2", 4);
	public static final StringPredictionField StopPointType = new StringPredictionField("StopPointType", 5);
	public static final LongPredictionField StopPointState = new LongPredictionField("StopPointState", 9);
	public static final StringPredictionField StopPointIndicator = new StringPredictionField("StopPointIndicator", 8);
	public static final StringPredictionField Towards = new StringPredictionField("Towards", 6);
	public static final LongPredictionField Bearing = new LongPredictionField("Bearing", 7);
	public static final DoublePredictionField Latitude = new DoublePredictionField("Latitude", 10);
	public static final DoublePredictionField Longitude = new DoublePredictionField("Longitude", 11);
	public static final LongPredictionField VisitNumber = new LongPredictionField("VisitNumber", 12);
	public static final StringPredictionField TripID = new StringPredictionField("TripID", 19);
	public static final StringPredictionField VehicleID = new StringPredictionField("VehicleID", 18);
	public static final StringPredictionField RegistrationNumber = new StringPredictionField("RegistrationNumber", 20);
	public static final StringPredictionField LineID = new StringPredictionField("LineID", 13);
	public static final StringPredictionField LineName = new StringPredictionField("LineName", 14);
	public static final LongPredictionField DestinationID = new LongPredictionField("DestinationID", 15);
	public static final StringPredictionField DestinationText = new StringPredictionField("DestinationText", 16);
	public static final StringPredictionField DestinationName = new StringPredictionField("DestinationName", 17);
	public static final DateTimePredictionField EstimatedTime = new DateTimePredictionField("EstimatedTime", 21);
	public static final DateTimePredictionField ExpireTime = new DateTimePredictionField("ExpireTime", 22);
	
}
