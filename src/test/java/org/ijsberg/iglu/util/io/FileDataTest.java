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

import junit.framework.TestCase;

public class FileDataTest extends TestCase {
	private FileData filedata;

	/**
	 */
	public void setUp() {
		System.out.println("setting up test environment for class org.ijsberg.iglu.util.io.FileData");
		System.gc();
		System.out.println("Total memory " + (Runtime.getRuntime().totalMemory() / Math.pow(2, 20)) + " Mb");
		System.out.println("Memory use " + Math.floor(((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().totalMemory())) + " %");
		filedata = new FileData("random test string", "random test string");
	}

	/**
	 */
	public void tearDown() {
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#toString()
	 */
	public void testToString() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.toString()
		try {
			java.lang.String retval = filedata.toString();
			assertEquals("file random test string (random test string) size=0 bytes ()", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: 'file random test string (random test string) size=0 bytes ()', instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getPath()
	 */
	public void testGetPath() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.getPath()
		try {
			java.lang.String retval = filedata.getPath();
			assertEquals("", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: '', instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getSize()
	 */
	public void testGetSize() {

		//tests for signature public int org.ijsberg.iglu.util.io.FileData.getSize()
		try {
			int retval = filedata.getSize();
			assertEquals(0, retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: 0, instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getFileName()
	 */
	public void testGetFileName() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.getFileName()
		try {
			java.lang.String retval = filedata.getFileName();
			assertEquals("random test string", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: 'random test string', instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#setDescription(java.lang.String param1)
	 */
	public void testSetDescription() {

		//tests for signature public void org.ijsberg.iglu.util.io.FileData.setDescription(java.lang.String)
		try {
			filedata.setDescription(";k,lqw[965* \n ^$3445//''@");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setDescription("random test string");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setDescription(null);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#setFullFileName(java.lang.String param1)
	 */
	public void testSetFullFileName() {

		//tests for signature public void org.ijsberg.iglu.util.io.FileData.setFullFileName(java.lang.String)
		try {
			filedata.setFullFileName(";k,lqw[965* \n ^$3445//''@");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setFullFileName("random test string");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setFullFileName(null);
			fail("expected : Throwable: java.lang.NullPointerException (message: null)");
		}
		catch (java.lang.NullPointerException expectedThrowable) {
			//expected
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected java.lang.NullPointerException to be thrown (with message: null) instead of " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getRawData()
	 */
	public void testGetRawData() {

		//tests for signature public byte[] org.ijsberg.iglu.util.io.FileData.getRawData()
		try {
			byte[] retval = filedata.getRawData();
			assertNull("result should be null but is: " + retval + "", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: null, instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getRawDataBase64Encoded()
	 */
	public void testGetRawDataBase64Encoded() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.getRawDataBase64Encoded()
		try {
			java.lang.String retval = filedata.getRawDataBase64Encoded();
			assertEquals("", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: '', instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#setRawData(byte[] param1)
	 */
	public void testSetRawData() {

		//tests for signature public void org.ijsberg.iglu.util.io.FileData.setRawData(byte[])
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#setMimeType(java.lang.String param1)
	 */
	public void testSetMimeType() {

		//tests for signature public void org.ijsberg.iglu.util.io.FileData.setMimeType(java.lang.String)
		try {
			filedata.setMimeType("random test string");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setMimeType(null);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setMimeType(";k,lqw[965* \n ^$3445//''@");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#setFileName(java.lang.String param1)
	 */
	public void testSetFileName() {

		//tests for signature public void org.ijsberg.iglu.util.io.FileData.setFileName(java.lang.String)
		try {
			filedata.setFileName(";k,lqw[965* \n ^$3445//''@");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setFileName(null);
			fail("expected : Throwable: java.lang.NullPointerException (message: null)");
		}
		catch (java.lang.NullPointerException expectedThrowable) {
			//expected
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected java.lang.NullPointerException to be thrown (with message: null) instead of " + t.getClass().getName());
		}
		try {
			filedata.setFileName("random test string");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#setPath(java.lang.String param1)
	 */
	public void testSetPath() {

		//tests for signature public void org.ijsberg.iglu.util.io.FileData.setPath(java.lang.String)
		try {
			filedata.setPath(null);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setPath(";k,lqw[965* \n ^$3445//''@");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
		try {
			filedata.setPath("random test string");
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("did not expect exception " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getMimeType()
	 */
	public void testGetMimeType() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.getMimeType()
		try {
			java.lang.String retval = filedata.getMimeType();
			assertEquals("random test string", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: 'random test string', instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getExtension()
	 */
	public void testGetExtension() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.getExtension()
		try {
			java.lang.String retval = filedata.getExtension();
			assertEquals("", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: '', instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getFileNameWithoutExtension()
	 */
	public void testGetFileNameWithoutExtension() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.getFileNameWithoutExtension()
		try {
			java.lang.String retval = filedata.getFileNameWithoutExtension();
			assertEquals("random test string", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: 'random test string', instead of Throwable: " + t.getClass().getName());
		}
	}

	/**
	 * @see org.ijsberg.iglu.util.io.FileData#getFullFileName()
	 */
	public void testGetFullFileName() {

		//tests for signature public java.lang.String org.ijsberg.iglu.util.io.FileData.getFullFileName()
		try {
			java.lang.String retval = filedata.getFullFileName();
			assertEquals("random test string", retval);
		}
		catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) throw (junit.framework.AssertionFailedError) t;
			fail("expected return value: 'random test string', instead of Throwable: " + t.getClass().getName());
		}
	}
}

