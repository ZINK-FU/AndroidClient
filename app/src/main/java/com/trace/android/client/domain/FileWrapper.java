package com.trace.android.client.domain;

import java.io.File;

/**
 * 作者：ZINK
 * 邮箱：fulibosudo@163.com
 */
public class FileWrapper {

    private String fileName;
    private File file;

    public FileWrapper() {
    }

    public FileWrapper(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
