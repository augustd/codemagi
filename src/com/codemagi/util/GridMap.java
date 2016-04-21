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

import java.awt.Point;
import java.util.*;
import org.apache.log4j.*;

/**
 * GridMap is an expandable two-dimensional data structure of Objects, backed by
 * a HashMap. Keys are generated as Points from the input row and column.
 * <P>
 * In general, for a fully filled grid (such as a result set from a database) a
 * List of Lists will provide better performance. This data structure is better
 * suited to datasets where there are many outlying values and it doesn't make
 * sense to allocate space for rows and columns that will never be filled.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class GridMap implements java.io.Serializable {

    Logger log = Logger.getLogger(this.getClass());

    //members
    private Map map;
    private Map columnNames = new HashMap();
    private Map rowNames = new HashMap();

    private int numRows = 0;
    private int numCols = 0;

    //options for output
    private boolean unquote = false;
    private boolean trim = false;
    private boolean returnNulls = false;

    /**
     * Constructs an empty GridMap with the default initial capacity and load
     * factor.
     */
    public GridMap() {
        map = new HashMap();
    }

    /**
     * Constructs an empty GridMap with the specified initial capacity and
     * default load factor.
     */
    public GridMap(int initialCapacity) {
        map = new HashMap(initialCapacity);
    }

    /**
     * Constructs an empty GridMap with the specified initial capacity and load
     * factor.
     */
    public GridMap(int initialCapacity, int loadFactor) {
        map = new HashMap(initialCapacity, loadFactor);
    }

    //GETTERS AND SETTERS
    /**
     * If true, whitespace will be trimmed from the beginning and end of Strings
     * (default false).
     */
    public void setTrim(boolean newValue) {
        trim = newValue;
    }

    /**
     * If true, Strings in this data structure will be unquoted (default false).
     */
    public void setUnquote(boolean newValue) {
        unquote = newValue;
    }

    /**
     * If true, null values will be returned by getString(), else "" will be
     * returned instead (default false).
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
     * Returns the (maximum) number of data rows in the GridMap. Since each
     * column may have a different number of elements, the number returned is
     * the size of the longest column.
     *
     * @return int The number of rows in the GridMap
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Returns the (maximum) number of columns in the GridMap. IE: The size of
     * the longest row, since each row may have a different number of elements.
     *
     * @return int The number of columns in the GridMap
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Applies the selected String transformations (unquote, trim) to the input
     * String.
     */
    protected String applyTransformations(String input) {
        if (returnNulls && input == null) {
            return null;
        }

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
     * Gets an Object from a specified position within the GridMap
     *
     * @param row zero-based integer for the row number
     * @param column zero-based integer for the column number
     * @return Object Object at the specified position within the GridMap, or ""
     * if returnNulls is false
     */
    public Object getItem(int row, int column) {

        log.debug("getItem( " + row + ", " + column + " )");

        Point p = new Point(row, column);

        Object output = map.get(p);

        if (output == null && !returnNulls) {
            output = "";
        }

        return output;
    }

    /**
     * Overridden version of getItem that accepts row number and column name.
     */
    public Object getItem(int row, String columnName) {
        int col = getColumnNumber(columnName);

        return getItem(row, col);
    }

    /**
     * Returns a String representation of the Object contained in the GridMap.
     * If the Object is not a String, this methods returns the result of that
     * Object's toString() method.
     */
    public String getString(int row, int column) {
        String output = null;
        Object obj = getItem(row, column);

        if (obj != null) {
            output = obj.toString();
        }

        return applyTransformations(output);
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
     * Sets or replaces the value of an item at a specified position in the
     * GridMap. If row or column is not found, it is created.
     *
     * @param row zero-based row number to set at
     * @param column zero-based column number to set at
     * @param value value to set
     */
    public void setItem(int row, int column, Object value) {

        Point p = new Point(row, column);

        map.put(p, value);

        numRows = Math.max(numRows, row);
        numCols = Math.max(numCols, column);
    }

    /**
     * Sets (replaces) the value of an item at a specified position in the
     * GridMap. If row or column is not found, it is created. USE WITH CARE.
     *
     * @param row zero-based row number to set at
     * @param columnName column name to set at
     * @param value value to set
     */
    public void setItem(int row, String columnName, Object value) {

        log.debug("GridMap.setItem(" + row + ", " + columnName + ", " + value + ")");

        int columnNumber = getColumnNumber(columnName);

        //if the new column name does not exist yet, add it
        if (columnNumber < 0 && !Utils.isEmpty(columnName)) {
            columnNumber = addColumn(columnName);
        }

        setItem(row, columnNumber, value);
    }

    /**
     * Adds a new column to the end of the GridList
     */
    public int addColumn(String columnName) {
        Integer colPos = columnNames.size();

        log.debug(" Adding new column: " + columnName + " at position: " + colPos);

        columnNames.put(columnName.toUpperCase(), colPos);

        log.debug("COLUMNS: " + columnNames);

        return colPos;
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
            return -1;  //changed from -1 for fault tolerance
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

}
