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

import java.util.*;
import org.apache.log4j.Logger;

/**
 * This class defines static utility methods for working with Collections.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class CollectionUtils {

    static Logger log = Logger.getLogger("com.codemagi.util.CollectionUtils");

    /**
     * Block constructor by design
     */
    private CollectionUtils() {
    }


    /**
     * Returns a comma-delimited String of the Objects (using each Object's toString() method) in the passed List
     */
    public static String commaDelimit(Collection list) {
        return delimit(list, ", ");
    }

    /**
     * returns a comma-delimited String of the Objects (using each Object's
     * toString() method) in the passed array
     */
    public static String commaDelimit(Object[] array) {
        return delimit(array, ", ");
    }

    /**
     * Returns a delimited String of the Objects (using each Object's toString()
     * method) in the passed List and the passed delimiter String.
     */
    public static String delimit(Collection list, String delimiter) {

        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuffer output = new StringBuffer(32 * list.size());

        Iterator i = list.iterator();
        while (i.hasNext()) {
            Object element = i.next();

            if (element == null) {
                output.append("null");
            } else {
                output.append(element.toString());
            }

            if (i.hasNext()) {
                output.append(delimiter);
            }
        }

        return output.toString();
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

            if (i < array.length - 1) {
                output.append(delimiter);
            }
        }

        return output.toString();
    }

    /**
     * Returns a List containing the un-delimited items in the passed String
     *
     * @param input The String to un-delimit
     * @param delimiter The String to use as the delimiter
     */
    public static List unDelimit(String input, String delimiter) {
        //sanity check
        if (Utils.isEmpty(input) || Utils.isEmpty(delimiter)) {
            return new ArrayList();
        }

        String[] values = input.split(delimiter);

        return Arrays.asList(values);
    }

    /**
     * Returns a Set containing the un-delimited items in the passed String
     *
     * @param input The String to un-delimit
     * @param delimiter The String to use as the delimiter
     */
    public static Set unDelimitAsSet(String input, String delimiter) {
        //sanity check
        if (Utils.isEmpty(input) || Utils.isEmpty(delimiter)) {
            return new HashSet();
        }

        String[] values = input.split(delimiter);

        return asSet(values);
    }

    /**
     * Returns a Set built from the input array. NOTE: Duplicate elements in the
     * array will be skipped!
     */
    public static Set asSet(Object[] input) {
        //sanity check
        if (input == null) {
            return new HashSet();
        }

        HashSet output = new HashSet(input.length);

        for (Object o : input) {
            output.add(o);
        }

        return output;
    }

    /**
     * Replaces an object in a list
     */
    public static void replace(List list, Object obj) {
        list.set(list.indexOf(obj), obj);
    }

    /**
     * Returns the elements of List a that are not in List b.
     */
    public static List notIn(List a, List b) {

        List output = new ArrayList();
        Iterator i = a.iterator();
        while (i.hasNext()) {
            Object takeAway = i.next();

            if (!b.contains(takeAway)) {
                output.add(takeAway);
            }
        }

        return output;
    }

    /**
     * Returns the last non-null element in a list
     */
    public static Object last(List input) {
        if (input == null) {
            return null;
        }

        for (int i = input.size() - 1; i >= 0; i--) {
            Object output = input.get(i);
            if (output != null) {
                return output;
            }
        }
        return null;
    }

    /**
     * Returns the last non-null element in a collection
     */
    public static Object last(Collection input) {
        if (input == null) {
            return null;
        }

        Object[] colArray = input.toArray();

        for (int i = colArray.length - 1; i >= 0; i--) {
            Object output = colArray[i];
            if (output != null) {
                return output;
            }
        }
        return null;
    }

    /**
     * Returns the first VALUE in a Map
     */
    public static Object firstValue(Map input) {
        if (input == null) {
            return null;
        }

        Iterator i = input.entrySet().iterator();
        Map.Entry first = (Map.Entry) i.next();

        if (first != null) {
            return first.getValue();
        }

        return null;
    }

    /**
     * Returns the first KEY in a Map
     */
    public static Object firstKey(Map input) {
        if (input == null) {
            return null;
        }

        Iterator i = input.entrySet().iterator();
        Map.Entry first = (Map.Entry) i.next();

        if (first != null) {
            return first.getKey();
        }

        return null;
    }

    /**
     * Emulates the putAt function in OrderedTable for a LinkedHashMap.
     */
    public static LinkedHashMap putAt(LinkedHashMap input, Object key, Object value, int where) {

        //sanity check
        if (input == null) {
            LinkedHashMap output = new LinkedHashMap(1);
            output.put(key, value);
            return output;
        }

        LinkedHashMap output = new LinkedHashMap(input.size() + 1);
        int index = 0;

        Iterator i = input.keySet().iterator();
        while (i.hasNext()) {
            if (index == where) {
                output.put(key, value);
            }

            //add the original key back in
            Object inputKey = i.next();
            Object inputValue = input.get(inputKey);
            if (inputKey != null && !inputKey.equals(key)) { // ensure new input is not overwritten 
                output.put(inputKey, inputValue);
            }

            index++;
        }

        if (index <= where) {
            output.put(key, value);
        }

        return output;
    }

    /**
     * Main method for testing only!
     */
    public static void main(String[] args) {

        //test for method: notIn()
        List orig = new ArrayList(2);
        orig.add(new Integer(1));
        orig.add(new Integer(2));

        List param = new ArrayList(3);
        param.add(new Integer(1));
        param.add(new Integer(3));
        param.add(new Integer(5));

        System.out.println("ORIG: " + orig);
        System.out.println("PARAM: " + param);
        System.out.println("REMOVE: " + notIn(orig, param));
        System.out.println("ADD: " + notIn(param, orig));

        //test for method: last();
        List test = new ArrayList(6);
        test.add(new Integer(1));
        test.add(new Integer(2));
        test.add(new Integer(3));
        test.add(new Integer(4));
        test.add(new Integer(5));
        test.add(null);

        System.out.println("");
        System.out.println("TEST LIST: " + test);
        System.out.println("LAST: " + last(test));
    }
}
