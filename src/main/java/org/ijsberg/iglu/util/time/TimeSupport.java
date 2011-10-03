/*
 * Copyright 2011 Jeroen Meetsma
 *
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Contains convenience methods concerning time and date.
 */
public abstract class TimeSupport {
	public static final long defaultGMTOffsetInMillis = getStandardGMTOffset();

	/**
	 * @return
	 */
	private static long getStandardGMTOffset() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		//determine the offset from GMT by adding daylight saving time and time zone offset
		return cal.get(Calendar.DST_OFFSET) + cal.get(Calendar.ZONE_OFFSET);
	}

	/**
	 * @param date
	 * @return true if the given date happens to be today
	 */
	public static boolean isToday(Date date) {
		Calendar calInput = new GregorianCalendar();
		calInput.setTime(date);

		Calendar calToday = new GregorianCalendar();
		calToday.setTime(new Date());

		return calInput.get(Calendar.DAY_OF_YEAR) == calToday.get(Calendar.DAY_OF_YEAR) &&
				calInput.get(Calendar.YEAR) == calToday.get(Calendar.YEAR);
	}

	/**
	 * @param date
	 * @return true if the given date is on a day earlier than today
	 */
	public static boolean isBeforeToday(Date date) {
		Calendar calInput = new GregorianCalendar();
		calInput.setTime(date);

		Calendar calToday = new GregorianCalendar();
		calToday.setTime(new Date());

		//it doesn't matter if you multiply nr of years with 365.25 or 1000
		return (calInput.get(Calendar.YEAR) * 1000 + calInput.get(Calendar.DAY_OF_YEAR)) < (calToday.get(Calendar.YEAR) * 1000 + calToday.get(Calendar.DAY_OF_YEAR));
	}


	/**
	 * @param date
	 * @return true if the given date is on a day later than today
	 */
	public static boolean isAfterToday(Date date) {
		Calendar calInput = new GregorianCalendar();
		calInput.setTime(date);

		Calendar calToday = new GregorianCalendar();
		calToday.setTime(new Date());

		//it doesn't any difference if you multiply nr of years with 365.25 or 1000
		return (calInput.get(Calendar.YEAR) * 1000 + calInput.get(Calendar.DAY_OF_YEAR)) > (calToday.get(Calendar.YEAR) * 1000 + calToday.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * @return The number of minutes passed since 00:00 hours
	 */
	public static int getMinutesSinceMidnight(long gmtOffset) {
		return getMinutesSinceMidnight(System.currentTimeMillis(), gmtOffset);
	}

	/**
	 * @param timeInMillis
	 * @return The number of minutes passed since 00:00 hours
	 */
	public static int getMinutesSinceMidnight(long timeInMillis, long gmtOffset) {
		if (gmtOffset > 86400000 || gmtOffset < -86400000) {
			throw new IllegalArgumentException("time can not differ more than a day from GMT");
		}
		timeInMillis += gmtOffset;
		return (int) (timeInMillis - (1440 * 60000 * ((timeInMillis + 30000) / (1440 * 60000)))) / 60000;
	}

	/**
	 * @param intervalInMinutes intervalInMinutes in minutes
	 * @param timeInMillis
	 * @return the number of passed intervals since midnight
	 */
	public static int getIntervalsSinceMidnight(int intervalInMinutes, long timeInMillis, long gmtOffset) {
		if (gmtOffset > 86400000 || gmtOffset < -86400000) {
			throw new IllegalArgumentException("time can not differ more than a day from GMT");
		}
		if (intervalInMinutes <= 0) {
			throw new IllegalArgumentException("intervalInMinutes must be larger than 0");
		}
		timeInMillis += gmtOffset;
		return (int) (timeInMillis - (1440 * 60000 * ((timeInMillis + 30000) / (1440 * 60000)))) / (60000 * intervalInMinutes);
	}

	/**
	 * @param intervalInMinutes intervalInMinutes in minutes
	 * @param time1			 time in millis
	 * @param time2			 time in millis
	 * @return true if the two specified times are part of the same intervalInMinutes
	 */
	public static boolean isSameInterval(int intervalInMinutes, long time1, long time2, long gmtOffset) {
		if (gmtOffset > 86400000 || gmtOffset < -86400000) {
			throw new IllegalArgumentException("time can not differ more than a day from GMT");
		}
		if (intervalInMinutes <= 0) {
			throw new IllegalArgumentException("intervalInMinutes must be larger than 0");
		}
		return floorToIntervalStart(intervalInMinutes, time1, gmtOffset) == floorToIntervalStart(intervalInMinutes, time2, gmtOffset);
	}

	/**
	 * @param time1 time in millis
	 * @param time2 time in millis
	 * @return true if the two specified times are part of the same day
	 */
	public static boolean isSameDay(long time1, long time2) {
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(new Date(time1));
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.setTime(new Date(time2));

		return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * @param time1 time in millis
	 * @param time2 time in millis
	 * @return true if the two specified times are part of the same week
	 */
	public static boolean isSameWeek(long time1, long time2) {
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(new Date(time1));
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.setTime(new Date(time2));

		return (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR));
	}

	/**
	 * Determines the exact time an interval starts based on a time within the interval.
	 *
	 * @param intervalInMinutes
	 * @param time			  time in millis
	 * @return the exact time in milliseconds the interval begins
	 */
	public static long floorToIntervalStart(int intervalInMinutes, long time, long gmtOffset) {
		return 60000 * intervalInMinutes * ((time + gmtOffset) / (60000 * intervalInMinutes));
	}

	/**
	 * @param time time in millis
	 * @return time in milles rounded to the minute
	 */
	public static long roundToMinute(long time) {
		return 60000 * ((time + 30000) / 60000);
	}

	/**
	 * @param intervalInMinutes
	 * @param offsetInMinutes
	 * @return true if an interval starts this minute
	 */
	public static boolean isIntervalStart(int intervalInMinutes, int offsetInMinutes, long gmtOffset) {
		if (gmtOffset > 86400000 || gmtOffset < -86400000) {
			throw new IllegalArgumentException("time can not differ more than a day from GMT");
		}
		if (intervalInMinutes <= 0) {
			throw new IllegalArgumentException("interval must be larger than 0");
		}

		while (offsetInMinutes < 0) {
			offsetInMinutes = intervalInMinutes + offsetInMinutes;
		}
		//offset must be smaller than interval

		while (offsetInMinutes > intervalInMinutes) {
			offsetInMinutes -= intervalInMinutes;
		}

		long currentTime = System.currentTimeMillis() + gmtOffset;
		long interval = 60000 * intervalInMinutes;

		//add half a minute to the time
		//(this will ensure that all values 1/2 min before and 1/2 min after are valid)
		//divide by the interval
		//decimals will be lost
		//multiply to original value
		//compare to 1 minute interval
		return (interval * ((currentTime + 30000) / interval)) + (offsetInMinutes * 60000)
				== roundToMinute(currentTime);
	}

	/**
	 * @param intervalInMinutes
	 * @return true if a (page) interval starts this minute
	 */
	public static boolean isIntervalStart(int intervalInMinutes, long gmtOffset) {
		if (gmtOffset > 86400000 || gmtOffset < -86400000) {
			throw new IllegalArgumentException("time can not differ more than a day from GMT");
		}
		if (intervalInMinutes <= 0) {
			throw new IllegalArgumentException("interval must be larger than 0");
		}
		long currentTime = System.currentTimeMillis() + gmtOffset;
		long interval = 60000 * intervalInMinutes;

		//add half a minute to the time
		//(this will ensure that all values 1/2 min before and 1/2 min after are valid)
		//divide by the interval
		//decimals will be lost
		//multiply to original value
		//compare to 1 minute interval

		return (interval * ((currentTime + 30000) / interval))
				== roundToMinute(currentTime);
	}

	/**
	 * @param intervalInMinutes
	 * @return the number of milliseconds to wait until the next interval starts
	 */
	public static long determineMillisUntilNextInterval(int intervalInMinutes, long gmtOffset) {
		if (gmtOffset > 86400000 || gmtOffset < -86400000) {
			throw new IllegalArgumentException("time can not differ more than a day from GMT");
		}
		if (intervalInMinutes <= 0) {
			throw new IllegalArgumentException("interval must be larger than 0");
		}
		long currentTime = System.currentTimeMillis() + gmtOffset;
		long interval = 60000l * intervalInMinutes;

		long result = interval - (currentTime - (interval * (currentTime / interval)));
		return result;
	}
}
