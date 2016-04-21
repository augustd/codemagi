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
 * Provides static utility methods for math and psuedo-math.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class MathUtils {

    /**
     * Block constructor by design
     */
    private MathUtils() {
    }

    /**
     * Returns true if both parameters are null or if both parameters are not
     * null and represent the same Integer
     */
    public static boolean equals(Integer a, Integer b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    /**
     * Returns the test value if it is within the range, otherwise returns the
     * minimum or maximum.
     */
    public static int minMax(int min, int testValue, int max) {
        if (testValue < min) {
            return min;
        }

        if (testValue > max) {
            return max;
        }

        return testValue;
    }

    /**
     * Returns (the absolute value of) the final digits of an int
     */
    public static int finalDigits(int numberIn, int numDigits) {

        numberIn = Math.abs(numberIn);

        int multiplier = (int) Math.pow(10, numDigits);

        return numberIn % multiplier;
    }

    public static void main(String[] args) {
        System.out.println("TESTING equals method:");

        Integer a = 5;
        Integer b = 7;
        Integer c = 7;
        Integer d = null;
        Integer e = null;

        System.out.println(a + " equals " + b + " ? " + equals(a, b));
        System.out.println(b + " equals " + a + " ? " + equals(b, a));
        System.out.println(b + " equals " + c + " ? " + equals(b, c));
        System.out.println(c + " equals " + d + " ? " + equals(c, d));
        System.out.println(d + " equals " + c + " ? " + equals(d, c));
        System.out.println(d + " equals " + e + " ? " + equals(d, e));
    }

}
