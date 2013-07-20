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

package org.ijsberg.iglu.util.logic;

/**
 * Subclasses are part of a logical expression.
 */
public class LogicalElement {
	//supported logical operators
	public static final int NONE = 0;
	public static final int NOT = 1;
	public static final int AND = 2;
	public static final int OR = 3;
	/*	public static final int EQ = 4;
	 public static final int NEQ = 5;
	 public static final int LT = 6;
	 public static final int GT = 7;
 */
	public String[] operatorStr = {" ? ", "NOT", " AND ", " OR "/*, " EQ ", " NEQ ", " LT ", " GT "*/};
}
