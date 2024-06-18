package main.rice.obj;

import java.util.Collection;

/**
 * An abstract class representing an iterable Python object (list, tuple, set, or
 * string).
 *
 * @param <InnerType> the type of object contained within this iterable object
 */
public abstract class AIterablePyObj<InnerType extends APyObj> extends APyObj {

    /**
     * The contents of this iterable object.
     */
    protected Collection<InnerType> value;

    /**
     * Returns the Java collection representing this iterable object.
     *
     * @return the Java collection representing this iterable object
     */
    @Override
    public Collection<InnerType> getValue() {
        return this.value;
    }
}
