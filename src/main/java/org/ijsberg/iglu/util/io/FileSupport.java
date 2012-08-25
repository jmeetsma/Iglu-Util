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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.ijsberg.iglu.util.collection.CollectionSupport;
import org.ijsberg.iglu.util.formatting.PatternMatchingSupport;
import org.ijsberg.iglu.util.misc.StringSupport;

/**
 * Supports retrieval and deletion of particular files in a directory structure
 * as well as other file system manipulation.
 */
public abstract class FileSupport {
	private static final int COPY_BUFFER = 100000;

	/**
	 * Retrieves all files from a directory and its subdirectories.
	 *
	 * @param path path to directory
	 * @return A list containing the found files
	 */
	public static ArrayList<File> getFilesInDirectoryTree(String path) {
		File directory = new File(path);
		return getContentsInDirectoryTree(directory, "*", true, false);
	}


	/**
	 * Retrieves all files from a directory and its subdirectories.
	 *
	 * @param directory directory
	 * @return a list containing the found files
	 */
	public static ArrayList<File> getFilesInDirectoryTree(File directory) {
		return getContentsInDirectoryTree(directory, "*", true, false);
	}


	/**
	 * Retrieves files for a given mask from a directory and its subdirectories.
	 *
	 * @param path root of directory tree
	 * @param includeMask exact filename, or mask containing wildcards
	 * @return A list containing the found files
	 */
	public static List<File> getFilesInDirectoryTree(String path, String includeMask) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, includeMask, true, false);
	}

	/**
	 * Retrieves files for a given mask from a directory and its subdirectories.
	 *
	 * @param path root of directory tree
	 * @param includeRuleSet rule set defining precisely which files to include
	 * @return A list containing the found files
	 */
	public static ArrayList<File> getFilesInDirectoryTree(String path, FileFilterRuleSet includeRuleSet) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, includeRuleSet, true, false);
	}

	/**
	 * Retrieves all files from a directory and its subdirectories
	 * matching the given mask.
	 *
	 * @param file directory
	 * @param includeMask mask to match
	 * @return a list containing the found files
	 */
	public static ArrayList<File> getFilesInDirectoryTree(File file, String includeMask) {
		return getContentsInDirectoryTree(file, includeMask, true, false);
	}


	/**
	 * Retrieves contents from a directory and its subdirectories matching a given mask.
	 *
	 * @param directory directory
	 * @param includeMask file name to match
	 * @param returnFiles return files
	 * @param returnDirs return directories
	 * @return a list containing the found contents
	 */
	private static ArrayList<File> getContentsInDirectoryTree(File directory, String includeMask, boolean returnFiles, boolean returnDirs) {
		return getContentsInDirectoryTree(directory, new FileFilterRuleSet(includeMask), returnFiles, returnDirs);
	}


	/**
	 * Retrieves contents from a directory and its subdirectories matching a given rule set.
	 *
	 * @param directory directory
	 * @param ruleSet file name to match
	 * @param returnFiles return files
	 * @param returnDirs return directories
	 * @return a list containing the found contents
	 */
	private static ArrayList<File> getContentsInDirectoryTree(File directory, FileFilterRuleSet ruleSet, boolean returnFiles, boolean returnDirs) {
		ArrayList<File> result = new ArrayList<File>();
		if (directory != null && !directory.isFile()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						if (returnDirs && ruleSet.fileMatchesRules(files[i])) {
							result.add(files[i]);
						}
						result.addAll(getContentsInDirectoryTree(files[i], ruleSet, returnFiles, returnDirs));
					}
					else if (returnFiles && ruleSet.fileMatchesRules(files[i])) {
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
	public static ArrayList<File> getDirectoriesInDirectoryTree(String path) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, "*", false, true);
	}


	/**
	 * Retrieves all directories from a directory and its subdirectories.
	 *
	 * @param path path to directory
	 * @param includeMask file name to match
	 * @return A list containing the found directories
	 */
	public static ArrayList<File> getDirectoriesInDirectoryTree(String path, String includeMask) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, includeMask, false, true);
	}


	/**
	 * Retrieves all files from a directory and its subdirectories.
	 *
	 * @param path path to directory
	 * @param includeMask file name to match
	 * @return a list containing the found files
	 */
	public static ArrayList<File> getFilesAndDirectoriesInDirectoryTree(String path, String includeMask) {
		File file = new File(path);
		return getContentsInDirectoryTree(file, includeMask, true, true);
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

		Collection<String> paths = StringSupport.split(classPath, ";:", false);

		for(String singlePath : paths) {
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
			return getBinaryFromFS(file);
		}
		throw new FileNotFoundException("file '" + fileName + "' does not exist");
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBinaryFromFS(File existingFile) throws IOException {
		FileInputStream in = new FileInputStream(existingFile);
		try {
			return StreamSupport.absorbInputStream(in);
		}
		finally {
			in.close();
		}
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

		Collection<String> jars = StringSupport.split(System.getProperty("java.class.path"), ";:", false);
		for(String jarFileName : jars) {
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
			ZipEntry entry = zipfile.getEntry(fileName);
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
		Collection<String> paths = StringSupport.split(classPath, ";:", false);
		for(String path : paths) {
			if (path.endsWith(".zip") || path.endsWith(".jar")) {
				retval = getBinaryFromJar(fileName, path);
			}
			else {
				retval = getBinaryFromFS(path + '/' + fileName);
			}
			if (retval == null) {
				File dir = new File(path);
				if (dir.exists() && dir.isDirectory()) {
					Collection<File> jars = FileSupport.getFilesInDirectoryTree(dir, "*.jar");
					for(File jar : jars) {
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


	public static byte[] getBinaryFromClassLoader(String path) throws IOException {
		return StreamSupport.absorbInputStream(getInputStreamFromClassLoader(path));
	}

	/**
	 *
	 * @param path path with regular path separators ('/')
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStreamFromClassLoader(String path) throws IOException {
		ClassLoader classLoader = FileSupport.class.getClassLoader();
		InputStream retval = classLoader.getResourceAsStream(path);

		if(retval == null) {
			throw new IOException("class loader can not load resource '" + path  + "'");
		}
		return retval;
	}

	/**
	 * @param pathToResource
	 * @param outputPath
	 * @throws IOException
	 */
	public static void copyClassLoadableResourceToFileSystem(String pathToResource, String outputPath) throws IOException{

		//TODO make sure that files exist
		InputStream input = getInputStreamFromClassLoader(pathToResource);

		File outputFile = createFile(outputPath);
		if(outputFile.isDirectory()) {
			outputFile = new File(outputFile.getPath() + '/' + getFileNameFromPath(pathToResource));
		}
		OutputStream output = new FileOutputStream(outputFile);
		try {
			StreamSupport.absorbInputStream(input, output);
		} finally {
			output.close();
			input.close();
		}
	}


	public static ZipEntry getZipEntryFromJar(String fileName, String jarFileName) throws IOException {
		//zipfile is opened for READ on instantiation
		ZipFile zipfile = new ZipFile(jarFileName);
		return zipfile.getEntry(fileName);
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
	 * @param file
	 */
	public static void emptyDirectory(File file) {
		deleteContentsInDirectoryTree(file, null);
	}

	/**
	 * Deletes a file or a directory including its contents;
	 *
	 * @param file
	 */
	public static boolean deleteFile(File file) {
		deleteContentsInDirectoryTree(file, null);
		return file.delete();
	}

	/**
	 * Copies a file.
	 *
	 * @param fileName
	 * @param newFileName
	 * @param overwriteExisting
	 * @throws IOException
	 */
	public static void copyFile(String fileName, String newFileName, boolean overwriteExisting) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new IOException("file '" + fileName + "' does not exist");
		}
		if (file.isDirectory()) {
			throw new IOException('\'' + fileName + "' is a directory");
		}
		File newFile = new File(newFileName);
		if(file.isDirectory()) {
			newFile = new File(file.getPath() + '/' + getFileNameFromPath(fileName));
		}
		if (!overwriteExisting && newFile.exists()) {
			throw new IOException("file '" + newFileName + "' already exists");
		} else {
			//newFile.mkdirs();
			newFile.getParentFile().mkdirs();
			newFile.createNewFile();
		}
		byte[] buffer = new byte[COPY_BUFFER];

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
	 * @param includeMask
	 */
	public static void deleteContentsInDirectoryTree(String path, String includeMask) {
		deleteContentsInDirectoryTree(new File(path), includeMask);
	}

	/**
	 * Deletes from a directory all files and subdirectories targeted by a given mask.
	 * The method will recurse into subdirectories.
	 *
	 * @param root
	 * @param includeMask
	 */
	public static void deleteContentsInDirectoryTree(File root, String includeMask) {
		Collection<File> files = getContentsInDirectoryTree(root, includeMask, true, true);
		for(File file : files) {
			if (file.exists()) {//file may meanwhile have been deleted
				if (file.isDirectory()) {
					//empty directory
					emptyDirectory(file.getAbsolutePath());
				}
				file.delete();				
			}
		}
	}

	/**
	 * @param file
	 * @param searchString
	 * @return a map containing the number of occurrences of the search string in lines, if 1 or more, keyed and sorted by line number
	 * @throws IOException if the file could not be found or is a directory or locked
	 */
	public static Map<Integer, Integer> countOccurencesInTextFile(File file, String searchString) throws IOException {
		TreeMap<Integer, Integer> retval = new TreeMap<Integer, Integer>();
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
				//System.out.println(">" + line + "<");
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
					CollectionSupport.print(getFilesAndDirectoriesInDirectoryTree(args[1], null));
					return;
				}
				if (args.length == 3) {
					CollectionSupport.print(getFilesAndDirectoriesInDirectoryTree(args[1], args[2]));
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
				Map<Integer, Integer> occurrences = countOccurencesInTextFile(file, searchString);
				if (occurrences.size() > 0) {
					int total = 0;
					StringBuffer message = new StringBuffer();
					for (Integer lineNo : occurrences.keySet()) {
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

	public static String getDirNameFromPath(String path) {
		String unixStylePath = convertToUnixStylePath(path);
		return unixStylePath.substring(0, unixStylePath.lastIndexOf('/') + 1);
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
