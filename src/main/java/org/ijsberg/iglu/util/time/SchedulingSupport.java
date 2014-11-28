/*
 * Copyright 2011-2014 Jeroen Meetsma - IJsberg Automatisering BV
 *
 * This file is part of Iglu.
 *
 * Iglu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Iglu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Iglu.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ijsberg.iglu.util.time;

/*
 * Support scheduling in a cron-like manner.
 * Intervals are assumed to divide a day in a round number of events.
 */

public class SchedulingSupport extends TimeSupport {

	/**
	 * @param time
	 * @param intervalInMinutes should be > 0
	 * @return the number of passed intervals since midnight in local time zone
	 */
	public static int getIntervalsSinceMidnight(long time, int intervalInMinutes) {
		time += LOCAL_UTC_OFFSET;
		return (int) (time - (DAY_IN_MS * ((time + HALF_MINUTE_IN_MS) / (DAY_IN_MS)))) / (MINUTE_IN_MS * intervalInMinutes);
	}

	
	/**
	 * Determines the exact time an interval starts based on a time within the interval.
	 * @param time time in millis
	 * @param intervalInMinutes
	 *
	 * @return the exact time in milliseconds the interval begins local time
	 */
	public static long getPreviousIntervalStart(long time, int intervalInMinutes) {
		return getPreviousIntervalStart(time, intervalInMinutes, 0);
	}
	
	/**
	 * 
	 * @param intervalInMinutes
	 * @return
	 */
	public static boolean isIntervalRegularDaily(int intervalInMinutes) {
		return (DAY_IN_MINS % intervalInMinutes) == 0;
	}
	

	/**
	 * Reformulates negative offsets or offsets larger than interval.
	 * 
	 * @param intervalInMinutes
	 * @param offsetInMinutes
	 * @return offset in milliseconds
	 */
	public static long calculateOffsetInMs(int intervalInMinutes, int offsetInMinutes) {
		while (offsetInMinutes < 0) {
			offsetInMinutes = intervalInMinutes + offsetInMinutes;
		}
		while (offsetInMinutes > intervalInMinutes) {
			offsetInMinutes -= intervalInMinutes;
		}
		return offsetInMinutes * MINUTE_IN_MS;
	}
	
	/**
	 * Determines the exact time an interval starts based on a time within the interval.
	 * @param time time in millis
	 * @param intervalInMinutes
	 * @param offsetInMinutes
	 *
	 * @return the exact time in milliseconds the interval begins local time
	 */
	public static long getPreviousIntervalStart(long time, int intervalInMinutes, int offsetInMinutes) {
		long interval = MINUTE_IN_MS * intervalInMinutes;
		long offset = calculateOffsetInMs(intervalInMinutes, offsetInMinutes);
		
		return (interval * ((time + LOCAL_UTC_OFFSET - offset) / (interval))) + offset - LOCAL_UTC_OFFSET;
	}

	/**
	 * Determines the exact time the next interval starts based on a time within the current interval.
	 * @param time time in millis
	 * @param intervalInMinutes
	 *
	 * @return the exact time in milliseconds the interval begins local time
	 */
	public static long getNextIntervalStart(long time, int intervalInMinutes) {
		return getNextIntervalStart(time, intervalInMinutes, 0);
	}

	/**
	 * Determines the exact time the next interval starts based on a time within the current interval.
	 * @param time time in millis
	 * @param intervalInMinutes
	 * @param offsetInMinutes
	 *
	 * @return the exact time in milliseconds the interval begins local time
	 */
	public static long getNextIntervalStart(long time, int intervalInMinutes, int offsetInMinutes) {
		long interval = MINUTE_IN_MS * intervalInMinutes;
		return getPreviousIntervalStart(time, intervalInMinutes, offsetInMinutes) + interval;
	}

	/**
	 * @param time1 time in millis
	 * @param time2 time in millis
	 * @param intervalInMinutes should be > 0
	 * @return true if the two specified times are part of the same intervalInMinutes in local time zone
	 */
	public static boolean isWithinSameInterval(long time1, long time2, int intervalInMinutes) {
		return getPreviousIntervalStart(time1, intervalInMinutes) == getPreviousIntervalStart(time2, intervalInMinutes);
	}

	/**
	 * @param time1 time in millis
	 * @param time2 time in millis
	 * @param intervalInMinutes should be > 0
	 * @return true if the two specified times are part of the same intervalInMinutes in local time zone
	 */
	public static boolean isWithinSameInterval(long time1, long time2, int intervalInMinutes, int offsetInMinutes) {
		return getPreviousIntervalStart(time1, intervalInMinutes, offsetInMinutes) == getPreviousIntervalStart(time2, intervalInMinutes, offsetInMinutes);
	}

	/**
	 * @param intervalInMinutes
	 * @return true if a (page) interval starts this minute in local time zone
	 */
	public static boolean isWithinMinuteOfIntervalStart(long time, int intervalInMinutes) {
		return isWithinMinuteOfIntervalStart(time, intervalInMinutes, 0);
	}
	
	/**
	 * Useful for scheduling jobs checking every minute on the minute if an event should be triggered.
	 * @param time
	 * @param intervalInMinutes
	 * @param offsetInMinutes
	 * 
	 * @return true if an interval starts this minute local time
	 */
	public static boolean isWithinMinuteOfIntervalStart(long time, int intervalInMinutes, int offsetInMinutes) {
	
		time += LOCAL_UTC_OFFSET;
		long interval = MINUTE_IN_MS * intervalInMinutes;
		long offset = calculateOffsetInMs(intervalInMinutes, offsetInMinutes);
	
		return (interval * ((time + HALF_MINUTE_IN_MS) / interval)) + offset
				== roundToMinute(time);
	}


	/**
	 * @param intervalInMinutes
	 * @return the number of milliseconds to wait until the next interval starts in local time zone
	 */
	public static long getTimeTillIntervalStart(long time, int intervalInMinutes) {
		return getNextIntervalStart(time, intervalInMinutes) - (time);
	}

	/**
	 * @param intervalInMinutes
	 * @return the number of milliseconds to wait until the next interval starts in local time zone
	 */
	public static long getTimeTillIntervalStart(long time, int intervalInMinutes, int offsetInMinutes) {
		return getNextIntervalStart(time, intervalInMinutes, offsetInMinutes) - (time);
	}

}
