package com.codemagi.util;

import java.io.*;
import java.util.zip.*;

import org.apache.log4j.Logger;

public class Zipper {

    Logger log = Logger.getLogger(this.getClass().getName());

    final int DEFAULT_SIZE = 4096;

    ByteArrayOutputStream baos;
    ZipOutputStream zip;

    public Zipper(String filename) throws IOException {
        baos = new ByteArrayOutputStream(DEFAULT_SIZE);
        zip = new ZipOutputStream(baos);
        zip.setMethod(ZipOutputStream.DEFLATED);
        zip.setLevel(Deflater.BEST_COMPRESSION);
        log.debug("File " + filename + new java.util.Date());
    }

    public int size() {
        return baos.size();
    }

    public void writeTo(OutputStream out) throws IOException {
        baos.writeTo(out);
    }

    private void addFile(ZipEntry ze, InputStream is) throws IOException {
        zip.putNextEntry(ze);
        log.debug("added stream " + ze + " (" + ze.getTime() + ") - ");
        int bRead;
        int counter = 0;
        byte[] buf = new byte[4096];

        while ((bRead = is.read(buf)) > -1) {
            counter += bRead;
            zip.write(buf, 0, bRead);
        }
        log.debug("" + counter + " bytes.");
    }

    public void addFile(String fname, long time, InputStream is) throws IOException {
        ZipEntry ze = new ZipEntry(fname);
        ze.setTime(time);
        addFile(ze, is);
    }

    public void addFile(String name, File file) throws IOException {
        String path = file.getPath();

        if (file.isFile()) {
            log.debug("added file " + name + " (" + path + ") - ");
            InputStream is = new FileInputStream(file);
            addFile(name, file.lastModified(), is);
        } else if (file.isDirectory()) {
            File[] contents = file.listFiles();
            log.debug("added directory " + name + " (" + path + ") - " + contents.length + " entries.");
            for (int i = 0; i < contents.length; i++) {
                String fileName = name + contents[i].getPath().substring(path.length());
                addFile(fileName, contents[i]);
                //        addFile("file_" + i, 0, "directory: " + directoryName + ", file: " + contents[i] + ", name: " + fileName);
            }
        } else {
            log.debug(name + " (" + path + ") - not found.");
        }
    }

    public void addFile(String fname, long time, String data) throws IOException {
        addFile(fname, time, data.getBytes());
    }

    public void addFile(ZipEntry ze, byte[] data) throws IOException {
        zip.putNextEntry(ze);
        zip.write(data, 0, data.length);
    }

    public void addFile(String fname, long time, byte[] data) throws IOException {
        ZipEntry ze = new ZipEntry(fname);
        ze.setTime(time == 0 ? new java.util.Date().getTime() : time);
        addFile(ze, data);
    }

    public void close() throws IOException {
        zip.close();
    }
}
