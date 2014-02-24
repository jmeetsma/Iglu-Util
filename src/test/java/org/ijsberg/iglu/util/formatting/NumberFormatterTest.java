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

package org.ijsberg.iglu.util.formatting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberFormatterTest {

	@Test
	public void testFormat() {
		String result = new NumberFormatter(',', '.').format(66.6666, 1);
		assertEquals("66,7", result);
	}

	@Test
	public void testFormatFloat() {
		String result = new NumberFormatter(',', '.').format(66.6666, 2);
		assertEquals("66,67", result);

		result = new NumberFormatter(',', '.').format(666666.6666, 2);
		assertEquals("666.666,67", result);

		result = new NumberFormatter(',', '.').format(666666.66500, 2);
		assertEquals("666.666,67", result);

		result = new NumberFormatter(',', '.').format(666666.66499, 2);
		assertEquals("666.666,66", result);

		result = new NumberFormatter(',', '.').format(666666.000000001, 2);
		assertEquals("666.666,00", result);

		result = new NumberFormatter(',', '.').format(666666.0, 2);
		assertEquals("666.666,00", result);

		result = new NumberFormatter(',', '.').format(666666, 1);
		assertEquals("666.666,0", result);

		result = new NumberFormatter(',', '.').format(666666.6666, 0);
		assertEquals("666.667", result);
	}


	@Test
	public void testFormatInt() {
		String result = new NumberFormatter(',', '.').format(666666);
		assertEquals("666.666", result);
	}
}