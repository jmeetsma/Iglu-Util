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

package org.ijsberg.iglu.util.formatting;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PatternMatchingSupportTest {
	private PatternMatchingSupport patternmatchingsupport;


	@Test
	public void testMatchWildcardExp() {


		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mask"));
		assertFalse(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mas", "*.mask"));
		assertFalse(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mas"));
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mas*"));

		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mask|*.test"));
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.test", "*.mask|*.test"));

	}

	@Test
	public void testMatchRegExp() {
		assertTrue(PatternMatchingSupport.valueMatchesRegularExpression("jeroen@ijsberg.nl", "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$"
		));
	}

}

