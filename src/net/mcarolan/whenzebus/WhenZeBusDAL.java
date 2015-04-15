package net.mcarolan.whenzebus;

import java.util.List;

import net.mcarolan.whenzebus.api.StopCode1;
import net.mcarolan.whenzebus.api.StopPointName;

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
			final String stopPointName = cursor.getString(cursor.getColumnIndexOrThrow("stoppointname"));
			busStops.add(new BusStop(new StopCode1(stopCode1), new StopPointName(stopPointName)));
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
	public void addBusStop(StopCode1 stopCode1, StopPointName stopPointName) {
		SQLiteDatabase db = null;
		try {
			db = openHelper.getWritableDatabase();
			final ContentValues contentValues = new ContentValues();
			
			contentValues.put("stopcode1", stopCode1.getValue());
			contentValues.put("stoppointname", stopPointName.getValue());
			
			db.insertOrThrow("busstop", null, contentValues);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

}
