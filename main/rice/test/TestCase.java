package main.rice.test;

import main.rice.obj.APyObj;
import java.util.List;

/**
 * A representation of a test case; a wrapper around its arguments, each of which is an
 * APyObj.
 */
public class TestCase {

    /**
     * The list of arguments (Python objects) comprising this test case.
     */
    private final List<APyObj> args;

    /**
     * Constructor for a TestCase; initializes its list of arguments to the input.
     *
     * @param args the list of arguments (Python objects) comprising this test case
     */
    public TestCase(List<APyObj> args) {
        this.args = args;
    }

    /**
     * Returns the list of arguments comprising this test case.
     *
     * @return the list of arguments comprising this test case
     */
    public List<APyObj> getArgs() {
        return this.args;
    }

    /**
     * Returns a string representation of this test's arguments.
     *
     * @return a string representation of this test's arguments
     */
    @Override
    public String toString() {
        return this.args.toString();
    }

    /**
     * Compares this test's arguments to the input object's arguments (if it's a TestCase)
     * by value.
     *
     * @param obj the object to compare against
     * @return true if this is equivalent by value to obj; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // If obj is not a TestCase, it's not equivalent
        if (!(obj instanceof TestCase other)) {
            return false;
        }

        // Compare the lists of arguments for equality
        return this.args.equals(other.getArgs());
    }

    /**
     * Computes a hash code based on this object's value, such that two objects that are
     * considered equal by .equals() will also have the same hash code.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return this.args.hashCode();
    }
}