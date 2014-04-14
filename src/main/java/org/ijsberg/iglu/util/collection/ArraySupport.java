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

package org.ijsberg.iglu.util.collection;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * Contains miscellaneous static methods for array handling.
 */
public abstract class ArraySupport {
	/**
	 * @param value
	 * @param range
	 * @param defaultIndex
	 * @return the index of an object equal to object within a range or defaultIndex in case nothing matches
	 */
	public static int determineIndexInRange(Object value, Object[] range, int defaultIndex) {
		if (value == null) {
			return defaultIndex;
		}
		int result = defaultIndex;

		for (int i = 0; i < range.length; i++) {
			if (value.equals(range[i])) {
				result = i;
				break;
			}
		}
		return result;
	}

	/**
	 * @param array
	 */
	public static void print(Object[] array) {
		print(array, System.out);
	}

	/**
	 * @param array
	 * @param out
	 */
	public static void print(Object[] array, PrintStream out) {
		print(array, out, System.getProperty("line.separator"));
	}

	/**
	 * @param array
	 * @param out
	 * @param separator
	 */
	public static void print(Object[] array, PrintStream out, String separator) {
		out.print(format(array, separator));
	}

	/**
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String format(Object[] array, String separator) {
		return format(null, array, separator);
	}

	/**
	 * @param itemPrefix
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String format(String itemPrefix, Object[] array, String separator) {
		if (array == null) {
			return "";
		}
		StringBuffer retval = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			retval.append((itemPrefix != null ? itemPrefix : "") + array[i] + (i + 1 != array.length ? separator : ""));
		}
		return retval.toString();
	}


	/**
	 * @param itemPrefix
	 * @param itemPostfix
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String format(String itemPrefix, String itemPostfix, Object[] array, String separator) {
		if (array == null) {
			return "";
		}
		StringBuffer retval = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			retval.append((itemPrefix != null ? itemPrefix : "") + array[i] + (itemPostfix != null ? itemPostfix : "") + (i + 1 != array.length ? separator : ""));
		}
		return retval.toString();
	}

	/**
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String format(int[] array, String separator) {
		if (array == null) {
			return null;
		}
		StringBuffer retval = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			retval.append(array[i] + (i + 1 != array.length ? separator : ""));
		}
		return retval.toString();
	}

    /**
     * @param array
     * @param separator
     * @return
     */
    public static String format(double[] array, String separator) {
        if (array == null) {
            return null;
        }
        StringBuffer retval = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            retval.append(array[i] + (i + 1 != array.length ? separator : ""));
        }
        return retval.toString();
    }

	/**
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String format(byte[] array, String separator) {
		if (array == null) {
			return null;
		}
		StringBuffer retval = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			retval.append(array[i] + (i + 1 != array.length ? separator : ""));
		}
		return retval.toString();
	}

	
	public static <T> T[] getFilledArray(T[] array, T val) {
		Arrays.fill(array, val);
		return array;
	}



}
