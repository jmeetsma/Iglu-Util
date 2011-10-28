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

import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class StringSupportTest {

	@Test
	public void testSplit() throws Exception {
		List result = StringSupport.split("org.ijsberg.iglu.util",".","");
		assertEquals("org", result.get(0));
		assertEquals("ijsberg", result.get(1));
		assertEquals("iglu", result.get(2));
		assertEquals("util", result.get(3));

		String line = "Harry went to the \"market square\" and bought some fish";
		result = StringSupport.split(line, " ", "\"");
		assertEquals("Harry", result.get(0));
		assertEquals("the", result.get(3));
		assertEquals("market square", result.get(4));
		assertEquals("and", result.get(5));

		line = "Harry went to the 'market square' and bought some fish";
		result = StringSupport.split(line, " ", "'");
		assertEquals("the", result.get(3));
		assertEquals("market square", result.get(4));
		assertEquals("and", result.get(5));

	}
}

