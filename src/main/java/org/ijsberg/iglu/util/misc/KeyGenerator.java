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

import java.util.Random;

/**
 * Generates keys for storage and security purposes.
 */
public abstract class KeyGenerator {
	public static final char[][] codeArray = new char[][]
			{{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'},
					{'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'},
					{'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3'},
					{'4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd'},
					{'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n'},
					{'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x'}};

	private static final Random randomizer = new Random(System.currentTimeMillis());
	private static long lastnr;


	/**
	 * Generates a random key which is guaranteed to be unique within the application.
	 *
	 * @return
	 */
	public static synchronized String generateKey() {
		String code = "";

		long nr = System.currentTimeMillis();
		if (nr <= lastnr) {
			nr = lastnr + 1;
		}
		lastnr = nr;
		String s = String.valueOf(nr);
		for (int i = 0; i < s.length(); i++) {
			code += codeArray[randomizer.nextInt(6)][s.charAt(i) - 48];
		}
		return code;
	}
}

