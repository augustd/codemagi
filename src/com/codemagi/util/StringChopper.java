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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class is a replacement for the flawed java.util.StringTokenizer Major
 * Differences:
 * <UL>
 * <LI>Supports multicharacter delimiters (only 1 delimiter String allowed per
 * instance, however)
 * <LI>Does not ignore multiple delimiters in sequence
 * <LI>No option to return tokens
 * <LI>No option to change delimiter mid-processing
 * </UL>
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class StringChopper implements Enumeration {

    Logger log = Logger.getLogger(this.getClass());

    private String inputString = null;
    private String token = null;

    private List vTokens = null;
    private Enumeration eTokens = null;

    private int tokensRemaining = -1;
    private int totalTokens = -1;

    /**
     * Constructor
     *
     * @param inputString The String to be tokenized
     */
    public StringChopper(String inputString) {
        this(inputString, " ");
    }

    /**
     * Constructor
     *
     * @param inputString The String to be tokenized
     * @param token The token to use
     */
    public StringChopper(String inputString, String token) {

        this.inputString = inputString;
        this.token = token;

        vTokens = splitFields(inputString, token);
        log.debug("new StringChopper(): vTokens: " + CollectionUtils.commaDelimit(vTokens));

        eTokens = Collections.enumeration(vTokens);
        log.debug("new StringChopper(): eTokens: " + eTokens.toString());

        totalTokens = vTokens.size();
        tokensRemaining = totalTokens;

    }

    public boolean hasMoreTokens() {
        return eTokens.hasMoreElements();
    }

    public String nextToken() {
        tokensRemaining--;
        return (String) eTokens.nextElement();
    }

    @Override
    public boolean hasMoreElements() {
        return eTokens.hasMoreElements();
    }

    @Override
    public Object nextElement() {
        tokensRemaining--;
        return eTokens.nextElement();
    }

    public int countTokens() {
        return tokensRemaining;
    }

    /**
     * Returns the delimiter-separated fields in a Vector
     *
     * @param source String containing delimited text
     * @param delimiter String consisting of the delimiter
     */
    private List<String> splitFields(String source, String delimiter) {

        List<String> output = new ArrayList<>();

        int delimiterLength = delimiter.length();
        int sourceLength = source.length();

        int i = 0;
        int j = 0;

        if (delimiter.length() > 0) {

            while (j + delimiterLength <= sourceLength) {
                if (source.substring(j, j + delimiterLength).equals(delimiter)) {

                    output.add(source.substring(i, j));
                    j += delimiterLength;
                    i = j;

                } else {
                    j++;
                }
            }

            output.add(source.substring(i));
        }

        return output;
    }

}
