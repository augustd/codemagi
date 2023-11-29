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

import java.text.BreakIterator;

import java.util.StringTokenizer;
import java.util.regex.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides static utility methods for working with Strings.
 *
 * @version 1.0
 * @author August Detlefsen for CodeMagi, Inc.
 */
public class StringUtils {

    static Logger log = LogManager.getLogger("com.codemagi.util.StringUtils");

    //Regex patterns for formatting links            
    protected static final Pattern HTTP_LINK_PATTERN = Pattern.compile("https?://\\S*");
    protected static final Pattern FTP_LINK_PATTERN = Pattern.compile("ftp://\\S*");

    private static final String[] PSEUDO = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    /**
     * Singleton - Block constructor by design
     */
    private StringUtils() {
    }

    /**
     * Returns true if the two input Strings are equal, false otherwise.
     */
    public static boolean isEqual(String string1, String string2) {

        try {
            return string1.equals(string2);
        } catch (NullPointerException e) {
            //nada
        }

        return false;
    }

    /**
     * HTML Entity-Encodes the given string. For use in output sanitisation to
     * protect against reflective XSS attacks. Taken verbatim from
     * http://www.owasp.org/index.php/How_to_perform_HTML_entity_encoding_in_Java
     */
    public static String HTMLEntityEncode(String s) {

        if (Utils.isEmpty(s)) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
                buf.append(c);
            } else {
                buf.append("&#" + (int) c + ";");
            }
        }
        return buf.toString();
    }

    /**
     * Returns a substring of the input String, or "" if the input String is
     * null, or returns a partial substring if an IndexOutOfBoundsException is
     * caught
     */
    public static String substring(String input, int beginIndex) {
        if (Utils.isEmpty(input)) {
            return "";
        }

        return substring(input, beginIndex, input.length());
    }

    /**
     * Returns a substring of the input String, or "" if the input String is
     * null, or returns a partial substring if an IndexOutOfBoundsException is
     * caught
     */
    public static String substring(String input, int beginIndex, int endIndex) {
        String output = "";

        if (beginIndex > endIndex) {
            return output;
        }

        if (Utils.isEmpty(input)) {
            return output;
        }

        try {
            output = input.substring(beginIndex);

        } catch (IndexOutOfBoundsException ioobe) {
            //condition: beginIndex is negative or larger than the length of this String object.
            return output; // "" at this point
        }

        try {
            output = output.substring(0, endIndex - beginIndex);

        } catch (IndexOutOfBoundsException ioobe) {
            //condition: endIndex is larger than the length of this String 
            //no-op: we return from begin index to end of string
        }

        return output;
    }

    /**
     * Returns characters from the END of a String.
     *
     * @param input The String to return characters from
     * @param length The number of characters to return
     */
    public static String right(String input, int length) {
        if (Utils.isEmpty(input)) {
            return "";
        }

        if (length <= 0 || length >= input.length()) {
            return input;
        }

        return input.substring(input.length() - length);
    }

    /**
     * Converts the input String to Upper-lower case
     *
     * @param input The String to convert
     * @return String The input String, converted
     */
    public static String initCap(String input) {

        if (Utils.isEmpty(input)) {
            return "";
        }

        StringBuilder buffer = new StringBuilder(input.length());

        char[] chars = input.toCharArray();
        Character currChar;
        Character prevChar = '.';
        for (int i = 0; i < chars.length; i++) {
            currChar = chars[i];
            if (Character.isLetter(currChar) && !Character.isLetter(prevChar)) {
                currChar = Character.toUpperCase(currChar);
            } else {
                currChar = Character.toLowerCase(currChar);
            }
            prevChar = currChar;
            buffer.append(currChar);
        }

        return buffer.toString();
    }

    /**
     * Strips punctuation characters from the input String
     *
     * @param input The String to strip from
     * @return String The input String, stripped of any punctuation characters
     */
    public static String stripPunctuation(String input) {

        if (Utils.isEmpty(input)) {
            return "";
        }

        return input.replaceAll("\\p{P}+", "");
    }

    /**
     * Strips non-unicode characters from the input String
     *
     * @param input The String to strip from
     * @return String The input String, stripped of any non-unicode characters
     */
    public static String stripNonUnicode(String input) {

        if (Utils.isEmpty(input)) {
            return "";
        }

        StringBuilder buffer = new StringBuilder(input.length());

        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDefined(chars[i])) {
                buffer.append(chars[i]);
            }
        }

        return buffer.toString();
    }

    /**
     * Strips non-ASCII characters from the input String
     *
     * @param input The String to strip from
     * @return String The input String, stripped of any non-ASCII characters
     */
    public static String stripNonASCII(String input) {

        if (Utils.isEmpty(input)) {
            return "";
        }

        StringBuilder buffer = new StringBuilder(input.length());

        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int unicodeNum = (int) chars[i];
            if (unicodeNum < 128) {
                buffer.append(chars[i]);
            }
        }

        return buffer.toString();
    }

    /**
     * Strips HTML characters (anything between < and >) from the input String
     *
     * @param input The String to strip from
     * @return String The input String, stripped of any HTML characters
     */
    public static String stripHtmlRegex(String input) {

        if (Utils.isEmpty(input)) {
            return "";
        }

        //replace <P> with double break
        input = input.replaceAll("(?i)<P.*?>", "\n\n");

        //replace <BR> with single break
        input = input.replaceAll("(?i)<BR.*?>", "\n");

        //replace <UL>, <OL> with break
        input = input.replaceAll("(?i)<UL.*?>", "\n");
        input = input.replaceAll("(?i)<OL.*?>", "\n");
        input = input.replaceAll("(?i)</UL>", "\n\n");
        input = input.replaceAll("(?i)</OL>", "\n\n");

        //replace <LI> with *
        input = input.replaceAll("(?i)<LI.*?>", "\n * ");

        //now strip out any tags
        input = input.replaceAll("<.*?>", "");

        //final pass to strip out any incomplete tags
        input = input.replaceAll("<.*", "");

        //replace some common HTML entities with plain text equivalents
        input = input.replaceAll("&nbsp;", " ");
        input = input.replaceAll("&rsquo;", "'");
        input = input.replaceAll("&lsquo;", "'");
        input = input.replaceAll("&apos;", "'");
        input = input.replaceAll("&#39;", "'");
        input = input.replaceAll("&ndash;", "-");
        input = input.replaceAll("&mdash;", "-");
        input = input.replaceAll("&quot;", "\"");
        input = input.replaceAll("&ldquo;", "\"");
        input = input.replaceAll("&rdquo;", "\"");
        input = input.replaceAll("&amp;", "&");

        return input;
    }

    /**
     * Trims a String to a specified length. If the String is shorter than
     * length, it is unchanged
     *
     * @param input The String to trim
     * @param length The target length
     * @return String The input String trimmed to length
     */
    public static String truncate(String input, int length) {
        return truncate(input, length, "");
    }

    /**
     * Trims the input String to the specified length. If the String is shorter
     * than length, it is unchanged. If the String is longer than the trim
     * length, the specified ellipsis will be appended.
     *
     * @param input The String to trim
     * @param length The target length
     * @param ellipsis String to append to the end of the truncated String
     * @return String The input String trimmed to length
     */
    public static String truncate(String input, int length, String ellipsis) {
        if (Utils.isEmpty(input)) {
            return "";
        }

        if (input.length() <= length) {
            return input;
        }

        return input.substring(0, length) + Utils.noNulls(ellipsis);
    }

    /**
     * Trims a String to a specified length, keeping whole words intact. If the
     * String is shorter than length, it is unchanged.
     *
     * @param input The String to trim
     * @param length The target length
     * @return String The input String trimmed to length
     */
    public static String truncateWords(String input, int length) {
        return truncateWords(input, length, null);
    }

    /**
     * Trims the input String to the specified length, keeping whole words
     * intact. If the String is shorter than length, it is unchanged. If the
     * input is longer than the specified length, the output will break at the
     * end of the next full word.
     *
     * If the String is longer than the trim length, the specified ellipsis will
     * be appended.
     *
     * @param input The String to trim
     * @param length The target length
     * @param ellipsis String to append to the end of the truncated String
     * @return String The input String trimmed to length
     */
    public static String truncateWords(String input, int length, String ellipsis) {
        if (Utils.isEmpty(input)) {
            return "";
        }

        if (input.length() <= length) {
            return input;
        }

        //find the end of the last word after the truncate length
        Pattern p = Pattern.compile("\\W");
        Matcher m = p.matcher(input);
        boolean found = m.find(length);

        if (found) {
            return input.substring(0, m.start()) + Utils.noNulls(ellipsis);
        }

        return input;
    }

    /**
     * Removes everything except digits [0-9] from input
     *
     * @param valueIn The String to trim
     * @return String The input String trimmed of non-numbers
     */
    public static String trimNonNumbers(String valueIn) {

        //make sure they entered SOMETHING
        if (Utils.isEmpty(valueIn)) {
            return "";
        }

        //loop through each char, checking if it is valid
        char[] charsIn = valueIn.toCharArray();

        //output will be appended to:
        StringBuilder charsOut = new StringBuilder(valueIn.length());

        for (int i = 0; i < charsIn.length; i++) {
            char currentChar = charsIn[i];
            if (Character.isDigit(currentChar)) {
                charsOut.append(currentChar + "");
            }
        }

        return charsOut.toString();

    }

    /**
     * Removes everything except letters [a-zA-Z], digits [0-9] and spaces from
     * input
     *
     * @param valueIn The String to trim
     * @return String The input String trimmed of non-letters, non-digits and
     * non-spaces
     */
    public static String trimPunctuation(String valueIn) {

        //make sure they entered SOMETHING
        if (Utils.isEmpty(valueIn)) {
            return "";
        }

        //loop through each char, checking if it is valid
        char[] charsIn = valueIn.toCharArray();

        //output will be appended to:
        StringBuilder charsOut = new StringBuilder(valueIn.length());

        for (int i = 0; i < charsIn.length; i++) {
            char currentChar = charsIn[i];
            if (Character.isLetterOrDigit(currentChar) || Character.isSpaceChar(currentChar)) {
                charsOut.append(currentChar + "");
            }
        }

        return charsOut.toString();

    }

    /**
     * Trims all leading and trailing whitespace from a String.
     *
     * @param valueIn The String to trim
     * @return String The input String trimmed of leading and trailing
     * whitespace, or ""
     */
    public static String trim(String valueIn) {
        //make sure they entered SOMETHING
        if (Utils.isEmpty(valueIn)) {
            return "";
        }

        return valueIn.trim();
    }

    /**
     * Unquotes a String (ie removes leading and training double-quotes). Also
     * removes leading and trailing whitespace.
     *
     * @param valueIn The String to unquote
     * @return String The input String, unquoted and trimmed of leading and
     * trailing whitespace
     */
    public static String unQuote(String valueIn) {
        //make sure they entered SOMETHING
        String output = trim(valueIn);

        if (Utils.isEmpty(output)) {
            return "";
        }

        //remove leading "s
        while (output.startsWith("\"")) {
            output = output.substring(1);
        }

        //remove trailing "s
        while (output.endsWith("\"")) {
            output = output.substring(0, output.length() - 1);
        }

        return output;

    }

    /**
     * Adds line breaks ('\n') to a paragraph of text
     *
     * @param input The String to format
     * @param colWidth The suggested column width (actual width will be greater
     * depending on word breaks)
     * @return String The input text formatted with line breaks every colWidth
     */
    public static String paragraphFormat(String input, int colWidth) {

        //set up the break iterator
        BreakIterator lines = BreakIterator.getLineInstance();
        lines.setText(input);

        //setup the output buffer
        StringBuilder output = new StringBuilder(input.length());

        int lastLineBreak = 0;
        int lastBreak = 0;
        int currentBreak = 0;

        StringBuffer currentWord = new StringBuffer();

        while (lines.next() != BreakIterator.DONE) {

            currentBreak = lines.current();

            //reset the currentWord buffer
            currentWord.setLength(0);
            currentWord.append(input.substring(lastBreak, currentBreak));

            if (currentWord.toString().contains("http:")) {
                try {
                    //don't chop URLs.
                    //Problem is that Break iterator sees '.' as a good place to break.
                    //Solution is to find the next space or line break
                    int nextLine = input.indexOf("\n", currentBreak);
                    int nextSpace = input.indexOf(" ", currentBreak);
                    int nextBreak = nextLine >= nextSpace ? nextSpace : nextLine;
                    if (nextBreak == -1) { //must be at end
                        nextBreak = input.length();
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("NEXT BREAK: " + nextBreak + " line: " + nextLine + " space: " + nextSpace + " length: " + input.length());
                    }

                    //append everything up to the end of the url
                    currentWord.append(input.substring(currentBreak, nextBreak));

                    //set the break iterator forward
                    for (int i = currentBreak; i != BreakIterator.DONE && i < nextBreak; i += 0) {
                        //keep getting next until ...
                        i = lines.next();
                        currentBreak = i;
                        if (log.isDebugEnabled()) {
                            log.debug("forwarding to " + i);
                        }
                    }

                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log.debug("", e);
                    }
                }

            } else if (currentWord.toString().contains("\n")) {
                //if there is already a line break, update
                lastLineBreak = currentBreak;

            } else if (currentBreak > lastLineBreak + colWidth) {
                //if column is getting too wide, break it
                if (log.isDebugEnabled()) {
                    log.debug("Breaking at: " + currentBreak + " last: " + lastBreak);
                }

                currentWord.append("\n");
                lastLineBreak = currentBreak;
            }

            //append currentWord to the output buffer
            output.append(currentWord);

            //reset the break
            lastBreak = currentBreak;

        }

        return output.toString();

    }

    /**
     * Remove excess spaces
     *
     * @param sourceString The String to clean
     * @return String The cleaned String or "" if the the input is null
     */
    public static String clean(String sourceString) {

        if (Utils.isEmpty(sourceString)) {
            return "";
        }

        String outputString = StringUtils.replace(sourceString, "  ", " ", true);

        return outputString.trim();
    }

    /**
     * Formats input String into HTML suitable for DB insert by removing excess
     * spaces and breaks, replacing double breaks with <BR><BR>
     */
    public static String formatHTML(String sourceString) {
        if (Utils.isEmpty(sourceString)) {
            return "";
        }

        String output = "";

        Matcher httpMatcher = HTTP_LINK_PATTERN.matcher(sourceString);
        output = httpMatcher.replaceAll("<A HREF=\"$0\">$0</A>");

        Matcher ftpMatcher = FTP_LINK_PATTERN.matcher(output);
        output = ftpMatcher.replaceAll("<A HREF=\"$0\">$0</A>");

        output = replace(output, "\r\n", "<BR>");
        output = replace(output, "\n", "<BR>");
        output = replace(output, "\r", "<BR>");

        return output;
    }

    /**
     * Translates input Strings to UPPER case
     *
     * @param sourceString String to convert
     * @return String The source String converted to UPPER case, or ""
     */
    public static String toUpperCase(String sourceString) {
        if (Utils.isEmpty(sourceString)) {
            return "";
        }

        return sourceString.toUpperCase();
    }

    /**
     * Translates input Strings to lower case
     *
     * @param sourceString String to convert
     * @return String The source String converted to lower case, or ""
     */
    public static String toLowerCase(String sourceString) {
        if (Utils.isEmpty(sourceString)) {
            return "";
        }

        return sourceString.toLowerCase();
    }

    /**
     * Concatenates 2 Strings, with the specified separator
     */
    public static String concat(String string1, String string2, String separator) {
        StringBuilder output = new StringBuilder();
        output.append(Utils.noNulls(string1));

        if (!Utils.isEmpty(string1) && !Utils.isEmpty(string2)) {
            output.append(Utils.noNulls(separator));
        }

        output.append(Utils.noNulls(string2));

        return output.toString();
    }

    /**
     * String replacement
     *
     * Keep in mind that "" != "" is true
     *
     * @param sourceString
     * @param target
     * @param replaceWith
     */
    public static String replace(String sourceString, String target, String replaceWith, boolean recursive) {

        //make sure params are not null
        if (Utils.isEmpty(sourceString) || target == null) {
            return sourceString;
        }

        if (replaceWith == null) {
            replaceWith = "";
        }

        int index = 0;
        int targetLength = target.length();
        int replaceLength = replaceWith.length();

        StringBuilder output = new StringBuilder(sourceString);

        while (true) {
            index = output.indexOf(target, index);

            if (index == -1) {
                break;
            }

            //needs to use StringBuffer
            output.replace(index, index + targetLength, replaceWith);

            if (!recursive) {
                index += replaceLength;
            }
        }

        return output.toString();
    }

    /**
     * Single-character replacement
     *
     * @param sourceString
     * @param target
     * @param replaceWith
     */
    public static String replace(String sourceString, char target, String replaceWith, boolean recursive) {

        //make sure params are not null
        if (Utils.isEmpty(sourceString)) {
            return sourceString;
        }

        if (replaceWith == null) {
            replaceWith = "";
        }

        int index = 0;
        int targetLength = 1;
        int replaceLength = replaceWith.length();

        StringBuilder output = new StringBuilder(sourceString);

        while (true) {
            index = output.toString().indexOf(target, index);

            if (index == -1) {
                break;
            }

            output.replace(index, index + targetLength, replaceWith);

            if (!recursive) {
                index += replaceLength;
            }
        }

        return output.toString();
    }

    public static String replace(String sourceString, char target, String replaceWith) {
        return replace(sourceString, target, replaceWith, false);
    }

    public static String replace(String sourceString, String target, String replaceWith) {
        return replace(sourceString, target, replaceWith, false);
    }

    public static String replace(String sourceString, String target, StringBuffer replaceWith) {
        return replace(sourceString, target, replaceWith.toString(), false);
    }

    /**
     * Escapes characters that are not valid XML values. Performs the following
     * transformations:
     *
     * ' -> &apos;
     * < -> &lt; > -> &gt; & -> &amp;
     *
     * @param valueIn String that needs to be escaped
     * @return String The transformed value.
     */
    public static String escapeXMLCharacters(String valueIn) {
        if (valueIn == null) {
            return "";
        }

        String valueOut = valueIn;

        valueOut = replace(valueOut, "&", "&amp;");
        valueOut = replace(valueOut, "<", "&lt;");
        valueOut = replace(valueOut, ">", "&gt;");
        valueOut = replace(valueOut, "'", "&apos;");
        valueOut = replace(valueOut, "\"", "\\\"");

        return valueOut;
    }

    /**
     * Un-Escapes characters that are not valid XML values. Performs the
     * following transformations:
     *
     * This method is the reverse of escapeXMLCharacters(String valueIn)
     *
     * ' -> &apos;
     * < -> &lt; > -> &gt; & -> &amp;
     *
     *
     * @param valueIn String that needs to be escaped
     * @return String The transformed value.
     * @see escapeXMLCharacters()
     */
    public static String unEscapeXMLCharacters(String valueIn) {
        if (valueIn == null) {
            return "";
        }

        String valueOut = valueIn;

        valueOut = replace(valueOut, "\\\"", "\"");
        valueOut = replace(valueOut, "&lt;", "<");
        valueOut = replace(valueOut, "&gt;", ">");
        valueOut = replace(valueOut, "&apos;", "'");
        valueOut = replace(valueOut, "&amp;", "&");

        return valueOut;
    }

    /**
     * deal with special characters in html for form submittals BROWSER
     * RESOLUTION ONLY
     */
    public static String warpHTML(String valueIn) {
        if (valueIn == null) {
            return "";
        }

        String valueOut = replace(valueIn, "&", "&amp;");

        return valueOut;
    }

    // always preserves minor html tags, does not always preserve
    // tags with " in them
    public static String unicodeToHTML(String text, boolean bPreserveFullHTMLTags) {

        if (text == null) {
            return null;
        }

        StringBuilder resultBuffer = new StringBuilder(text.length());

        char[] textChars = text.toCharArray();

        for (char x : textChars) {
            resultBuffer.append(convertUnicodeCharToHTML(x, !bPreserveFullHTMLTags));
        }

        return resultBuffer.toString();
    }

    /**
     * Converts a unicode text String (for example a form input) into HTML
     * characters. NOTE: Always preserves minor html tags, does not always
     * preserve tags with " in them.
     */
    public static String unicodeToHTML(String text) {
        return unicodeToHTML(text, false);
    }

    private static char cleanUTF8Char(char aChar) {
        int unicodeNum = (int) aChar;

        if (unicodeNum > 127) {
            return ' ';
        }

        return aChar;
    }

    /**
     * Replace Unicode characters with HTML equivalents. ASCII characters 0-31
     * and 127 are discarded. Extended ASCII 129, 141, 143, 144 and 157 are
     * discarded. Other ASCII characters are kept as-is.
     *
     * @param aChar A character to convert to its HTML equivalent
     * @param bTranslateAmpersands If true, ampersands will be converted to the
     * HTML equivalent "&amp;amp;"
     */
    private static String convertUnicodeCharToHTML(char aChar, boolean bTranslateAmpersands) {
        int unicodeNum = (int) aChar;

        if (unicodeNum < 32) {
            return ""; //ASCII control characters
        }
        if (unicodeNum == 127) {
            return ""; //ASCII control characters
        }
        if (unicodeNum == 38) {
            return bTranslateAmpersands ? "&amp;" : "&"; //Ampersand - special case
        }
        if (unicodeNum < 127) {
            return "" + aChar; //The rest of standard ASCII
        }
        if (unicodeNum == 129) {
            return ""; //Extended ASCII control characters
        }
        if (unicodeNum == 141) {
            return ""; //Extended ASCII control characters
        }
        if (unicodeNum == 143) {
            return ""; //Extended ASCII control characters
        }
        if (unicodeNum == 144) {
            return ""; //Extended ASCII control characters
        }
        if (unicodeNum == 157) {
            return ""; //Extended ASCII control characters
        }
        return "&#" + unicodeNum + ";"; //Encode the rest to numeric entities by default
    }

    /**
     * Convert a byte[] array to readable string format. This makes the "hex"
     * readable!
     *
     * @return result String buffer in String format
     * @param in byte[] buffer to convert to string format
     */
    public static String byteArrayToHexString(byte in[]) {

        byte ch = 0x00;

        int i = 0;

        if (in == null || in.length <= 0) {
            return null;
        }

        StringBuffer out = new StringBuffer(in.length * 2);

        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0); // Strip off high nibble

            ch = (byte) (ch >>> 4); // shift the bits down

            ch = (byte) (ch & 0x0F); // must do this is high order bit is on!

            out.append(PSEUDO[(int) ch]); // convert the nibble to a String Character

            ch = (byte) (in[i] & 0x0F); // Strip off low nibble 

            out.append(PSEUDO[(int) ch]); // convert the nibble to a String Character

            out.append(" ");

            i++;
        }

        String rslt = new String(out);

        return rslt;

    }

    public static String escapeQuotes(String bodycopy) {
        if (bodycopy == null) {
            return null;
        }

        return StringUtils.replace(bodycopy, "\"", "&quot;");
    }

    /**
     * Adds single quotes (') around a String. Escapes any single quotes within
     * the String with the escape String
     *
     * @param valueIn String to modify
     * @param escapeString String to escape single quotes with
     * @return String The original String, enclosed in single quotes.
     */
    public static String singleQuote(String valueIn, String escapeString) {

        if (Utils.isEmpty(valueIn)) {
            return null;
        } else {
            StringBuffer buf = new StringBuffer(valueIn.length() + (10 * escapeString.length()));
            // add the begining quote
            buf.append("'");

            //add escape chars for any single quotes within the string
            for (int i = 0; i < valueIn.length(); i++) {
                if (valueIn.charAt(i) == '\'') {
                    buf.append(escapeString);
                }
                buf.append(valueIn.charAt(i));
            }

            // add the ending quote
            buf.append("'");
            return buf.toString();
        }
    }

    /**
     * UTF8 is basically ascii, or the first 127 characters in the Unicode char
     * set (an 8 bit byte, get it?)
     */
    public static String unicodeToUTF8(String text) {
        if (text == null) {
            return null;
        }

        StringBuilder resultBuffer = new StringBuilder(text.length());

        char[] textChars = text.toCharArray();

        for (int x = 0; x < textChars.length; x++) {
            resultBuffer.append(cleanUTF8Char(textChars[x]));
        }

        return resultBuffer.toString();
    }

    /**
     * Filters out all characters in the filter string from the input string.
     *
     * @return String The input String with filter chars removed
     */
    public static String filter(String input, String filter) {

        StringBuilder result = new StringBuilder(input.length());

        StringTokenizer st = new StringTokenizer(input, filter, false);

        while (st.hasMoreTokens()) {
            result.append(st.nextToken());
        }

        return result.toString();
    }

    /**
     * Convert an input String into binary.
     *
     * @param data The input String to convert
     * @return String binary value of the input String
     */
    public static String toBinaryString(String data) {

        StringBuilder result = new StringBuilder(data.length() * 8); // 8 bits in a byte

        byte[] theBytes = data.getBytes();

        for (int x = 0; x < theBytes.length; x++) {
            result.append(padLeftZeros(Integer.toBinaryString((int) theBytes[x]) + "", 8));
        }

        return result.toString();
    }

    /**
     * Adds the passed character to the left side of a String until it reaches a
     * ceratin length
     *
     * @param data String to pad
     * @param length Length to pad to
     * @param padCharacter Character to pad with
     */
    public static String pad(String data, int length, char padCharacter) {
        if (data == null) {
            data = "";
        }

        StringBuilder output = new StringBuilder(length);
        output.append(data);

        while (output.length() < length) {
            output.insert(0, padCharacter); //data = padCharacter + data;
        }

        return data;
    }

    /**
     * Adds zeros to the left side of a String until it reaches a ceratin length
     *
     * @param data String to pad
     * @param length Length to pad to
     */
    public static String padZeros(String data, int length) {
        return pad(data, length, '0');
    }

    private static String padLeftZeros(String data, int maxChars) {
        if (log.isDebugEnabled()) {
            log.debug("Original: " + data + ":");
        }

        //String result = data;
        if (data.length() > 8) {
            data = data.substring(data.length() - 8);
        } else {
            int padZeros = maxChars - data.length();
            for (int x = 0; x < padZeros; x++) {
                data = "0" + data;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("return result: " + data);
        }

        return data;
    }

    /**
     * Returns the difference between two strings
     */
    public static String diff(String string1, String string2) {

        if (Utils.isEmpty(string1)) {
            string1 = "";
        }
        if (Utils.isEmpty(string2)) {
            string2 = "";
        }

        StringBuilder output = new StringBuilder(1024);

        String[] x = string1.split("\\n");
        String[] y = string2.split("\\n");

        // number of lines of each file
        int M = x.length;
        int N = y.length;

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M + 1][N + 1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M - 1; i >= 0; i--) {
            for (int j = N - 1; j >= 0; j--) {
                if (x[i].equals(y[j])) {
                    opt[i][j] = opt[i + 1][j + 1] + 1;
                } else {
                    opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
                }
            }
        }

        // recover LCS itself and print out non-matching lines to standard output
        int i = 0, j = 0;
        while (i < M && j < N) {
            if (x[i].equals(y[j])) {
                i++;
                j++;
            } else if (opt[i + 1][j] >= opt[i][j + 1]) {
                output.append("< ").append(x[i++]).append("\n");
            } else {
                output.append("> ").append(y[j++]).append("\n");
            }
        }

        // dump out one remainder of one string if the other is exhausted
        while (i < M || j < N) {
            if (i == M) {
                output.append("> ").append(y[j++]).append("\n");
            } else if (j == N) {
                output.append("< ").append(x[i++]).append("\n");
            }
        }

        return output.toString();
    }

    /**
     * MAIN METHOD FOR TESTING ONLY!
     */
    public static void main(String[] args) {

        String source = "Bristol-Myers Squibb Co. ipilimumab anti-CTLA4 monoclonal antibody (humanized) 2nd line metastatic Melanoma III Bristol-Myers Squibb Co. brivanib VEGFR-2 inhibitor 1st line Hepatocellular carcinoma (HCC) III";

        System.out.println(unicodeToHTML(source));

        String ccNum = "1234 5678 9101 1121";
        System.out.println("ccNum: " + ccNum + " last 4: " + right(ccNum, 4));

        ccNum = "1234567891011121";
        System.out.println("ccNum: " + ccNum + " last 4: " + right(ccNum, 4));

        source = "The quick\nbrown fox...";
        if (args.length > 0) {
            source = args[0];
        }

        System.out.println("Source: " + source);
        System.out.println(" Bytes: " + ArrayUtils.commaDelimit(source.getBytes()));

        Stopwatch timer = new Stopwatch();

        System.out.println("Testing StripHtmlRegex");
        System.out.println("      source: " + source);
        timer.start();
        System.out.println("      output: " + stripHtmlRegex(source));
        timer.stop();
        System.out.println("        time: " + timer.getElapsedTime());

        System.out.println("Testing regex");
        if (args.length < 2) {
            System.out.println("Usage: com.codemagi.util.StringUtils <source> <regex>");
            return;
        }
        source = args[0];
        String regex = args[1];

        System.out.println("      source: " + source);
        System.out.println("       regex: " + regex);

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        boolean b = m.matches();

        System.out.println(" exact match: " + b);
        if (b) {
            System.out.println("       group: " + m.group() + " (count: " + m.groupCount() + ")");
        }

        b = m.find();

        System.out.println("    contains: " + b);
        if (b) {
            System.out.println("       group: " + m.group() + " (count: " + m.groupCount() + ")");

            if (m.groupCount() > 0) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    System.out.println("     group " + i + ": " + m.group(i));
                }
            }
        }

        System.out.println("Testing replace method");

        String test = "OST'Q\\J";

        System.out.println("Original: " + test);

        System.out.println("Replace \\ with _: " + replace(test, "\\", "_"));

        System.out.println("Testing substring method");

        test = "(704) 933-1202 1234";

        System.out.println("Original: " + test);

        System.out.println("Area code: " + substring(test, 1, 4));
        System.out.println("Exchange:  " + substring(test, 6, 9));
        System.out.println("Number:    " + substring(test, 10, 14));
        System.out.println("Extension: " + substring(test, 15));

        System.out.println("Modified: " + test);
    }

}
