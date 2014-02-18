package org.ijsberg.iglu.util.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 */
public class FSFileCollection implements FileCollection {

    private String baseDir;
//    private String absSourceRoot;
    private Charset charSet;

//    private List<File> files;
    private Map<String, File> filesByRelativePathAndName = new TreeMap<String, File>();
    private FileFilterRuleSet includedFilesRuleSet;

    public FSFileCollection(String baseDir, FileFilterRuleSet fileFilterRuleSet) {

        this.baseDir = FileSupport.convertToUnixStylePath(baseDir);
//        this.absSourceRoot = FileSupport.convertToUnixStylePath(baseDir + "/" + relativeSourceRoot);

        this.includedFilesRuleSet = fileFilterRuleSet;
        refreshFiles();
    }

    @Override
    public List<String> getFileNames() {
        refreshFiles();
        return new ArrayList<String>(filesByRelativePathAndName.keySet());
    }

    @Override
    public byte[] getFileByName(String fileName) throws IOException {
        return FileSupport.getBinaryFromFS(filesByRelativePathAndName.get(fileName));
    }

    public File getActualFileByName(String fileName) throws IOException {
        return filesByRelativePathAndName.get(fileName);
    }

    @Override
    public String getFileContentsByName(String fileName) throws IOException {
        return new String(getFileByName(fileName));
    }

	@Override
	public FileFilterRuleSet getFileFilter() {
		return includedFilesRuleSet;
	}

	private void refreshFiles() {

        filesByRelativePathAndName.clear();

        List<File> files = FileSupport.getFilesInDirectoryTree(baseDir, includedFilesRuleSet);

        for (File file : files) {
            String relativePathAndName = FileSupport.convertToUnixStylePath(file.getPath()).substring(
                    baseDir.length());
            filesByRelativePathAndName.put(relativePathAndName, file);
        }
    }
}
