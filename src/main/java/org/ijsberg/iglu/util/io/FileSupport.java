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
import org.ijsberg.iglu.util.formatting.PatternMatchingSupport;
import org.ijsberg.iglu.util.misc.StringSupport;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Supports retrieval and deletion of particular files in a directory structure
 * as well as other file system manipulation.
 */
public abstract class FileSupport {
	/**
	 * Retrieves all files from a directory and its subdirectories.
	 *
	 * @param path path to directory
	 * @return A list containing the found files
	 */
	public static ArrayList getFilesInDirectoryTree(String path) {
		File directory = new File(path);
		return getContentsInDirectoryTree(directory, null, true, false);
	}


	/**
	 * Retrieves all files from a directory and its subdirectories.
	 *
	 * @param directory directory
	 * @return a list containing the found files
	 */
	public static ArrayList getFilesInDirectoryTree(File directory) {
		return getContentsInDirectoryTree(directory, null, true, false);
	}


	/**
	 * Retrieves files for a given mask from a directory and its subdirectories.
	 *
	 * @param path root of directory tree
	 * @param mask exact filename, or mask containing wildcards
	 * @return A list containing the found files
	 */
	public static ArrayList getFilesInDirectoryTree(String path, String mask) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, mask, true, false);
	}


	/**
	 * Retrieves all files from a directory and its subdirectories
	 * matching the given mask.
	 *
	 * @param file directory
	 * @param mask mask to match
	 * @return a list containing the found files
	 */
	public static ArrayList getFilesInDirectoryTree(File file, String mask) {
		return getContentsInDirectoryTree(file, mask, true, false);
	}


	/**
	 * Retrieves contents from a directory and its subdirectories matching a given mask.
	 *
	 * @param directory   directory
	 * @param mask		file name to match
	 * @param returnFiles return files
	 * @param returnDirs  return directories
	 * @return a list containing the found contents
	 */
	private static ArrayList getContentsInDirectoryTree(File directory, String mask, boolean returnFiles, boolean returnDirs) {
		ArrayList result = new ArrayList();
		if (directory != null && !directory.isFile()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						if (returnDirs && (mask == null || PatternMatchingSupport.valueMatchesWildcardExpression(files[i].getName(), mask))) {
							result.add(files[i]);
						}
						result.addAll(getContentsInDirectoryTree(files[i], mask, returnFiles, returnDirs));
					}
					else if (returnFiles && (mask == null || PatternMatchingSupport.valueMatchesWildcardExpression(files[i].getName(), mask))) {
						result.add(files[i]);
					}
				}
			}
		}
		return result;
	}


	/**
	 * Retrieves all directories from a directory and its subdirectories.
	 *
	 * @param path path to directory
	 * @return A list containing the found directories
	 */
	public static ArrayList getDirectoriesInDirectoryTree(String path) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, null, false, true);
	}


	/**
	 * Retrieves all directories from a directory and its subdirectories.
	 *
	 * @param path path to directory
	 * @param mask file name to match
	 * @return A list containing the found directories
	 */
	public static ArrayList getDirectoriesInDirectoryTree(String path, String mask) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, mask, false, true);
	}


	/**
	 * Retrieves all files from a directory and its subdirectories.
	 *
	 * @param path	   path to directory
	 * @param mask	   file name to match
	 * @param returnDirs indicates if directories must be returned as well
	 * @return a list containing the found files
	 */
	public static ArrayList getFilesInDirectoryTree(String path, String mask, boolean returnDirs) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, mask, true, returnDirs);
	}

	/**
	 * Tries to retrieve a class as File from all directories mentioned in system property java.class.path
	 *
	 * @param className class name as retrieved in myObject.getClass().getName()
	 * @return a File if the class file was found or null otherwise
	 */
	public static File getClassFileFromDirectoryInClassPath(String className) {
		String fileName = StringSupport.replaceAll(className, ".", "/");
		fileName += ".class";
		return getFileFromDirectoryInClassPath(fileName, System.getProperty("java.class.path"));
	}


	/**
	 * Locates a file in the classpath.
	 *
	 * @param fileName
	 * @param classPath
	 * @return the found file or null if the file can not be located
	 */
	public static File getFileFromDirectoryInClassPath(String fileName, String classPath) {

		Collection paths = StringSupport.split(classPath, ";:", false);

		Iterator i = paths.iterator();
		while (i.hasNext()) {
			String singlePath = (String) i.next();
			File dir = new File(singlePath);
			if (dir.isDirectory()) {
				File file = new File(singlePath + '/' + fileName);
				if (file.exists()) {
					return file;
				}
			}
		}
		return null;
	}

	/**
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBinaryFromFS(String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			InputStream in = new FileInputStream(file);
			try {
				return StreamSupport.absorbInputStream(new FileInputStream(file));
			}
			finally {
				in.close();
			}
		}
		throw new FileNotFoundException("file '" + fileName + "' does not exist");
	}

	/**
	 * Tries to retrieve a class as ZipEntry from all jars mentioned in system property java.class.path
	 *
	 * @param className class name as retrieved in myObject.getClass().getName()
	 * @return a ZipEntry if the class file was found or null otherwise
	 */
	public static ZipEntry getClassZipEntryFromJarInClassPath(String className) throws IOException {
		String fileName = StringSupport.replaceAll(className, ".", "/");
		fileName += ".class";

		Collection jars = StringSupport.split(System.getProperty("java.class.path"), ";:", false);
		Iterator i = jars.iterator();
		while (i.hasNext()) {
			String jarFileName = (String) i.next();
			if (jarFileName.endsWith(".jar") || jarFileName.endsWith(".zip")) {
				ZipEntry entry = getZipEntryFromJar(fileName, jarFileName);
				if (entry != null) {
					return entry;
				}
			}
		}
		return null;
	}

	/**
	 * Tries to retrieve a file as ZipEntry from all jars mentioned in system property java.class.path
	 *
	 * @param fileName class name as retrieved in myObject.getClass().getName()
	 * @return a ZipEntry if the class file was found or null otherwise
	 */
	public static byte[] getBinaryFromJar(String fileName, String jarFileName) throws IOException {
		//zipfile is opened for READ on instantiation
		ZipFile zipfile = new ZipFile(jarFileName);
		try {
			ZipEntry entry = getZipEntryFromJar(fileName, zipfile);
			if (entry == null) {
				throw new IOException("entry " + fileName + " not found in jar " + jarFileName);
			}
			InputStream in = zipfile.getInputStream(entry);
			try {
				return StreamSupport.absorbInputStream(in);
			}
			finally {
				in.close();
			}
		}
		finally {
			zipfile.close();
		}
	}

	public static byte[] getBinaryFromClassPath(String fileName, String classPath) throws IOException {
		byte[] retval = null;
		Collection paths = StringSupport.split(classPath, ";:", false);
		Iterator i = paths.iterator();
		while (i.hasNext()) {
			String path = (String) i.next();
			if (path.endsWith(".zip") || path.endsWith(".jar")) {
				retval = getBinaryFromJar(fileName, path);
			}
			else {
				retval = getBinaryFromFS(path + '/' + fileName);
			}
			if (retval == null) {
				File dir = new File(path);
				if (dir.exists() && dir.isDirectory()) {
					Collection jars = FileSupport.getFilesInDirectoryTree(dir, "*.jar");
					Iterator j = jars.iterator();
					while (j.hasNext()) {
						File jar = (File) j.next();
						try {
							retval = getBinaryFromJar(fileName, jar.getPath());
						}
						catch (IOException ioe) {
							//FIXME
						}
						if (retval != null) {
							return retval;
						}
					}

				}
			}
			if (retval != null) {
				return retval;
			}
		}
		return null;
	}


	public static byte[] getBinaryFromClassloader(String path) throws IOException {
		return StreamSupport.absorbInputStream(getInputStreamFromClassLoader(path));
	}

	public static InputStream getInputStreamFromClassLoader(String path) throws IOException {
		InputStream retval = FileSupport.class.getClassLoader().getResourceAsStream(path);
		if(retval == null) {
			throw new IOException("class loader can not load resource '" + path  + "'");
		}
		return retval;
	}

	public static OutputStream getOutputStreamToFileSystem(String path) throws IOException {
		return new FileOutputStream(path);
	}

	public static void copyClassLoadableResourceToFileSystem(String pathToResource, String outputPath) throws IOException{
		InputStream input = getInputStreamFromClassLoader(pathToResource);
		OutputStream output = getOutputStreamToFileSystem(outputPath);
		try {
			StreamSupport.absorbInputStream(input, output);
		} finally {
			output.close();
			input.close();
		}
	}

	/**
	 * Input stream is closed after reading.
	 *
	 * @param input
	 * @param outputPath
	 * @throws IOException
	 */
	public static void copyToFileSystem(InputStream input, String outputPath) throws IOException {
		OutputStream output = null;
		try {
			output = new FileOutputStream(outputPath);
			StreamSupport.absorbInputStream(input, output);
		} finally {
			output.close();
			input.close();
		}
	}




	public static ZipEntry getZipEntryFromJar(String fileName, String jarFileName) throws IOException {
		//zipfile is opened for READ on instantiation
		ZipFile zipfile = new ZipFile(jarFileName);
		return getZipEntryFromJar(fileName, zipfile);
	}

	/**
	 * @param fileName
	 * @param zipfile
	 * @return
	 * @throws IOException
	 */
	public static ZipEntry getZipEntryFromJar(String fileName, ZipFile zipfile) throws IOException {
		return zipfile.getEntry(fileName);
/*		Enumeration e = zipfile.entries();
		while (e.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry) e.nextElement();
			if (entry.getName().equals(fileName))
			{
				return entry;
				//a resource can be read into a byte[] as well, but it's easier to use the classloader
			}
		}
		return null;*/
	}


	/**
	 * Creates a file.
	 * The file, and the directory structure is created physically,
	 * if it does not exist already.
	 *
	 * @param filename
	 * @return
	 */
	public static File createFile(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			File path = new File(file.getParent());
			path.mkdirs();
			file.createNewFile();
		}
		return file;
	}

	/**
	 * Deletes all files and subdirectories from a directory.
	 *
	 * @param path
	 */
	public static void emptyDirectory(String path) {
		deleteContentsInDirectoryTree(path, null);
	}

	/**
	 * Deletes all files and subdirectories from a directory.
	 *
	 * @param path
	 */
	public static void emptyDirectory(File file) {
		deleteContentsInDirectoryTree(file, null);
	}

	/**
	 * Deletes a file or a directory including its contents;
	 *
	 * @param path
	 */
	public static void deleteFile(File file) {
		deleteContentsInDirectoryTree(file, null);
		file.delete();
	}

	/**
	 * Copies a file.
	 *
	 * @param filename
	 * @param newFilename
	 * @param overwriteExisting
	 * @throws IOException
	 */
	public static void copyFile(String filename, String newFilename, boolean overwriteExisting) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			throw new IOException("file '" + filename + "' does not exist");
		}
		if (file.isDirectory()) {
			throw new IOException('\'' + filename + "' is a directory");
		}
		File newFile = new File(newFilename);
		if (!overwriteExisting && newFile.exists()) {
			throw new IOException("file '" + newFilename + "' already exists");
		}
		byte[] buffer = new byte[100000];//+/-100kB

		int read = 0;

		FileInputStream in = new FileInputStream(file);
		FileOutputStream out = new FileOutputStream(newFile);

		while ((read = in.read(buffer)) > 0) {
			out.write(buffer, 0, read);
		}
		out.close();
		in.close();
	}

	/**
	 * Deletes from a directory all files and subdirectories targeted by a given mask.
	 * The method will recurse into subdirectories.
	 *
	 * @param path
	 * @param mask
	 */
	public static void deleteContentsInDirectoryTree(String path, String mask) {
		deleteContentsInDirectoryTree(new File(path), mask);
	}

	/**
	 * Deletes from a directory all files and subdirectories targeted by a given mask.
	 * The method will recurse into subdirectories.
	 *
	 * @param root
	 * @param mask
	 */
	public static void deleteContentsInDirectoryTree(File root, String mask) {
		Collection files = getContentsInDirectoryTree(root, mask, true, true);
		Iterator i = files.iterator();
		while (i.hasNext()) {
			File file = (File) i.next();
			if (file.exists())//file may meanwhile have been deleted
			{
				if (file.isDirectory()) {
					//empty directory
					emptyDirectory(file.getAbsolutePath());
				}
				if (file.exists())//file may meanwhile have been deleted
				{
					boolean result = file.delete();
				}
			}
		}
	}

	/**
	 * @param file
	 * @param searchString
	 * @return a map containing the number of occurrences of the search string in lines, if 1 or more, keyed and sorted by line number
	 * @throws IOException if the file could not be found or is a directory or locked
	 */
	public static Map countOccurencesInTextFile(File file, String searchString) throws IOException {
		TreeMap retval = new TreeMap();
		InputStream input;

		input = new FileInputStream(file);

		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		String line = reader.readLine();
		int lineCount = 1;
		while (line != null) {
			int occCount = -1;
			while ((occCount = line.indexOf(searchString, occCount + 1)) != -1) {
				Integer lineNo = new Integer(lineCount);
				Integer nrofOccurrencesInLine = (Integer) retval.get(lineNo);
				if (nrofOccurrencesInLine == null) {
					nrofOccurrencesInLine = new Integer(1);
				}
				else {
					nrofOccurrencesInLine = new Integer(nrofOccurrencesInLine.intValue() + 1);
				}
				retval.put(lineNo, nrofOccurrencesInLine);
			}

			lineCount++;
			line = reader.readLine();
		}
		return retval;
	}

	/**
	 * Prints a message to a user that invokes FileSupport commandline.
	 */
	private static void printUsage() {
		System.out.println("Commandline use of FileSupport only supports recursive investigation of directories");
		System.out.println("Usage: java FileSupport -{d(elete)|s(how)} <path> [<mask>] [-f(ind) <word>]");
	}


	/**
	 * Commandline use of FileSupport only supports recursive investigation of directories.
	 * Usage: java FileSupport -<d(elete)|s(how)> [<path>] [<filename>]
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO error message if root dir does not exist
		if (args.length >= 2) {
			if (args[0].startsWith("-d")) {
				if (args.length == 3) {
					deleteContentsInDirectoryTree(args[1], args[2]);
					return;
				}
				if (args.length == 2) {
					emptyDirectory(args[1]);
					return;
				}
			}
			if (args[0].startsWith("-s")) {
				if (args.length == 2) {
					CollectionSupport.print(getFilesInDirectoryTree(args[1], null, true));
					return;
				}
				if (args.length == 3) {
					CollectionSupport.print(getFilesInDirectoryTree(args[1], args[2], true));
					return;
				}
				if (args.length > 3) {

					if (args.length == 4 && args[2].startsWith("-f")) {
						List files = getFilesInDirectoryTree(args[1]);
						printOccurringString(files, args[3]);
						return;
					}
					if (args.length == 5 && args[3].startsWith("-f")) {
						List files = getFilesInDirectoryTree(args[1], args[2]);
						printOccurringString(files, args[4]);
						return;
					}
				}
			}
		}
		printUsage();
	}

	/**
	 * @param files
	 * @param searchString
	 */
	private static void printOccurringString(List files, String searchString) {
		//TODO use unix conventions for arguments
		//TODO print message if no files found
		Iterator i = files.iterator();
		while (i.hasNext()) {
			File file = (File) i.next();
			try {
				Map occurrences = countOccurencesInTextFile(file, searchString);
				if (occurrences.size() > 0) {
					int total = 0;
					Iterator j = occurrences.keySet().iterator();
					StringBuffer message = new StringBuffer();
					while (j.hasNext()) {
						Integer lineNo = (Integer) j.next();
						Integer no = (Integer) occurrences.get(lineNo);
						total += no.intValue();
						message.append("line " + lineNo + ": " + no + "\n");
					}
					System.out.println(total + " occurrence" +
							(total > 1 ? "s" : "") + " found in file '" +
							file.getAbsolutePath() + "'\n" + message);
				}
			}
			catch (IOException e) {
				System.out.println("error while trying to read file " + file.getAbsolutePath() + " with message: " + e.getMessage());
			}
		}
	}

	/**
	 * Converts backslashes into forward slashes.
	 *
	 * @param path
	 * @return
	 */
	public static String convertToUnixStylePath(String path) {
		String retval = StringSupport.replaceAll(path, "\\", "/");
		retval = StringSupport.replaceAll(retval, "//", "/");
		return retval;
	}

	public static String getFileNameFromPath(String path) {
		String unixStylePath = convertToUnixStylePath(path);
		if(unixStylePath.endsWith("/")) {
			throw new IllegalArgumentException("path '" + path + "' points to a directory");
		}
		return unixStylePath.substring(unixStylePath.lastIndexOf('/') + 1);
	}

/*
	public static void main(String[] args)
	{
		//CollectionSupport.print(FileSupport.getDirectoriesInDirectoryTree("/home/jeroen/development/flash/src"));
		try
		{
			createFile("C:\\development\\test\\test.ini");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
*/


	public static File createTmpDir() throws IOException {
		File file = File.createTempFile("iglu_util_test_", null);
		file.delete();
		file.mkdirs();
		return file;
	}

	public static File createTmpDir(String prefix) throws IOException {
		File file = File.createTempFile(prefix, null);
		file.delete();
		file.mkdirs();
		return file;
	}
}
