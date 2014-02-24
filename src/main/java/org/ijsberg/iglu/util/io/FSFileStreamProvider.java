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

import java.io.*;
import java.util.Map;

/**
 */
public class FSFileStreamProvider implements FileStreamProvider {

	private OutputStream currentStream;
	private String path;

	public FSFileStreamProvider(String path) {
		this.path = path;
	}

	@Override
	public PrintStream createPrintStream(String fileName) {
		return new PrintStream(createOutputStream(fileName));
	}

	@Override
	public OutputStream createOutputStream(String fileName) {
		try {
			File file = FileSupport.createFile(path + "/"+ fileName);
			return currentStream = new FileOutputStream(file);
		} catch (IOException ioe) {
			throw new RuntimeException("cannot save to file " + fileName, ioe);
		}
	}

	@Override
	public void closeCurrentStream() {
		if(currentStream != null) {
			try {
				currentStream.close();
			} catch (IOException ioe) {
				throw new RuntimeException("cannot close output stream", ioe);
			}
		}
	}


	@Override
	public void close() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
