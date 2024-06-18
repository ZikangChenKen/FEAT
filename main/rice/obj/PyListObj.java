package main.rice.obj;

import java.util.List;

/**
 * A representation of Python objects of type list.
 *
 * @param <InnerType> the type of each element in this list
 */
public class PyListObj<InnerType extends APyObj> extends AIterablePyObj<InnerType> {

    /**
     * Constructor for a PyListObj; initializes its value to the input.
     *
     * @param value the value of this PyListObj
     */
    public PyListObj(List<InnerType> value) {
        this.value = value;
    }

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation (i.e., [elem1, elem2, elem3, ...]).
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return this.getValue().toString();
    }

    /**
     * Compares this to the input object by value; a wrapper around the implementation
     * in the superclass (APyObj) that first checks that obj is a PyListObj.
     *
     * @param obj the object to compare against
     * @return true if this is equivalent by value to obj; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PyListObj)) {
            return false;
        }
        return super.equals(obj);
    }
}