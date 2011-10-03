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
package org.ijsberg.iglu.util.logic;

/**
 * A statement may be evaluated as being 'true' or 'false'
 * but it may also be some string of which its presence may be tested.
 */
public class Statement extends LogicalElement {
	protected String statement;

	/**
	 *
	 */
	protected Statement() {
	}

	/**
	 * @param statement
	 */
	public Statement(String statement) {
		this.statement = statement;
	}

	/**
	 * @return
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * @return
	 */
	public String toString() {
		return statement;
	}

	/**
	 * @param elements
	 * @return
	 */
	public boolean match(Object[] elements) {
		for (int i = 0; i < elements.length; i++) {
			if (statement.equals(elements[i])) {
				return true;
			}
		}
		return false;
	}

}
