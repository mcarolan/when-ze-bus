package net.mcarolan.whenzebus;

import org.joda.time.DateTime;

import android.content.Context;

public class TimeRemainingCalculator {
	
	private final Context context;
	
	public TimeRemainingCalculator(Context context) {
		this.context = context;
	}

	public class TimeRemaining {
		private final boolean isInPast;
		private final String timeRemainingString;
		
		public TimeRemaining(boolean isInPast, String timeRemainingString) {
			this.isInPast = isInPast;
			this.timeRemainingString = timeRemainingString;
		}

		public boolean isInPast() {
			return isInPast;
		}

		public String getTimeRemainingString() {
			return timeRemainingString;
		}
		
	}
	
	public TimeRemaining getTimeRemaining(DateTime until) {
		final DateTime now = new DateTime();
		
		if (now.getMillis() >= until.getMillis()) {
			return new TimeRemaining(true, "");
		}
		
		final DateTime difference = until.minus(now.getMillis());
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append(difference.getMinuteOfHour());
		
		if (difference.getMinuteOfHour() == 1) {
			sb.append(context.getResources().getString(R.string.time_oneminute));
		}
		else {
			sb.append(context.getResources().getString(R.string.time_manyminutes));
		}
		
		sb.append(difference.getSecondOfMinute());
		
		if (difference.getSecondOfMinute() == 1) {
			sb.append(context.getResources().getString(R.string.time_onesecond));
		}
		else {
			sb.append(context.getResources().getString(R.string.time_manyseconds));
		}
		
		return new TimeRemaining(false, sb.toString());
	}

}
