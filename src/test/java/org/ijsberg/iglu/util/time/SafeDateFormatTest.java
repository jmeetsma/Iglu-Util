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

package org.ijsberg.iglu.util.time;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.ijsberg.iglu.util.execution.Executable;
import org.ijsberg.iglu.util.execution.TimeOutException;
import org.junit.Test;


public class SafeDateFormatTest {

	@Test
	public void testConstructor() throws Exception {
		SafeDateFormat dateFormat = new SafeDateFormat("ddMMyyyy");
		
		assertEquals("27011970", dateFormat.format(new Date(TimeSupportTest.getTime(10, 25))));
	}
	
/*
 * 
 * TODO try to create a test that invokes a method with a number of concurrent threads		
	public void test() {
		Executable exec = new Executable() {
			public Object execute() {
				return null;
			}
		};
		exec.executeAsyncDelayed(SchedulingSupport.getTimeTillIntervalStart(System.currentTimeMillis(), 1));
	}
*/	
}

