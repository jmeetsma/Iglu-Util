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

import java.io.InputStream;
import java.io.IOException;

/**
 * Receives and retransmits objects asynchronously.
 */
public class Transponder implements Receiver {
	private Transmitter outputTransmitter;
	private InputStream inputStream;
	public static final long DEFAULT_POLLING_INTERVAL = 10;
	private long pollingInterval = DEFAULT_POLLING_INTERVAL;
	public static final int DEFAULT_QUEUE_SIZE = 0;//unlimited
	private int queueSize = DEFAULT_QUEUE_SIZE;
	public static final int DEFAULT_BUFFER_SIZE = 80;
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private ReceiverQueue receiverQueue;
	private Queue queue;
	private boolean haltRequest;
	protected Thread forwarderThread;


	/**
	 * Creates transponder with undefined input source.
	 * Input must be obtained by attaching transponder as receiver to a transmission source.
	 *
	 * @param outputTransmitter
	 */
	public Transponder(Transmitter outputTransmitter) {
		if (outputTransmitter == null) {
			throw new IllegalArgumentException("transmitter may not be null");
		}
		this.outputTransmitter = outputTransmitter;
		receiverQueue = new ReceiverQueue(queueSize);
		this.queue = receiverQueue;
		forwarderThread = new Thread(new Forwarder());
	}

	/**
	 * Creates transponder with a queue as input source.
	 *
	 * @param queue
	 * @param outputTransmitter
	 */
	public Transponder(Queue queue, Transmitter outputTransmitter) {
		if (queue == null) {
			throw new IllegalArgumentException("queue may not be null");
		}
		if (outputTransmitter == null) {
			throw new IllegalArgumentException("transmitter may not be null");
		}
		this.outputTransmitter = outputTransmitter;
		this.queue = queue;
		forwarderThread = new Thread(new Forwarder());
	}

	/**
	 * Creates transponder with a channel as input source.
	 *
	 * @param channel
	 * @param outputTransmitter
	 */
	public Transponder(Channel channel, Transmitter outputTransmitter) {
		if (channel == null) {
			throw new IllegalArgumentException("channel may not be null");
		}
		if (outputTransmitter == null) {
			throw new IllegalArgumentException("transmitter may not be null");
		}
		this.outputTransmitter = outputTransmitter;
		receiverQueue = new ReceiverQueue(queueSize);
		this.queue = receiverQueue;
		channel.registerReceiver(receiverQueue);
		forwarderThread = new Thread(new Forwarder());
	}


	/**
	 * Constructs a StreamReader that translates input to byte arrays
	 * which are forwarded to a transmitter.
	 * <p/>
	 * Note: transponder will not be able to read from alternate sources.
	 *
	 * @param inputStream
	 * @param outputTransmitter
	 */
	public Transponder(InputStream inputStream, Transmitter outputTransmitter) {
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream may not be null");
		}
		if (outputTransmitter == null) {
			throw new IllegalArgumentException("transmitter may not be null");
		}
		this.inputStream = inputStream;
		this.outputTransmitter = outputTransmitter;
		forwarderThread = new Thread(new Forwarder());
	}

	/**
	 * Polls the queue for new objects and retransmits them.
	 */
	private class Forwarder implements Runnable {
		/**
		 * Contains read loop.
		 */
		public void run() {
//			System.out.println("starting thread " + Thread.currentThread() + " for Transponder");
			while (!haltRequest) {
				if (inputStream != null) {
					try {
						byte[] buffer = new byte[bufferSize];
						int count = 0;
						while (count >= 0) {
							//TODO nullpointer deeper down encountered (arraycopy)
							count = inputStream.read(buffer);
							if (count > 0) {
								byte[] message = new byte[count];
								System.arraycopy(buffer, 0, message, 0, count);
								outputTransmitter.transmit(message);//deliver to transmitter
							}
							if (outputTransmitter.isClosed()) {
								stop();
							}
						}
						//loop is broken when count == -1 (inputsream closed)
					}
					catch (IOException e)//NullPointerException in FilterInpitStream.read() may occur instead of IOException
					{
						//abnormal termination
						outputTransmitter.transmit("stream can no longer be forwarded...".getBytes());
						stop();
					}
				}
				else {
					while (queue.available() > 0) {
						outputTransmitter.transmit(queue.read());
					}
					if (queue.isClosed()) {
						stop();
					}
					if (outputTransmitter.isClosed()) {
						stop();
					}
				}
				try {
					Thread.sleep(pollingInterval);
				}
				catch (InterruptedException ie) {
					stop();
				}
			}
			if (queue != null && !queue.isClosed()) {
				queue.close();
			}
			if (receiverQueue != null && !receiverQueue.isClosed()) {
				receiverQueue.close();
			}
			if (outputTransmitter != null && !outputTransmitter.isClosed()) {
				outputTransmitter.close();
			}
//			System.out.println("stopping thread " + Thread.currentThread() + " for Transponder");
		}
	}

	/**
	 * Starts forwarding objects.
	 */
	public void start() {
		forwarderThread.start();
	}

	/**
	 * Stops forwarding objects.
	 */
	public void stop() {
		haltRequest = true;
	}

	/**
	 * @param message
	 */
	public void onReceive(Object message) {
		if (receiverQueue == null) {
			throw new IllegalStateException("transponder is configured to retransmit messages from alternate source only");
		}
		receiverQueue.onReceive(message);
	}

	/**
	 *
	 */
	public void onTransmissionClose() {
		if (receiverQueue != null) {
			receiverQueue.onTransmissionClose();
		}
	}

}
