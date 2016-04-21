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

import org.apache.log4j.Logger;

/**
 * The Timer class allows a graceful exit when an application is stalled due to
 * a networking timeout. Once the timer is set, it must be cleared via the
 * reset() method, or the timeout() method is called.
 * <p>
 * The timeout length is customizable, by changing the 'length' property, or
 * through the constructor. The length represents the length of the timer in
 * milliseconds.
 *
 * This work is adapted from the original Timer class by David Reilly, primarily
 * to throw an exception instead of calling System.exit() when the timeout is
 * reached.
 *
 * @author August Detlefsen for CodeMagi, Inc.
 * @author David Reilly (original author)
 * @see http://www.javacoffeebreak.com/articles/network_timeouts/
 */
public class Timer extends Thread {

    Logger log = Logger.getLogger(this.getClass());

    /**
     * Rate at which timer is checked
     */
    protected int m_rate = 100;

    /**
     * Length of timeout
     */
    private int m_length;

    /**
     * Time elapsed
     */
    private int m_elapsed;

    /**
     * Creates a timer of a specified length
     *
     * @paramlengthLength of time before timeout occurs
     */
    public Timer(int length) {
        // Assign to member variable
        m_length = length;

        log.debug("Instantiated Timer with length: " + length);

        // Set time elapsed
        m_elapsed = 0;
    }

    /**
     * Resets the timer back to zero
     */
    public synchronized void reset() {
        log.debug("Timer reset");
        m_elapsed = 0;
    }

    /**
     * Performs timer specific code
     */
    public void run() {
        log.debug("Timer running for: " + m_length);

        // Keep looping
        for (;;) {
            // Put the timer to sleep
            try {
                Thread.sleep(m_rate);
            } catch (InterruptedException ioe) {
                continue;
            }

            // Use 'synchronized' to prevent conflicts
            synchronized (this) {
                // Increment time remaining
                m_elapsed += m_rate;

                // Check to see if the time has been exceeded
                if (m_elapsed > m_length) {
                    // Trigger a timeout
                    timeout();
                }
            }

        }
    }

    // Override this to provide custom functionality
    public void timeout() throws TimeoutError {

        log.debug("Network timeout occurred.... terminating");
        throw new TimeoutError();
    }

}
