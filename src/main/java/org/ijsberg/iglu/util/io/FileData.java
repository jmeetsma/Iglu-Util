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

import org.ijsberg.iglu.util.mail.MimeTypeSupport;
import org.ijsberg.iglu.util.misc.EncodingSupport;
import org.ijsberg.iglu.util.misc.StringSupport;


//TODO it is not obvious that raw data needs to be set

/**
 * Is a transient carrier for a file stored in the file system.
 */
public class FileData {
	//contents
	private byte[] rawData;
	private String description = "";
	//
	private String fileName = "";
	private String fileNameNoExt = "";
	private String path = "";
	private String extension = "";
	private String mimeType = "";


	/**
	 */
	public FileData() {
	}

	/**
	 * @param fullFileName
	 */
	public FileData(String fullFileName) {
		setFullFileName(fullFileName);
	}


	/**
	 * Copy constructor.
	 * Creates a shallow copy of the original.
	 *
	 * @param fileData
	 */
	public FileData(FileData fileData) {
		rawData = fileData.rawData;
		description = fileData.description;
		//
		fileName = fileData.fileName;
		fileNameNoExt = fileData.fileNameNoExt;
		path = fileData.path;
		extension = fileData.extension;
		mimeType = fileData.mimeType;
	}

	/**
	 * @param fullFileName
	 * @param mimeType
	 */
	public FileData(String fullFileName, String mimeType) {
		setFullFileName(fullFileName);
		this.mimeType = mimeType;
	}


	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * Sets the file name including path and extension
	 *
	 * @param fullFileName
	 */
	public void setFullFileName(String fullFileName) {
		fullFileName = StringSupport.replaceAll(fullFileName, "\\", "/");
		fullFileName = StringSupport.replaceAll(fullFileName, "//", "/");
		int lastFileSeparator = fullFileName.lastIndexOf('/');

		if (lastFileSeparator != -1) {
			path = fullFileName.substring(0, lastFileSeparator);
		}
		else {
			path = "";
		}
		setFileName(fullFileName.substring(lastFileSeparator + 1, fullFileName.length()));
	}


	/**
	 * @return the data contained in the file
	 */
	public byte[] getRawData() {
		return rawData;
	}

	/**
	 * @return
	 */
	public String getRawDataBase64Encoded() {
		return EncodingSupport.encodeBase64(rawData, 57);
	}


	/**
	 * @param data
	 */
	public void setRawData(byte[] data) {
		this.rawData = data;
	}

	/**
	 * @param mimeType
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
		if (fileName.lastIndexOf('.') != -1) {
			extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
			fileNameNoExt = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		else {
			extension = "";
			fileNameNoExt = fileName;
		}
		setMimeType(MimeTypeSupport.getMimeTypeForFileExtension(extension));
	}

	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @return
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return
	 */
	public String getFileNameWithoutExtension() {
		return fileNameNoExt;
	}

	/**
	 * @return
	 */
	public String getFullFileName() {
		return (!"".equals(path) ? path + '/' : "") + fileName;

	}

	/**
	 * @return
	 */
	public int getSize() {
		if (rawData != null) {
			return rawData.length;
		}
		return 0;
	}

	/**
	 * @return a brief description
	 */
	public String toString() {
		return "file " + fileName + " (" + mimeType + ") size=" + getSize() + " bytes (" + description + ')';
	}
}
