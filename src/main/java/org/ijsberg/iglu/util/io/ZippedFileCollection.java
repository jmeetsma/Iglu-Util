package org.ijsberg.iglu.util.io;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 */
public class ZippedFileCollection implements FileCollection {

    //private List<File> files;
    private Map<String, ZipEntry> filesByRelativePathAndName = new TreeMap<String, ZipEntry>();
    private FileFilterRuleSet includedFilesRuleSet;
    private ZipFile zipFile;


    public ZippedFileCollection(String zipFileName, FileFilterRuleSet fileFilterRuleSet) throws IOException {

        this(new ZipFile(zipFileName), fileFilterRuleSet);
    }

    public ZippedFileCollection(ZipFile zipFile, FileFilterRuleSet fileFilterRuleSet) {

        this.includedFilesRuleSet = fileFilterRuleSet;
        this.zipFile = zipFile;
    }

    @Override
    public List<String> getFileNames() {
        refreshFiles();
        return new ArrayList<String>(filesByRelativePathAndName.keySet());
    }

    @Override
    public byte[] getFileByName(String fileName) throws IOException {
        return FileSupport.getBinaryFromJar(fileName, zipFile);
    }

    @Override
    public String getFileContentsByName(String fileName) throws IOException {
        return FileSupport.getTextFileFromJar(fileName, zipFile);
    }

    private void refreshFiles() {

        filesByRelativePathAndName.clear();

        List<ZipEntry> zipEntries = FileSupport.getContentsFromZipFile(zipFile, includedFilesRuleSet);

        for (ZipEntry zipEntry : zipEntries) {
            String relativePathAndName = FileSupport.convertToUnixStylePath(zipEntry.getName());
            filesByRelativePathAndName.put(relativePathAndName, zipEntry);
        }
    }

}
