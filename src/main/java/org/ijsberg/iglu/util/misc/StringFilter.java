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

package org.ijsberg.iglu.util.misc;

/**
 */
public class StringFilter {
	private String[] targetSubstringArray;
	private String[] substituteSubstringArray;


	public StringFilter(String[] targetSubstringArray, String[] substituteSubstringArray) {
		if (targetSubstringArray.length != substituteSubstringArray.length) {
			throw new IllegalArgumentException("sizes of target and substitute string arrays must match: " + targetSubstringArray.length + " != " + substituteSubstringArray.length);
		}
		this.targetSubstringArray = targetSubstringArray;
		this.substituteSubstringArray = substituteSubstringArray;
	}

	public String filter(String input) {
		StringBuffer retval = new StringBuffer(input);
		for (int i = 0; i < targetSubstringArray.length; i++) {
			StringSupport.replaceAll(retval, targetSubstringArray[i], substituteSubstringArray[i]);
		}
		return retval.toString();
	}

}
