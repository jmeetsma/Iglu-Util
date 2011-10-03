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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/**
 * Takes a configured In- and OutputStream such as System.in and System.out
 * which are used as standard in- and output for an underlying process
 * such as a command shell.
 */
public class StdIODialog implements Receiver, Runnable {
	private PushbackInputStream stdIn;
	private OutputStream stdOut;
	private Transceiver transceiver;
	private boolean closed;

	/**
	 * Constructs dialog for an object transceiver.
	 *
	 * @param in
	 * @param out
	 * @param transceiver
	 */
	public StdIODialog(InputStream in, OutputStream out, Transceiver transceiver) {
		this.stdIn = new PushbackInputStream(in);
		this.stdOut = out;

		this.transceiver = transceiver;
	}

	/**
	 * Starts communicating in a separate thread.
	 */
	public void openAsync() throws IOException {
		transceiver.open(this);
		new Thread(this).start();
	}

	/**
	 * Starts communicating.
	 * This method blocks until the dialog or the underlying (transceiver) process is closed.
	 */
	public void open() throws IOException {
		transceiver.open(this);
		run();
	}

	/**
	 * Closes dialog and (transceiver) process.
	 */
	public void close() {
		if (!closed) {
			try {
				stdOut.write("close of dialog forced...".getBytes());
			}
			catch (IOException ioe) {
				//..
			}
			transceiver.close();
		}
	}

	/**
	 * Starts communicating.
	 * This method blocks until the dialog or the underlying (transceiver) process is closed.
	 */
	public void run() {
		byte[] buffer = new byte[80];
		byte[] message;
		int count = 0;

		while (!closed && count >= 0) {
			try {
				//statement blocks
				count = stdIn.read(buffer);
			}
			catch (IOException ioe) {
				close();
			}
			if (count > 0) {
				message = new byte[count];
				System.arraycopy(buffer, 0, message, 0, count);
				if (!closed) {
					transceiver.transmit(message);
				}
				else {
					try {
						stdOut.write(("dialog closed: unable to transmit '" + new String(message).trim() + "'...").getBytes());
					}
					catch (IOException ioe) {
						//...
					}
				}
			}
		}
	}


	/**
	 * Writes received object to an output stream.
	 *
	 * @param o
	 */
	public void onReceive(Object o) {
		try {
			stdOut.write((byte[]) o);
		}
		catch (IOException ioe) {
			close();
		}
	}

	/**
	 * Enters closed state.
	 */
	public void onTransmissionClose() {
		//stop reading input stream
		closed = true;
/*		try
		{
			stdOut.write("closing dialog...".getBytes());
		}
		catch(IOException ioe)
		{
			//...
		}*/
	}
}


















