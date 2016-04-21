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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides static utility methods for working with databases.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class DbUtils {

    public static final SimpleDateFormat INSERT_FORMAT
            = new SimpleDateFormat("'TO_DATE('''MM dd yyyy HH mm ss'', '''MM DD YYYY HH24 MI SS'')'");

    //SimpleDateFormat used for parsing dates
    private static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /**
     * Block constructor by design
     */
    private DbUtils() {
    }

    ;

    /**
     * Quotes a String for DB insert as a BOOLEAN: 
     * Returns "TRUE" if the input String is a valid true boolean (true, 1, yes, t, y), 
     * returns "FALSE" if the input String is a valid false boolean (false, 0, no, f, n), 
     * returns "NULL" otherwise
     *
     * @param input   String to quote
     */
    public static String quoteBoolean(String input) {
        if (Utils.isEmpty(input)) {
            return "NULL";
        }

        if ("true".equalsIgnoreCase(input)) {
            return "TRUE";
        }
        if ("false".equalsIgnoreCase(input)) {
            return "FALSE";
        }

        if ("t".equalsIgnoreCase(input)) {
            return "TRUE";
        }
        if ("f".equalsIgnoreCase(input)) {
            return "FALSE";
        }

        if ("1".equalsIgnoreCase(input)) {
            return "TRUE";
        }
        if ("0".equalsIgnoreCase(input)) {
            return "FALSE";
        }

        if ("yes".equalsIgnoreCase(input)) {
            return "TRUE";
        }
        if ("no".equalsIgnoreCase(input)) {
            return "FALSE";
        }

        if ("y".equalsIgnoreCase(input)) {
            return "TRUE";
        }
        if ("n".equalsIgnoreCase(input)) {
            return "FALSE";
        }

        return "NULL";
    }

    public static String quoteBoolean(boolean input) {
        if (input) {
            return "TRUE";
        }

        return "FALSE";
    }

    public static String quoteBoolean(Boolean input) {
        if (input == null) {
            return "NULL";
        }

        return quoteBoolean(input.booleanValue());
    }

    /**
     * Quotes a boolean for insert as a number (character): Returns '1' for
     * true, '0' for false
     */
    public static String quoteBooleanAsNum(Boolean input) {
        if (input == null) {
            return "NULL";
        }

        if (input) {
            return "'1'";
        } else {
            return "'0'";
        }
    }

    /**
     * Quotes a Date for DB insert as a Date (ISO 8601 format: 2003-01-24)
     * Returns "NULL" if the input String is "" or null.
     *
     * @param input Date to quote
     */
    public static String quoteDate(java.util.Date input) {
        if (input == null) {
            return "NULL";
        }

        String output = DateUtils.formatDate(input, DateUtils.ISO_8601);

        if (Utils.isEmpty(output)) {
            return "NULL";
        }

        return "'" + output + "'";
    }

    /**
     * Creates a java.util.Date object from a String in the format: MM/DD/YYYY
     * HH24:MI:SS. Use in conjunction with dateToChar. Accurate to 1 second.
     *
     * @param dateString the String to convert
     */
    public static java.util.Date charToDate(String dateString) {
        try {
            return DateUtils.toDate(dateString, DB_DATE_FORMAT);
        } catch (Exception e) {
            //do nothing
        }

        return null;
    }

    /**
     * Convert Date Object to Oracle Date String
     *
     * @param inDate	java.util.Date to convert. If inDate is null, current time
     * will be used
     *
     * @return String	Oracle Specific Date String: 'MM DD YYYY HH24 MI SS'
     */
    public static String toDbDate(java.util.Date inDate) {
        if (inDate == null) {
            return "NULL";
        }

        String result = "TO_DATE('" + DateUtils.formatDate(inDate, DateUtils.TO_DATE) + "', 'MM DD YYYY HH24 MI SS')";

        return result;
    }

    /**
     * Creates a date String for the current date
     */
    public static String sysdate() {
        return DateUtils.formatDate(new Date(), INSERT_FORMAT);
    }

    /**
     * Convert Date Object to PSQL Date String
     *
     * @param inDate	java.util.Date to convert. If inDate is null, current time
     * will be used
     *
     * @return String	PSQL Specific Date String: 'MM-DD-YYYY HH24:MI:SS'
     */
    public static String toTimestamp(java.util.Date inDate) {
        if (inDate == null) {
            return "NULL";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String result = "TO_TIMESTAMP('" + DateUtils.formatDate(inDate, DateUtils.TO_DATE) + "', 'MM-DD-YYYY HH24:MI:SS')";

        return result;
    }

}
