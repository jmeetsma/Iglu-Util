package org.ijsberg.iglu.util.io;

import java.io.IOException;
import java.util.List;

/**
 */
public interface FileCollection {

    List<String> getFileNames();

    byte[] getFileByName(String fileName) throws IOException;

    String getFileContentsByName(String fileName) throws IOException;

	FileFilterRuleSet getFileFilter();

	boolean containsFile(String fileName);

	Directory getRootDirectory();

	void setFileFilter(FileFilterRuleSet fileFilter);

	void refreshFiles();

	String getDescription();
}
