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

package org.ijsberg.iglu.util.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class SchedulingSupportTest extends TimeSupportTest {
	@Test
	public void testGetIntervalsSinceMidnight() throws Exception {
		
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(now);
		assertEquals(cal.get(Calendar.MINUTE) + (cal.get(Calendar.HOUR_OF_DAY) * 60), SchedulingSupport.getIntervalsSinceMidnight(now.getTime(), 1));

		assertEquals((cal.get(Calendar.MINUTE) + (cal.get(Calendar.HOUR_OF_DAY) * 60)) / 5, SchedulingSupport.getIntervalsSinceMidnight(now.getTime(), 5));

	}
	

	@Test
		public void testIsIntervalRegularDaily() throws Exception {
			assertTrue(SchedulingSupport.isIntervalRegularDaily(1));
			assertTrue(SchedulingSupport.isIntervalRegularDaily(5));
			assertTrue(SchedulingSupport.isIntervalRegularDaily(6));
	
			assertFalse(SchedulingSupport.isIntervalRegularDaily(7));
	
			assertTrue(SchedulingSupport.isIntervalRegularDaily(10));
			assertTrue(SchedulingSupport.isIntervalRegularDaily(12));
			assertTrue(SchedulingSupport.isIntervalRegularDaily(15));
			
			assertFalse(SchedulingSupport.isIntervalRegularDaily(17));
	
			assertTrue(SchedulingSupport.isIntervalRegularDaily(60));

			assertTrue(SchedulingSupport.isIntervalRegularDaily(1440));
	
			assertFalse(SchedulingSupport.isIntervalRegularDaily(2 * 1440));
		}
	
	@Test
	public void testCalculateOffsetInMs() throws Exception {
	
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 5));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 20));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, -10));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, -25));

		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 1440 + 5));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 1440 + 20));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, -1440 - 10));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, -1440 - 25));

		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * 1440 + 5));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * 1440 + 20));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * -1440 - 10));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * -1440 - 25));
	
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, 5));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, 22));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, -12));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, -29));
/*
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, 1440 + 5));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, 1440 + 22));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, -1440 - 12));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(17, -1440 - 29));

		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * 1440 + 5));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * 1440 + 20));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * -1440 - 10));
		assertEquals(300000, SchedulingSupport.calculateOffsetInMs(15, 2 * -1440 - 25));
*/
	}

	
	

	@Test
	public void testGetPreviousIntervalStart() throws Exception {
		
		Calendar cal = new GregorianCalendar();
		long time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 31), 5);
		cal.setTimeInMillis(time);
		assertEquals(30, cal.get(Calendar.MINUTE));
		
		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 44), 15);
		cal.setTimeInMillis(time);
		assertEquals(30, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 31), 12);
		cal.setTimeInMillis(time);
		assertEquals(24, cal.get(Calendar.MINUTE));
        assertEquals(9, cal.get(Calendar.HOUR));

        time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 31), (TimeSupport.DAY_IN_MINS / 4));
		cal.setTimeInMillis(time);
		assertEquals(0, cal.get(Calendar.MINUTE));
        //TODO next test fails during DST season
//		assertEquals(6, cal.get(Calendar.HOUR));

		
		//next: intervals not dividing day in round numbers

//		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 31), 7);

	}
	

	@Test
	public void testGetPreviousIntervalStartWithOffset() throws Exception {
		
		Calendar cal = new GregorianCalendar();
		long time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 31), 5, 5);
		cal.setTimeInMillis(time);
		assertEquals(30, cal.get(Calendar.MINUTE));
		
		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 44), 15, 5);
		cal.setTimeInMillis(time);
		assertEquals(35, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 31), 12, 5);
		cal.setTimeInMillis(time);
		assertEquals(29, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 37), 12, 5);
		cal.setTimeInMillis(time);
		assertEquals(29, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 42), 12, 5);
		cal.setTimeInMillis(time);
		assertEquals(41, cal.get(Calendar.MINUTE));
		
		//negative offset

		time = SchedulingSupport.getPreviousIntervalStart(getTime(9, 42), 12, -31);
		cal.setTimeInMillis(time);
		assertEquals(41, cal.get(Calendar.MINUTE));
		
		//odd offset
	}

	
	@Test
	public void testGetNextPreviousIntervalStart() throws Exception {
		
		Calendar cal = new GregorianCalendar();
		long time = SchedulingSupport.getNextIntervalStart(getTime(9, 31), 5);
		cal.setTimeInMillis(time);
		assertEquals(35, cal.get(Calendar.MINUTE));
		
		time = SchedulingSupport.getNextIntervalStart(getTime(9, 44), 15);
		cal.setTimeInMillis(time);
		assertEquals(45, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getNextIntervalStart(getTime(9, 31), 12);
		cal.setTimeInMillis(time);
		assertEquals(36, cal.get(Calendar.MINUTE));
	}
	

	@Test
	public void testGetNextIntervalStartWithOffset() throws Exception {
		
		Calendar cal = new GregorianCalendar();
		long time = SchedulingSupport.getNextIntervalStart(getTime(9, 31), 5, 5);
		cal.setTimeInMillis(time);
		assertEquals(35, cal.get(Calendar.MINUTE));
		
		time = SchedulingSupport.getNextIntervalStart(getTime(9, 44), 15, 5);
		cal.setTimeInMillis(time);
		assertEquals(50, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getNextIntervalStart(getTime(9, 31), 12, 5);
		cal.setTimeInMillis(time);
		assertEquals(41, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getNextIntervalStart(getTime(9, 37), 12, 5);
		cal.setTimeInMillis(time);
		assertEquals(41, cal.get(Calendar.MINUTE));

		time = SchedulingSupport.getNextIntervalStart(getTime(9, 42), 12, 5);
		cal.setTimeInMillis(time);
		assertEquals(53, cal.get(Calendar.MINUTE));
	}
	
	@Test
	public void testIsWithinSameInterval() throws Exception {
		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(9, 30), getTime(9, 30), 1));
		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(9, 30), getTime(9, 30), 5));
		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(9, 30), getTime(9, 30), 30));
		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(9, 30), getTime(9, 30), 60));

		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(9, 31), getTime(9, 33), 5));

		assertFalse(SchedulingSupport.isWithinSameInterval(getTime(9, 30), getTime(9, 34), 1));
		assertFalse(SchedulingSupport.isWithinSameInterval(getTime(9, 31), getTime(9, 35), 5));
		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(13, 03), getTime(13, 13), 15));
		assertFalse(SchedulingSupport.isWithinSameInterval(getTime(13, 03), getTime(14, 13), 15));
	}
	

	@Test
	public void testIsWithinSameIntervalWithOffset() throws Exception {
		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(9, 30), getTime(9, 30), 1, 0));
		assertTrue(SchedulingSupport.isWithinSameInterval(getTime(9, 31), getTime(9, 33), 5, 1));
		assertFalse(SchedulingSupport.isWithinSameInterval(getTime(9, 31), getTime(9, 33), 5, 2));
	}
		
	@Test
	public void testIsWithinMinuteOfIntervalStart() throws Exception {

		assertTrue(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(9, 30) + TimeSupport.SECOND_IN_MS, 10));
		assertFalse(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(9, 31) + TimeSupport.SECOND_IN_MS, 10));

		assertTrue(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(12, 48) + TimeSupport.SECOND_IN_MS, 12));
		assertFalse(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(12, 47) + TimeSupport.SECOND_IN_MS, 12));

	}

	@Test
	public void testIsWithinMinuteOfIntervalPlusOffsetStart() throws Exception {

		assertTrue(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(9, 33) + TimeSupport.SECOND_IN_MS, 10, 3));
		assertFalse(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(9, 34) + TimeSupport.SECOND_IN_MS, 10, 3));

		assertTrue(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(12, 53) + TimeSupport.SECOND_IN_MS, 12, 5));
		assertFalse(SchedulingSupport.isWithinMinuteOfIntervalStart(getTime(12, 52) + TimeSupport.SECOND_IN_MS, 12, 5));

	}

	@Test
		public void testGetTimeTillIntervalStart() throws Exception {
			
			assertEquals(2 * TimeSupport.MINUTE_IN_MS, SchedulingSupport.getTimeTillIntervalStart(getTime(9, 33), 5));
			assertEquals(3 * TimeSupport.MINUTE_IN_MS, SchedulingSupport.getTimeTillIntervalStart(getTime(9, 33), 12));
			
			assertEquals(27 * TimeSupport.MINUTE_IN_MS - TimeSupport.SECOND_IN_MS, SchedulingSupport.getTimeTillIntervalStart(getTime(9, 33) + TimeSupport.SECOND_IN_MS, 30));
	
			//TODO other than happy path tests
			
		}

	@Test
	public void testGetMillisNextIntervalStartWithOffSet() throws Exception {
		
		assertEquals(5 * TimeSupport.MINUTE_IN_MS, SchedulingSupport.getTimeTillIntervalStart(getTime(9, 33), 5, 3));
		assertEquals(10 * TimeSupport.MINUTE_IN_MS, SchedulingSupport.getTimeTillIntervalStart(getTime(9, 33), 12, 7));
		
		assertEquals(8 * TimeSupport.MINUTE_IN_MS - TimeSupport.SECOND_IN_MS, SchedulingSupport.getTimeTillIntervalStart(getTime(9, 33) + TimeSupport.SECOND_IN_MS, 30, 11));

		//TODO other than happy path tests
		
	}

}
