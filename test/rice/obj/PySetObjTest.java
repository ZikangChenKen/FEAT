package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PySetObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PySetObjTest {

    /**
     * A non-empty set containing floats.
     */
    protected static Set<PyFloatObj> nonEmptyFloatVal;

    /**
     * An empty PySetObj designed to contain floats.
     */
    private static PySetObj<PyFloatObj> emptyFloatSet;

    /**
     * An empty PySetObj designed to contain ints.
     */
    private static PySetObj<PyIntObj> emptyIntSet;

    /**
     * Two identical non-empty PySetObjs containing floats.
     */
    private static PySetObj<PyFloatObj> nonEmptyFloatSet;
    private static PySetObj<PyFloatObj> nonEmptyFloatSet2;

    /**
     * A non-empty PySetObj containing floats whose values are different from the other
     * three nonEmptyFloatSets.
     */
    private static PySetObj<PyFloatObj> nonEmptyFloatSet3;

    /**
     * A non-empty PySetObj containing ints.
     */
    private static PySetObj<PyIntObj> nonEmptyIntSet;

    /**
     * A PySetObj containing a subset of the elements in nonEmptyIntList.
     */
    private static PySetObj<PyIntObj> nonEmptyIntSubset;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Create two distinct non-empty sets of floats.
        nonEmptyFloatVal = Set.of(new PyFloatObj(2.3), new PyFloatObj(3.1),
                new PyFloatObj(6.0));
        Set<PyFloatObj> nonEmptyFloatVal2 = Set.of(new PyFloatObj(-11.11),
                new PyFloatObj(11.11), new PyFloatObj(-1.111));

        // Create two non-empty sets of ints, one of which is a subset of the other
        Set<PyIntObj> nonEmptyIntVal = Set.of(new PyIntObj(1), new PyIntObj(2));
        Set<PyIntObj> nonEmptyIntSubsetVal = Set.of(new PyIntObj(1));

        // Create two empty sets of different types
        emptyFloatSet = new PySetObj<>(new HashSet<>());
        emptyIntSet = new PySetObj<>(Collections.emptySet());

        // Create two identical non-empty sets of floats, and a third distinct non-empty
        // set of floats
        nonEmptyFloatSet = new PySetObj<>(nonEmptyFloatVal);
        nonEmptyFloatSet2 = new PySetObj<>(new HashSet<>(nonEmptyFloatVal));
        nonEmptyFloatSet3 = new PySetObj<>(nonEmptyFloatVal2);

        // Create two non-empty sets of ints, one of which is a subset of the other
        nonEmptyIntSet = new PySetObj<>(nonEmptyIntVal);
        nonEmptyIntSubset = new PySetObj<>(nonEmptyIntSubsetVal);
    }

    /**
     * Tests getValue() on a set containing multiple elements.
     */
    @Test
    @Tag("0.3")
    @Order(0)
    void testGetValue() {
        assertEquals(nonEmptyFloatVal, nonEmptyFloatSet.getValue());
    }

    /**
     * Tests toString() on an empty set.
     */
    @Test
    @Tag("1.0")
    @Order(1)
    void testToStringEmpty() {
        assertEquals("set()", emptyFloatSet.toString());
    }

    /**
     * Tests toString() on a set containing a single element.
     */
    @Test
    @Tag("0.5")
    @Order(2)
    void testToStringSingle() {
        assertEquals("{1}", nonEmptyIntSubset.toString());
    }

    /**
     * Tests toString() on a set containing multiple elements.
     */
    @Test
    @Tag("0.5")
    @Order(3)
    void testToStringMultiple() {
        // Since sets are unordered, there are multiple valid (equivalent) options for
        // toString()
        String[] options = new String[]{"{2.3, 3.1, 6.0}", "{2.3, 6.0, 3.1}",
            "{3.1, 2.3, 6.0}", "{3.1, 6.0, 2.3}", "{6.0, 2.3, 3.1}",
            "{6.0, 3.1, 2.3}"};
        assertTrue(Arrays.asList(options).contains(nonEmptyFloatSet.toString()));
    }

    /**
     * Tests equals() on two empty sets of different declared types.
     */
    @Test
    @Tag("0.3")
    @Order(4)
    void testEqualsEmpty() {
        assertEquals(emptyFloatSet, emptyIntSet);
    }

    /**
     * Tests equals() on two non-empty sets containing the same elements.
     */
    @Test
    @Tag("0.5")
    @Order(5)
    void testEqualsMultiple() {
        assertEquals(nonEmptyFloatSet, nonEmptyFloatSet2);
    }

    /**
     * Tests equals() on two sets with different values.
     */
    @Test
    @Tag("0.3")
    @Order(6)
    void testNotEqual() {
        assertNotEquals(nonEmptyFloatSet, nonEmptyFloatSet3);
    }

    /**
     * Tests equals() on two sets with different values, where one set is a subset of the
     * other.
     */
    @Test
    @Tag("0.5")
    @Order(7)
    void testNotEqualSubset1() {
        assertNotEquals(nonEmptyIntSubset, nonEmptyIntSet);
    }

    /**
     * Tests equals() on two sets with different values, where one set is a subset of the
     * other (in the opposite direction of testNotEqualSubset1).
     */
    @Test
    @Tag("0.5")
    @Order(8)
    void testNotEqualSubset2() {
        assertNotEquals(nonEmptyIntSet, nonEmptyIntSubset);
    }

    /**
     * Tests that a PySetObj is not considered to be equivalent to a non-APyObj.
     */
    @Test
    @Tag("0.5")
    @Order(9)
    void testNotEqualNonAPyObj() {
        assertNotEquals(nonEmptyFloatSet, nonEmptyFloatVal);
    }

    /**
     * Tests that hashCode() returns the same value for two non-empty sets containing the
     * same elements.
     */
    @Test
    @Tag("0.3")
    @Order(10)
    void testHashCodeEqual() {
        assertEquals(nonEmptyFloatSet.hashCode(), nonEmptyFloatSet2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two sets containing different elements.
     */
    @Test
    @Tag("0.3")
    @Order(11)
    void testHashCodeNotEqual() {
        assertNotEquals(nonEmptyFloatSet.hashCode(), nonEmptyIntSet.hashCode());
    }
}
