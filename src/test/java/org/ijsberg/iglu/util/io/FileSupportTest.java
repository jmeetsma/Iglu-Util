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


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.ijsberg.iglu.util.collection.CollectionSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileSupportTest extends DirStructureDependentTest {


	@Test
	public void testGetFilesInDirectoryTree() {

		File file = new File(dirStructRoot);
		

//		System.out.println(file.getAbsolutePath());
		assertTrue(file.exists());

		List<File> foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot);
		
		assertEquals(169, foundFiles.size());
		
//		CollectionSupport.print(foundFiles);

		String testDirPath = dirStructRoot + '/';

		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot);
		assertEquals(169, foundFiles.size());

	}

	@Test
	public void testGetFilesInDirectoryTreeWithMask() {

		File file = new File(dirStructRoot);
		

		assertTrue(file.exists());

		List<File> foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, "*/_d0/*");
		
		assertEquals(27, foundFiles.size());

		String testDirPath = dirStructRoot + '/';

		foundFiles = FileSupport.getFilesInDirectoryTree(testDirPath, "*.LOG");
		assertEquals(19, foundFiles.size());

	}


	@Test
	public void testGetFilesInDirectoryTreeWithRuleSet() {

		File file = new File(dirStructRoot);
		assertTrue(file.exists());
		
		FileFilterRuleSet ruleSet = new FileFilterRuleSet("*.LOG");
		
		List<File> foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);
		assertEquals(19, foundFiles.size());
		
		ruleSet.setIncludeFilesWithNameMask("*/_d0/*");
		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);
		assertEquals(27, foundFiles.size());
		
		ruleSet.setExcludeFilesWithNameMask("*.LOG");
		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);		
		assertEquals(26, foundFiles.size());
		
		
		ruleSet.setExcludeFilesWithNameMask("*.LOG|*.css");
		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);		
		assertEquals(25, foundFiles.size());

		ruleSet.setExcludeFilesWithNameMask("*.gif|*.jpg");
		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);		
		assertEquals(2, foundFiles.size());

		ruleSet.setIncludeFilesContainingLineMask("*ijsberg*");
		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);		
		assertEquals(1, foundFiles.size());

		ruleSet.setIncludeFilesContainingLineMask("*title*");
		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);		
		assertEquals(2, foundFiles.size());

		ruleSet.setExcludeFilesContainingLineMask("*ijsberg*");
		foundFiles = FileSupport.getFilesInDirectoryTree(dirStructRoot, ruleSet);		
		assertEquals(1, foundFiles.size());
		
		assertEquals("ijsberg.css", foundFiles.get(0).getName());
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

		//a dir can be loaded
		input = FileSupport.getInputStreamFromClassLoader(RELATIVE_DIR_PATH + "WWW");
		//(input.available produces NullPointer on Apple)
		
		input = FileSupport.getInputStreamFromClassLoader(RELATIVE_DIR_PATH + "WWW/route.gif");
		
		byte[] thing = StreamSupport.absorbInputStream(input);

		input.close();
	}

	@Test
	public void testCopyClassLoadableResourceToFileSystem() throws IOException{

		int nrofFilesInTmpDir = tmpDir.listFiles().length;
		FileSupport.copyClassLoadableResourceToFileSystem("iglu_logo_ice.gif", tmpDir.getPath() + "/iglu_logo.gif");
		assertEquals(nrofFilesInTmpDir + 1, tmpDir.listFiles().length);
		assertEquals("iglu_logo.gif", tmpDir.listFiles()[1].getName());

		FileSupport.copyClassLoadableResourceToFileSystem("iglu_logo_ice.gif", tmpDir.getPath());
		assertEquals(nrofFilesInTmpDir + 2, tmpDir.listFiles().length);

		assertTrue(new File(tmpDir.getPath() + "/iglu_logo_ice.gif").exists());
	}

	@Test
	public void testGetDirNameFromPath() throws Exception {
		assertEquals("/hop/", FileSupport.getDirNameFromPath("/hop/la"));
		assertEquals("/hop/", FileSupport.getDirNameFromPath("/hop/"));
		assertEquals("/hop/", FileSupport.getDirNameFromPath("/hop/la"));
		assertEquals("/", FileSupport.getDirNameFromPath("/hop"));
		assertEquals("", FileSupport.getDirNameFromPath("hop"));
	}

	
}
