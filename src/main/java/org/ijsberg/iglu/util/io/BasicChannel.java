/*
 * Copyright 2011-2014 Jeroen Meetsma - IJsberg Automatisering BV
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

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Transmits messages to a number of Receivers.
 */
public class BasicChannel implements Channel, Transmitter {
	private ArrayList<Receiver> receivers = new ArrayList<Receiver>();
	private int maxNrofReceivers;//unlimited
	//echo input to transceivers
	private boolean echo;
	private String name;
	private boolean closed;


	/**
	 * Constructs a channel with a certain name.
	 *
	 * @param name name
	 */
	public BasicChannel(String name) {
		this.name = name;
	}


	/**
	 * Constructs a channel with a certain name and a maximun nunber of receivers.
	 *
	 * @param name name
	 * @param maxNrofReceivers maximum number of receivers
	 */
	public BasicChannel(String name, int maxNrofReceivers) {
		this.name = name;
		this.maxNrofReceivers = maxNrofReceivers;
	}


	/**
	 * Constructs a channel with a certain name and the ability to echo objects back to the transceiver.
	 *
	 * @param name name
	 * @param echo echo back to transceiver
	 */
	public BasicChannel(String name, boolean echo) {
		this.name = name;
		this.echo = echo;
	}


	/**
	 * Constructs a channel with a certain name, a maximun nunber of receivers and the ability to echo objects back to the transceiver.
	 *
	 * @param name			 name
	 * @param maxNrofReceivers maximum number of receivers
	 * @param echo			 echo back to transceiver
	 */
	public BasicChannel(String name, int maxNrofReceivers, boolean echo) {
		this.name = name;
		this.maxNrofReceivers = maxNrofReceivers;
		this.echo = echo;
	}


	/**
	 * Dispatches an object to all connected receivers.
	 *
	 * @param o the object to dispatch
	 */
	public void transmit(Object o) {
		transmit(o, null);
	}


	/**
	 * Dispatches an object to all connected receivers.
	 *
	 * @param o the object to dispatch
	 * @param t the transceiver sending the object
	 */
	public synchronized void transmit(Object o, ReceiverQueue t) {
		if (!closed) {
			ArrayList<Receiver> toBeRemoved = new ArrayList<Receiver>();
			for (Receiver r : receivers) {
				//cleanup
				//TODO separate cleanup from transmit
				//
				if (r instanceof ReceiverQueue && ((ReceiverQueue)r).isClosed()) {
					toBeRemoved.add(r);
				}
				else {
					if (echo || r != t) {
						r.onReceive(o);
					}
				}
			}
			for (Receiver r : toBeRemoved) {
				receivers.remove(r);
			}
		}
	}


	/**
	 * @return the number of listening receivers
	 */
	public int getNrofReceivers() {
		return receivers.size();
	}

	/**
	 * Creates a receiver for this channel.
	 *
	 * @return a receiver
	 */
	public ReceiverQueue createReceiver() {
		return createReceiver(0);
	}


	/**
	 * Creates a receiver for this channel.
	 *
	 * @param limit the maximum number of objects stored in the receiver queue
	 * @return a receiver
	 */
	public ReceiverQueue createReceiver(int limit) {
		synchronized (receivers) {
			ReceiverQueue q = null;
			if (!closed && (maxNrofReceivers == 0 || receivers.size() < maxNrofReceivers)) {
				q = new ReceiverQueue(/*this, */limit);
				receivers.add(q);
			}
			return q;
		}
	}

	/**
	 * Adds a receiver so that it will receive transmitted messages.
	 *
	 * @param receiver
	 * @return the registered receiver for convenience or null if the channel is closed or the maximum
	 *         number of receivers is reached
	 */
	public Receiver registerReceiver(Receiver receiver) {
		synchronized (receivers) {
			if (!closed && (maxNrofReceivers == 0 || receivers.size() < maxNrofReceivers)) {
				receivers.add(receiver);
			}
			else {
				return null;
			}
			return receiver;
		}
	}


	/**
	 * Closes the channel and all receivers.
	 */
	public void close() {
		synchronized (receivers) {
			Iterator i = receivers.iterator();
			while (i.hasNext()) {
				ReceiverQueue r = (ReceiverQueue) i.next();
				r.onTransmissionClose();
			}
			closed = true;
			receivers.clear();
		}
	}


	/**
	 * @return true if the channel is closed
	 */
	public boolean isClosed() {
		return closed;
	}


	/**
	 * @return the name of the channel
	 */
	public String getName() {
		return name;
	}


	/**
	 * Returns a string representation of a channel object including all connected receivers and queue sizes.
	 *
	 * @return a string representation of a channel object
	 */
	public String toString() {
		StringBuffer result = new StringBuffer("Pipe: " + name + "\n");
		synchronized (receivers) {
			Iterator i = receivers.iterator();
			while (i.hasNext()) {
				ReceiverQueue r = (ReceiverQueue) i.next();
				result.append("- r: " + r.getNrofQueuedMessages() + " queued\n");
			}
			return result.toString();
		}
	}
}
