package org.ijsberg.iglu.util.io;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

/**
 */
public class Directory {

	private String fullDirectoryName = "";
	private String directoryName;
	private List<String> fileNames = new ArrayList<String>();
	private TreeMap<String, Directory> subdirectories = new TreeMap<String, Directory>();

	public Directory(String directoryName) {
		this.directoryName = directoryName;
	}

	protected void addFile(String pathAndName) {
		addFile(pathAndName, pathAndName);
	}

	protected void addFile(String relativePathAndName, String fullPathAndName) {


		String pathAndName = relativePathAndName;
		if(pathAndName.startsWith("/")) {
			pathAndName = pathAndName.substring(1);
		}

//		if(fullPathAndName.contains("/")) {
			fullDirectoryName = fullPathAndName.substring(0, fullPathAndName.lastIndexOf(relativePathAndName));
//		}

		if(pathAndName.contains("/")) {
			String directoryName = pathAndName.substring(0, pathAndName.indexOf("/"));
			String fileName = pathAndName.substring(pathAndName.indexOf("/") + 1);
			Directory subdir = subdirectories.get(directoryName);
			if(subdir == null) {
				subdir = new Directory(directoryName);
				subdirectories.put(directoryName, subdir);
			}
			subdir.addFile(fileName, fullPathAndName);
		} else {
			fileNames.add(fullPathAndName);
		}
	}

	public Collection<Directory> getSubdirs() {
		return subdirectories.values();
	}

	public String toString() {
		return directoryName;
	}

	public String getName() {
		return directoryName;
	}

	public String getFullName() {
		return fullDirectoryName;
	}

	public List<String> getFileNames() {
		return fileNames;
	}
}
