package main.rice.obj;

/**
 * A helper class for implementing PyStringObjs; each instance represents a single
 * character in a Python string.
 */
public class PyCharObj extends APyObj {

    /**
     * The value of this PyCharObj.
     */
    private final Character value;

    /**
     * Constructor for a PyCharObj; initializes its value to a string of the input.
     *
     * @param value a character representing the value of this PyCharObj
     */
    public PyCharObj(Character value) {
        this.value = value;
    }

    /**
     * Returns the underlying (Java Character) representation of this PyCharObj.
     *
     * @return the underlying (Java Character) representation of this PyCharObj
     */
    @Override
    public Character getValue() {
        return this.value;
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
        return "'" + this.value.toString() + "'";
    }
}
