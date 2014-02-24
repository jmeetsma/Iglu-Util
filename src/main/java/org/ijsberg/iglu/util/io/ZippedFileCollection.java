package org.ijsberg.iglu.util.io;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 */
public class ZippedFileCollection implements FileCollection {

    private Map<String, ZipEntry> filesByRelativePathAndName = new TreeMap<String, ZipEntry>();
    private FileFilterRuleSet includedFilesRuleSet;
    private ZipFile zipFile;

	private String relativeDir = "";

    public ZippedFileCollection(String zipFileName, FileFilterRuleSet fileFilterRuleSet) throws IOException {

        this(new ZipFile(zipFileName), fileFilterRuleSet);
    }

    public ZippedFileCollection(ZipFile zipFile, FileFilterRuleSet fileFilterRuleSet) {

        this.includedFilesRuleSet = fileFilterRuleSet;
        this.zipFile = zipFile;
    }

	public ZippedFileCollection(ZipFile zipFile, String relativeDir, FileFilterRuleSet fileFilterRuleSet) {

		this.includedFilesRuleSet = fileFilterRuleSet;
		this.zipFile = zipFile;
		this.relativeDir = FileSupport.convertToUnixStylePath(relativeDir);
		if(!relativeDir.endsWith("/")) {
			relativeDir += "/";
		}
	}

	@Override
    public List<String> getFileNames() {
        refreshFiles();
        return new ArrayList<String>(filesByRelativePathAndName.keySet());
    }

    @Override
    public byte[] getFileByName(String fileName) throws IOException {
        return FileSupport.getBinaryFromJar(relativeDir + fileName, zipFile);
    }

    @Override
    public String getFileContentsByName(String fileName) throws IOException {
        return FileSupport.getTextFileFromJar(relativeDir + fileName, zipFile);
    }

	@Override
	public FileFilterRuleSet getFileFilter() {
		return includedFilesRuleSet;
	}

	private void refreshFiles() {

        filesByRelativePathAndName.clear();

        List<ZipEntry> zipEntries = FileSupport.getContentsFromZipFile(zipFile, includedFilesRuleSet);

        for (ZipEntry zipEntry : zipEntries) {
            String relativePathAndName = FileSupport.convertToUnixStylePath(zipEntry.getName());

			if(relativePathAndName.startsWith(relativeDir)){
				relativePathAndName = relativePathAndName.substring(relativeDir.length());
				filesByRelativePathAndName.put(relativePathAndName, zipEntry);
			}


        }
    }

}
