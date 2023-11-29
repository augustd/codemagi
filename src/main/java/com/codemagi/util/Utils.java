/* 
 *  Copyright 2008 CodeMagi, Inc.
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

import java.util.*;

/**
 * Provides static general utility routines
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class Utils {

    /**
     * Block constructor by design
     */
    private Utils() {
    }

    /**
     * Returns the first non-null Integer in the passed array. If no non-null
     * values are present, or the input array is null, null is returned.
     */
    public static Integer nvl(Integer[] input) {
        if (input == null) {
            return null;
        }
        for (int i = 0; i < input.length; i++) {
            if (!isEmpty(input[i])) {
                return input[i];
            }
        }
        return null;
    }

    /**
     * Returns the first non-empty String in the passed arguments. If no
     * non-empty values are present, "" is returned.
     */
    public static String nvl(String... input) {
        for (String arg : input) {
            if (!isEmpty(arg)) {
                return arg;
            }
        }
        return "";
    }

    /**
     * Returns the input String if it is non-empty, the default otherwise
     */
    public static String nvl(String input, String sDefault) {
        if (Utils.isEmpty(input)) {
            return sDefault;
        }

        return input;
    }

    /**
     * Returns the String representation of the input Integer if it is non-null,
     * the default String otherwise
     */
    public static String nvl(Integer input, String sDefault) {
        if (input == null) {
            return sDefault;
        }

        return input + "";
    }

    /**
     * Converts null Objects to empty String ("").
     *
     * @return Object Empty String if the input was null, the input unchanged
     * otherwise
     */
    public static Object noNulls(Object input) {
        if (input == null) {
            return "";
        }

        return input;
    }

    /**
     * Returns "" if the input Object is null, otherwise returns output
     *
     * @param input String to test for empty/nullness
     * @param output String to output if the test String is not empty
     * @return String Empty String if the input was null, otherwise output
     */
    public static String noNulls(Object input, String output) {
        if (input == null) {
            return "";
        }

        return output;
    }

    /**
     * Returns the passed Integer, or zero if it is null.
     */
    public static Integer noNulls(Integer test) {
        return (test == null) ? 0 : test;
    }

    /**
     * Converts null Strings to empty String ("").
     */
    public static String noNulls(String test) {
        return noNulls(test, test, "");
    }

    /**
     * Outputs the String output if the test String is not empty, otherwise ""
     *
     * @param test String to test for emptyness
     * @param output String to output if the test String is not empty
     * @return Object Empty String if the input was null, the input unchanged
     * otherwise
     */
    public static String noNulls(String test, String output) {
        return noNulls(test, output, "");
    }

    /**
     * Outputs the String output if the test String is not empty, otherwise
     * outputs default
     *
     * @param test String to test for emptyness
     * @param output String to output if the test String is not empty
     * @param defaultOutput String to output if the test String is empty
     * @return Object Empty String if the input was null, the input unchanged
     * otherwise
     */
    public static String noNulls(String test, String output, String defaultOutput) {
        if (isEmpty(test)) {
            return defaultOutput;
        }

        return output;
    }

    /**
     * Converts null Objects to empty String ("").
     *
     * @return Object Empty String if the input was null, the input unchanged
     * otherwise
     */
    public static Object noNullsTrim(String input) {
        if (input == null) {
            return "";
        }

        return input.trim();
    }

    /**
     * Determines if an Object is null or empty
     */
    public static boolean isEmpty(Object value) {
        return (value == null);
    }

    /**
     * Determines if a string is null or empty
     *
     * @param value string to test
     * @return       <code>true</code> if the string is empty or null;
     * <code>false</code> otherwise
     */
    public static boolean isEmpty(String value) {
        return (value == null || value.trim().length() == 0);
    }

    /**
     * Determines if an array of Strings contains any null or empty elements
     *
     * @param value[] String array to test
     * @return       <code>true</code> if any of the String elements is empty or null;
     * <code>false</code> otherwise
     */
    public static boolean isEmpty(String[] value) {

        if (value == null) {
            return true;
        }

        int numElements = value.length;
        String currentElement = null;

        for (int i = 0; i < numElements; i++) {
            currentElement = value[i];

            if (isEmpty(currentElement)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if a StringBuffer is null or empty
     *
     * @param value StringBuffer to test
     * @return       <code>true</code> if the StringBuffer is empty or null;
     * <code>false</code> otherwise
     */
    public static boolean isEmpty(StringBuffer value) {
        return (value == null || value.length() == 0);
    }

    /**
     * Determines if a Collection of Strings is empty, ie: does not contain at
     * least one element that is not null or ""
     *
     * @param value String Collection to test
     * @return       <code>true</code> if all of the String elements is not "" or
     * null; <code>false</code> otherwise
     */
    public static boolean isEmpty(Collection value) {

        if (value == null) {
            return true;
        }

        Iterator e = value.iterator();

        Object currentObject = null;
        String currentString = null;

        while (e.hasNext()) {

            currentObject = e.next();
            if (currentObject != null) {

                try {
                    currentString = (String) currentObject;
                } catch (ClassCastException cce) {
                    //if we have a non-string object that is not null, return false
                    return false;
                }

                if (!isEmpty(currentString)) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * Determines if a Map is empty, ie: does not contain at least one element
     * that is not null or ""
     *
     * @param value Map to test
     * @return       <code>true</code> if all of the keys and values are "" or null;
     * <code>false</code> otherwise
     */
    public static boolean isEmpty(Map map) {

        if (map == null) {
            return true;
        }

        if (map.isEmpty()) {
            return true;
        }

        Iterator keys = map.keySet().iterator();

        while (keys.hasNext()) {

            Object key = keys.next();
            if (key == null) {
                continue;
            }

            try {
                String keyString = (String) key;

                if (!isEmpty(keyString)) {
                    return false;
                }

            } catch (ClassCastException cce) {
                //if we have a non-string object that is not null, return false
                return false;
            }

            Object value = map.get(key);
            if (value == null) {
                continue;
            }

            try {
                String valueString = (String) key;

                if (!isEmpty(valueString)) {
                    return false;
                }

            } catch (ClassCastException cce) {
                //if we have a non-string object that is not null, return false
                return false;
            }

        }

        return true;
    }

    /**
     * Determines if an array of Strings is non-empty, ie: contains at least one
     * element that is not null or ""
     *
     * @param value[] String array to test
     * @return       <code>true</code> if any of the String elements is not "" or
     * null; <code>false</code> otherwise
     */
    public static boolean isNonEmpty(String[] value) {

        int numElements = value.length;
        String currentElement = null;

        for (int i = 0; i < numElements; i++) {

            currentElement = value[i];

            if (currentElement != null && !(currentElement.length() == 0)) {
                return true;
            }

        }

        return false;
    }

    /**
     * Determines if a Vector of Strings is non-empty, ie: contains at least one
     * element that is not null or ""
     *
     * @param value String Vector to test
     * @return       <code>true</code> if any of the String elements is not "" or
     * null; <code>false</code> otherwise
     */
    public static boolean isNonEmpty(Vector value) {

        Enumeration e = value.elements();

        Object currentObject = null;
        String currentString = null;

        while (e.hasMoreElements()) {

            currentObject = e.nextElement();
            if (currentObject != null) {

                try {
                    currentString = (String) currentObject;
                } catch (ClassCastException cce) {
                    //if we have a non-string object that is not null, return true
                    return true;
                }

                if (!(currentString.length() == 0)) {
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * Strips the non numeric characters out of a value
     *
     * @param value value to be stripped
     * @return value with numbers only
     */
    public static String stripNonNumericChars(String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c >= '0' && c <= '9') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Adds TO_DATE function around the string specified
     *
     * @param value value to add TO_DATE to
     * @param format format of the value parameter
     * @return value inside TO_DATE function
     */
    public final static String toDate(String value, String format) {
        return "TO_DATE('" + value + "', '" + format + "')";
    }

    /**
     * MAIN METHOD IS FOR TESTING ONLY!
     */
    public static void main(String[] args) {

        String nullString = null;
        String[] nullStringArray = null;

        System.out.println("Testing nvl function, should return: \"3\"");
        String[] bleh = {nullString, "", "3"};
        System.out.println(nvl(bleh));

        System.out.println("Testing nvl function, should return: \"\"");
        String[] bleh2 = {nullString, "", nullString};
        System.out.println(nvl(bleh2));

        System.out.println("Testing nvl function, should return: \"\"");
        System.out.println(nvl(nullStringArray));
    }
}
