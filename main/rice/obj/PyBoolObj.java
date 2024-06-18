package main.rice.obj;

/**
 * A representation of Python objects of type bool.
 */
public class PyBoolObj extends APyObj {

    /**
     * The value of this PyBoolObj.
     */
    private final Boolean value;

    /**
     * Constructor for a PyBoolObj; initializes its value to the input.
     *
     * @param value the value of this PyBoolObj
     */
    public PyBoolObj(Boolean value) {
        this.value = value;
    }

    /**
     * Returns the underlying (Java Boolean) representation of this PyBoolObj.
     *
     * @return the underlying (Java Boolean) representation of this PyBoolObj
     */
    @Override
    public Boolean getValue() {
        return this.value;
    }

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation (i.e., True or False).
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        if (!this.value) {
            return "False";
        }
        return "True";
    }
}