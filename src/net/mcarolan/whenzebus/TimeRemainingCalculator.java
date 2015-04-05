package net.mcarolan.whenzebus;

import org.joda.time.DateTime;

public class TimeRemainingCalculator {
	
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
			sb.append(" minute and ");
		}
		else {
			sb.append(" minutes and ");
		}
		
		sb.append(difference.getSecondOfMinute());
		
		if (difference.getSecondOfMinute() == 1) {
			sb.append(" second");
		}
		else {
			sb.append(" seconds");
		}
		
		return new TimeRemaining(false, sb.toString());
	}

}
