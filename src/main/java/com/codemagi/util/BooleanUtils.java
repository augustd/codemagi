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
 * Provides static utility methods for working with Booleans.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class BooleanUtils {

    /**
     * Block constructor by design
     */
    private BooleanUtils() {
    }

    /**
     * Returns a Boolean that is the boolean AND of the two inputs. Null input values are assumed FALSE. 
     */
    public static Boolean and(Boolean in1, Boolean in2) {
        if (in1 == null) {
            in1 = false;
        }
        if (in2 == null) {
            in2 = false;
        }

        return in1 && in2;
    }

    /**
     * Returns a Boolean that is the boolean OR of the two inputs. Null input
     * values are assumed FALSE.
     */
    public static Boolean or(Boolean in1, Boolean in2) {
        if (in1 == null) {
            in1 = false;
        }
        if (in2 == null) {
            in2 = false;
        }

        return in1 || in2;
    }

    /**
     * Returns a String representing the Boolean argument as yes/no
     */
    public static String toYesNo(Boolean input) {
        if (input == null || !input) {
            return "no";
        }

        return "yes";
    }

}
