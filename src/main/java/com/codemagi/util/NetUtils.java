/* 
 *  Copyright 2010 CodeMagi, Inc.
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

import java.net.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides static utility methods for working with network connections.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class NetUtils {

    static Logger log = LogManager.getLogger("com.codemagi.util.NetUtils");

    /**
     * Block constructor by design
     */
    private NetUtils() {
    }


    /** 
     * Tries to connect to a domain multiple times, to deal with domains that take extra time to resolve.
     * NOTE: Each failed resolution attempt will result in a 1 second delay. 
     *
     * @param domainName The domain name to resolve
     * @param numTries   The number of times to try to resolve the domain
     * @return String    The IP address the domain resolves to, or null if the domain cannot be resolved
     */
    public static String resolveDomain(String domainName, int numTries) {

        for (int i = 0; i < numTries; i++) {
            try {
                InetAddress inet = InetAddress.getByName(domainName);
                return inet.getHostAddress();  //address resolves correctly 

            } catch (UnknownHostException uhe) {
                try {
                    Thread.sleep(1000); //pause for a second before repeating 
                } catch (InterruptedException ie) {
                }
            }
        }

        return null;
    }

}
