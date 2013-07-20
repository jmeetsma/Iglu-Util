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

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PatternMatchingSupportTest {


	@Test
	public void testMatchWildcardExp() {

		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*"));
		assertFalse(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", ""));

		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mask"));
		assertFalse(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mas", "*.mask"));
		assertFalse(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mas"));
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mas*"));

		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("/dir/subdir/file.ext", "/dir/subdir/*"));
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("/dir/subdir/file.ext", "/dir/*dir/*.ext"));
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("/dir/subdir/file.ext", "*/file.ext"));

		assertFalse(PatternMatchingSupport.valueMatchesWildcardExpression("/dir/subdir/file.ext", "sub*/file.ext"));

		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("/dir/subdir/file.ext", "/dir/s?bdir/file.ext"));
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("/dir/subdir/file.ext", "*/subdir/*"));


	}

	@Test
	public void testMatchWildcardExpOr() {
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.mask", "*.mask|*.test"));
		assertTrue(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.test", "*.mask|*.test"));
		assertFalse(PatternMatchingSupport.valueMatchesWildcardExpression("hopla.test", "*.mask|*.hopla"));
	}
	
	
	@Test
	public void testMatchRegExp() {
		assertTrue(PatternMatchingSupport.valueMatchesRegularExpression("jeroen@ijsberg.nl", "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$"
		));

		assertTrue(PatternMatchingSupport.valueMatchesRegularExpression("hopla.mask", ".*mask"));
		assertTrue(PatternMatchingSupport.valueMatchesRegularExpression("hopla.mask.mask", ".*mask"));
	}

}

