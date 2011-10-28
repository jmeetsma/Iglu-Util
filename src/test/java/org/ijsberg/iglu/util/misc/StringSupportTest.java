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

import static junit.framework.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class StringSupportTest {

	@Test
	public void extractStringsInbetweenTagsFromText() throws Exception {

		String text = "this is a text with a [part inbetween] brackets";
		Set result = StringSupport.extractStringsInbetweenTagsFromText(text, '[', ']', false);
		assertEquals(1, result.size());
		assertEquals("part inbetween", result.toArray()[0]);

		text = "this is a text";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '[', ']', false);
		assertEquals(0, result.size());

		text = "this is a text with a {part inbetween} brackets {and another part}";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '{', '}', false);
		assertEquals(2, result.size());
		assertEquals("part inbetween", result.toArray()[0]);
		assertEquals("and another part", result.toArray()[1]);

		//sort results
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '{', '}', true);
		assertEquals(2, result.size());
		assertEquals("and another part", result.toArray()[0]);
		assertEquals("part inbetween", result.toArray()[1]);

	}

	@Test
	public void extractStringsInbetweenQuotesFromText() throws Exception {

		String text = "this is a text with a 'part inbetween' brackets";
		Set result = StringSupport.extractStringsInbetweenTagsFromText(text, '\'', '\'', false);
		assertEquals(1, result.size());
		assertEquals("part inbetween", result.toArray()[0]);

		text = "this is a text";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '\'', '\'', false);
		assertEquals(0, result.size());

		text = "this is a text with a \"part inbetween\" brackets \"and anhother part\"";
		result = StringSupport.extractStringsInbetweenTagsFromText(text, '\"', '\"', false);
		assertEquals(2, result.size());
		assertEquals("part inbetween", result.toArray()[0]);
		assertEquals("and another part", result.toArray()[1]);
	}
}

