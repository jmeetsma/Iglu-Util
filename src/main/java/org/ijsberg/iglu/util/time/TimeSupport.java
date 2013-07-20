/*
 * Copyright 2011-2013 Jeroen Meetsma - IJsberg
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
 * Contains convenience methods concerning time, date and scheduling.
 */
public abstract class TimeSupport {
	
	public static final int SECOND_IN_MS = 1000;
	public static final int MINUTE_IN_MS = 60 * SECOND_IN_MS;
	public static final int HALF_MINUTE_IN_MS = 30 * SECOND_IN_MS;
	public static final int HOUR_IN_MS = 60 * MINUTE_IN_MS;
	public static final int DAY_IN_MS = 24 * HOUR_IN_MS;
	public static final int DAY_IN_MINS = 24 * 60;

	public static final long LOCAL_UTC_OFFSET = getLocalUtcOffset();
	public static final long LOCAL_UTC_OFFSET_IN_MINUTES = LOCAL_UTC_OFFSET / MINUTE_IN_MS;

	/**
	 * The offset from UTC can be used to calculate time and date of time stamps based on
	 * <code>System.currentTimeInMillis()</code>.
	 * 
	 * @return offset from UTC in milliseconds
	 */
	private static long getLocalUtcOffset() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return getUtcOffset(cal);
	}

	/**
	 * @return offset from UTC in milliseconds
	 */
	public static long getUtcOffset(Calendar cal) {
		//offset from UTC consists of daylight saving time and time zone offset
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

		//it doesn't make any difference if you multiply nr of years with 365.25 or 1000
		return (calInput.get(Calendar.YEAR) * 1000 + calInput.get(Calendar.DAY_OF_YEAR)) > (calToday.get(Calendar.YEAR) * 1000 + calToday.get(Calendar.DAY_OF_YEAR));
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
	 * @param time time in millis
	 * @return time in milles rounded to the minute
	 */
	public static long roundToMinute(long time) {
		return MINUTE_IN_MS * ((time + HALF_MINUTE_IN_MS) / MINUTE_IN_MS);
	}

	/**
	 * @return The number of minutes passed since 00:00 hours
	 */
	public static int getMinutesSinceMidnight() {
		return getMinutesSinceMidnight(System.currentTimeMillis());
	}

	/**
	 * @param time
	 * @return the number of minutes passed since 00:00 hours in current time zone
	 */
	public static int getMinutesSinceMidnight(long time) {
		return SchedulingSupport.getIntervalsSinceMidnight(time, 1);
	}


}
