package main.rice.obj;

/**
 * A representation of Python objects of type int.
 */
public class PyIntObj extends APyObj {

    /**
     * The value of this PyIntObj.
     */
    private final Integer value;

    /**
     * Constructor for a PyIntObj; initializes its value to the input.
     *
     * @param value the value of this PyIntObj
     */
    public PyIntObj(Integer value) {
        this.value = value;
    }

    /**
     * Returns the underlying (Java Integer) representation of this PyIntObj.
     *
     * @return the underlying (Java Integer) representation of this PyIntObj
     */
    @Override
    public Integer getValue() {
        return this.value;
    }

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return this.value.toString();
    }
}