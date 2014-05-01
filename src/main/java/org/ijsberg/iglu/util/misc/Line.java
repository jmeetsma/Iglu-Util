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

package org.ijsberg.iglu.util.misc;

public class Line {
	
	private String fileName;
	private int number;
	private String line;

	public Line(String fileName, int number, String line) {
		super();
		this.fileName = fileName;
		this.number = number;
		this.line = line;
	}

	public Line(int number, String line) {
		super();
		this.number = number;
		this.line = line;
	}
	
	public int getNumber() {
		return number;
	}

	public String getLine() {
		return line;
	}

    public void setLine(String line) {
        this.line = line;
    }

	public String toString() {
		return (fileName != null ? fileName + " " : "") + number + ": " + line;
	}
	
	public int hashCode() {
		return StringSupport.condenseWhitespace(line).trim().hashCode();
	}

}
