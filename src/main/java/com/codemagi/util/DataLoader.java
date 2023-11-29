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
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DataLoader class handles loading and parsing of data from tab or comma
 * delimited text files.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class DataLoader {

    static Logger log = LogManager.getLogger("com.codemagi.util.DataLoader");

    /**
     * Read data from a file.
     *
     * @param name Filename/path to load data from
     * @return BufferedReader BufferedReader containing data loaded from the
     * file
     */
    private static BufferedReader openDataFile(String name) {
        try {

            String fileData = FileUtils.getFileAsString(name);

            ByteArrayInputStream in = new ByteArrayInputStream(fileData.getBytes());

            return new BufferedReader(new InputStreamReader(in));

        } catch (Exception e) {
            System.out.println("Error reading file " + name);
        }

        return null;
    }

    /**
     * Cleans data before parsing.
     *
     * @param data data String to clean
     * @return String input data converted from unicode to safe html, \n's
     * removed.
     */
    public static String preCleanData(String data) {
        return StringUtils.unicodeToHTML(data, false);
    }

    /**
     * Constructs a ObjectFlatFile data structure from an input String
     *
     * @param data The dataset to build the ObjectFlatFile from
     * @param delimiter The delimiter to use when parsing the data. This should
     * be a single character such as "," or "\t"
     * @param hasHeaders Whether or not the input dataset has headers. If true,
     * the first line of the dataset will be skipped
     * @return ObjectFlatFile ObjectFlatFile data structure constructed from
     * input data
     */
    public static ObjectFlatFile loadDataFromString(String data, String delimiter, boolean hasHeaders) {
        //this method returns a ObjectFlatFile (vector of vectors, of Strings) object
        log.debug("loading data from String");

        ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
        BufferedReader f = new BufferedReader(new InputStreamReader(in));

        return parseData(f, delimiter, hasHeaders);
    }

    /**
     * Constructs a ObjectFlatFile data structure from an input String where
     * fields are quoted
     *
     * @param data The dataset to build the ObjectFlatFile from
     * @param delimiter The delimiter to use when parsing the data. This should
     * be a single character such as "," or "\t"
     * @param hasHeaders Whether or not the input dataset has headers. If true,
     * the first line of the dataset will be skipped
     * @return ObjectFlatFile ObjectFlatFile data structure constructed from
     * input data
     */
    public static ObjectFlatFile loadDataFromQuotedString(String data, String delimiter, boolean hasHeaders) {
        //this method returns a ObjectFlatFile (vector of vectors, of Strings) object
        log.debug("loading data from quoted String");

        ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
        BufferedReader f = new BufferedReader(new InputStreamReader(in));

        return parseQuotedData(f, delimiter, hasHeaders);
    }

    /**
     * Constructs a ObjectFlatFile data structure from an input File
     *
     * @param dataFile The filename/path of the input file
     * @param delimiter The delimiter to use when parsing the data. This should
     * be a single character such as "," or "\t"
     * @param hasHeaders Whether or not the input dataset has headers. If true,
     * the first line of the dataset will be skipped
     * @return ObjectFlatFile ObjectFlatFile data structure constructed from
     * input data
     */
    public static ObjectFlatFile loadDataFromFile(String dataFile, String delimiter, boolean hasHeaders) {
        //this method returns a ObjectFlatFile (vector of vectors, of Strings) object
        log.debug("loading data from file");

        BufferedReader f = openDataFile(dataFile);

        return parseData(f, delimiter, hasHeaders);
    }

    /**
     * Constructs a ObjectFlatFile data structure from and input File
     *
     * @param dataFile The filename/path of the input file
     * @param dataMap An array of ints that specify the width of each column
     * @param hasHeaders Whether or not the input dataset has headers. If true,
     * the first line of the dataset will be skipped
     * @return ObjectFlatFile ObjectFlatFile data structure constructed from
     * input data
     */
    public static ObjectFlatFile loadFixedWidthFile(String dataFile, int[] dataMap, boolean hasHeaders) {
        //this method returns a ObjectFlatFile (vector of vectors, of Strings) object
        log.debug("loading data from fixed-width file");

        BufferedReader f = openDataFile(dataFile);

        return parseFixedWidth(f, dataMap, hasHeaders);
    }

    /**
     * Helper method that does the actual parsing for loadDataFromX methods.
     *
     * @param f BufferedReader to construct the ObjectFlatFile from
     * @param delimiter The delimiter to use when parsing the data. This should
     * be a single character such as "," or "\t"
     * @param hasHeaders Whether or not the input dataset has headers. If true,
     * the first line of the dataset will be skipped
     * @return ObjectFlatFile ObjectFlatFile data structure constructed from
     * input data
     */
    private static ObjectFlatFile parseData(BufferedReader f, String delimiter, boolean hasHeaders) {

        ObjectFlatFile parsedData = new ObjectFlatFile();

        String line;
        String lineCleaned;

        StringChopper st;

        List lineValues;

        try {
            String element;

            System.out.print("parsing data ");

            if (hasHeaders) {
                f.readLine();   //skip header row
            }
            while ((line = f.readLine()) != null) {
                //read lines from the data file
                System.out.print(".");

                //first clean the line
                lineCleaned = cleanString(line);

                st = new StringChopper(lineCleaned, delimiter);

                lineValues = new ArrayList();

                while (st.hasMoreTokens()) {
                    element = (String) st.nextElement();

                    if (element == null) {
                        element = "";
                    }

                    lineValues.add(element);
                    //System.out.print(element + "|");
                }
                //System.out.println("");
                parsedData.addRow(lineValues);
                //System.out.println("loadDataFromFile: new row added to loadedData");
            }

            System.out.println("");

        } catch (Exception e) {
            log.debug("loadDataFromFile: Exception", e);
        }

        return parsedData;

    }

    /**
     * Helper method that does the actual parsing for loadFixedWidthX methods.
     *
     * @param f BufferedReader to construct the ObjectFlatFile from
     * @param dataMap The delimiter to use when parsing the data. This should be
     * a single character such as "," or "\t"
     * @param hasHeaders Whether or not the input dataset has headers. If true,
     * the first line of the dataset will be skipped
     * @return ObjectFlatFile ObjectFlatFile data structure constructed from
     * input data
     */
    private static ObjectFlatFile parseFixedWidth(BufferedReader f, int[] dataMap, boolean hasHeaders) {

        ObjectFlatFile parsedData = new ObjectFlatFile();

        String line;
        String lineCleaned;

        List lineValues;

        try {
            String element;

            System.out.print("parsing data ");

            if (hasHeaders) {
                f.readLine();   //skip header row
            }
            while ((line = f.readLine()) != null) {
                //read lines from the data file
                System.out.print(".");

                int lineIndex = 0;

                lineValues = new ArrayList();

                for (int i = 0; i < dataMap.length; i++) {
                    int colWidth = dataMap[i];

                    element = line.substring(Math.min(lineIndex, line.length()), Math.min((lineIndex + colWidth), line.length()));

                    log.debug("ELEMENT FROM: " + lineIndex + " TO: " + (lineIndex + colWidth) + " IS: " + element);

                    lineIndex += colWidth;

                    if (element == null) {
                        element = "";
                    }

                    lineValues.add(element);
                    //System.out.print(element + "|");
                }
                //System.out.println("");
                parsedData.addRow(lineValues);
                //System.out.println("loadDataFromFile: new row added to loadedData");
            }

            System.out.println("");

        } catch (Exception e) {
            log.debug("loadDataFromFile: Exception", e);
        }

        return parsedData;

    }

    /**
     * Helper method that does the actual parsing of quoted strings for
     * loadDataFromX methods.
     *
     * @param f BufferedReader to construct the ObjectFlatFile from
     * @param delimiter The delimiter to use when parsing the data. This should
     * be a single character such as "," or "\t"
     * @param hasHeaders Whether or not the input dataset has headers. If true,
     * the first line of the dataset will be skipped
     * @return ObjectFlatFile ObjectFlatFile data structure constructed from
     * input data
     */
    private static ObjectFlatFile parseQuotedData(BufferedReader f, String delimiter, boolean hasHeaders) {

        ObjectFlatFile parsedData = new ObjectFlatFile();

        String line;
        String lineCleaned;

        StringChopper st;

        List lineValues;

        try {
            String element;

            System.out.print("loading data from file ");

            if (hasHeaders) {
                f.readLine();   //skip header row
            }
            while ((line = f.readLine()) != null) {
                //read lines from the data file
                System.out.print(".");

                //first clean the line
                lineCleaned = cleanQuotedString(line, delimiter);

                st = new StringChopper(lineCleaned, "|");

                lineValues = new ArrayList();

                while (st.hasMoreTokens()) {
                    element = (String) st.nextElement();
                    lineValues.add(element);
                    //System.out.print(element + "|");
                }
                //System.out.println("");
                parsedData.addRow(lineValues);
                //System.out.println("loadDataFromFile: new row added to loadedData");
            }

            System.out.println("");

        } catch (Exception e) {
            log.debug("loadDataFromFile: Exception", e);
        }

        return parsedData;

    }

    /**
     * Performs data cleanup, especially when importing from Excel tab-delimited
     * text files
     *
     * @param valueIn The String to clean
     * @return String Input String with excess double quotes and double tabs
     * removed
     */
    private static String cleanString(String valueIn) {

        if (valueIn == null) {
            return "";
        }

        String value = valueIn;

        StringUtils.replace(value, "\t\"", "\t");       //replace: <tab>"      with: <tab>
        StringUtils.replace(value, "\"\t", "\t");       //replace: "<tab>      with: <tab>
        StringUtils.replace(value, "\"\r", "\r");       //replace: "<linefeed> with: <linefeed>
        StringUtils.replace(value, "\t\t", "\t \t");    //replace: <tab><tab>  with: <tab> <tab>

        return value;
    }

    /**
     * Performs data cleanup on quoted strings, especially when importing from
     * Excel tab-delimited text files
     *
     * @param valueIn The String to clean
     * @return String Input String with excess double quotes and double tabs
     * removed
     */
    private static String cleanQuotedString(String valueIn, String delimiter) {

        if (valueIn == null) {
            return "";
        }

        String value = valueIn;

        StringUtils.replace(value, "\t\"", "\t");     //replace: <tab>"      with: <tab>
        StringUtils.replace(value, "\"\t", "\t");     //replace: "<tab>      with: <tab>
        StringUtils.replace(value, "\"\r", "\r");     //replace: "<linefeed> with: <linefeed>
        StringUtils.replace(value, "\t\t", "\t \t");  //replace: <tab><tab>  with: <tab> <tab>

        StringUtils.replace(value, "\"" + delimiter + "\"", "|");   // replace: "<delimiter>" with |
        StringUtils.replace(value, "||", "| |", true);              // replace: || with | | (recursive)
        StringUtils.replace(value, "\"", "");                       // eliminate excess "s

        log.debug("CLEANED: " + value);

        return value;

    }

}
