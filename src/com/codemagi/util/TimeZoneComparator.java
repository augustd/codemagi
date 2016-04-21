package com.codemagi.util;

import java.util.TimeZone;

/**
 * Comparator to sort TimeZones by offset from GMT
 */
public class TimeZoneComparator implements java.util.Comparator<TimeZone> {

    public int compare(TimeZone o1, TimeZone o2) {

        if (o1.getRawOffset() != o2.getRawOffset()) {
            return o1.getRawOffset() - o2.getRawOffset();
        }

        String n1 = o1.getDisplayName() + " (" + o1.getID() + ")";
        String n2 = o2.getDisplayName() + " (" + o2.getID() + ")";

        return n1.compareTo(n2);
    }

}
