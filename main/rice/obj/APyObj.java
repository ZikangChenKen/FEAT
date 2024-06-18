package main.rice.obj;

/**
 * An abstract class whose instances represent specific Python objects.
 */
public abstract class APyObj {

    /**
     * Returns the underlying (Java) representation of this Python object.
     *
     * @return the underlying (Java) representation of this Python object
     */
    public abstract Object getValue();

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation.
     *
     * @return a string representation of this object
     */
    @Override
    public abstract String toString();

    /**
     * Compares this to the input object by value.
     *
     * @param obj the object to compare against
     * @return true if this is equivalent by value to obj; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // If obj is the wrong type, it's clearly not equivalent
        if (!(obj instanceof APyObj other)) {
            return false;
        }

        // Compare by value
        return this.getValue().equals(other.getValue());
    }

    /**
     * Computes a hash code based on this object's value, such that two objects that are
     * considered equal by .equals() will also have the same hash code.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }
}