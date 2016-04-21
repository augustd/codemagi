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

import java.io.*;

import org.apache.log4j.Logger;

/**
 * Provides static utility methods for working with Input/Output Streams.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class StreamUtils {

    static Logger log = Logger.getLogger("com.codemagi.util.StringUtils");

    /**
     * Singleton - Block constructor by design
     */
    private StreamUtils() {
    }


    /**
     * Reads data from an InputStream to an OutputStream.
     *
     * @param input       The InputStream to read from
     * @param output      The OutputStream to write to
     * @param bufferSize  The size of the byte buffer to use when writing from one to the other 
     * @return boolean    True if the operation was a success, false otherwise
     */
    public static boolean readWrite(InputStream input, OutputStream output, int bufferSize) {

        //sanity check
        if (input == null || output == null) {
            return false;
        }

        try {
            byte[] buffer = new byte[bufferSize];
            int byteCount = 0;

            while ((byteCount = input.read(buffer)) >= 0) {
                output.write(buffer, 0, byteCount);
            }

        } catch (IOException ioe) {
            return false;
        }

        return true;
    }

}
