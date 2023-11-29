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

/**
 * Models name-value data structure.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class Tuple {

    private String name = null;
    private Object value = null;

    /**
     * Default no-arg constructor
     */
    public Tuple() {
        //no-op
    }

    /**
     * Constructor.
     *
     * @param name Name of this Tuple
     * @param value value (Object) of this Tuple
     */
    public Tuple(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of this Tuple
     *
     * @return String The name of this Tuple
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value (Object) of this Tuple
     *
     * @return Object The value (Object) of this Tuple
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the name of this Tuple
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value of this Tuple
     *
     * @param value The new value (Object)
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
