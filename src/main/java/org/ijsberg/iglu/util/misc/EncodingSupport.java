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
package org.ijsberg.iglu.util.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Helper class for encoding and decoding byte arrays.
 */
public abstract class EncodingSupport {
	//String containing 64 characters used for encoding
	public static final String base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	/**
	 * Converts a chunk of data into base 64 encoding.
	 *
	 * @param rawData
	 * @return
	 */
	public static String encodeBase64(byte[] rawData) {
		return encodeBase64(rawData, 0);
	}


	/**
	 * Converts a chunk of data into base 64 encoding.
	 *
	 * @param rawData
	 * @param lineLength optional length of lines in return value
	 * @return
	 */
	public static String encodeBase64(byte[] rawData, int lineLength) {
		if (rawData == null) {
			return "";
		}
		StringBuffer retval = new StringBuffer();
		int i = 0;
		int n = 0;
		for (; i < rawData.length - 2; i += 3) {
			if (lineLength > 0 && i > 0 && i % lineLength == 0) {
				retval.append("\n");
			}
			//n is a 32 bit number
			//shift all the way to left first to get rid of sign
			//  3.shift last 8 bits to left    2.shift next 8 bits left of first 1. start here with last 8 bits shifted into int
			n = (((rawData[i] << 24) >>> 8) + ((rawData[i + 1] << 24) >>> 16) + ((rawData[i + 2] << 24) >>> 24));
			//this results in a 24 bit number (stored in 32 bit int)
			//of which 4 chunks of 6 bits are used to pick characters of base64Chars
			retval.append(base64Chars.charAt((n >>> 18) & 63));
			retval.append(base64Chars.charAt((n >>> 12) & 63));
			retval.append(base64Chars.charAt((n >>> 6) & 63));
			retval.append(base64Chars.charAt(n & 63));
		}
		//finish according to spec
		if (i + 1 == rawData.length) {
			n = ((rawData[i] << 24) >>> 8);

			retval.append(base64Chars.charAt((n >>> 18) & 63));
			retval.append(base64Chars.charAt((n >>> 12) & 63));
			retval.append("==");
		}
		if (i + 2 == rawData.length) {
			n = ((rawData[i] << 24) >>> 8) + ((rawData[i + 1] << 24) >>> 16);

			retval.append(base64Chars.charAt((n >>> 18) & 63));
			retval.append(base64Chars.charAt((n >>> 12) & 63));
			retval.append(base64Chars.charAt((n >>> 6) & 63));
			retval.append('=');
		}

		return retval.toString();
	}

	/**
	 * Decodes base 64 encoded string.
	 *
	 * @param encodedData
	 * @return
	 */
	public static byte[] decodeBase64(String encodedData) {
		BufferedReader reader = new BufferedReader(new StringReader(encodedData));
		int length = encodedData.length();
		byte[] retval = new byte[length];
		int actualLength = 0;

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				byte[] rawData = line.getBytes();


				int n = 0;
				for (int i = 0; i < rawData.length; i += 4) {
					n = (base64Chars.indexOf(rawData[i]) << 18) |
							(base64Chars.indexOf(rawData[i + 1]) << 12);

					retval[actualLength++] = (byte) ((n >>> 16) & 255);

					if (rawData[i + 2] != '=') {
						n |= (base64Chars.indexOf(rawData[i + 2]) << 6);
						retval[actualLength++] = (byte) ((n >>> 8) & 255);
					}
					if (rawData[i + 3] != '=') {
						n |= (base64Chars.indexOf(rawData[i + 3]));
						retval[actualLength++] = (byte) (n & 255);
					}
				}
			}
		}
		catch (IOException ioe) {
			throw new IllegalStateException("exception while reading input with message: " + ioe);
		}
		if (actualLength != length) {
			byte[] actualRetval = new byte[actualLength];
			System.arraycopy(retval, 0, actualRetval, 0, actualLength);
			return actualRetval;
		}
		return retval;
	}
}
