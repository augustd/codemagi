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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class defines static security utility routines.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public abstract class SecurityUtils {

    static Logger log = LogManager.getLogger("com.codemagi.util.SecurityUtils");

    /**
     * This class is not meant to be instantiated
     */
    private SecurityUtils() {
    }

    /**
     * Returns true if the calling class is in the stack trace.
     *
     * @param className The fully qualified class name to be checked
     */
    public static boolean isAllowedCallingClass(String className) {

        if (Utils.isEmpty(className)) {
            return false;
        }

        boolean output = false;

        Throwable t = new Throwable();
        StackTraceElement[] trace = t.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            StackTraceElement element = trace[i];
            if (className.equals(element.getClassName())) {
                output = true;
                break;
            }
        }

        if (!output) {
            log.warn("WARNING: Attempt to call method from a non-approved class");
        }

        return output;
    }

}
