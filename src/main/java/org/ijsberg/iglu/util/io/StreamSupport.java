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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class for stream related functions.
 */
public abstract class StreamSupport {

	public static int BUF_SIZE = 100000;

	/**
	 * Reads all bytes from an input stream.
	 *
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] absorbInputStream(InputStream input) throws IOException {
		ByteArrayOutputStream output = null;
		try{
			output = new ByteArrayOutputStream();
			absorbInputStream(input, output);
			return output.toByteArray();
		} finally {
			output.close();
		}
	}

	/**
	 *
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void absorbInputStream(InputStream input, OutputStream output) throws IOException {
		int available;
		while ((available = input.available()) > 0) {
			byte[] buf = new byte[available < BUF_SIZE ? available : BUF_SIZE];
			int len = input.read(buf);
			output.write(buf, 0, len);
		}
	}
}
