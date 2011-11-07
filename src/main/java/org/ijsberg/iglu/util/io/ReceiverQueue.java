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

import java.util.LinkedList;

/**
 * A message queue which receives objects from a transmitter.
 *
 * @see BasicChannel
 */
public class ReceiverQueue implements Receiver, Queue {
	private LinkedList queue = new LinkedList();
	private int limit;//unlimited queue size by default
	private boolean closed;

	/**
	 * Constructs a receiver with an unlimited queue size.
	 */
	public ReceiverQueue() {
	}

	/**
	 * Constructs a receiver with a limited queue size.
	 * If the queue size is exceeded, the first object is discarded.
	 *
	 * @param limit maximum queue size
	 */
	public ReceiverQueue(int limit) {
		this.limit = limit;
	}

	/**
	 * Receives an object from a channel.
	 *
	 * @param object
	 */
	public void onReceive(Object object) {
		if (!closed && object != null) {
			synchronized (queue) {
				queue.addLast(object);
				if (limit > 0 && queue.size() > limit && !queue.isEmpty()) {
					queue.removeFirst();
				}
			}
		}
	}


	/**
	 * Retrieves and removes the first object from the queue.
	 *
	 * @return the first object from the queue.
	 */
	public Object read() {
		synchronized (queue) {
			if (!queue.isEmpty()) {
				return queue.removeFirst();
			}
		}
		return null;
	}


	/**
	 * @return true if there are more objects waiting on the queue.
	 */
	public boolean hasNext() {
		return !queue.isEmpty();
	}


	/**
	 * Closes the receiver (for incoming messages) but does <em>not</em> clear the queue.
	 */
	public void close() {
		closed = true;
		//the receiver will eventually be removed from the channel
	}

	/**
	 * Queue is closed when transmission closes.
	 */
	public void onTransmissionClose() {
		close();
	}


	/**
	 * @return true if the receiver is closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * Clears the queue.
	 */
	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}


	/**
	 * @return queue size
	 */
	public int getNrofQueuedMessages() {
		return queue.size();
	}


	/**
	 * @return queue size
	 */
	public int available() {
		return queue.size();
	}

	/**
	 * @return maximum number of queued messages
	 */
	public int getLimit() {
		return limit;
	}
}
