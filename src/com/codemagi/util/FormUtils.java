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

import java.util.*;
import javax.servlet.ServletRequest;

/**
 * Provides static utility methods for verifying form submissions.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class FormUtils {

    /**
     * Block constructor by design
     */
    private FormUtils() {
    }

    /**
     * Gets a date from the request. Used in conjunction with the DateSelect or
     * DateTimeSelect taglib.
     *
     * @param request The current ServletRequest
     * @param name The name of the taglib
     * @return java.util.Date Validated date from form input, or null
     */
    public static Date getDate(ServletRequest request, String name) {

        if (request == null) {
            return null;
        }

        String day = request.getParameter("day_" + name + "_day");
        String month = request.getParameter("month_" + name + "_month");
        String year = request.getParameter("year_" + name + "_year");
        String hour = request.getParameter("hour_" + name + "_hour");
        String minute = request.getParameter("minute_" + name + "_minute");

        if (Utils.isEmpty(hour)) {
            hour = "00";
        }

        if (Utils.isEmpty(minute)) {
            minute = "00";
        }

        //returns null if can't parse
        return DateUtils.toDate(month, day, year, hour, minute);

    }

    /**
     * Gets a date from a Map. Used in conjunction with the DateSelect or
     * DateTimeSelect taglib and a multipart request, parsed into a Hashtable
     *
     * @param request A multipart ServletRequest, parsed into a Hashtable
     * @param name The name of the taglib
     * @return java.util.Date Validated date from form input, or null
     */
    public static Date getDate(Map request, String name) {

        if (request == null) {
            return null;
        }

        String day = (String) request.get("day_" + name + "_day");
        String month = (String) request.get("month_" + name + "_month");
        String year = (String) request.get("year_" + name + "_year");
        String hour = (String) request.get("hour_" + name + "_hour");
        String minute = (String) request.get("minute_" + name + "_minute");

        if (Utils.isEmpty(hour)) {
            hour = "00";
        }

        if (Utils.isEmpty(minute)) {
            minute = "00";
        }

        //returns null if can't parse
        return DateUtils.toDate(month, day, year, hour, minute);

    }

    /**
     * Returns an Enumeration of request Parameter NAMES that begin with a
     * specified String. By default, the matchString will be trimmed from the
     * names in the output Enumeration
     *
     * @param request The request to build the Enumeration from
     * @param matchString The matching String
     * @return Enumeration An enumeration of attributes and values that begin
     * with the specified String
     */
    public static Enumeration getParametersByName(javax.servlet.ServletRequest request, String matchString) {
        return getParametersByName(request, matchString, true);
    }

    /**
     * Returns an Enumeration of request Parameter NAMES that begin with a
     * specified String
     *
     * @param request The request to build the Enumeration from
     * @param matchString The matching String
     * @param trim If true, the names in the output will have the match String
     * trimmed
     * @return Enumeration An enumeration of parameter NAMES that begin with the
     * specified String
     */
    public static Enumeration getParametersByName(javax.servlet.ServletRequest request, String matchString, boolean trim) {

        Enumeration e = request.getParameterNames();
        List<String> namedParameters = new ArrayList<>();
        String parameterName = null;

        while (e.hasMoreElements()) {
            parameterName = (String) e.nextElement();
            if (parameterName.startsWith(matchString)) {
                if (trim) {
                    parameterName = parameterName.substring(matchString.length());
                }

                namedParameters.add(parameterName);
            }
        }

        return Collections.enumeration(namedParameters);
    }

    /**
     * Returns a List of parameter NAMES (ie: keys) that begin with a specified
     * String. This version is to be used with multipart requests that are
     * parsed into a Hashtable. By default, the matchString will be trimmed from
     * the names in the output List.
     *
     * @param request The request to build the List from
     * @param matchString The matching String
     * @return List A List of attributes and values that begin with the
     * specified String
     */
    public static List getParametersByName(Map request, String matchString) {
        return getParametersByName(request, matchString, true);
    }

    /**
     * Returns a List of request Parameter NAMES that begin with a specified
     * String. This version is to be used with multipart requests that are
     * parsed into a Hashtable.
     *
     * @param request The request to build the List from
     * @param matchString The matching String
     * @param trim If true, the names in the output will have the match String
     * trimmed
     * @return List A List of parameter NAMES that begin with the specified
     * String
     */
    public static List getParametersByName(Map request, String matchString, boolean trim) {

        ArrayList output = new ArrayList();
        Iterator i = request.keySet().iterator();
        while (i.hasNext()) {
            String parameterName = (String) i.next();
            if (parameterName.startsWith(matchString)) {
                if (trim) {
                    parameterName = parameterName.substring(matchString.length());
                }

                output.add(parameterName);
            }
        }

        return output;
    }

    /**
     * Returns a Collection of parameter VALUES for a multi-select list
     *
     * @param request The current ServletRequest
     * @param selectName The name of the select list
     * @return Collection List of Strings of submitted values
     */
    public static List<String> getSelectValues(javax.servlet.ServletRequest request, String selectName) {

        String[] values = request.getParameterValues(selectName);

        if (values == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(values);
    }

    /**
     * Returns the name of the first form element whose value is non-empty -Used
     * to determine which submit button was pressed when a form has multiple
     * submits.
     */
    public static String getFirstSubmit(javax.servlet.ServletRequest request, String[] submitNames) {

        for (String name : submitNames) {
            if (!Utils.isEmpty(request.getParameter(name))) {
                return name;
            }
        }

        return "";
    }

    /**
     * Returns a Date object given a date string from the Calendar widget.
     *
     * @param request The current ServletRequest
     * @param name The name of the item in the request
     * @return Date
     */
    public static Date getCalendarDate(ServletRequest request, String name) {

        if (request == null) {
            return null;
        }

        String date = request.getParameter(name);

        return DateUtils.toDate(date, DateUtils.CALENDAR_FORMAT);

    }

    /**
     * Returns a Date object given a date string from the Calendar widget. Gets
     * a date from a Map. Used in conjunction with the DateSelect or
     * DateTimeSelect taglib and a multipart request, parsed into a Hashtable
     *
     * @param request A multipart ServletRequest, parsed into a Hashtable
     * @param name The name of the item in the request
     * @return Date
     */
    public static Date getCalendarDate(Map request, String name) {

        if (request == null) {
            return null;
        }

        String date = (String) request.get(name);

        return DateUtils.toDate(date, DateUtils.CALENDAR_FORMAT);
    }

}
