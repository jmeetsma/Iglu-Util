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

import junit.framework.TestCase;

public class OperatorTest extends TestCase {
	private Operator operator;

	/**
	 */
	public void setUp() {
		System.out.println("setting up test environment for class org.ijsberg.iglu.util.logic.Operator");
		System.gc();
		System.out.println("Total memory " + (Runtime.getRuntime().totalMemory() / Math.pow(2, 20)) + " Mb");
		System.out.println("Memory use " + Math.floor(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().totalMemory())) + " %");
		operator = new Operator(123);
	}

	/**
	 */
	public void tearDown() {
	}

	/**
	 * @see org.ijsberg.iglu.util.logic.Operator#toString()
	 */
	public void testToString() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.logic.Operator.toString()
		try {
			java.lang.String retval = operator.toString();
			fail("expected : Throwable: java.lang.ArrayIndexOutOfBoundsException (message: 123)");
		}
		catch (java.lang.ArrayIndexOutOfBoundsException expectedThrowable) {
			//expected
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected java.lang.ArrayIndexOutOfBoundsException to be thrown (with message: 123) instead of " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.logic.Operator#getType()
	 */
	public void testGetType() {

		//tests for signature public int org.ijsberg.iglu.util.logic.Operator.getType()
		try {
			int retval = operator.getType();
			assertEquals(123, retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: 123, instead of Throwable: " + t.getClass().getName());
		}
	}
}

