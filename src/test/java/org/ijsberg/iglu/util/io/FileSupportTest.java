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


import org.ijsberg.iglu.util.collection.CollectionSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;

public class FileSupportTest {

	private static final String ROOT = "org/ijsberg/iglu/util/io/directory structure/root/";
	private File tmpDir;

	@Before
	public void setUp() throws Exception {
		tmpDir = FileSupport.createTmpDir("Iglu-Util-test");
	}

	@After
	public void tearDown() throws Exception {
//		tmpDir.delete();
	}

	@Test
	public void testGetFilesInDirectoryTree() {

		String testDirPath = "./src/test/resources/org/ijsberg/iglu/util/io/directory structure";
		File file = new File(testDirPath);

		System.out.println(file.getAbsolutePath());
		assertTrue(file.exists());

		List foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath);
		assertEquals(169, foundFiles.size());

		testDirPath += '/';

		foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath);
		assertEquals(169, foundFiles.size());

		foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath, "*.LOG");
		assertEquals(19, foundFiles.size());

	}

	@Test
	public void testGetFilesInDirectoryTreeForWindows() {
		String testDirPath = ".\\src\\test\\resources\\org\\ijsberg\\iglu\\util\\io\\directory structure";
		File file = new File(testDirPath);

		assertTrue(file.exists());

		List foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath);
		assertEquals(336, foundFiles.size());

		foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath, "*");
		assertEquals(336, foundFiles.size());

		testDirPath += '\\';

		foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath);
		assertEquals(336, foundFiles.size());

		foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath, "*");
		assertEquals(336, foundFiles.size());

		foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath, "*.LOG");
		assertEquals(19, foundFiles.size());

	}


	@Test
	public void testCreateTmpDir() throws Exception {
		File dir = FileSupport.createTmpDir();
		assertTrue(dir.isDirectory());
		assertTrue(dir.exists());
		assertTrue(dir.delete());
	}


	@Test
	public void getFileNameFromPath() throws Exception {
		assertEquals("TestFile.tst", FileSupport.getFileNameFromPath("/this/is/a/TestFile.tst"));
		assertEquals("TestFile.tst", FileSupport.getFileNameFromPath("/this/is/a//TestFile.tst"));
		assertEquals("TestFile.tst", FileSupport.getFileNameFromPath("TestFile.tst"));
		assertEquals("TestFile.tst", FileSupport.getFileNameFromPath("\\this\\is/a\\\\TestFile.tst"));
		try {
			FileSupport.getFileNameFromPath("/this/is/a/");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException expected) {}
	}



	@Test
	public void testGetInputStreamFromClassLoader() throws Exception{
		//NOTE: IDEs will not automatically regard any file as resource
		InputStream input = FileSupport.getInputStreamFromClassLoader("iglu_logo_ice.gif");
		try {
			FileSupport.getInputStreamFromClassLoader("not_existing.file");
			fail("IOException expected");
		} catch (IOException expected) {}

		input = FileSupport.getInputStreamFromClassLoader("test/ijsberg.jpg");

		//apparently a dir can be loaded
		input = FileSupport.getInputStreamFromClassLoader(ROOT + "WWW");

		byte[] thing = StreamSupport.absorbInputStream(input);
		System.out.println(new String(thing));

		input.close();
	}

	@Test
	public void testCopyClassLoadableResourceToFileSystem() throws IOException{

		//assertEquals(0, tmpDir.listFiles().length);
		//FileSupport.copyClassLoadableResourceToFileSystem("iglu_logo_ice.gif", tmpDir.getPath());
		//assertEquals(1, tmpDir.listFiles().length);
	}

}
