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

/**
 * Provides static utility methods for working with arrays.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class ArrayUtils {

    /**
     * Block constructor by design
     */
    private ArrayUtils() {
    }

    /**
     * returns a comma-delimited String of the Objects (using each Object's toString() method) in the passed array
     */
    public static String commaDelimit(Object[] array) {
        return delimit(array, ", ");
    }

    /**
     * Returns a delimited String of the Objects (using each Object's toString()
     * method) in the passed array and the passed delimiter String.
     */
    public static String delimit(Object[] array, String delimiter) {

        if (array == null || array.length == 0) {
            return "";
        }

        StringBuffer output = new StringBuffer(32 * array.length);

        for (int i = 0; i < array.length; i++) {
            Object element = array[i];

            if (element == null) {
                output.append("null");
            } else {
                output.append(element.toString());
            }

            if (i < array.length) {
                output.append(delimiter);
            }
        }

        return output.toString();
    }

    /**
     * returns a comma-delimited String of the bytes in the passed array
     */
    public static String commaDelimit(byte[] array) {
        return delimit(array, ", ");
    }

    /**
     * Returns a delimited String of the bytes in the passed array and the
     * passed delimiter String.
     */
    public static String delimit(byte[] array, String delimiter) {

        if (array == null || array.length == 0) {
            return "";
        }

        StringBuffer output = new StringBuffer(32 * array.length);

        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                output.append(delimiter);
            }

            output.append(array[i]);
        }

        return output.toString();
    }

    /**
     * returns a comma-delimited String of the ints in the passed array
     */
    public static String commaDelimit(int[] array) {
        return delimit(array, ", ");
    }

    /**
     * Returns a delimited String of the ints in the passed array and the passed
     * delimiter String.
     */
    public static String delimit(int[] array, String delimiter) {

        if (array == null || array.length == 0) {
            return "";
        }

        StringBuffer output = new StringBuffer(32 * array.length);

        for (int i = 0; i < array.length; i++) {

            if (i != 0) {
                output.append(delimiter);
            }

            output.append(array[i]);
        }

        return output.toString();
    }

    /**
     * returns a comma-delimited String of the chars in the passed array
     */
    public static String commaDelimit(char[] array) {
        return delimit(array, ", ");
    }

    /**
     * Returns a delimited String of the chars in the passed array and the
     * passed delimiter String.
     */
    public static String delimit(char[] array, String delimiter) {

        if (array == null || array.length == 0) {
            return "";
        }

        StringBuffer output = new StringBuffer(32 * array.length);

        for (int i = 0; i < array.length; i++) {

            if (i != 0) {
                output.append(delimiter);
            }

            output.append(array[i]);
        }

        return output.toString();
    }

}
