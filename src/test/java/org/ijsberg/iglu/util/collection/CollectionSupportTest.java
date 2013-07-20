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

import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

/**
 */
public class CollectionSupportTest {
	@Test
	public void testPrint() throws Exception {

		Collection coll = Arrays.asList(new String[]{"one","two","three"});
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(buf);
		CollectionSupport.print(coll, out, "->");
		assertEquals("one->two->three", buf.toString());
	}

	@Test
	public void testFormat() throws Exception {
		Collection coll = Arrays.asList(new String[]{"one","two","three"});
		String result = CollectionSupport.format("- ", coll, ", ");
		assertEquals("- one, - two, - three", result);
	}
}
