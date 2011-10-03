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
package org.ijsberg.iglu.util.collection;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Contains static methods for formatting of collections, as well as arrays and maps.
 * Print methods print to standard out by default and use an end of line (EOL) as separator.
 */
public abstract class CollectionSupport {
	/**
	 * @param coll
	 */
	public static void print(Collection coll) {
		print(coll, System.out);
	}

	/**
	 * @param coll
	 * @param out
	 */
	public static void print(Collection coll, PrintStream out) {
		print(coll, out, System.getProperty("line.separator"));
	}

	/**
	 * @param coll
	 * @param out
	 * @param separator
	 */
	public static void print(Collection coll, PrintStream out, String separator) {
		out.println(format(coll, separator));
	}

	/**
	 * @param coll
	 * @param separator
	 * @return
	 */
	public static String format(Collection coll, String separator) {
		return format(null, coll, separator);
	}

	/**
	 * @param itemPrefix
	 * @param coll
	 * @param separator
	 * @return
	 */
	public static String format(String itemPrefix, Collection coll, String separator) {
		if (coll == null) return "";
		StringBuffer retval = new StringBuffer();
		Iterator i = coll.iterator();
		while (i.hasNext()) {
			retval.append((itemPrefix != null ? itemPrefix : "") + i.next() + (i.hasNext() ? separator : ""));
		}
		return retval.toString();
	}

	/**
	 * @param map
	 */
	public static void print(Map map) {
		print(map, System.out);
	}

	/**
	 * @param map
	 * @param out
	 */
	public static void print(Map map, PrintStream out) {
		print(map, System.out, System.getProperty("line.separator"));
	}

	/**
	 * @param map
	 * @param out
	 * @param separator
	 */
	public static void print(Map map, PrintStream out, String separator) {
		out.println(format(map, separator));
	}


	/**
	 * @param map
	 * @param separator
	 * @return
	 */
	public static String format(Map map, String separator) {
		StringBuffer retval = new StringBuffer();
		Iterator i = map.keySet().iterator();
		while (i.hasNext()) {
			Object key = i.next();
			retval.append(key + "=" + map.get(key) + (i.hasNext() ? separator : ""));
		}
		return retval.toString();
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
		print(array, System.out, System.getProperty("line.separator"));
	}

	/**
	 * @param array
	 * @param out
	 * @param separator
	 */
	public static void print(Object[] array, PrintStream out, String separator) {
		out.println(format(array, separator));
	}

	/**
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String format(Object[] array, String separator) {
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


}
