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

package org.ijsberg.iglu.util.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasicChannelTest {

	@Test
	public void testTransmit() throws Exception {
		BasicChannel channel = new BasicChannel("channel");
		ReceiverQueue receiver = new ReceiverQueue();

		channel.registerReceiver(receiver);

		assertEquals(0, receiver.available());

		channel.transmit("message");

		assertEquals(1, receiver.available());
		assertEquals("message", receiver.read());
	}

	@Test
	public void testTransmitBroadcast() throws Exception {
		BasicChannel channel = new BasicChannel("channel");
		ReceiverQueue receiver1 = new ReceiverQueue();
		ReceiverQueue receiver2 = new ReceiverQueue();

		channel.registerReceiver(receiver1);
		channel.registerReceiver(receiver2);

		assertEquals(0, receiver1.available());

		channel.transmit("message");

		assertEquals(1, receiver1.available());
		assertEquals("message", receiver1.read());
		assertEquals(0, receiver1.available());

		assertEquals(1, receiver2.available());
		assertEquals("message", receiver2.read());
		assertEquals(0, receiver2.available());
	}

}

