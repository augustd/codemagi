/* 
 *  Copyright 2012 CodeMagi, Inc.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.codemagi.util;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides static utility methods for working with files.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class FileUtils {

    static Logger log = LogManager.getLogger("com.codemagi.util.FileUtils");

    /**
     * block constructor by design
     */
    private FileUtils() {
    }

    /**
     * Returns the size of a file in bytes. Returns 0 if file is a directory.
     *
     * @param fileName Full file path
     * @return long Size of file in bytes
     */
    public static long getFileSize(String fileName) {
        return getFileSize(new File(fileName));
    }

    /**
     * Returns the size of a file in bytes. Returns 0 if file is a directory.
     *
     * @param file File to test
     * @return long Size of file in bytes
     */
    public static long getFileSize(File file) {
        try {
            return file.isDirectory() ? 0 : file.length();

        } catch (NullPointerException npe) {
            return -1;
        }
    }

    /**
     * Returns the size of a file as a formatted String
     *
     * @param fileName Full file path
     * @return String Size of file as a formatted String
     */
    public static String getFileSizeString(String fileName) {
        long size = getFileSize(fileName);

        return getFileSizeString(size);
    }

    /**
     * Returns the size of a file as a formatted String
     *
     * @param fileName Full file path
     * @return String Size of file as a formatted String
     */
    public static String getFileSizeString(Long input) {
        if (input == null) {
            return "-1";
        }

        long size = input.longValue();

        int steps = 0;
        while (size > 1024) {
            size = size / 1024;
            steps++;
        }
        if (steps == 0) {
            return size + " bytes";
        } else if (steps == 1) {
            return size + " KB";
        } else if (steps == 2) {
            return size + " MB";
        } else if (steps == 3) {
            return size + " GB";
        } else if (steps == 4) {
            return size + " TB";
        } else if (steps == 5) {
            return size + " PB";
        } else if (steps == 6) {
            return size + " EB";
        } else if (steps == 7) {
            return size + " ZB";
        } else if (steps == 8) {
            return size + " YB";
        } else if (steps == 9) {
            return size + " BB";
        } else {
            return "Huge";
        }
    }

    /**
     * Returns the <code>String</code> contents of an ASCII file.
     * <p>
     * This method throws any fileIO errors.
     *
     * @param sFileName Full file path.
     * @return String The contents of the file as a String object.
     * @throws Exception Any fileIO errors
     */
    public static String getFileAsString(String sFileName) throws Exception {
        RandomAccessFile inputFile = new RandomAccessFile(sFileName, "r");
        int length = (int) inputFile.length();
        byte[] inputbytes = new byte[length];
        int numread = inputFile.read(inputbytes);
        inputFile.close();
        return new String(inputbytes);
    }

    /**
     * Returns the <code>String</code> contents of an ASCII file.
     * <p>
     * This method throws any fileIO errors.
     *
     * @param url Full file path.
     * @return String The contents of the file as a String object.
     * @throws IOException Any fileIO errors
     */
    public static String getFileAsString(URL url) throws IOException {
        StringBuilder output = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                output.append(line);
            }
        }

        return output.toString();
    }

    /**
     * Returns the contents of an ASCII file as an array of bytes
     * <p>
     * This method throws any fileIO errors.
     *
     * @param fileName Full file path.
     * @return inputBytes Byte array containing the contents of the file.
     * @throws IOException Any fileIO errors
     */
    public static byte[] getFileAsBytes(String fileName) throws IOException {
        return getFileAsBytes(new File(fileName));
    }

    /**
     * Returns the contents of an ASCII file as an array of bytes
     * <p>
     * This method throws any fileIO errors.
     *
     * @param file The java.io.File to load
     * @return Byte array containing the contents of the file.
     * @throws IOException Any fileIO errors
     */
    public static byte[] getFileAsBytes(File file) throws IOException {
        byte[] inputbytes;
        try (RandomAccessFile inputFile = new RandomAccessFile(file, "r")) {
            int length = (int) inputFile.length();
            inputbytes = new byte[length];
            int numread = inputFile.read(inputbytes);
        }

        return inputbytes;
    }

    /**
     * Returns the contents of an ASCII file as an array of chars
     * <p>
     * This method throws any fileIO errors.
     *
     * @param sFileName Full file path.
     * @return char[] char array containing the contents of the file.
     * @throws Exception Any fileIO errors
     */
    public static char[] getFileAsChars(String sFileName) throws Exception {
        byte[] inputbytes;
        try (RandomAccessFile inputFile = new RandomAccessFile(sFileName, "r")) {
            int length = (int) inputFile.length();
            inputbytes = new byte[length];
            int numread = inputFile.read(inputbytes);
        }

        String inputString = new String(inputbytes);

        return inputString.toCharArray();
    }

    /**
     * Returns the contents of an ASCII file as an array of Strings
     * <p>
     * This method throws any fileIO errors.
     *
     * @param sFileName Full file path.
     * @return String[] String array containing the contents of the file, one
     * element per line
     * @throws Exception Any fileIO errors
     */
    public static String[] getFileAsLines(String sFileName) throws Exception {

        FileInputStream fIn = null;
        BufferedReader fileReader = null;

        try {
            //open the file 
            fIn = new FileInputStream(sFileName);
            fileReader = new BufferedReader(new InputStreamReader(fIn));

            //create a Vector for output
            List<String> outputVector = new ArrayList<>();

            //read the file line by line, append lines to the Vector
            String line = null;

            while ((line = fileReader.readLine()) != null) {
                outputVector.add(line);
            }

            //convert the outputVector to an array
            int numLines = outputVector.size();

            String[] outputArray = new String[numLines];

            for (int i = 0; i < numLines; i++) {
                outputArray[i] = outputVector.get(i);
            }

            return outputArray;

        } catch (Exception e) {

            throw e;

        } finally {

            fIn.close();
            fileReader.close();

        }

    }

    /**
     * Move a file from one location/filename to another.
     *
     * @param source The file starting location/filename (case-sensitive)
     * @param destination The file ending location/filename (case-sensitive)
     * @throws IOException If the source file is not readable
     */
    public static boolean moveFile(File source, File destination) throws java.io.IOException {
        if (source == null || !source.exists()) {
            throw new java.io.IOException("Source file does not exist or is not readable");
        }

        return source.renameTo(destination);
    }

    /**
     * Recursively copies a directory tree from one location to another
     *
     * @param source The file to be copied
     * @param target The destination file
     * @throws IOException Throws any file I/O exceptions
     */
    public static void copyTree(File source, File target)
            throws java.io.IOException {
        copyTree(source, target, true);
    }

    private static void copyTree(File source, File target, boolean first)
            throws java.io.IOException {

        log.debug("CopyTree: from: " + source + " to: " + target);

        if (first) {
            log.debug("FIRST");
        }

        //check input first
        if (source == null || target == null) {
            throw new java.io.IOException("Source or target is null");
        }

        File dest = target;

        //if source is a directory, iterate the contents
        if (source.isDirectory()) {

            //cant copy directory into a file
            if (dest.isFile()) {
                throw new java.io.IOException("Can't copy directory into file");
            }

            if (!first) {
                dest = new File(target.getPath() + File.separator + source.getName());
            }

            dest.mkdirs();

            File[] contents = source.listFiles();

            for (File file : contents) {
                copyTree(file, dest, false);
            }
        }

        copyFile(source, dest);
    }

    /**
     * Copy a file or directory from one location to another. NOTE: If copying
     * directories, the contents are not copied.
     *
     * @see copyTree for copying entire directories
     *
     * @param source The file to be copied
     * @param target The destination file
     * @throws IOException Throws any file I/O exceptions
     */
    public static void copyFile(File source, File target)
            throws java.io.IOException {

        if (source.isDirectory()) {
            log.debug("CopyFile: making dir: " + target);
            target.mkdirs();
            return;
        }

        FileWriter out;
        try (FileReader in = new FileReader(source)) {
            File dest = target;
            if (dest.isDirectory()) {
                dest = new File(target.getPath() + File.separator + source.getName());
            }
            log.debug("CopyFile: from: " + source + " to: " + dest);
            out = new FileWriter(dest);
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        }
        out.close();
    }

    /**
     * Returns an array of files in a directory
     *
     * @param directory File Object representing the directory to check
     * @return File[] Array of files (excluding subdirectories) in the directory
     * @throws IOException if the File argument is not a directory
     */
    public static File[] findFiles(File directory) throws IOException {

        Date beginningOfTime = new Date(0l);

        return findNewerFiles(directory, beginningOfTime);
    }

    /**
     * Overridden to accept a String constructor instead of File
     */
    public static File[] findFiles(String directory) throws IOException {

        File dir = new File(directory);

        return findFiles(dir);

    }

    /**
     * Returns an array of files in a directory that are NEWER than the input
     * date
     *
     * @param directory File Object representing the directory to check
     * @param oldestDate java.util.Date to find files newer than
     * @return File[] Array of files (excluding subdirectories) newer than the
     * specified Date object
     * @throws IOException if the File argument is not a directory
     */
    public static File[] findNewerFiles(File directory, Date oldestDate) throws IOException {

        //2001/12/17 added single file handling
        if (!directory.isDirectory()) {
            //one File
            File[] output = new File[1];

            //check if single file is newer
            Date fileDate = new Date(directory.lastModified());
            if (oldestDate.before(fileDate)) {
                output[0] = directory;
            }

            return output;
        }

        //get directory listing
        File[] directoryFiles = directory.listFiles();
        int numFiles = directoryFiles.length;

        //create a Vector to hold the output Files
        List<File> outputVector = new ArrayList<>();

        //iterate through the files and add newer ones to outputVector Vector
        for (int i = 0; i < numFiles; i++) {
            File currentFile = directoryFiles[i];

            //skip directories
            if (currentFile.isDirectory()) {
                continue;
            }

            //check if it is newer
            Date fileDate = new Date(currentFile.lastModified());
            if (oldestDate.before(fileDate)) {
                outputVector.add(currentFile);
                log.debug("Newer: " + currentFile.toString());
            }

        }

        //convert the outputVector to an array
        int numNewerFiles = outputVector.size();
        log.debug(numNewerFiles + " newer files in directory");

        File[] outputArray = new File[numNewerFiles];

        for (int i = 0; i < numNewerFiles; i++) {
            outputArray[i] = (File) outputVector.get(i);
        }

        return outputArray;

    }

    /**
     * Overridden to accept a String constructor instead of File
     */
    public static File[] findNewerFiles(String directory, Date oldestDate) throws Exception {

        File dir = new File(directory);

        return findNewerFiles(dir, oldestDate);

    }

    /**
     * Writes a String to a file with the specified name
     *
     * @param filename The filename of the file created
     * @param fileData The String to write to the new file
     * @param overWrite Overwrites existing files if true. Otherwise appends.
     * @return boolean True if the file was written successfully
     */
    public static boolean writeFile(String filename, String fileData, boolean overWrite) {

        try {
            RandomAccessFile output = new RandomAccessFile(filename, "rw");

            if (overWrite) {
                output.setLength(0);
            } else {
                output.seek(output.length() + 1);
            }

            output.write(fileData.getBytes());

            return true;

        } catch (Exception e) {
            log.debug("", e);
        }

        return false;

    }

    /**
     * Gets the file extension, that is: Any letters or numbers after the final
     * dot in the filename
     *
     * @param filename The full filename from which to extract the extension
     * @return String The file extension, or "" if none was found
     */
    public static String getExtension(String filename) {

        StringTokenizer st = new StringTokenizer(filename, ".");

        String extension = "";

        while (st.hasMoreTokens()) {
            extension = st.nextToken();
        }

        return extension;
    }

    /**
     * returns the file name portion of a file identifier string. For example,
     * <code>getFileName("/path/to/the/file.java")</code> would return
     * "file.java"
     */
    public static String getFileName(String input) {
        int index = input.lastIndexOf(System.getProperty("file.separator"));

        return (index > 0) ? input.substring(index + 1) : input;
    }

    /**
     * Returns <code>true</code> if both input streams byte contents are
     * identical.
     *
     * @param is1 first input to contents compare
     * @param is2 second input to contents compare
     * @return <code>true</code> if content is equal
     * @author checG (posted on Java Technology Forums, Oct 20, 2004)
     */
    public static boolean areFilesEqual(InputStream is1, InputStream is2, boolean ignoreWhitespace) {

        try {
            if (is1 == is2) {
                return true;
            }

            if (is1 == null && is2 == null) // no byte contents
            {
                return true;
            }

            if (is1 == null || is2 == null) // only one has contents
            {
                return false;
            }

            while (true) {

                int c1 = is1.read();
                while (ignoreWhitespace && isWhitespace(c1)) {
                    c1 = is1.read();
                }

                int c2 = is2.read();
                while (ignoreWhitespace && isWhitespace(c2)) {
                    c2 = is2.read();
                }

                if (c1 == -1 && c2 == -1) {
                    return true;  //we've gotten to the end of both streams
                }
                if (c1 != c2) {
                    break;
                }

            }

        } catch (IOException ex) {
            //nada

        } finally {
            try {
                try {
                    if (is1 != null) {
                        is1.close();
                    }

                } finally {
                    if (is2 != null) {
                        is2.close();
                    }
                }
            } catch (IOException e) {
                // Ignore
            }
        }

        return false;
    }

    private static boolean isWhitespace(int c) {
        if (c == -1) {
            return false;
        }
        return Character.isWhitespace((char) c);
    }

}
