/*
 * Copyright 2011-2013 Jeroen Meetsma - IJsberg
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import org.junit.Test;

/**
 */
public class ExecutableTest {
	public Object sleepAWhile(long time) {
		try {
			System.out.println("about to sleep " + time + "ms");
			Thread.sleep(time);
			System.out.println("waking up");
		}
		catch (InterruptedException ie) {
			System.out.println("interrupted");
			return "interrupted";
		}
		return "done";
	}

	public Object doStuff() {
		return "done";
	}

	public void abortSleep() {
		System.out.println("Tester aborted");
	}


	@Test
	public void testExecuteTimed() throws Throwable {
		Executable exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};
		try {
			Object result = exec.executeTimed(200);
			fail("execution should result in a TimeOutException");
		}
		catch (TimeOutException toe) {
			//expected
		}

		try {
			Object result = exec.executeTimed(200);
			fail("should not be able to call executor twice");
		}
		catch (IllegalStateException ere) {
			//expected
		}

		exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};
		Object result = exec.executeTimed(700);
		assertEquals("done", result);
	}


	public void testExecuteAsync() throws Throwable {
		Executable exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};

		exec.executeAsync();
		Thread.sleep(300);
		assertNull("call should not be finished", exec.getReturnValue());

		exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};
		exec.executeAsync();
		Thread.sleep(600);
		assertEquals("done", exec.getReturnValue());

		try {
			exec.executeAsync();
			fail("should not be able to call executor twice");
		}
		catch (IllegalStateException ere) {
			//expected
		}


		exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};
		exec.executeAsync();
		Thread.sleep(200);
		exec.interrupt();
		Thread.sleep(400);
		assertNull("call should not be finished", exec.getReturnValue());
	}


	public void testExecuteAsyncTimed() throws Throwable {
		Executable exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};

		exec.executeAsyncTimed(200);
		Thread.sleep(300);
		assertTrue(exec.getExecutionException() instanceof TimeOutException);

		try {
			exec.executeAsyncTimed(200);
			fail("should not be able to call executor twice");
		}
		catch (IllegalStateException ere) {
			//expected
		}

		exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};
		exec.executeAsyncTimed(700);
		Thread.sleep(600);
		assertEquals("done", exec.getReturnValue());

		exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};
		exec.executeAsyncTimed(700);
		Thread.sleep(200);
		assertTrue("asynchronous call must not be finished", exec.getReturnValue() == null);

		exec = new Executable() {
			protected Object execute() throws Throwable {
				return sleepAWhile(500);
			}
		};
		System.out.print("111");
		exec.executeAsyncTimed(700);
		System.out.print("222");
		Thread.sleep(200);
		exec.interrupt();
		Thread.sleep(200);
		assertTrue("executor aborted after 200 ms, so timeout must have occured", exec.getExecutionException() instanceof TimeOutException);
	}

	public void testExecuteAsyncDelayed() throws Throwable {
		Executable exec = new Executable() {
			protected Object execute() throws Throwable {
				return doStuff();
			}
		};

		exec.executeAsyncDelayed(200);
		Thread.sleep(100);
		assertNull(exec.getReturnValue());
		Thread.sleep(300);
		assertEquals("done", exec.getReturnValue());
	}
}
