package org.ijsberg.iglu.util.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

	public ZippedFileCollection(File file) throws IOException {
		this(new ZipFile(file), new FileFilterRuleSet("*.*"));
	}

	public ZippedFileCollection(ZipFile zipFile, FileFilterRuleSet fileFilterRuleSet) {

        this.includedFilesRuleSet = fileFilterRuleSet;
        this.zipFile = zipFile;
		refreshFiles();

    }

	public ZippedFileCollection(ZipFile zipFile, String relativeDir, FileFilterRuleSet fileFilterRuleSet) {

		this.includedFilesRuleSet = fileFilterRuleSet;
		this.zipFile = zipFile;
		this.relativeDir = FileSupport.convertToUnixStylePath(relativeDir);
		if(!relativeDir.endsWith("/")) {
			relativeDir += "/";
		}
		refreshFiles();

	}

	@Override
    public List<String> getFileNames() {
        return new ArrayList<String>(filesByRelativePathAndName.keySet());
    }

    @Override
    public byte[] getFileByName(String fileName) throws IOException {
        return FileSupport.getBinaryFromZip(relativeDir + fileName, zipFile);
    }

    @Override
    public String getFileContentsByName(String fileName) throws IOException {
        return FileSupport.getTextFileFromZip(relativeDir + fileName, zipFile);
    }

	@Override
	public FileFilterRuleSet getFileFilter() {
		return includedFilesRuleSet;
	}

	@Override
	public boolean containsFile(String fileName) {
		return  filesByRelativePathAndName.containsKey(fileName);
//				FileSupport.containsFileInZip(relativeDir + fileName, zipFile);
	}

	@Override
	public void refreshFiles() {

        filesByRelativePathAndName.clear();
		rootDir = new Directory("ROOT");

        List<ZipEntry> zipEntries = FileSupport.getContentsFromZipFile(zipFile, includedFilesRuleSet);

        for (ZipEntry zipEntry : zipEntries) {
            String relativePathAndName = FileSupport.convertToUnixStylePath(zipEntry.getName());

			if(relativePathAndName.startsWith(relativeDir)) {
				relativePathAndName = relativePathAndName.substring(relativeDir.length());
				filesByRelativePathAndName.put(relativePathAndName, zipEntry);
				rootDir.addFile(relativePathAndName);

			}
		}
	}

	@Override
	public Directory getRootDirectory() {
		return rootDir;
	}

	@Override
	public void setFileFilter(FileFilterRuleSet fileFilter) {
		this.includedFilesRuleSet = fileFilter;
		refreshFiles();
	}


	Directory rootDir = new Directory("ROOT");

}
