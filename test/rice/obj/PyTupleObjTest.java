package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyTupleObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyTupleObjTest {

    /**
     * A list of PyTupleObjs.
     */
    protected static List<PyTupleObj<PyBoolObj>> nestedVal;

    /**
     * An empty PyTupleObj designed to contain floats.
     */
    private static PyTupleObj<PyFloatObj> emptyFloatTup;

    /**
     * An empty PyTupleObj designed to contain ints.
     */
    private static PyTupleObj<PyIntObj> emptyIntTup;

    /**
     * Two identical non-empty PyTupleObjs containing floats.
     */
    private static PyTupleObj<PyFloatObj> nonEmptyFloatTup;
    private static PyTupleObj<PyFloatObj> nonEmptyFloatTup2;

    /**
     * A non-empty PyTupleObj containing floats whose values are different from the other
     * two nonEmptyFloatTups.
     */
    private static PyTupleObj<PyFloatObj> nonEmptyFloatTup3;

    /**
     * A non-empty PyTupleObj containing ints.
     */
    private static PyTupleObj<PyIntObj> nonEmptyIntTup;

    /**
     * A PyTupleObj containing a subset of the elements in nonEmptyIntTup.
     */
    private static PyTupleObj<PyIntObj> nonEmptyIntSubsetTup;

    /**
     * Two identical nested PyTupleObjs.
     */
    private static PyTupleObj<PyTupleObj<PyBoolObj>> nestedTup;
    private static PyTupleObj<PyTupleObj<PyBoolObj>> nestedTup2;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Create two empty lists of different sizes
        List<PyFloatObj> emptyFloatVal = new ArrayList<>();
        List<PyIntObj> emptyIntVal = Collections.emptyList();

        // Create two identical non-empty lists of floats, and a third distinct
        // non-empty tuple of floats.
        List<PyFloatObj> nonEmptyFloatVal = new ArrayList<>();
        nonEmptyFloatVal.add(new PyFloatObj(7.7));
        nonEmptyFloatVal.add(new PyFloatObj(8.8));
        nonEmptyFloatVal.add(new PyFloatObj(9.9));
        List<PyFloatObj> nonEmptyFloatVal2 = List.of(new PyFloatObj(7.7),
                new PyFloatObj(8.8), new PyFloatObj(9.9));
        List<PyFloatObj> nonEmptyFloatVal3 = List.of(new PyFloatObj(8.8),
                new PyFloatObj(7.7), new PyFloatObj(9.9));

        // Create two non-empty lists of ints, one of which is a subset of the other
        List<PyIntObj> nonEmptyIntVal = List.of(new PyIntObj(1), new PyIntObj(2));
        List<PyIntObj> nonEmptyIntSubsetVal = Collections.singletonList(new PyIntObj(1));

        // Create two empty lists of different types
        emptyFloatTup = new PyTupleObj<>(emptyFloatVal);
        emptyIntTup = new PyTupleObj<>(Collections.emptyList());

        // Create two identical non-empty lists of floats, and a third distinct
        // non-empty List of floats.
        nonEmptyFloatTup = new PyTupleObj<>(nonEmptyFloatVal);
        nonEmptyFloatTup2 = new PyTupleObj<>(nonEmptyFloatVal2);
        nonEmptyFloatTup3 = new PyTupleObj<>(nonEmptyFloatVal3);

        // Create two non-empty lists of ints, one of which is a subset of the other
        nonEmptyIntTup = new PyTupleObj<>(nonEmptyIntVal);
        nonEmptyIntSubsetTup = new PyTupleObj<>(nonEmptyIntSubsetVal);

        // Create two identical nested lists
        nestedVal = getNestedVal();
        List<PyTupleObj<PyBoolObj>> nestedVal2 = getNestedVal();
        nestedTup = new PyTupleObj<>(nestedVal);
        nestedTup2 = new PyTupleObj<>(nestedVal2);
    }

    /**
     * Tests getValue() on a nested tuple.
     */
    @Test
    @Tag("0.3")
    @Order(1)
    void testGetValueNested() {
        assertEquals(nestedVal, nestedTup.getValue());
    }

    /**
     * Tests toString() on an empty tuple.
     */
    @Test
    @Tag("0.2")
    @Order(2)
    void testToStringEmpty() {
        assertEquals("()", emptyFloatTup.toString());
    }

    /**
     * Tests toString() on a tuple containing a single element; must have a trailing
     * comma.
     */
    @Test
    @Tag("1.0")
    @Order(3)
    void testToStringSingle() {
        assertEquals("(1,)", nonEmptyIntSubsetTup.toString());
    }

    /**
     * Tests toString() on a tuple containing multiple elements.
     */
    @Test
    @Tag("0.2")
    @Order(4)
    void testToStringMultiple() {
        assertEquals("(7.7, 8.8, 9.9)", nonEmptyFloatTup.toString());
    }

    /**
     * Tests toString() on a nested tuple.
     */
    @Test
    @Tag("0.3")
    @Order(5)
    void testToStringNested() {
        assertEquals("((True, True), (True, False), (False, False, True, False))",
            nestedTup.toString());
    }

    /**
     * Tests equals() on two empty tuples of different declared inner types (should still
     * be considered equal).
     */
    @Test
    @Tag("0.3")
    @Order(6)
    void testEqualsEmpty() {
        assertEquals(emptyFloatTup, emptyIntTup);
    }

    /**
     * Tests equals() on two non-empty tuples containing identical elements (but whose
     * underlying lists are unique objects).
     */
    @Test
    @Tag("0.3")
    @Order(7)
    void testEqualsMultiple() {
        assertEquals(nonEmptyFloatTup, nonEmptyFloatTup2);
    }

    /**
     * Tests equals() on two tuples with different elements.
     */
    @Test
    @Tag("0.3")
    @Order(8)
    void testNotEqual() {
        assertNotEquals(nonEmptyFloatTup2, nonEmptyFloatTup3);
    }

    /**
     * Tests equals() on two tuples with different elements, where one is a subset of the
     * other.
     */
    @Test
    @Tag("0.5")
    @Order(9)
    void testNotEqualSubset1() {
        assertNotEquals(nonEmptyIntTup, nonEmptyIntSubsetTup);
    }

    /**
     * Tests equals() on two tuples with different elements, where one is a subset of the
     * other (in the opposite direction of testNotEqualSubset1).
     */
    @Test
    @Tag("0.5")
    @Order(10)
    void testNotEqualSubset2() {
        assertNotEquals(nonEmptyIntSubsetTup, nonEmptyIntTup);
    }

    /**
     * Tests that a PyCharObj is not considered to be equivalent to a non-APyObj.
     */
    @Test
    @Tag("0.5")
    @Order(11)
    void testNotEqualNonAPyObj() {
        assertNotEquals(nestedTup, nestedVal);
    }

    /**
     * Tests equals() on two identical nested tuples.
     */
    @Test
    @Tag("1.0")
    @Order(12)
    void testEqualsNested() {
        assertEquals(nestedTup, nestedTup2);
    }

    /**
     * Tests that hashCode() returns the same value for two nested tuples containing
     * identical elements.
     */
    @Test
    @Tag("0.3")
    @Order(13)
    void testHashCodeEqual() {
        assertEquals(nestedTup.hashCode(), nestedTup2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two tuples containing different elements.
     */
    @Test
    @Tag("0.3")
    @Order(13)
    void testHashCodeNotEqual() {
        assertNotEquals(nestedTup.hashCode(), nonEmptyFloatTup.hashCode());
    }

    /**
     * Sets up and returns value that will be used for nestedVal and nestedVal2.
     *
     * @return value for nestedVal and nestedVal2
     */
    private static List<PyTupleObj<PyBoolObj>> getNestedVal() {
        List<PyBoolObj> nestedSubval1 = List.of(new PyBoolObj(true),
                new PyBoolObj(true));
        List<PyBoolObj> nestedSubval2 = List.of(new PyBoolObj(true),
                new PyBoolObj(false));
        List<PyBoolObj> nestedSubval3 = List.of(new PyBoolObj(false),
                new PyBoolObj(false), new PyBoolObj(true), new PyBoolObj(false));
        return List.of(new PyTupleObj<>(nestedSubval1),
                new PyTupleObj<>(nestedSubval2), new PyTupleObj<>(nestedSubval3));
    }
}