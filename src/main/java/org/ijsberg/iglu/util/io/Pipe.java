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

/**
 * Transmits objects to a specific receiver.
 */
public class Pipe implements Transmitter {
	private Receiver receiver;
	private Filter filter;
	private boolean isClosed;


	/**
	 *
	 */
	public Pipe() {
	}

	/**
	 * @param receiver
	 */
	public Pipe(Receiver receiver) {
		if (receiver == null) {
			throw new IllegalArgumentException("receiver may not be null");
		}
		this.receiver = receiver;
	}

	/**
	 * @param receiver
	 * @param filter
	 */
	public Pipe(Receiver receiver, Filter filter) {
		if (receiver == null) {
			throw new IllegalArgumentException("receiver may not be null");
		}
		this.receiver = receiver;
		this.filter = filter;
	}

	/**
	 * @param object
	 */
	public void transmit(Object object) {
		if (isClosed) {
			throw new IllegalStateException("pipe is closed");
		}
		if (filter != null) {
			receiver.onReceive(filter.filter(object));
		}
		else {
			receiver.onReceive(object);
		}
	}

	/**
	 * @param receiver
	 */
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	/**
	 *
	 */
	public void close() {
		if (isClosed) {
			throw new IllegalStateException("pipe is closed");
		}
		receiver.onTransmissionClose();
		receiver = null;
		isClosed = true;
	}

	/**
	 * @return
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * @param filter
	 * @return
	 */
	public Filter addFilter(Filter filter) {
		this.filter = filter;
		return filter;
	}
}
