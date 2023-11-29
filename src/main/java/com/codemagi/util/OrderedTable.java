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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * OrderedTable class implements a HashMap whose items can be iterated in the
 * order they were added.
 *
 * NOTE: Use java.util.LinkedHashMap if possible. However, OrderedTable is not
 * d3precated or scheduled for removal from the API.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class OrderedTable extends java.util.HashMap implements Cloneable {

    //set to true to view debug output
    Logger log = LogManager.getLogger(this.getClass());

    //Vector holds the keys to this Hashtable in the order they were inserted
    protected List orderedKeys = new ArrayList(3);

    /**
     * Default null constructor
     */
    public OrderedTable() {
    }

    public OrderedTable(int size) {
        super(size);

        orderedKeys = new ArrayList(size);
    }

    public OrderedTable(int size, float factor) {
        super(size, factor);

        orderedKeys = new ArrayList(size);
    }

    public List orderedKeys() {
        return orderedKeys;
    }

    public List orderedValues() {
        ArrayList output = new ArrayList(orderedKeys.size());
        Iterator i = orderedKeys.iterator();
        while (i.hasNext()) {
            Object key = i.next();
            Object value = get(key);
            output.add(value);
        }

        return output;
    }

    public Set keySet() {
        return new LinkedHashSet(orderedKeys);
    }

    /**
     * Returns the position of the given arguement in the keys of this
     * OrderedTable.
     *
     * @param value The key to search for
     * @return int The index of the key in the table, or -1 if it is not found
     */
    public int indexOf(Object value) {
        return orderedKeys.indexOf(value);
    }

    /**
     * This is a simple extension to the inherited method of Hashtable. It
     * assigns the object @value to the database using the key @key.
     * Additionally, it adds the element to the end of the ordering.
     */
    synchronized public Object put(Object key, Object value) {

        Object ret = super.put(key, value);

        //check whether we already have this Object as a key
        if (ret == null) {
            //nope, insert new
            orderedKeys.add(key);
        }

        return super.put(key, value);
    }

    /**
     * Inserts an element at the specified position within the OrderedTable. If
     * the key already existed in the table, it will be moved to the new
     * position.
     *
     * @return Object The previous value of the specified key in this hashtable,
     * or null if it did not have one.
     */
    synchronized public Object putAt(Object key, Object value, int where) {

        Object ret = super.put(key, value);

        //check whether we already have this Object as a key
        if (ret == null) {
            //nope, insert new
            log.debug("NEW OBJECT: " + key + " ADDED AT: " + where);
            orderedKeys.add(where, key);
        } else {
	    //already in the vector, move it to new home
            //ie: remove and re-insert
            log.debug("MOVING OBJECT: " + key + " TO: " + where);
            boolean works = orderedKeys.remove(key);
            orderedKeys.add(where, key);
        }

        return ret;
    }

    public Object remove(Object key) {
        //remove from vector
        orderedKeys.remove(key);

        //remove from hash
        return super.remove(key);
    }

    /**
     * Removes the key/value at the specified index
     */
    public Object remove(int which) {
        Object key = orderedKeys.get(which);

        orderedKeys.remove(which);

        return remove(key);
    }

    /**
     * Returns the Object at the specified position in the ordered List
     */
    public Object get(int which) {
        Object key = orderedKeys.get(which);

        return get(key);
    }

    /**
     * Returns the key at the specified position in the ordered List
     */
    public Object getKey(int which) {
        Object key = orderedKeys.get(which);

        return key;
    }

    public java.util.Enumeration keys() {
        return Collections.enumeration(orderedKeys);
    }

    /**
     * NOTE: Output Collection from values method is NOT ordered
     */
    public java.util.Collection values() {
        return super.values();
    }

    /**
     * Returns an iterator over the VALUES of the table
     */
    public java.util.Iterator iterator() {
        Iterator keys = orderedKeys.iterator();
        Vector values = new Vector(orderedKeys.size());

        while (keys.hasNext()) {
            Object key = keys.next();

            Object o = super.get(key);
            values.add(o);
        }

        return values.iterator();
    }

    public void clear() {
        super.clear();

        orderedKeys.clear();
    }

    public Object elementAt(int which) {
        if (which > -1 && which < size()) {
            Object key = orderedKeys.get(which);

            return super.get(key);
        }
        return null;
    }

    public Object add(Object value) {
        return put(value, value);
    }

    /**
     * Returns a copy of this OrderedTable as a java.util.LinkedHashMap
     */
    public LinkedHashMap getLinkedHashMap() {
        LinkedHashMap output = new LinkedHashMap(this.size());
        Iterator i = this.orderedKeys().iterator();
        while (i.hasNext()) {
            Object key = i.next();
            Object value = this.get(key);

            output.put(key, value);
        }

        return output;
    }

    /**
     * Returns a copy of this Object
     */
    @Override
    public Object clone() {
        OrderedTable output = (OrderedTable) super.clone();
        Collections.copy(output.orderedKeys, orderedKeys);

        return output;
    }

    /**
     * MAIN METHOD FOR TESTING ONLY!
     */
    public static void main(String[] args) {

        System.out.println("Creating table");
        OrderedTable t = new OrderedTable();

        System.out.println("Adding default elements");
        t.put("1", "Joebob");
        t.put("2", "Jimbob");
        t.put("3", "Jimjoebob");

        System.out.println("Adding element at position 0");
        t.putAt("", "SELECT NAME", 0);

        System.out.println("Adding duplicate element at position 0");
        t.putAt("", "SELECT NAME", 0);

        System.out.println("Moving duplicate element to position 3");
        t.putAt("", "SELECT NAME", 3);

        System.out.println("Moving duplicate element to position 0");
        t.putAt("", "SELECT NAME", 0);

        System.out.println("Contents:");
        java.util.Enumeration e = t.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String val = (String) t.get(key);
            System.out.println("Tuple: " + key + " - " + val);
        }

        System.out.println("Cloning table");
        OrderedTable ct = (OrderedTable) t.clone();

        System.out.println("Adding duplicate element to clone at position 0");
        ct.putAt("", "SELECT NAME", 0);

        System.out.println("Adding duplicate element to clone at position 0");
        ct.putAt("", "SELECT NAME", 0);

        System.out.println("Clone Contents:");
        java.util.Enumeration ce = ct.keys();
        while (ce.hasMoreElements()) {
            String key = (String) ce.nextElement();
            String val = (String) ct.get(key);
            System.out.println("Tuple: " + key + " - " + val);
        }

    }

}
