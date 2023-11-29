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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GridList is an expandable two-dimensional data structure of Objects,
 * implemented as an ArryList (rows) of ArrayLists (columns).
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class GridList implements java.io.Serializable {

    //set to true to view debug output
    Logger log = LogManager.getLogger(this.getClass());

    //members
    private ArrayList rows;
    private Map columnNames = new HashMap();

    //options
    private boolean unquote = false;
    private boolean trim = false;
    private boolean returnNulls = false;

    //constants
    private static final int DEFAULT_ELEMENT_SIZE = 25;

    /**
     * null constructor.
     */
    public GridList() {
        rows = new ArrayList();
    }

    /**
     * Constructs an empty GridList with the specified initial number of rows.
     */
    public GridList(int initialNumRows) {
        rows = new ArrayList(initialNumRows);
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
     * Sets a map of column names. Null input is skipped.
     */
    public void setColumnNames(Map newValue) {
        if (newValue != null) {
            columnNames = newValue;
        }
    }

    /**
     * Returns a Map containing the column names and their position in the grid
     * (String and Integer respectively).
     */
    public Map getColumnNames() {
        return columnNames;
    }

    /**
     * Sets ArrayList of rows
     */
    public void setRows(ArrayList newValue) {
        rows = newValue;
    }

    /**
     * Returns ArrayList of rows
     */
    public ArrayList getRows() {
        return rows;
    }

    /**
     * Adds a new row (new ArrayList) to the end of the GridList
     */
    private void addRow() {
        rows.add(new ArrayList());
    }

    /**
     * Adds a new row to the end of the GridList. Does nothing if the input is
     * null.
     *
     * @param newRow New row to add to GridList. Should be a List of Objects.
     */
    public void addRow(ArrayList newRow) {
        if (newRow != null) {
            rows.add(newRow);
        }
    }

    /**
     * Adds a new column to the end of the GridList
     */
    public void addColumn(String columnName) {
        Integer colPos = columnNames.size();

        log.debug(" Adding new column: " + columnName + " at position: " + colPos);

        columnNames.put(columnName.toUpperCase(), colPos);

        log.debug("COLUMNS: " + columnNames);
    }

    /**
     * Returns a row from the GridList. If a row number is entered that does not
     * exist in the grid, null is returned.
     *
     * @param rowNumber zero-based integer for the row to return
     * @return List List of Objects representing the GridList row
     */
    private ArrayList getRow(int rowNumber) {
        ArrayList output = null;

        try {
            output = (ArrayList) rows.get(rowNumber);

        } catch (IndexOutOfBoundsException ioobe) {
            log.debug(" Adding new row at position: " + rowNumber);

            output = new ArrayList(getNumCols());
            while (rows.size() < rowNumber - 1) {
                rows.add(new ArrayList());
            }
            rows.add(output);

        } catch (Exception e) {
            log.debug("Some other exception...", e);
        }

        return output;
    }

    /**
     * Returns a column from the GridList. If a column name that does not exist
     * is passed, an empty ArrayList is returned.
     *
     * @param colName The name of the column to return
     * @return List List containing the objects in the column
     */
    public List getColumn(String colName) {
        ArrayList output = new ArrayList(getNumRows());
        for (int i = 0; i < getNumRows(); i++) {
            output.add(getItem(i, colName));
        }
        return output;
    }

    /**
     * Returns the number of data rows in the GridList
     *
     * @return int The number of rows in the GridList
     */
    public int getNumRows() {
        return rows.size();
    }

    /**
     * Returns the (maximum) number of columns in the GridList. IE, The size of
     * the longest row, since each row may have a different number of elements.
     * TODO: Remove iteration, add tracking.
     *
     * @return int The number of columns in the GridList
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
     * Returns the number of columns for one row in the GridList.
     *
     * @param rowNum The zero-based row number
     * @return int The number of columns in the specified row, or -1 if the row
     * does not exist
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
     * Gets an Object from a specified position within the GridList
     *
     * @param row zero-based integer for the row number
     * @param column zero-based integer for the column number
     * @return Object Object at the specified position within the GridList, or
     * "" if nothing is found
     */
    public Object getItem(int row, int column) {
        try {
            Object output = getRow(row).get(column);

            //apply transformations if asked
            applyTransforms(output);

            return output;
        } catch (Exception e) {
            //nada
        }

        return "";
    }

    /**
     * Overridden version of getItem that accepts row number and column name.
     */
    public Object getItem(int row, String columnName) {
        int col = getColumnNumber(columnName);

        return getItem(row, col);
    }

    public String getString(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof String) {
            return (String) output;
        }

        return (output != null) ? output.toString() : "";
    }

    public String getString(int row, String columnName) {
        //get column number from map
        int col = getColumnNumber(columnName);

        return getString(row, col);
    }

    public Integer getInteger(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Integer) {
            return (Integer) output;
        }

        return null;
    }

    public Integer getInteger(int row, String columnName) {
        //get column number from map
        int col = getColumnNumber(columnName);

        return getInteger(row, col);
    }

    public Float getFloat(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Float) {
            return (Float) output;
        } else if (output instanceof String) {
            try {
                return Float.parseFloat((String) output);
            } catch (Exception e) {
                //nada
            }
        }

        return null;
    }

    public Float getFloat(int row, String columnName) {
        //get column number from map
        int col = getColumnNumber(columnName);

        return getFloat(row, col);
    }

    public Double getDouble(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Double) {
            return (Double) output;
        } else if (output instanceof String) {
            try {
                return Double.parseDouble((String) output);
            } catch (Exception e) {
                //nada
            }
        }

        return null;
    }

    public Double getDouble(int row, String columnName) {
        //get column number from map
        int col = getColumnNumber(columnName);

        return getDouble(row, col);
    }

    public Date getDate(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Date) {
            return (Date) output;
        }

        return null;
    }

    public Date getDate(int row, String columnName) {
        //get column number from map
        int col = getColumnNumber(columnName);

        return getDate(row, col);
    }

    public Boolean getBoolean(int row, int column) {
        Object output = getItem(row, column);

        if (output instanceof Boolean) {
            return (Boolean) output;
        }

        return null;
    }

    public Boolean getBoolean(int row, String columnName) {
        //get column number from map
        int col = getColumnNumber(columnName);

        return getBoolean(row, col);
    }

    /**
     * Returns the column number (zero-based) for the specified column name
     *
     * @param columnName The name of the column
     * @return int The column's number in the result set
     */
    private int getColumnNumber(String columnName) {

        //prevent NullPointerException
        if (Utils.isEmpty(columnName)) {
            return 0;
        }

        Integer theInt = (Integer) columnNames.get(columnName.toUpperCase());

        if (theInt == null) {
            return 0;  //changed from -1 for fault tolerance
        }
        return theInt;
    }

    /**
     * Returns the column name, for the specified column number (zero-based)
     *
     * @param columnNum The number of the column (zero-based)
     * @return String The column's name
     */
    public String getColumnName(int columnNum) {

        ArrayList cols = new ArrayList(columnNames.keySet());

        String columnName = (String) cols.get(columnNum);

        return columnName;
    }

    /**
     * Sets (replaces) the value of an item at a specified position in the
     * GridList. If row or column is not found, it is created. USE WITH CARE.
     *
     * @param row zero-based row number to set at
     * @param column zero-based column number to set at
     * @param value value to set
     * @return Object The Object that was previously at that position in the
     * grid
     */
    public Object setItem(int row, int column, Object value) {

        log.debug("GridList.setItem(" + row + ", " + column + ", " + value + ")");

        //if the row called for is not found, create enough rows to add it in
        rows.ensureCapacity(row);
        log.debug("NUM ROWS: " + getNumRows());

        ArrayList rowToSet = getRow(row);
        log.debug("ROW TO SET: " + rowToSet);

        //if there are not enough items, add blanks ("") until there are
        log.debug("ROW CAPACITY: " + rowToSet.size());
        rowToSet.ensureCapacity(column);
        log.debug("ROW CAPACITY: " + rowToSet.size());
        return rowToSet.set(column, value);
    }

    /**
     * Sets (replaces) the value of an item at a specified position in the
     * GridList. If row or column is not found, it is created. USE WITH CARE.
     *
     * @param row zero-based row number to set at
     * @param columnName column name to set at
     * @param value value to set
     * @return Object The Object that was previously at that position in the
     * grid
     */
    public Object setItem(int row, String columnName, Object value) {

        log.debug("GridList.setItem(" + row + ", " + columnName + ", " + value + ")");

        //if the row called for is not found, create enough rows to add it in       
        rows.ensureCapacity(row);
        log.debug("NUM ROWS: " + getNumRows());

        ArrayList rowToSet = getRow(row);
        log.debug("ROW TO SET: " + rowToSet);

        log.debug("setItem(): COLUMNS: " + columnNames);
        int columnNumber = getColumnNumber(columnName);
        log.debug("setItem(): COLUMN NUMBER: " + columnNumber);

        //if there are not enough items, add blanks ("") until there are        
        ensureCapacity(rowToSet, columnNumber + 1);
        log.debug("ROW CAPACITY: " + rowToSet.size() + " ENSURING CAPACITY: " + (columnNumber + 1));
        log.debug("ROW CAPACITY: " + rowToSet.size());
        return rowToSet.set(columnNumber, value);
    }

    private void ensureCapacity(List list, int capacity) {
        while (list.size() < capacity) {
            list.add("");
        }
    }

    /**
     * Outputs this GridList object to the filesystem
     *
     * @param filename The full filename of the output file
     * @param delimiter The delimiter to separate values
     * @return boolean True if the file output completed successfully
     */
    public boolean toFile(String filename, String delimiter) {

        try {
            //create a new file for read/write
            RandomAccessFile output = new RandomAccessFile(filename, "rw");

            output.writeBytes(toString(delimiter));

            return true;

        } catch (Exception e) {
            log.debug("", e);
        }

        return false;

    }

    /**
     * Outputs this Object as a tab delimited String, with a System specific
     * line separator
     */
    @Override
    public String toString() {
        return toString("\t", System.getProperty("line.separator"));
    }

    /**
     * Outputs this GridList object to a delimited String. Rows will be
     * separated by a System specific line separator (the result of:
     * <code>System.getProperty("line.separator")</code>).
     *
     * @param delimiter The delimiter to separate values
     * @return String True if the file output completed successfully
     */
    public String toString(String delimiter) {
        return toString(delimiter, System.getProperty("line.separator"));
    }

    /**
     * Outputs this GridList object to a delimited String. Rows will be
     * separated by the specified line separator
     *
     * @param delimiter The delimiter to separate values
     * @param lineSep The line separator to use
     * @return String True if the file output completed successfully
     */
    public String toString(String delimiter, String lineSep) {
        return toString(delimiter, lineSep, true);
    }

    /**
     * Outputs this GridList object to a delimited String. Rows will be
     * separated by the specified line separator
     *
     * @param delimiter The delimiter to separate values
     * @param lineSep The line separator to use
     * @param includeHeaders True if the output Strin should include the column
     * header names
     * @return String True if the file output completed successfully
     */
    public String toString(String delimiter, String lineSep, boolean includeHeaders) {

        int approxOutputSize = getNumRows() * getNumCols() * DEFAULT_ELEMENT_SIZE;
        StringBuilder output = new StringBuilder(approxOutputSize);

        //output header row
        if (includeHeaders) {
            Set headers = columnNames.keySet();
            Iterator hi = headers.iterator();
            while (hi.hasNext()) {
                String header = (String) hi.next();
                output.append(header);

                if (hi.hasNext()) {
                    output.append(delimiter);
                }
            }
            output.append(lineSep);
        }

        //output data
        for (int i = 0; i < rows.size(); i++) {
            String row = getRowString(i, delimiter);
            output.append(row);

            //output newline
            output.append(lineSep);

        }

        return output.toString();
    }

    /**
     * Returns a row from this GridList as a String using the specified
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
        Iterator i = row.iterator();

        StringBuilder output = new StringBuilder(row.size() * DEFAULT_ELEMENT_SIZE);
        Object currentItem = "";

        while (i.hasNext()) {
            currentItem = i.next();

            applyTransforms(currentItem);

            if (currentItem != null) {
                output.append(currentItem.toString());
            } else {
                output.append("");
            }

            if (i.hasNext()) {
                output.append(delimiter);
            }
        }

        return output.toString();
    }

    /**
     * Applies the selected String transformations (unquote, trim) to the input
     * String.
     */
    protected String applyTransforms(String input) {
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
    protected Object applyTransforms(Object input) {
        if (!returnNulls && input == null) {
            input = "";
        }

        if (input instanceof String) {
            return applyTransforms((String) input);
        }

        return input;
    }

    /**
     * Returns a shallow clone of this GridList (the elements themselves are not
     * copies)
     */
    public Object clone() {
        GridList clone = new GridList(this.getNumRows());

        clone.setTrim(trim);
        clone.setUnquote(unquote);
        clone.setReturnNulls(returnNulls);
        try {
            clone.setColumnNames((Map) ((HashMap) columnNames).clone());
        } catch (Exception e) {
            //ClassCastException most likely...
            log.debug("", e);
            clone.setColumnNames(columnNames);
        }

        for (int i = 0; i < getNumRows(); i++) {
            ArrayList newRow = (ArrayList) (getRow(i).clone());
            clone.addRow(newRow);
        }

        return clone;
    }

}
