package main.rice.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of Python objects of type string.
 */
public class PyStringObj extends AIterablePyObj<PyCharObj> {

    /**
     * Constructor for a PySetObj; initializes its value to a list containing each
     * character in the input String.
     *
     * @param value the sequence of characters representing the value of this PySetObj
     */
    public PyStringObj(String value) {
        this.value = new ArrayList<>();
        for (int idx = 0; idx < value.length(); idx++) {
            this.value.add(new PyCharObj(value.charAt(idx)));
        }
    }

    /**
     * Constructor for a PySetObj; initializes its value to the input.
     *
     * @param value the value of this PySetObj
     */
    public PyStringObj(List<PyCharObj> value) {
        this.value = value;
    }

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation; uses single quotes for compatibility with command-line
     * invocation of Python scripts.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        // Concatenate all characters within this.value to get a single string
        StringBuilder sb = new StringBuilder();
        for (PyCharObj character : this.value) {
            sb.append(character.getValue());
        }

        // Concatenate single quotes; this will automatically call sb.toString()
        return "'" + sb + "'";
    }

    /**
     * Compares this to the input object by value; a wrapper around the implementation
     * in the superclass (APyObj) that first checks that obj is a PyStringObj.
     *
     * @param obj the object to compare against
     * @return true if this is equivalent by value to obj; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PyStringObj)) {
            return false;
        }
        return super.equals(obj);
    }
}