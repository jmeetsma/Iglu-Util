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

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class PagedListTest extends TestCase {
	
	private static String[] testlist = {"1","2","3","4","5","6","7","8","9","10"};

	@Test
	public void testGetNrofPages() throws Exception {

		List list = new ArrayList(Arrays.asList(testlist));
		PagedList<String> pagedList = new PagedList<String>(list, 10);
		assertEquals(1, pagedList.getNrofPages());

		list.add("11");
		pagedList = new PagedList<String>(list, 10);
		assertEquals(2, pagedList.getNrofPages());

		pagedList = new PagedList<String>(list, 5);
		assertEquals(3, pagedList.getNrofPages());
	}

	@Test
	public void testGetPage() throws Exception {

		List list = new ArrayList(Arrays.asList(testlist));
		PagedList<String> pagedList = new PagedList<String>(list, 10);
		assertEquals(10, pagedList.getPage(0).size());

		list.add("11");
		pagedList = new PagedList<String>(list, 10);
		assertEquals(10, pagedList.getPage(0).size());
		assertEquals(1, pagedList.getPage(1).size());

		pagedList = new PagedList<String>(list, 5);
		assertEquals(1, pagedList.getPage(2).size());
	}
}
