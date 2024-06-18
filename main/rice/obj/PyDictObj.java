package main.rice.obj;

import java.util.Map;

/**
 * A representation of Python objects of type dict.
 *
 * @param <KeyType> the type of each key in the dictionary
 * @param <ValType> the type of each value in the dictionary
 */
public class PyDictObj<KeyType extends APyObj, ValType extends APyObj> extends APyObj {

    /**
     * The contents of this PyDictObj.
     */
    private final Map<KeyType, ValType> value;

    /**
     * Constructor for a PyDictObj; initializes its value to the input.
     *
     * @param value the value of this PyDictObj
     */
    public PyDictObj(Map<KeyType, ValType> value) {
        this.value = value;
    }

    /**
     * Returns the underlying (Java Map) representation of this PyDictObj.
     *
     * @return the underlying (Java Map) representation of this PyDictObj
     */
    @Override
    public Map<KeyType, ValType> getValue() {
        return this.value;
    }

    /**
     * Builds and returns a string representation of this object that mirrors the Python
     * string representation (i.e., {key1: val1, key2: val2, ...}).
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        StringBuilder repr = new StringBuilder("{");

        // Add all of the (key, value) pairs
        for (Map.Entry<KeyType, ValType> entry : this.value.entrySet()) {
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            repr.append(key).append(": ").append(val).append(", ");
        }

        // Remove last ", "
        if (this.value.size() > 0) {
            repr = new StringBuilder(repr.substring(0, repr.length() - 2));
        }

        repr.append("}");
        return repr.toString();
    }
}