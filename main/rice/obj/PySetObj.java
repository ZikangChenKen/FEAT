package main.rice.obj;

import java.util.Set;

/**
 * A representation of Python objects of type set.
 *
 * @param <InnerType> the type of each element in this set
 */
public class PySetObj<InnerType extends APyObj> extends AIterablePyObj<InnerType> {

    /**
     * Constructor for a PySetObj; initializes its value.
     *
     * @param value a set whose contents will become the value of this PySetObj
     */
    public PySetObj(Set<InnerType> value) {
        this.value = value;
    }

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation (i.e., {elem1, elem2, elem3, ...})).
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        // Special case for an empty set
        if (this.value.size() == 0) {
            return "set()";
        }
        else {
            String retval = this.value.toString();
            retval = retval.substring(1, retval.length() - 1);
            return "{" + retval + "}";
        }
    }
}