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

import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * Provides static utility methods for number validation and formatting.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class NumberUtils {

    //NUMBER FORMATS:
    public static final DecimalFormat ZIP_5 = new DecimalFormat("00000");
    public static final DecimalFormat ZIP_4 = new DecimalFormat("0000");
    public static final DecimalFormat COMMA = new DecimalFormat("###,###,###,###,###,###,###");
    public static final DecimalFormat MONEY = new DecimalFormat("###,###,###,###,###,###,##0.00");

    /**
     * Block constructor by design. NumberUtils is not meant to be instantiated.
     */
    private NumberUtils() {
    }

    /**
     * Rounds a Float to two decimal places
     */
    public static Double roundCurrency(Float input) {
        return (int) Math.round(input * 100) / 100.0;
    }

    /**
     * Rounds a Double to two decimal places
     */
    public static Double roundCurrency(Double input) {
        return (int) Math.round(input * 100) / 100.0;
    }

    /**
     * Formats a double into a String
     */
    public static String format(double number, NumberFormat format) {
        return format.format(number);
    }

    /**
     * Formats a float into a String
     */
    public static String format(float number, NumberFormat format) {
        return format.format((double) number);
    }

    /**
     * Formats a long into a String
     */
    public static String format(long number, NumberFormat format) {
        return format.format(number);
    }

    /**
     * Formats a int into a String
     */
    public static String format(int number, NumberFormat format) {
        return format.format((long) number);
    }

    /**
     * Formats an Double into a String
     */
    public static String format(Double number, NumberFormat format) {
        if (number == null) {
            return "";
        }

        return format(number.doubleValue(), format);
    }

    /**
     * Formats an Float into a String
     */
    public static String format(Float number, NumberFormat format) {
        if (number == null) {
            return "";
        }

        return format(number.doubleValue(), format);
    }

    /**
     * Formats an Long into a String
     */
    public static String format(Long number, NumberFormat format) {
        if (number == null) {
            return "";
        }

        return format(number.longValue(), format);
    }

    /**
     * Formats an Integer into a String
     */
    public static String format(Integer number, NumberFormat format) {
        if (number == null) {
            return "";
        }

        return format(number.longValue(), format);
    }

    /**
     * Parses a String into a double. This method differs from
     * Double.parseDouble in that it first removes any commas from the input
     * String.
     */
    public static Double parseDouble(String input) {
        //sanity check
        if (Utils.isEmpty(input)) {
            return 0.0d;
        }

        input = StringUtils.replace(input, ",", "");

        return Double.parseDouble(input);
    }

    /**
     * Parses a String into a Float. This method differs from Float.parseFloat
     * in that it first removes any non-numeric characters from the input String
     * (comma, $, etc).
     */
    public static Float parseFloat(String input) {
        //sanity check
        if (Utils.isEmpty(input)) {
            return 0.0f;
        }

        input = input.replaceAll("[^0-9.]", "");

        return Float.parseFloat(input);
    }

    /**
     * Main method for testing only!
     */
    public static void main(String[] args) {

        String moneyString = "$150.00";
        float moneyFloat = parseFloat(moneyString);
        System.out.println("moneyString: " + moneyString + " moneyFloat: " + moneyFloat);

        float testFloat = 328.46f;
        System.out.println("testFloat: " + testFloat);

        testFloat = testFloat / 1000f;
        System.out.println("testFloat / 1000: " + testFloat);

        double testDouble = 328.46d;
        System.out.println("testDouble: " + testDouble);

        testDouble = testDouble / 1000d;
        System.out.println("testDouble / 1000: " + testDouble);

        System.out.println("testDouble as float: " + ((float) testDouble));
    }

}
