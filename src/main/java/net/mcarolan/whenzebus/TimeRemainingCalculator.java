package net.mcarolan.whenzebus;

import org.joda.time.DateTime;

import android.content.Context;

public class TimeRemainingCalculator {

	public static class TimeRemaining {
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
	
	public static TimeRemaining getTimeRemaining(DateTime until) {
		final DateTime now = new DateTime();
		
		if (now.getMillis() >= until.getMillis()) {
			return new TimeRemaining(true, "");
		}
		
		final DateTime difference = until.minus(now.getMillis());
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append(difference.getMinuteOfHour());
		
		if (difference.getMinuteOfHour() == 1) {
			sb.append(WhenZeBusApplication.getResourceString(R.string.timeremainingcalculator_minute_and));
		}
		else {
			sb.append(WhenZeBusApplication.getResourceString(R.string.timeremainingcalculator_minutes_and));
		}
		
		sb.append(difference.getSecondOfMinute());
		
		if (difference.getSecondOfMinute() == 1) {
			sb.append(WhenZeBusApplication.getResourceString(R.string.timeremainingcalculator_second));
		}
		else {
			sb.append(WhenZeBusApplication.getResourceString(R.string.timeremainingcalculator_seconds));
		}
		
		return new TimeRemaining(false, sb.toString());
	}

}
