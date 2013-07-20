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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread-safe date converter for heavily reused date formats.
 * Wraps a <i>non lenient</i> SimpleDateFormat.
 */
public final class SafeDateFormat {

	private final java.lang.ThreadLocal<DateFormat> dateFormat;

	/**
	 * @param aDateFormat
	 */
	public SafeDateFormat(final DateFormat aDateFormat) {
		dateFormat = new
				java.lang.ThreadLocal<DateFormat>() {
					protected DateFormat initialValue() {
						DateFormat dateFormat = (DateFormat) aDateFormat.clone();
						dateFormat.setLenient(false);
						return dateFormat;
					}
				};
	}

	/**
	 * @param simpleDateFormat
	 */
	public SafeDateFormat(final String simpleDateFormat) {
		dateFormat = new
				java.lang.ThreadLocal<DateFormat>() {
					protected DateFormat initialValue() {
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								simpleDateFormat);
						dateFormat.setLenient(false);
						return dateFormat;
					}
				};
	}

	/**
	 * @param simpleDateFormat
	 * @param symbols
	 */
	public SafeDateFormat(final String simpleDateFormat, final DateFormatSymbols symbols) {
		dateFormat = new
				java.lang.ThreadLocal<DateFormat>() {
					protected DateFormat initialValue() {
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								simpleDateFormat, symbols);
						dateFormat.setLenient(false);
						return dateFormat;
					}
				};
	}

	/**
	 * @param date
	 * @return
	 */
	public String format(Date date) {
		return ((DateFormat) dateFormat.get()).format(date);
	}


	public Date parse(final String dateStr) throws ParseException {
		return ((DateFormat) dateFormat.get()).parse(dateStr);
	}

}

