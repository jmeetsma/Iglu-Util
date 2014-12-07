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

package org.ijsberg.iglu.util.execution;

import org.ijsberg.iglu.util.collection.ArraySupport;
import org.ijsberg.iglu.util.io.*;
import org.ijsberg.iglu.util.misc.StringSupport;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Tries to start a command shell in the OS and
 * establishes standard in- and output.
 * <p/>
 * Input may be passed by invoking transmit().
 * Output can be obtained by passing registering a receiver
 * by invoking open(Listener).
 */
public class CommandShell implements Transceiver {
	//shell process
	private Process proc;
	//output receiver
	private Receiver receiver;
	//output retrieval and forwarding
	private Transponder errorForwarder;
	private Transponder outputForwarder;

	private String[] commandArray;
	private File workingDir;

	private boolean isClosed;

	private StringBuffer command = new StringBuffer();

	/**
	 * @param shellCommandArray
	 * @param alternativeEnvVars
	 * @param workingDir
	 * @see Runtime#exec(String[], String[], File) exec()
	 */
	public CommandShell(String[] shellCommandArray, String[] alternativeEnvVars, File workingDir) {
		commandArray = shellCommandArray;
		String[] envVars = alternativeEnvVars;
		this.workingDir = workingDir;
	}

	/**
	 * @param shellCommand
	 * @param alternativeEnvVars
	 * @param workingDir
	 */
	public CommandShell(String shellCommand, String[] alternativeEnvVars, File workingDir) {
		this(new String[]{shellCommand}, alternativeEnvVars, workingDir);
	}

	/**
	 * @param shellCommand
	 * @param workingDir
	 */
	public CommandShell(String shellCommand, File workingDir) {
		this(new String[]{shellCommand}, null, workingDir);
	}

	/**
	 * @param shellCommandArray
	 * @param workingDir
	 */
	public CommandShell(String[] shellCommandArray, File workingDir) {
		this(shellCommandArray, null, workingDir);
	}

	/**
	 * Transmits
	 *
	 * @param message
	 */
	public void transmit(Object message) {
		try {
			boolean commandFlushed = false;
			byte[] b = (byte[]) message;
			for (int i = 0; i < b.length; i++) {
				byte b2 = b[i];
				if ((b2 == 10 || b2 == 13) && !commandFlushed) //NEWLINE / RETURN
				{
					commandFlushed = true;
					if ("exit".equals(command.toString().trim())) {
						close();
					}
					else {
						command.append((char) 10);//.append((char)13);
						proc.getOutputStream().write(command.toString().getBytes());
						proc.getOutputStream().flush();
						command = new StringBuffer();
					}
				}
				else// if (b2 != 13)
				{
					command = command.append((char) b2);
				}
			}
		}
		catch (IOException e) {
//			e.printStackTrace();
			close();
		}
	}

	/**
	 *
	 */
	public void close() {
		if (receiver != null) {
			receiver.onTransmissionClose();
		}
		if (proc != null) {
			proc.destroy();
			errorForwarder.stop();
			outputForwarder.stop();
		}
		isClosed = true;
	}

	/**
	 * @return
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * @return
	 */
	public static int available() {
		return 0;
	}

	/**
	 * @return
	 */
	public static Object read() {
		return null;
	}

	/**
	 *
	 */
	public static class NewLineFilter implements Filter<byte[]> {
		public byte[] filter(byte[] input) {
			StringBuffer temp = new StringBuffer(new String(input));
			StringSupport.replaceAll(temp, "\r\n", "\n");
			StringSupport.replaceAll(temp, "\r", "\n");
			StringSupport.replaceAll(temp, "\n", "\r\n");
			return temp.toString().getBytes();
		}
	}

	/**
	 * Opens permanent shell
	 *
	 * @param receiver
	 * @return
	 */
	public Receiver open(Receiver receiver)// throws IOException
	{
		this.receiver = receiver;

		Runtime rt = Runtime.getRuntime();
		//command + arguments, environment parameters, workingdir
		try {
			proc = rt.exec(commandArray, null, workingDir);
		}
		catch (IOException e) {
			throw new RuntimeException("can not start command shell [" + ArraySupport.format(commandArray, ",") + "] + in directory " + workingDir);
		}

		// any error message?
		errorForwarder = new
				Transponder(proc.getErrorStream(), new Pipe(receiver, new NewLineFilter()));

		// any output?
		outputForwarder = new
				Transponder(proc.getInputStream(), new Pipe(receiver, new NewLineFilter()));

		errorForwarder.start();
		outputForwarder.start();

		return receiver;
	}

	/**
	 * execute particular command
	 *
	 * @return
	 * @throws IOException
	 */
	public static int execute(String[] commandArray, String[] alternativeEnvVars, File workingDir, Receiver outputReceiver) throws IOException {
		Runtime rt = Runtime.getRuntime();
		//command + arguments, environment parameters, workingdir
        Process proc = null;
        try {
		    proc = rt.exec(commandArray, alternativeEnvVars, workingDir);
        } catch (Exception e) {
            System.out.println("unable to execute command: " + ArraySupport.format(commandArray, ", "));
            e.printStackTrace();
            throw new RuntimeException("unable to execute command: " + ArraySupport.format(commandArray, ", "), e);
        }

		// any error message?
		Transponder errorForwarder = new
				Transponder(proc.getErrorStream(), new Pipe(outputReceiver));

		// any output?
		Transponder outputForwarder = new
				Transponder(proc.getInputStream(), new Pipe(outputReceiver));

		errorForwarder.start();
		outputForwarder.start();

		try {
			int retval = proc.waitFor();//threads will stop when proc is done and InputStream and ErrorStream close
			errorForwarder.stop();
			outputForwarder.stop();
			return retval;
		}
		catch (InterruptedException ie) {
			//...
		}
		return -1;
	}

	/**
	 * @param command
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static int execute(String command, File dir) throws IOException {
		return execute(command, null, dir);
	}

	/**
	 * Blindly invokes a shell command on Windows or Unix systems.
	 * It seems that the command is not executed asynchronously
	 * Note: this command will hang if user input is required.
	 *
	 *
	 * @param command
	 * @param dir
	 * @return status code
	 */
	public static int execute(String command, String[] envVars, File dir, Receiver outputReceiver) throws IOException {
		return execute(getCommandArrayForCurrentOS(command), envVars, dir, outputReceiver);
	}

	public static int execute(String command, String[] envVars, File dir) throws IOException {
		return execute(getCommandArrayForCurrentOS(command), envVars, dir, new Forwarder(System.out));
	}

	private static String[] getCommandArrayForCurrentOS(String command) {
		String osName = System.getProperty("os.name");
		String[] cmd;
		if ("Windows 95".equals(osName)) {
			cmd = new String[3];
			cmd[0] = "command.com";
			cmd[1] = "/C";//shell will stop after carrying out command
			cmd[2] = command;
		}
		else if (osName.startsWith("Windows")) {
			cmd = new String[3];
			cmd[0] = "cmd.exe";
			cmd[1] = "/C";//shell will stop after carrying out command
			cmd[2] = command;
		}
		else {
			cmd = new String[3];
			cmd[0] = "/bin/sh";
			cmd[1] = "-c";
			cmd[2] = command;
		}
		return cmd;
	}

	/**
	 * @param command
	 * @param rootdir
	 * @throws IOException
	 */
	public static void executeRecursive(String command, String rootdir) throws IOException {
		Collection dirs = FileSupport.getDirectoriesInDirectoryTree(rootdir);

		File root = new File(rootdir);
		System.out.println("EXECUTING " + command + " in dir " + root);
		if (!root.exists() || !root.isDirectory()) {
			throw new IllegalArgumentException("root dir '" + rootdir + "' not valid");
		}
		execute(command, new File(rootdir));

		Iterator i = dirs.iterator();
		while (i.hasNext()) {
			File file = (File) i.next();
			System.out.println("EXECUTING " + command + " in dir " + file);
			execute(command, file);
		}
	}

	/**
	 * Runs test dialog.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

//		StdIODialog dialog = new StdIODialog(System.in, System.out, new CommandShell("cmd.exe", new File("C:\\")));
		StdIODialog dialog = new StdIODialog(System.in, System.out, new CommandShell(new String[]{"cmd.exe"}, new File("./")));
		try {
			dialog.open();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
