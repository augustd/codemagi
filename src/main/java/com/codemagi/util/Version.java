package com.codemagi.util;

/**
 * This class models a software version string, like: 3.1.21
 *
 * It includes methods for comparing versions.
 */
public class Version {

    private String version;
    private int[] parts = {};

    private boolean alpha = false;
    private boolean beta = false;

    public Version(String version) {
        this.version = version;

        parse();
    }

    private void parse() {

        //sanity check 
        version = Utils.noNulls(version);

        String[] sParts = version.split("\\.");

        parts = new int[sParts.length];

        for (int i = 0; i < sParts.length; i++) {
            String part = sParts[i];

            if (part.contains("a")) {
                alpha = true;
            } else if (part.contains("b")) {
                beta = true;
            }

            part = part.replaceAll("\\D", "");

            parts[i] = Integer.parseInt(part);
        }
    }

    //GETTERS AND SETTERS ----------------------------------------
    public void setVersion(String newValue) {
        version = newValue;

        parse();
    }

    public String getVersion() {
        return version;
    }

    //UTILITY METHODS -------------------------------------------
    public int getMajorVersion() {
        return getVersionElement(0);
    }

    public int getVersionElement(int elementNumber) {
        if (parts == null || parts.length <= elementNumber) {
            return 0;
        }

        return parts[elementNumber];
    }

    public int getNumElements() {
        if (parts == null) {
            return 0;
        }

        return parts.length;
    }

    /**
     * Returns true if this version is greater than the passed version
     */
    public boolean isGreater(Version that) {
        return this.compareTo(that) > 0;
    }

    public boolean isGreater(String that) {
        return isGreater(new Version(that));
    }

    /**
     * Implementation of the Comparable interface.
     */
    public int compareTo(Object o) {

        Version that = (Version) o;

        int numElementsToCompare = Math.max(this.getNumElements(), that.getNumElements());

        for (int i = 0; i < numElementsToCompare; i++) {
            int thisElement = this.getVersionElement(i);
            int thatElement = that.getVersionElement(i);

            if (thisElement > thatElement) {
                return 1;
            }
            if (thatElement > thisElement) {
                return -1;
            }
        }

        return 0;
    }

    /**
     * Main method for testing only!
     */
    public static void main(String[] args) {

        Version test = new Version("1.1");
        System.out.println("1.1 greater than 1.0? " + test.isGreater("1.0"));

        test.setVersion("1.0.1");
        System.out.println("1.0.1 greater than 1.1? " + test.isGreater("1.1"));

        test.setVersion("1.9");
        System.out.println("1.9 greater than 1.10? " + test.isGreater("1.10"));

        test.setVersion("1.1");
        System.out.println("1.1 greater than 1.1? " + test.isGreater("1.1"));

    }

}
