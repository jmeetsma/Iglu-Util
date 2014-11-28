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

package org.ijsberg.iglu.util.misc;

import java.io.*;

/**
 */
public class CloneSupport {

	public static <T> T cloneSerializable(T object) {
		System.out.println("start cloning");
		T clone = null;
		try
		{
			PipedOutputStream pipedOut = new PipedOutputStream();
			PipedInputStream pipedIn = new PipedInputStream(pipedOut);
			ObjectOutputStream out = new ObjectOutputStream(pipedOut);
			ObjectInputStream in = new ObjectInputStream(pipedIn);
			out.writeObject(object);
			out.flush();
			out.close();
			clone = (T)in.readObject();
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		System.out.println("done cloning");
		return clone;
	}

}
