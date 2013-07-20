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

package org.ijsberg.iglu.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;

/**
 * Copies a directory structure from test resources to temporary directory tmpDir.
 * Subclasses may use the directory structure to perform all kinds of tests.
 * 
 */
public abstract class DirStructureDependentTest {

	protected static final String BASE_RELATIVE_DIR_PATH = "org/ijsberg/iglu/util/io/directory structure/";
	protected static final String RELATIVE_DIR_PATH = BASE_RELATIVE_DIR_PATH + "root/";
	protected static File tmpDir;
	protected static String dirStructRoot;

	@Before
	public void setUp() throws Exception {

		tmpDir = FileSupport.createTmpDir("Iglu-Util-test");
		dirStructRoot = tmpDir.getPath() + '/';

		byte[] bytes = FileSupport.getBinaryFromClassLoader(BASE_RELATIVE_DIR_PATH + "files.txt");
		String fileList = new String(bytes);
		StringReader reader = new StringReader(fileList);
		BufferedReader bf = new BufferedReader(reader);
		String line;
		while((line = bf.readLine()) != null) {
			FileSupport.copyClassLoadableResourceToFileSystem(RELATIVE_DIR_PATH + line, dirStructRoot + "/" + line);
		}
	}

	@After
	public void tearDown() throws Exception {
		FileSupport.deleteFile(tmpDir);
	}
	

	

}