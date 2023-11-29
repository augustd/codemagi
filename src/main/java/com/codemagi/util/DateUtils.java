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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DateUtils provides static utility methods for date manipulation, validation
 * and formatting.
 *
 * Example dates given are based on: Friday, January 24th, 2003, 3:07pm
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class DateUtils {

    static Logger log = LogManager.getLogger("com.codemagi.util.DateUtils");

    //DATE FORMATS:
    /**
     * ODBC Timestamp format: {'ts' '2003-01-24 03:07:00'}
     */
    public static final DateFormat ODBC_DATE_TIME = new SimpleDateFormat("{'ts' ''yyyy-MM-dd HH:mm:ss''}");

    /**
     * XML Timestamp format: 2003-01-24T03:07:00.000-0800
     */
    public static final DateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");

    /**
     * Oracle date format: 2003-01-24 03:07:00.0
     */
    public static final DateFormat ORACLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    /**
     * MySQL DATETIME format: STR_TO_DATE('2003-01-24 03:07:00.0', );
     */
    public static final DateFormat TO_DATETIME_MYSQL
            = new SimpleDateFormat("'STR_TO_DATE('''yyyy-MM-dd HH:mm:ss''', ''%Y-%m-%d %k:%i:%S'')'");

    /**
     * MySQL DATE format: STR_TO_DATE('2003-01-24', );
     */
    public static final DateFormat TO_DATE_MYSQL
            = new SimpleDateFormat("'STR_TO_DATE('''yyyy-MM-dd''', ''%Y-%m-%d'')'");

    public static final DateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");
    public static final DateFormat MMDDHHMM = new SimpleDateFormat("MMddHHmm");

    /**
     * Wikipedia date format: 15:07, 24 January 2003 (UTC)
     */
    public static final DateFormat WIKIPEDIA_DATE_FORMAT = new SimpleDateFormat("HH:mm, dd MMMM yyyy (z)");

    public static final DateFormat MMDDYYYY = new SimpleDateFormat("MMddyyyy");

    static {
        //this one is used for date validiation so it can't be lenient
        MMDDYYYY.setLenient(false);
    }

    public static final DateFormat MM_DD_YYYY = new SimpleDateFormat("MM dd yyyy");
    public static final DateFormat MM_DD_YYYY_HH_MM = new SimpleDateFormat("MM dd yyyy HH mm");
    public static final DateFormat MM_DD_YYYY_HH_MM_A = new SimpleDateFormat("MM dd yyyy hh mm a");
    public static final DateFormat MMYYYY = new SimpleDateFormat("MMyyyy");

    /**
     * Nicely formatted date: January 24, 2003
     */
    public static final DateFormat MMMM_DD_YYYY = new SimpleDateFormat("MMMM d, yyyy");

    /**
     * Nicely formatted date, with day: Friday, January 24, 2003
     */
    public static final DateFormat DAY_MONTH_DD_YYYY = new SimpleDateFormat("EEEE, MMMM d, yyyy");

    /**
     * Nicely formatted date, with time: January 24, 2003 3:07pm
     */
    public static final DateFormat MONTH_DD_YYYY_TIME = new SimpleDateFormat("MMMM d, yyyy h:mma");

    /**
     * Month digits, not padded: 1
     */
    public static final DateFormat MONTH_DIGITS_SHORT = new SimpleDateFormat("M");

    /**
     * Month digits, padded: 01
     */
    public static final DateFormat MONTH_DIGITS = new SimpleDateFormat("MM");

    /**
     * Month abbreviation: Jan
     */
    public static final DateFormat MONTH_ABBREV = new SimpleDateFormat("MMM");

    /**
     * Month abbreviation with year: Jan 2003
     */
    public static final DateFormat MONTH_ABBREV_YEAR = new SimpleDateFormat("MMM yyyy");

    /**
     * Month name: January
     */
    public static final DateFormat MONTH_NAME = new SimpleDateFormat("MMMM");

    /**
     * Day digits, not padded: 24
     */
    public static final DateFormat DAY_DIGITS_SHORT = new SimpleDateFormat("d");

    /**
     * Day digits, padded: 24
     */
    public static final DateFormat DAY_DIGITS = new SimpleDateFormat("dd");

    /**
     * Day Abbreviation: Fri
     */
    public static final DateFormat DAY_ABBREV = new SimpleDateFormat("ddd");

    /**
     * Day name: Friday
     */
    public static final DateFormat DAY_NAME = new SimpleDateFormat("dddd");

    /**
     * Day name: Friday
     */
    public static final DateFormat DAY = new SimpleDateFormat("EEEE");

    /**
     * Four digit year: 2003
     */
    public static final DateFormat YEAR = new SimpleDateFormat("yyyy");

    /**
     * Hours on a 24 hour clock, not padded: 15 (3pm)
     */
    public static final DateFormat HOUR_DIGITS_SHORT = new SimpleDateFormat("H");

    /**
     * Hours on a 24 hour clock, padded: 15 (3pm)
     */
    public static final DateFormat HOUR_DIGITS = new SimpleDateFormat("HH");

    /**
     * Hours on a 12 hour clock, padded: 03 (3pm)
     */
    public static final DateFormat HOUR_12_DIGITS = new SimpleDateFormat("hh");

    /**
     * Nicely formatted time on a 12 hour clock: 3:07pm
     */
    public static final DateFormat TIME_12_HOUR = new SimpleDateFormat("hh:mma");

    /**
     * Meridian, am or pm (pm)
     */
    public static final DateFormat MERIDIAN = new SimpleDateFormat("a");

    /**
     * Minutes, not padded: 7
     */
    public static final DateFormat MINUTE_DIGITS_SHORT = new SimpleDateFormat("m");

    /**
     * Minutes, padded: 07
     */
    public static final DateFormat MINUTE_DIGITS = new SimpleDateFormat("mm");

    public static final DateFormat TO_DATE = new SimpleDateFormat("MM dd yyyy HH mm ss");

    /**
     * US Standard date, padded: 01/24/2003 (NOTE: years are interpreted
     * literally, so 9/1/93 is considered to be in year 93 AD)
     */
    public static final DateFormat US_STANDARD = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * US Standard date, padded, with time: 01/24/2003 3:07pm (NOTE: years are
     * interpreted literally, so 9/1/93 is considered to be in year 93 AD)
     */
    public static final DateFormat US_STANDARD_TIME = new SimpleDateFormat("MM/dd/yyyy hh:mma");

    /**
     * European standard date, padded: 24/01/2003
     */
    public static final DateFormat EUROPE_STANDARD = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Date that comes from the Calendar widget: 01/24/2003
     */
    public static final DateFormat CALENDAR_FORMAT = DateUtils.US_STANDARD;

    /**
     * ISO-8601 format: 2003-01-24
     */
    public static final DateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * ISO-8601 time format: 03:07:00
     */
    public static final DateFormat ISO_8601_TIME = new SimpleDateFormat("HH:mm:ss");

    //date constants
    public static final Date BEGINNING_OF_TIME = new Date(0);  //beginning of current era (1/1/1970)
    public static final Date END_OF_TIME = new Date(Long.MAX_VALUE);  // 10000 years from beginning

    /**
     * Block constructor by design. DateUtils is not meant to be instantiated.
     */
    private DateUtils() {
    }

    /**
     * Converts a number of seconds into hours:minutes:seconds
     */
    public static String toTime(Integer input) {

        //sanity check
        if (input == null) {
            return "00:00:00";
        }

        int hours = input / 3600;
        int remainder = input % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        String output = "";
        output += (hours < 10 ? "0" : "") + hours + ":";
        output += (minutes < 10 ? "0" : "") + minutes + ":";
        output += (seconds < 10 ? "0" : "") + seconds;

        return output;
    }

    /**
     * Converts a number of seconds into hours:minutes:seconds
     *
     * NOTE: Fractional seconds are ignored
     */
    public static String toTime(Float input) {

        //sanity check
        if (input == null) {
            return "00:00:00";
        }

        return toTime(input.intValue());
    }

    /**
     * Sets the time on the passed Date.
     */
    public static Date setTime(Date inDate, int hour, int minute, int second) {

        return setTime(inDate, hour, minute, second, TimeZone.getDefault());
    }

    /**
     * Sets the time on the passed Date.
     */
    public static Date setTime(Date inDate, int hour, int minute, int second, String timeZone) {

        TimeZone tz = (Utils.isEmpty(timeZone)) ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZone);

        return setTime(inDate, hour, minute, second, tz);
    }

    /**
     * Sets the time on the passed Date.
     */
    public static Date setTime(Date inDate, int hour, int minute, int second, TimeZone tz) {

        if (tz == null) {
            tz = TimeZone.getDefault();
        }

        Calendar cal = Calendar.getInstance(tz);
        cal.setTime(inDate);

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * Returns the current time in a different time zone.
     */
    public static Date getDate(TimeZone tz) {
        if (tz == null) {
            tz = TimeZone.getDefault();
        }

        Calendar cal = Calendar.getInstance(tz);

        return cal.getTime();
    }

    /**
     * Returns true if the current date is in daylight savings time in the
     * default time zone.
     */
    public static boolean isDaylightTime() {
        return isDaylightTime(new Date());
    }

    /**
     * Returns true if the passed date is in daylight savings time in the
     * default time zone.
     */
    public static boolean isDaylightTime(Date inDate) {
        return isDaylightTime(inDate, TimeZone.getDefault());
    }

    /**
     * Returns true if the current date is in daylight savings time in the
     * passed time zone.
     */
    public static boolean isDaylightTime(TimeZone tz) {
        if (tz == null) {
            tz = TimeZone.getDefault();
        }

        return isDaylightTime(new Date(), tz);
    }

    /**
     * Returns true if the passed date is in daylight savings time in the passed
     * time zone.
     */
    public static boolean isDaylightTime(Date inDate, TimeZone tz) {
        if (tz == null) {
            tz = TimeZone.getDefault();
        }

        return tz.inDaylightTime(inDate);
    }

    /**
     * Adds the specified number of 'business days' (Monday-Friday, holidays
     * included) to the passed Date.
     *
     * NOTE: This method does not do negative date addition. If numDays is zero
     * or less, inDate will be returned.
     */
    public static Date addBusinessDays(Date inDate, int numDays) {
        if (inDate == null) {
            return null;
        }

        if (numDays <= 0) {
            return inDate;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        int i = 0;
        while (i < numDays) {
            cal.add(Calendar.DATE, 1);
            if (isBusinessDay(cal)) {
                i++;
            }
        }

        return cal.getTime();
    }

    /**
     * Returns true if the passed in Date is a 'business day' (Any
     * Monday-Friday, including holidays)
     */
    public static boolean isBusinessDay(Date input) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(input);

        return isBusinessDay(cal);
    }

    /**
     * Returns true if the passed in Calendar represents a 'business day' (Any
     * Monday-Friday, including holidays)
     */
    public static boolean isBusinessDay(Calendar input) {
        int day = input.get(Calendar.DAY_OF_WEEK);

        return (day == Calendar.MONDAY
                || day == Calendar.TUESDAY
                || day == Calendar.WEDNESDAY
                || day == Calendar.THURSDAY
                || day == Calendar.FRIDAY);
    }

    /**
     * Adds the specified number of minutes to the current Date.
     */
    public static Date addMinutes(int num) {
        return addMinutes(new Date(), num);
    }

    /**
     * Adds the specified number of minutes to the passed Date
     */
    public static Date addMinutes(Date inDate, int num) {
        if (inDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(Calendar.MINUTE, num);

        return cal.getTime();
    }

    /**
     * Adds the specified number of hours to the current Date.
     */
    public static Date addHours(int num) {
        return addHours(new Date(), num);
    }

    /**
     * Adds the specified number of hours to the passed Date
     */
    public static Date addHours(Date inDate, int numHours) {
        if (inDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(Calendar.HOUR, numHours);

        return cal.getTime();
    }

    /**
     * Adds the specified number of days to the current Date.
     */
    public static Date addDays(int num) {
        return addDays(new Date(), num);
    }

    /**
     * Adds the specified number of days to the passed Date
     */
    public static Date addDays(Date inDate, int numDays) {
        if (inDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(Calendar.DATE, numDays);

        return cal.getTime();
    }

    /**
     * Adds the specified number of months to the current Date.
     */
    public static Date addMonths(int num) {
        return addMonths(new Date(), num);
    }

    /**
     * Adds the specified number of months to the passed Date
     */
    public static Date addMonths(Date inDate, int numMonths) {
        if (inDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(Calendar.MONTH, numMonths);

        return cal.getTime();
    }

    /**
     * Adds the specified number of years to the current Date.
     */
    public static Date addYears(int num) {
        return addYears(new Date(), num);
    }

    /**
     * Adds the specified number of years to the passed Date
     */
    public static Date addYears(Date inDate, int numYears) {
        if (inDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(Calendar.YEAR, numYears);

        return cal.getTime();
    }

    /**
     * Adds the specified number of units to the passed Date
     *
     * @param inDate The starting date
     * @param unit The unit to increment by. These should be fields from the
     * java.util.Calendar class
     * @param numUnits The number of units to add to the start date
     */
    public static Date addUnits(Date inDate, int unit, int numUnits) {
        if (inDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.add(unit, numUnits);

        return cal.getTime();
    }

    /**
     * Returns the next day matching dayOfWeek after (or including) startDate
     *
     * @param startDate date to start from
     * @param dayOfWeek date containing the day of week to advance to
     * @return Date startDate rolled forward to match the day in dayOfWeek
     */
    public static Date rollToDay(Date startDate, Date dayOfWeek) {

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar dayOfWeekCal = Calendar.getInstance();
        dayOfWeekCal.setTime(dayOfWeek);

        while (startCal.get(Calendar.DAY_OF_WEEK) != dayOfWeekCal.get(Calendar.DAY_OF_WEEK)) {
            startCal.add(Calendar.DATE, 1);
        }
        return startCal.getTime();
    }

    /**
     * Returns the previous day matching dayOfWeek before (or including)
     * startDate
     *
     * @param startDate date to start from
     * @param dayOfWeek date containing the day of week to go back to
     * @return Date startDate rolled back to match the day in dayOfWeek
     */
    public static Date rollBackToDay(Date startDate, Date dayOfWeek) {

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar dayOfWeekCal = Calendar.getInstance();
        dayOfWeekCal.setTime(dayOfWeek);

        while (startCal.get(Calendar.DAY_OF_WEEK) != dayOfWeekCal.get(Calendar.DAY_OF_WEEK)) {
            startCal.add(Calendar.DATE, -1);
        }
        return startCal.getTime();
    }

    /**
     * Returns the start Date of the quarter which contains the input Date. For
     * example: The quarter containing May 23rd begins on April 1st. Result is
     * accurate to the millisecond.
     *
     * @param inDate Input Date
     * @return Date The start date of the quarter
     */
    public static Date getQuarterStart(Date inDate) {
        if (inDate == null) {
            return null;
        }
        int quarter = getQuarter(inDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, (quarter - 1) * 3);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * Overridden version of getQuarterStart(Date) which accepts Calendar
     */
    public static Date getQuarterStart(Calendar inCal) {
        if (inCal == null) {
            return getQuarterStart(new Date());
        }

        return getQuarterStart(inCal.getTime());
    }

    /**
     * Returns the start date of the quarter entered.
     *
     * @param quarter The quarter to get the date for
     * @param year The year to get the date for
     * @return Date The start date of the quarter
     */
    public static Date getQuarterStart(int quarter, int year) {
        return getQuarterStart(quarter, year, 0);
    }

    /**
     * Returns the start date of the quarter entered. This method also accepts
     * an offset to allow for fiscal years that do not start in January
     *
     * @param quarter The quarter to get the date for
     * @param year The year to get the date for
     * @param offset number of months to offset the first quarter
     * @return Date The start date of the quarter
     */
    public static Date getQuarterStart(int quarter, int year, int offset) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, (quarter - 1) * 3);
        cal.add(Calendar.MONTH, offset);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * Returns the end Date of the quarter which contains the input Date. For
     * example: The quarter containing May 23rd ends on June 30th. Result is
     * accurate to the millisecond.
     *
     * @param inDate Input Date
     * @return Date The start date of the quarter
     */
    public static Date getQuarterEnd(Date inDate) {
        if (inDate == null) {
            return null;
        }

        int quarter = getQuarter(inDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.set(Calendar.MONTH, quarter * 3);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

        return cal.getTime();
    }

    /**
     * Returns the quarter that a month is in. For example, January-March is Q1,
     * April-June is Q2, etc.
     *
     * @param qDate Date Object to determine quarter from
     * @return int quarter number 1 through 4
     */
    public static int getQuarter(Date qDate) {
        Calendar qCal = Calendar.getInstance();
        qCal.setTime(qDate);

        return getQuarter(qCal, 0);
    }

    /**
     * Returns the quarter that a month is in. For example, January-March is Q1,
     * April-June is Q2, etc. This method also accepts an offset to allow for
     * fiscal years that do not start in January
     *
     * @param qDate Date Object to determine quarter from
     * @param offset number of months to offset the first quarter
     * @return int quarter number 1 through 4
     */
    public static int getQuarter(Date qDate, int offset) {
        Calendar qCal = Calendar.getInstance();
        qCal.setTime(qDate);

        return getQuarter(qCal, offset);
    }

    /**
     * Returns the quarter that a month is in. For example, January-March is Q1,
     * April-June is Q2, etc.
     *
     * @param qCal Calendar Object to determine quarter from
     * @return int quarter number 1 through 4
     */
    public static int getQuarter(Calendar qCal) {
        return getQuarter(qCal, 0);
    }

    /**
     * Returns the quarter that a month is in. For example, January-March is Q1,
     * April-June is Q2, etc. This method also accepts an offset to allow for
     * fiscal years that do not start in January
     *
     * @param month Calendar Object to determin quarter from
     * @param offset number of months to offset the first quarter
     * @return int quarter number 1 through 4
     */
    public static int getQuarter(Calendar qCal, int offset) {

        Calendar tempCal = (Calendar) qCal.clone();

        tempCal.add(Calendar.MONTH, offset);

        int realMonth = tempCal.get(Calendar.MONTH);

        if (0 <= realMonth && realMonth <= 2) {
            return 1;
        } else if (3 <= realMonth && realMonth <= 5) {
            return 2;
        } else if (6 <= realMonth && realMonth <= 8) {
            return 3;
        }

        return 4;

    }

    /**
     * Returns the start date of the current year.
     */
    public static Date getYearStart() {
        return getYearStart(new Date());
    }

    /**
     * Returns a Date representing the start of the passed year.
     *
     * @param year The year to find the start date for
     */
    public static Date getYearStart(Integer year) {
        Date date = toDate(1, 1, year);

        return getYearStart(date);
    }

    /**
     * Returns the start Date of the year which contains the input Date. For
     * example: The year containing 23 May 2001 begins on 1 Jan 2001. Result is
     * accurate to the millisecond.
     *
     * @param inDate Input Date
     * @return Date The start date of the year
     */
    public static Date getYearStart(Date inDate) {
        if (inDate == null) {
            return null;
        }
        inDate = getMonthStart(inDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        return cal.getTime();
    }

    /**
     * Returns the end date of the passed year.
     */
    public static Date getYearEnd(Date inDate) {
        if (inDate == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

        return cal.getTime();
    }

    /**
     * Returns the day (day of month) portion of a Date.
     */
    public static Integer getDay(Date inDate) {
        if (inDate == null) {
            return null;
        }

        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(inDate);

        return tempCal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the current day of the month
     */
    public static Integer getDay() {
        return getDay(new Date());
    }

    /**
     * Returns the month portion of a Date. Note that Java months are
     * zero-based, so January is 0, February is 1, etc.
     */
    public static Integer getMonth(Date inDate) {
        if (inDate == null) {
            return null;
        }

        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(inDate);

        return tempCal.get(Calendar.MONTH);
    }

    /**
     * Returns the current month.
     */
    public static Integer getMonth() {
        return getMonth(new Date());
    }

    /**
     * Returns the start Date of the month which contains the input Date. For
     * example: The month containing May 23rd begins on May 1st. Result is
     * accurate to the millisecond.
     *
     * @param inDate Input Date
     * @return Date The start date of the month
     */
    public static Date getMonthStart(Date inDate) {
        if (inDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getMonthStart() {
        return getMonthStart(new Date());
    }

    /**
     * Returns the end date of the passed month.
     */
    public static Date getMonthEnd(Date inDate) {
        if (inDate == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));

        return cal.getTime();
    }

    /**
     * Returns a Date object representing the start of the current week (Weeks
     * start on Monday).
     */
    public static Date getWeekStart() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * Returns a date Object representing the end of the day for the passed in
     * date. IE: If the passed in date is 9/3/2003 1:43:24 pm, the returned date
     * will be 9/3/2003 11:59:59 pm.
     */
    public static Date getEndOfDay(Date inDate) {

        if (inDate == null) {
            return null;
        }
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(inDate);
        tempCal.set(Calendar.HOUR, tempCal.getActualMaximum(Calendar.HOUR));
        tempCal.set(Calendar.MINUTE, tempCal.getActualMaximum(Calendar.MINUTE));
        tempCal.set(Calendar.SECOND, tempCal.getActualMaximum(Calendar.SECOND));

        return tempCal.getTime();
    }

    /**
     * Returns a date Object representing the end of the current day.
     */
    public static Date getEndOfDay() {
        return getEndOfDay(new Date());
    }

    /**
     * Returns a date Object representing the start of the day for the passed in
     * date. IE: If the passed in date is 9/3/2003 1:43:24 pm, the returned date
     * will be 9/3/2003 12:00:00 am.
     *
     * @deprecated Replaced with getDayStart for consistency
     */
    public static Date getStartOfDay(Date inDate) {
        return getDayStart(inDate);
    }

    /**
     * Returns a date Object representing the start of the day for the passed in
     * date. IE: If the passed in date is 9/3/2003 1:43:24 pm, the returned date
     * will be 9/3/2003 12:00:00 am.
     */
    public static Date getDayStart(Date inDate) {

        if (inDate == null) {
            return null;
        }
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(inDate);
        tempCal.set(Calendar.HOUR_OF_DAY, 0);
        tempCal.set(Calendar.MINUTE, 0);
        tempCal.set(Calendar.SECOND, 0);
        tempCal.set(Calendar.MILLISECOND, 0);

        return tempCal.getTime();
    }

    /**
     * Returns a date Object representing the start of the current day.
     */
    public static Date getDayStart() {
        return getDayStart(new Date());
    }

    /**
     * Returns the year portion of a Date
     */
    public static Integer getYear(Date inDate) {
        if (inDate == null) {
            return null;
        }

        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(inDate);

        return tempCal.get(Calendar.YEAR);
    }

    /**
     * Returns the current year.
     */
    public static Integer getYear() {
        return getYear(new Date());
    }

    /**
     * Returns a fiscal year based on a Calendar Object and an offset. For
     * example, if your fiscal year starts in July, offset will be 6
     *
     * @param cal Date Object to retrieve fiscal year from
     * @param offset Number of months to offset the fiscal year by
     * @return int The fiscal year represented by the Calendar's date
     */
    public static int getFiscalYear(Date cal, int offset) {

        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(cal);

        return getFiscalYear(tempCal, offset);
    }

    /**
     * Returns a fiscal year based on a Calendar Object and an offset. For
     * example, if your fiscal year starts in July, offset will be 6
     *
     * @param cal Calendar Object to retrieve fiscal year from
     * @param offset Number of months to offset the fiscal year by
     * @return int The fiscal year represented by the Calendar's date
     */
    public static int getFiscalYear(Calendar cal, int offset) {

        Calendar tempCal = (Calendar) cal.clone();

        tempCal.add(Calendar.MONTH, offset);

        return tempCal.get(Calendar.YEAR);
    }

    /**
     * Returns the number of months a range of two dates spans. For example, if
     * startDate is 6/10/2005 and endDate is 8/1/2005 months in span = 3.
     *
     * @param startDate Date to begin counting from
     * @param endDate Date to count to
     * @return int Number of field between start and end
     */
    public static int monthsInSpan(Date startDate, Date endDate) {
        if (endDate == null || startDate == null || startDate.after(endDate)) {
            return -1;
        }

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(getMonthStart(startDate));

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int monthsInSpan = 0;
        while (true) {
            monthsInSpan++;
            startCal.add(Calendar.MONTH, 1);

            if (endCal.compareTo(startCal) < 0) {
                break;
            }
        }

        return monthsInSpan;
    }

    /**
     * Returns the number of days between two dates.
     *
     * @param startDate Date to begin counting from
     * @param endDate Date to count to
     * @return int Number of field between start and end
     */
    public static int daysBetween(Date startDate, Date endDate) {
        if (endDate == null || startDate == null || startDate.after(endDate)) {
            return -1;
        }

        long start = startDate.getTime();
        long end = endDate.getTime();

        return (int) ((end - start) / 86400000);
    }

    /**
     * Returns the number of minutes between two dates.
     *
     * @param startDate Date to begin counting from
     * @param endDate Date to count to
     * @return int Number of field between start and end
     */
    public static int minutesBetween(Date startDate, Date endDate) {
        if (endDate == null || startDate == null || startDate.after(endDate)) {
            return -1;
        }

        long start = startDate.getTime();
        long end = endDate.getTime();

        return (int) ((end - start) / 60000); //60000 = milliseconds in one minute
    }

    //DATE FORMATTING METHODS ------------------------------------------------------------
    /**
     * returns a current timestamp in the format: YYYYMMDDHHMM
     */
    public static String getTimestamp() {
        return getTimeStamp(YYYYMMDDHHMM);
    }

    /**
     * returns a current timestamp using the DateFormat passed
     *
     * @param DateFormat The format for the timestamp
     * @return String Current timestamp in the specified format
     */
    public static String getTimestamp(DateFormat timestampFormat) {

        return getTimeStamp(timestampFormat);

    }

    /**
     * returns a current timestamp in the passed timezone using the specified
     * DateFormat
     *
     * @param timeZone A String representing the timezone to get a timestamp for
     * @param format The format for the timestamp
     * @return String Current timestamp in the specified format
     */
    public static String getTimestamp(DateFormat format, String timeZone) {
        TimeZone tz = (Utils.isEmpty(timeZone)) ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZone);
        Calendar cal = Calendar.getInstance(tz);

        //clone the DateFormat so we can set the time zone
        DateFormat clonedFormat = (DateFormat) format.clone();
        clonedFormat.setCalendar(cal);

        return clonedFormat.format(cal.getTime());
    }

    /**
     * returns a current timestamp in the format: YYYYMMDDHHMM
     */
    public static String getTimeStamp() {
        return getTimeStamp(YYYYMMDDHHMM);
    }

    /**
     * returns a current timestamp using the DateFormat passed
     *
     * @param DateFormat The format for the timestamp
     * @return String Current timestamp in the specified format
     */
    public static String getTimeStamp(DateFormat timestampFormat) {

        //clone the format to avoid thread safety issues
        DateFormat clone = (DateFormat) timestampFormat.clone();

        return clone.format(new Date());

    }

    /**
     * Formats a Calendar object into a String. This is an overloaded version of
     * formatDate(java.util.Date, DateFormat).
     *
     * @param dateIn java.util.Calendar to format
     * @param dateFormat Date format to use
     * @return String dateIn formatted as a String or "" if the date could not
     * be formatted
     */
    public static String formatDate(Calendar dateIn, DateFormat dateFormat) {
        return formatDate(dateIn.getTime(), dateFormat);
    }

    /**
     * Formats a date object into a String. If date is unparsable, "" is
     * returned
     *
     * @param dateIn java.util.Date to format
     * @param dateFormat Date format to use
     * @return String dateIn formatted as a String or "" if the date could not
     * be formatted
     */
    public static String formatDate(Date dateIn, DateFormat dateFormat) {
        try {
            //clone the format to avoid thread safety issues                                                                       
            DateFormat clone = (DateFormat) dateFormat.clone();

            String output = clone.format(dateIn);

            if (Utils.isEmpty(output)) {
                output = "";
            }

            return output;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns the appropriate suffix for an ordinal number (in English). For
     * example: With input 1, suffix is 'st', 2: 'nd', etc.
     */
    public static String getSuffix(int numberIn) {
        //no dealing with negatives
        numberIn = Math.abs(numberIn);

        //extract final two digits
        int tempI = MathUtils.finalDigits(numberIn, 2);

        //first deal with teens
        if (tempI < 20 && tempI > 10) {
            return "th";
        }

        //extract final digit
        tempI = MathUtils.finalDigits(numberIn, 1);

        //decide suffix for 1 digit
        if (tempI == 1) {
            return "st";
        }

        if (tempI == 2) {
            return "nd";
        }

        if (tempI == 3) {
            return "rd";
        }

        return "th";
    }

    /**
     * Overloaded version of getSuffix() that takes a String argument
     */
    public static String getSuffix(String numberIn) {
        try {
            int num = Integer.parseInt(numberIn);
            return getSuffix(num);
        } catch (Exception e) {
        }

        return "";
    }

    //DATE PARSING METHODS --------------------------------------------------------------
    /**
     * Convenience method to return a Date from a Calendar.
     */
    public static Date toDate(Calendar input) {
        if (input == null) {
            return null;
        }

        return input.getTime();
    }

    /**
     * Creates a Date object from a String and a DateFormat
     *
     * @param dateString The string to parse
     * @param format DateFormat with proper formatting for the dateString
     * @return Date java.util.Date, or null if the String could not be parsed
     */
    public static Date toDate(String dateString, DateFormat format) {

        try {
            //clone the format to avoid thread safety issues                                                                       
            DateFormat clone = (DateFormat) format.clone();

            return clone.parse(dateString);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * returns a java.util.Date from the passed Strings
     *
     * @param month The TWO DIGIT month
     * @param day The TWO DIGIT day
     * @param year The FOUR DIGIT year
     * @return java.util.Date date object created from passed params
     */
    public static Date toDate(String month, String day, String year) {

        try {
            //clone the format to avoid thread safety issues
            DateFormat clone = (DateFormat) MM_DD_YYYY.clone();

            return clone.parse(month + " " + day + " " + year);
        } catch (Exception pe) {
            return null;
        }

    }

    /**
     * Overridden version of toDate that accepts int arguments
     */
    public static Date toDate(int month, int day, int year) {
        return toDate(month + "", day + "", year + "");
    }

    /**
     * returns a java.util.Date from the passed Strings
     *
     * @param month The TWO DIGIT month
     * @param day The TWO DIGIT day
     * @param year The FOUR DIGIT year
     * @param hour The TWO DIGIT hour, based on a 24 hour clock (0-23)
     * @param minute The TWO DIGIT minute (0-59)
     *
     * @return java.util.Date date object created from passed params
     */
    public static Date toDate(String month, String day, String year, String hour, String minute) {

        try {
            //clone the format to avoid thread safety issues
            DateFormat clone = (DateFormat) MM_DD_YYYY_HH_MM.clone();

            return clone.parse(month + " " + day + " " + year + " " + hour + " " + minute);
        } catch (Exception pe) {
            return null;
        }

    }

    /**
     * Overridden version of toDate that accepts int arguments
     */
    public static Date toDate(int month, int day, int year, int hour, int minute) {
        return toDate(month + "", day + "", year + "", hour + "", minute + "");
    }

    //DATE COMPARISON METHODS ----------------------------------------------------
    /**
     * Returns true if the input date is in the past. Note that this method will
     * return false if a null input is passed.
     *
     * @param aDate	The date to check
     *
     * @return boolean	True if the date is in the past
     */
    public static boolean isDatePast(Date aDate) {
        if (aDate == null) {
            return false;
        }

        Date nowDate = new Date();
        return nowDate.after(aDate);
    }

    /**
     * Returns true if the input date is in the future. Note that this method
     * will return false if a null input is passed.
     *
     * @param aDate	The date to check
     *
     * @return boolean	True if the date is in the future
     */
    public static boolean isDateFuture(Date aDate) {
        if (aDate == null) {
            return false;
        }

        Date nowDate = new Date();
        return nowDate.before(aDate);
    }

    /**
     * Returns true if two Dates represent the same DAY (regardless of time)
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));

    }

    /**
     * Returns true if the passed date is today (regardless of time)
     */
    public static boolean isToday(Date date1) {
        Date today = new Date();
        return isSameDay(date1, today);
    }

    /**
     * Returns true if two Dates are equal. Note that this method returns true
     * if both arguments are null
     */
    public static boolean isEqual(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        }

        return (date1 != null && date1.equals(date2));
    }

    /**
     * Returns true if the argument date falls within the specified date range
     *
     * @param argDate The date to check
     * @param startDate The start date of the range
     * @param endDate Then end date of the range
     * @return boolean True if startDate <= argDate <= endDate
     */
    public static boolean isWithinRange(Date argDate, Date startDate, Date endDate) {
        if (argDate == null || startDate == null || endDate == null) {
            return false;
        }

        long argDateTime = argDate.getTime();
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();

        return (startDateTime <= argDateTime && argDateTime <= endDateTime);
    }

    /**
     * Returns true if date1 is less than date2
     */
    public static boolean isLessThan(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        return date1.before(date2);
    }

    /**
     * Returns true if date1 is less than date2 or date1 is equal to date 2
     */
    public static boolean isLessThanOrEqual(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        return date1.getTime() <= date2.getTime();
    }

    /**
     * Returns true if date1 is greater than date2
     */
    public static boolean isGreater(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        return date1.after(date2);
    }

    /**
     * Returns true if date1 is greater than date2 or date1 is equal to date 2
     */
    public static boolean isGreaterOrEqual(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        return date1.getTime() >= date2.getTime();
    }

    /**
     * Returns the greater of the two passed dates. If either date is null, the
     * other date is returned. If both are null, null is returned.
     */
    public static Date greatest(Date date1, Date date2) {
        if (date1 == null) {
            return date2;
        }
        if (date2 == null) {
            return date1;
        }

        return (date1.getTime() > date2.getTime()) ? date1 : date2;
    }

    //OLD FORMATTING METHODS --------------------------------------------------
    /**
     * Checks to see if a date (specified by month, day, year) is valid
     */
    public static boolean validateDate(String month, String day, String year) {
        log.debug("DateUtils.validateDate(" + month + ", " + day + ", " + year + ")");

        try {
            //clone the format to avoid thread safety issues
            DateFormat clone = (DateFormat) MMDDYYYY.clone();

            clone.parse(month + day + year);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Checks to see if a date (specified by month, day, year) is valid
     */
    public static boolean validateDate(String date, DateFormat format) {

        try {
            //clone the format to avoid thread safety issues
            DateFormat clone = (DateFormat) format.clone();

            clone.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * MAIN METHOD FOR TESTING ONLY!
     *
     * Run with java -classpath path/to/codemagi.jar DateUtils
     */
    public static void main(String[] args) {

        Date timeDate = setTime(new Date(), 7, 00, 00);
        System.out.println("7am: " + timeDate);

        timeDate = setTime(new Date(), 7, 00, 00, TimeZone.getTimeZone("America/New_York"));
        System.out.println("7am EST: " + timeDate);

        timeDate = setTime(new Date(), 19, 00, 00, "America/New_York");
        System.out.println("7pm EST: " + timeDate);

        /*
         Calendar startCal = Calendar.getInstance();
         startCal.set(2005, 11, 1, 10, 43);

         Calendar endCal   = Calendar.getInstance();
         endCal.set(2006, 3, 30, 10, 43);

         Date startDate = startCal.getTime();
         Date endDate   = endCal.getTime();

         System.out.println("Start Date: " + formatDate(startDate, TO_DATE_MYSQL) );
         System.out.println(" End  Date: " + formatDate(endDate, TO_DATE_MYSQL) );
         System.out.println("Months in span: " + monthsInSpan(startDate, endDate));
         */
    }

}
