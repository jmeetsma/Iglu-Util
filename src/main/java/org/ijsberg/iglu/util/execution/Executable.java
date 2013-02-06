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

package org.ijsberg.iglu.util.execution;

/**
 * Subclasses implement a piece of code that should be executed in a special way, such as:
 * <ul>
 * <li>asynchronous</li>
 * <li>within a specified amount of time</li>
 * <li>after a specified amount of time</li>
 * </ul>
 * Executables are (usually) stateful and should be instantiated for each and every call.
 * It's convenient to define executables as anonymous inner classes.
 * E.g.:
 * <code>
 * new Executable(){protected Object execute(){System.out.println("this message is printed asynchronously");return null;}}.executeAsync();
 * </code>
 */
public abstract class Executable implements Runnable {

	private boolean finished;
	private boolean aborted;
	private Throwable execException;
	private Thread executeThread;
	private Object retval;

	/**
	 */
	public Executable() {
	}


	/**
	 * Used to invoke executable asynchronously.
	 */
	public void run() {
		try {
			Object result = execute();
			if (!aborted) {
				this.retval = result;
				finished = true;
			}
		}
		catch (InterruptedException ie) {
			//
		}
		catch (Throwable t) {
			execException = t;
			finished = true;
		}
	}

	/**
	 * Executes asynchronously.
	 */
	public void executeAsync() {
		if (executeThread != null) {
			throw new IllegalStateException("use executable once per call");
		}
		executeThread = new Thread(this);
		executeThread.start();
	}


	/**
	 * Executes asynchronously.
	 * After <emph>timeout</emph> ms the abort method is invoked if the executable has not finished processing
	 *
	 * @param timeout timeout value in milliseconds
	 */
	public void executeAsyncTimed(final long timeout) {
		if (executeThread != null) {
			throw new IllegalStateException("use executable once per call");
		}
		final Executable nestedExec = this;
		Executable exec = new Executable() {
			public Object execute() throws Throwable {
				return nestedExec.executeTimed(timeout);
			}
		};
		exec.executeAsync();
	}


	/**
	 * Executes after <emph>delay</emph> ms.
	 *
	 * @param delay
	 */
	public void executeAsyncDelayed(final long delay) {
		final Executable nestedExec = this;
		Executable exec = new Executable() {
			public Object execute() throws Throwable {
				return nestedExec.executeDelayed(delay);
			}
		};
		exec.executeAsync();
	}

	/**
	 * Executes synchronously.
	 * After <emph>timeout</emph> ms the abort method is invoked if the executable has not finished processing.
	 *
	 * @param timeout timeout value in milliseconds
	 * @return
	 * @throws TimeOutException	 in case the call is timed out
	 * @throws InterruptedException if execution was interrupted before timeout
	 * @throws Throwable			in case the executable throws
	 */
	public Object executeTimed(long timeout) throws TimeOutException, InterruptedException, Throwable {
		//TODO this also executes asynchronously, yet the current thread waits for it to execute
		executeAsync();
		executeThread.join(timeout);
		if (execException == null && !finished) {
			interrupt();
			if (!finished)//execution could still have been finished by now
			{
				execException = new TimeOutException("execution timed out (" + timeout + "ms)");
			}
		}
		if (execException != null) {
			throw execException;
		}
		return retval;
	}

	/**
	 * @param delay
	 * @return
	 * @throws TimeOutException
	 * @throws Throwable
	 */
	public Object executeDelayed(long delay) throws TimeOutException, Throwable {
		Thread.sleep(delay);
		return executeTimed(0);
	}

	/**
	 * Implements code to be executed.
	 *
	 * @return
	 * @throws Throwable
	 */
	protected abstract Object execute() throws Throwable;


	/**
	 * Tries to interrupt execution.
	 * Execution code is interrupted if it sleeps once in a while.
	 */
	public void interrupt() {
		if (!aborted) {
			aborted = true;
			if (executeThread != null) {
				executeThread.interrupt();
			}
		}
	}

	/**
	 * Joins execution thread
	 */
	public void waitUntilFinished() throws InterruptedException {
		if (executeThread != null) {
			executeThread.join();
		}
	}

	/**
	 * @return the (runtime) exception that may have occurred during execution
	 */
	public Throwable getExecutionException() {
		return execException;
	}


	/**
	 * @return true if execution has been finished (un)successfully
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @return
	 */
	public Thread getExecutionThread() {
		return executeThread;
	}

	/**
	 * @return
	 */
	public Object getReturnValue() {
		return retval;
	}
}
