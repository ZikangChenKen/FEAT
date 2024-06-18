package main.rice.obj;

import java.util.List;

/**
 * A representation of Python objects of type tuple.
 *
 * @param <InnerType> the type of each element in this tuple
 */
public class PyTupleObj<InnerType extends APyObj> extends AIterablePyObj<InnerType> {

    /**
     * Constructor for a PyTupleObj; initializes its value to the input.
     *
     * @param value the value of this PyListObj
     */
    public PyTupleObj(List<InnerType> value) {
        this.value = value;
    }

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation (i.e., (elem1, elem2, elem3, ...)).
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        StringBuilder repr = new StringBuilder("(");

        // Add all of the elements
        for (InnerType elem : this.value) {
            repr.append(elem).append(", ");
        }

        // Remove last ", " if length is greater than 1; otherwise just remove last " "
        if (this.value.size() > 1) {
            repr = new StringBuilder(repr.substring(0, repr.length() - 2));
        } else if (this.value.size() == 1) {
            repr = new StringBuilder(repr.substring(0, repr.length() - 1));
        }
        repr.append(")");
        return repr.toString();
    }

    /**
     * Compares this to the input object by value; a wrapper around the implementation
     * in the superclass (APyObj) that first checks that obj is a PyTupleObj.
     *
     * @param obj the object to compare against
     * @return true if this is equivalent by value to obj; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PyTupleObj)) {
            return false;
        }
        return super.equals(obj);
    }
}