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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Helper class for stream related functions.
 */
public abstract class StreamSupport {
	/**
	 * Reads all bytes from an input stream.
	 *
	 * @param input
	 * @return
	 * @throws IOException
	 */
/*
	public static byte[] absorbInputStream(InputStream input) throws IOException
	{
		int bytesAvail = 0;
		int totalBytesRead = 0;
		int totalBytesCopied = 0;
		byte[] byteArray;
		ArrayList byteArraysRead = new ArrayList();

		byte[] buf = new byte[1024];
		while ((bytesAvail = input.read(buf)) > 0)
		{
			totalBytesRead += bytesAvail;
			byteArray = new byte[bytesAvail];
			System.arraycopy(buf, 0, byteArray, 0, bytesAvail);
			byteArraysRead.add(byteArray);
		}

		byte[] retval = new byte[totalBytesRead];
		Iterator i = byteArraysRead.iterator();
		while (i.hasNext())
		{
			byteArray = (byte[]) i.next();
			System.arraycopy(byteArray, 0, retval, totalBytesCopied, byteArray.length);
			totalBytesCopied += byteArray.length;
		}
		return retval;
	}
*/
	public static byte[] absorbInputStream(InputStream input) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int available = input.available();
		while (available > 0) {
			byte[] buf = new byte[available];
			int len = input.read(buf);
			out.write(buf, 0, len);
			available = input.available();
		}
		return out.toByteArray();
	}

}
