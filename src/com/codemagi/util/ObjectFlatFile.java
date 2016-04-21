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

import java.io.RandomAccessFile;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * ObjectFlatFile is an expandable two-dimensional data structure of Objects,
 * implemented as a List (rows) of Lists (columns).
 * <P>
 * The rows list is created automatically at instantiation as an ArrayList.
 * Columns can be any List type (ArrayList, LinkedList, Vector, etc) depending
 * on performance needs. If no List type is given, or if an initial capacity is
 * specified, ArrayList is used.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class ObjectFlatFile implements java.io.Serializable {

    Logger log = Logger.getLogger(this.getClass());

    //members
    private List rows;
    private List currentRow;

    //options
    private boolean unquote = false;
    private boolean trim = false;
    private boolean returnNulls = false;

    /**
     * Default null constructor.
     */
    public ObjectFlatFile() {
        rows = new ArrayList();
    }

    /**
     * Constructs an empty ObjectFlatFile with the specified initial number of
     * rows.
     */
    public ObjectFlatFile(int initialNumRows) {
        rows = new ArrayList(initialNumRows);
    }

    /**
     * This Constructor allows the specification of the type of the rows List.
     *
     * @param rowsType Class indicating the type of List to use for the rows of
     * this FlatFile
     * @exception IllegalArgumentException If a class not implementing the List
     * interface is passed to the constructor
     */
    public ObjectFlatFile(Class rowsType)
            throws IllegalArgumentException {

        try {
            Object rowsTypeObject = rowsType.newInstance();

            rows = (List) rowsTypeObject;
        } catch (Exception e) {
            throw new IllegalArgumentException("Input to this constructor must be a List");
        }
    }

    //GETTERS AND SETTERS
    /**
     * If true, whitespace will be trimmed from the beginning and end of Strings
     * in this flat file
     */
    public void setTrim(boolean newValue) {
        trim = newValue;
    }

    /**
     * If true, Strings in this data structure will be unquoted.
     */
    public void setUnquote(boolean newValue) {
        unquote = newValue;
    }

    /**
     * If true, nulls will be returned, else "" will be returned instead.
     */
    public void setReturnNulls(boolean newValue) {
        returnNulls = newValue;
    }

    /**
     * Adds a new row to the end of the FlatFile
     *
     * @param newRow New row to add to FlatFile. Should be a List of Objects.
     */
    public void addRow(List newRow) {
        rows.add(newRow);
    }

    /**
     * Adds a new row (new ArrayList) to the end of the FlatFile
     */
    public void addRow() {
        rows.add(new ArrayList());
    }

    /**
     * Adds a new row at the end of the FlatFile. NOTE that the input array is
     * converted to a List via Arrays.asList, so changes to the list will
     * write-through to the array
     *
     * @param values Array of values to set
     */
    public void addRow(Object[] values) {
        addRow(Arrays.asList(values));
    }

    /**
     * Returns a row from the FlatFile. NOTE: This method catches the
     * IndexOutOfBoundsException if an invalid row number is entered.
     *
     * @param rowNumber zero-based integer for the row to return
     * @return List List of Objects representing the FlatFile row
     */
    private List getRow(int rowNumber) {
        try {
            return (List) rows.get(rowNumber);
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * Returns a row from this FlatFile as a String using the specified
     * delimiter
     *
     * @param rowNumber The row number to return
     * @param delimiter String to use as delimiter in output
     *
     * @return String This row, with fields separated by the delimiter
     */
    public String getRowString(int rowNumber, String delimiter) {

        //get the currentRow
        List row = getRow(rowNumber);
        if (row == null) {
            return "";
        }
        Iterator e = row.iterator();

        StringBuilder output = new StringBuilder(100);
        Object currentElement = "";

        while (e.hasNext()) {
            currentElement = e.next();

            applyTransformations(currentElement);

            if (currentElement != null) {
                output.append(currentElement.toString());
            }

            if (e.hasNext()) {
                output.append(delimiter);
            }
        }

        return output.toString();
    }

    /**
     * Returns the number of data rows in the FlatFile
     *
     * @return int The number of rows in the FlatFile
     */
    public int getNumRows() {
        return rows.size();
    }

    /**
     * Returns the (maximum) number of columns in the FlatFile. IE, The size of
     * the longest row, since each row may have a different number of elements.
     *
     * @return int The number of columns in the FlatFile
     */
    public int getNumCols() {
        int output = 0;

        for (Object row1 : rows) {
            List row = (List) row1;
            if (row != null) {
                output = Math.max(output, row.size());
            }
        }

        return output;
    }

    /**
     * Returns the number of columns for one row in the FlatFile.
     *
     * @param rowNum The zero-based row number
     * @return The number of columns in the specified row, or -1 if the row does
     * not exist
     */
    public int getNumCols(int rowNum) {
        if (rowNum < 0 || rowNum > rows.size()) {
            return -1;
        }

        List row = (List) rows.get(rowNum);

        int output = -1;
        if (row != null) {
            output = row.size();
        }

        return output;
    }

    /**
     * Gets an Object from a specified position within the FlatFile
     *
     * @param row zero-based integer for the row number
     * @param column zero-based integer for the column number
     * @return Object Object at the specified position within the FlatFile, or
     * "" if nothing is found
     */
    public Object getItem(int row, int column) {
        try {
            Object output = getRow(row).get(column);

            //apply transformations if asked
            applyTransformations(output);

            return output;
        } catch (Exception e) {
            //nada
        }

        return "";
    }

    public String getString(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof String) {
            return (String) output;
        }

        return (output != null) ? output.toString() : "";
    }

    public Integer getInteger(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Integer) {
            return (Integer) output;
        }

        return null;
    }

    public Date getDate(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Date) {
            return (Date) output;
        }

        return null;
    }

    public Boolean getBoolean(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Boolean) {
            return (Boolean) output;
        }

        return null;
    }

    /**
     * Overridden version of getItem
     */
    public Object getItem(int row, Integer column) {
        if (column == null) {
            return "";
        }

        return getItem(row, column.intValue());
    }

    /**
     * Sets (replaces) the value of an item at a specified position in the
     * FlatFile. If row or column is not found, it is created. USE WITH CARE.
     *
     * @param row zero-based row number to set at
     * @param column zero-based column number to set at
     * @param value value to set
     */
    public void setItem(int row, int column, Object value) {

        log.debug("FlatFile.setItem(" + row + ", " + column + ", " + value + ")");

        //if the row called for is not found, create enough rows to add it in
        while (getNumRows() <= row) {
            addRow();
        }
        log.debug("NUM ROWS: " + getNumRows());

        List rowToSet = getRow(row);
        log.debug("ROW TO SET: " + rowToSet);

        //if there are not enough items, add blanks ("") until there are
        while (getNumCols(row) <= column) {
            rowToSet.add("");
        }
        rowToSet.set(column, value);

    }

    /**
     * Outputs this FlatFile object to the filesystem
     *
     * @param filename The full filename of the output file
     * @param delimiter The delimiter to separate values
     * @return boolean True if the file output completed successfully
     */
    public boolean toFile(String filename, String delimiter) {

        try {
            //create a new file for read/write
            RandomAccessFile output = new RandomAccessFile(filename, "rw");

            Iterator rowsI = rows.iterator();
            while (rowsI.hasNext()) {
                List currentRow = (List) rowsI.next();

                if (currentRow == null) {
                    continue;
                }

                Iterator colsI = currentRow.iterator();
                while (colsI.hasNext()) {
                    Object currentItem = colsI.next();

                    //apply transformations if asked
                    applyTransformations(currentItem);

                    //make sure cell is not null or writeBytes will choke
                    if (currentItem == null) {
                        if (returnNulls) {
                            currentItem = "null";
                        } else {
                            currentItem = "";
                        }
                    }

                    //write the cell to output file 
                    output.writeBytes(currentItem.toString());

                    if (colsI.hasNext()) {
                        output.writeBytes(delimiter);
                    }
                }

                //output newline
                output.writeBytes(System.getProperty("line.separator"));
            }

            return true;

        } catch (Exception e) {
            log.debug("", e);
        }

        return false;

    }

    /**
     * Outputs this FlatFile object to a delimited String. Rows will be
     * separated by a System specific lne separator (the result of:
     * <code>System.getProperty("line.separator")</code>).
     *
     * @param delimiter The delimiter to separate values
     * @return String True if the file output completed successfully
     */
    public String toString(String delimiter) {

        int approxOutputSize = getNumRows() * getNumCols() * 25;
        StringBuilder output = new StringBuilder(approxOutputSize);

        Iterator rowsI = rows.iterator();
        while (rowsI.hasNext()) {
            List currentRow = (List) rowsI.next();

            if (currentRow == null) {
                continue;
            }

            Iterator colsI = currentRow.iterator();
            while (colsI.hasNext()) {
                Object currentItem = colsI.next();

                //apply transformations if asked
                applyTransformations(currentItem);

                //make sure cell is not null or writeBytes will choke
                if (currentItem == null) {
                    if (returnNulls) {
                        currentItem = "null";
                    } else {
                        currentItem = "";
                    }
                }

                //write the cell to output file 
                output.append(currentItem.toString());

                if (colsI.hasNext()) {
                    output.append(delimiter);
                }
            }

            //output newline
            output.append(System.getProperty("line.separator"));
        }

        return output.toString();
    }

    /**
     * Applies the selected String transformations (unquote, trim) to the input
     * String.
     */
    protected String applyTransformations(String input) {
        String output = input;

        if (trim) {
            output = StringUtils.trim(output);
        }
        if (unquote) {
            output = StringUtils.unQuote(output);
        }

        return output;
    }

    /**
     * Applies the selected transformations (returnNulls) to the input Object.
     */
    protected Object applyTransformations(Object input) {
        if (!returnNulls && input == null) {
            input = "";
        }

        if (input instanceof String) {
            return applyTransformations((String) input);
        }

        return input;
    }

    /**
     * For sorting
     */
    public void sort(Comparator c) {
        Collections.sort(rows, c);
    }
}
