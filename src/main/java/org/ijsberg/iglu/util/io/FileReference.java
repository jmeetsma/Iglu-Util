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

import java.io.File;

/**
 */
public class FileReference {
	private File file;
	long lastModified;

	public FileReference(String fileName) {
		this.file = new File(fileName);
		reset();
	}

	public FileReference(File file) {
		this.file = file;
		reset();
	}

	public boolean isModified() {
		return file.lastModified() > lastModified;
	}

	public void reset() {
		lastModified = file.lastModified();
	}

	public String toString() {
		return file.getName();
	}

	public boolean equals(Object other) {
		return (other instanceof FileReference && ((FileReference) other).file.getAbsolutePath().equals(file.getAbsolutePath()));
	}

	public int hashCode() {
		return file.getAbsolutePath().hashCode();
	}
}
