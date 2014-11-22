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
