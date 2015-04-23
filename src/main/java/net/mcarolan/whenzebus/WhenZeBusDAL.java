package net.mcarolan.whenzebus;

import java.util.List;

import net.mcarolan.whenzebus.api.value.*;

import com.google.common.collect.Lists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WhenZeBusDAL {
	
	private final WhenZeBusOpenHelper openHelper;

	public WhenZeBusDAL(Context context) {
		openHelper = new WhenZeBusOpenHelper(context);
	}
	
	public List<BusStop> getBusStops() {
		final SQLiteDatabase db = openHelper.getReadableDatabase();
		final Cursor cursor = db.rawQuery("select * from busstop", new String[0]);
		
		cursor.moveToFirst();

		List<BusStop> busStops = Lists.newArrayList();
		
		while (!cursor.isAfterLast()) {
			final String stopCode1 = cursor.getString(cursor.getColumnIndexOrThrow("stopcode1"));
			final String stopPointIndicator = cursor.getString(cursor.getColumnIndexOrThrow("stoppointindicator"));
			final String stopPointName = cursor.getString(cursor.getColumnIndexOrThrow("stoppointname"));
			final String towards = cursor.getString(cursor.getColumnIndexOrThrow("towards"));
			final double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
			final double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
			busStops.add(new BusStop(new StopCode1(stopCode1), new StopPointIndicator(stopPointIndicator), new StopPointName(stopPointName), new Towards(towards), new Location(new Latitude(latitude), new Longitude(longitude))));
			cursor.moveToNext();
		}
		
		return busStops;
	}
	
	public int countBusStopsWith(StopCode1 stopCode1) {
		final SQLiteDatabase db = openHelper.getReadableDatabase();
		final Cursor cursor = db.rawQuery("select * from busstop where stopcode1 = ?", new String[] { stopCode1.getValue() });
		
		cursor.moveToFirst();

		int result = 0;
		while (!cursor.isAfterLast()) {
			++result;
			cursor.moveToNext();
		}
		
		return result;
	}
	
	public void removeBusStop(StopCode1 stopCode1) {
		SQLiteDatabase db = null;
		try {
			db = openHelper.getWritableDatabase();
			db.delete("busstop", "stopcode1 = ?", new String[] { stopCode1.getValue() });
		}
		finally {
			if (db != null) {
				db.close();
			}
		}
	}
	public void addBusStop(BusStop busStop) {
		SQLiteDatabase db = null;
		try {
			db = openHelper.getWritableDatabase();
			final ContentValues contentValues = new ContentValues();
			
			contentValues.put("stopcode1", busStop.getStopCode1().getValue());
			contentValues.put("stoppointindicator", busStop.getStopPointIndicator().getValue());
			contentValues.put("stoppointname", busStop.getStopPointName().getValue());
			contentValues.put("towards", busStop.getTowards().getValue());
			contentValues.put("latitude", busStop.getLocation().getLatitude().getValue());
			contentValues.put("longitude", busStop.getLocation().getLongitude().getValue());
			
			db.insertOrThrow("busstop", null, contentValues);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

}
