package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyDictObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyDictObjTest {

    /**
     * Two empty dicts of different declared types.
     */
    private static PyDictObj<PyBoolObj, PyIntObj> emptyDict1;
    private static PyDictObj<PyIntObj, PyBoolObj> emptyDict2;

    /**
     * Two identical non-empty dicts.
     */
    private static PyDictObj<PyIntObj, PyIntObj> nonEmptyDict1;
    private static PyDictObj<PyIntObj, PyIntObj> nonEmptyDict2;

    /**
     * Two dicts with one (key, val) pair each, where the key in one is the value in the
     * other and vice versa.
     */
    private static PyDictObj<PyIntObj, PyBoolObj> singlePairDict1;
    private static PyDictObj<PyBoolObj, PyIntObj> singlePairDict2;

    /**
     * Two identical nested dicts.
     */
    private static PyDictObj<PyBoolObj, PyDictObj<PyFloatObj, PyFloatObj>> nestedDict1;
    private static PyDictObj<PyBoolObj, PyDictObj<PyFloatObj, PyFloatObj>> nestedDict2;
    private static Map<PyBoolObj, PyDictObj<PyFloatObj, PyFloatObj>> nestedVal;

    /**
     * Sets up all static fields for use in test cases.
     */
    @BeforeAll
    static void setUp() {
        // Create two empty dicts
        emptyDict1 = new PyDictObj<>(Collections.emptyMap());
        emptyDict2 = new PyDictObj<>(Collections.emptyMap());

        // Create two dicts with identical contents
        Map<PyIntObj, PyIntObj> nonEmptyVal =
            Map.of(new PyIntObj(1), new PyIntObj(1), new PyIntObj(2),
                new PyIntObj(4), new PyIntObj(3), new PyIntObj(9));
        nonEmptyDict1 = new PyDictObj<>(nonEmptyVal);

        nonEmptyVal = Map.of(new PyIntObj(1), new PyIntObj(1), new PyIntObj(2),
            new PyIntObj(4), new PyIntObj(3), new PyIntObj(9));
        nonEmptyDict2 = new PyDictObj<>(nonEmptyVal);

        // Create two dicts with one (key, val) pair each, where the key in one is the
        // value in the other and vice versa
        Map<PyIntObj, PyBoolObj> singlePairVal1 =
            Map.of(new PyIntObj(1), new PyBoolObj(true));
        singlePairDict1 = new PyDictObj<>(singlePairVal1);
        singlePairDict2 = new PyDictObj<>(Map.of(new PyBoolObj(true), new PyIntObj(1)));

        // Create two identical nested dicts
        nestedVal = getNestedVal();
        nestedDict1 = new PyDictObj<>(nestedVal);
        nestedVal = getNestedVal();
        nestedDict2 = new PyDictObj<>(nestedVal);
    }

    /**
     * Tests getValue() on a nested dict.
     */
    @Test
    @Tag("0.5")
    @Order(1)
    void testGetValueNested() {
        assertEquals(nestedVal, nestedDict2.getValue());
    }

    /**
     * Tests toString() on an empty dict.
     */
    @Test
    @Tag("0.5")
    @Order(2)
    void testToStringEmpty() {
        assertEquals(emptyDict1.toString(), "{}");
    }

    /**
     * Tests toString() on a dict containing a single (key, val) pair.
     */
    @Test
    @Tag("0.5")
    @Order(3)
    void testToStringSingle() {
        assertEquals("{1: True}", singlePairDict1.toString());
    }

    /**
     * Tests toString() on a dict containing multiple (key, val) pairs.
     */
    @Test
    @Tag("0.5")
    @Order(4)
    void testToStringMultiple() {
        // Since dicts are unordered, there are multiple valid (equivalent) options for
        // toString()
        String[] options =
            new String[]{"{1: 1, 2: 4, 3: 9}", "{1: 1, 3: 9, 2: 4}",
                "{2: 4, 1: 1, 3: 9}", "{2: 4, 3: 9, 1: 1}",
                "{3: 9, 1: 1, 2: 4}", "{3: 9, 2: 4, 1: 1}"};
        assertTrue(Arrays.asList(options).contains(nonEmptyDict1.toString()));
    }

    /**
     * Tests toString() on a nested dict.
     */
    @Test
    @Tag("0.5")
    @Order(5)
    void testToStringNested() {
        // Since dicts are unordered, there are multiple valid (equivalent) options for
        // toString()
        String[] options =
            new String[]{"{True: {1.0: 1.0, 2.0: 4.0}, False: {1.0: -1.0}}",
                "{False: {1.0: -1.0}, True: {1.0: 1.0, 2.0: 4.0}}",
                "{True: {2.0: 4.0, 1.0: 1.0}, False: {1.0: -1.0}}",
                "{False: {1.0: -1.0}, True: {2.0: 4.0, 1.0: 1.0}}"};
        assertTrue(Arrays.asList(options).contains(nestedDict2.toString()));
    }

    /**
     * Tests equals() on two dicts that are both empty, but have different declared
     * types.
     */
    @Test
    @Tag("0.5")
    @Order(6)
    void testEqualsEmpty() {
        assertEquals(emptyDict1, emptyDict2);
    }

    /**
     * Tests equals() on two identical non-empty dicts.
     */
    @Test
    @Tag("0.5")
    @Order(7)
    void testEqualsNonEmpty() {
        assertEquals(nonEmptyDict1, nonEmptyDict2);
    }

    /**
     * Tests equals() on two non-empty dicts that have reversed (key, val) pairs.
     */
    @Test
    @Tag("0.5")
    @Order(8)
    void testNotEqual() {
        assertNotEquals(singlePairDict2, singlePairDict1);
    }

    /**
     * Tests equals() on two different dicts, where one's set of (key, val) pairs is a
     * subset of the other's.
     */
    @Test
    @Tag("0.5")
    @Order(9)
    void testNotEqualSubset1() {
        assertNotEquals(singlePairDict2, nonEmptyDict2);
    }

    /**
     * Tests equals() on two different dicts, where one's set of (key, val) pairs is a
     * subset of the other's (in the opposite direction of testNotEqualsSubset1).
     */
    @Test
    @Tag("0.5")
    @Order(10)
    void testNotEqualSubset2() {
        assertNotEquals(nonEmptyDict2, singlePairDict2);
    }

    /**
     * Tests that a PyDictObj is not considered to be equivalent to a non-APyObj.
     */
    @Test
    @Tag("0.5")
    @Order(11)
    void testNotEqualNonAPyObj() {
        assertNotEquals(nestedDict1, nestedVal);
    }

    /**
     * Tests equals() on two identical nested dicts.
     */
    @Test
    @Tag("0.5")
    @Order(12)
    void testEqualsNested() {
        assertEquals(nestedDict1, nestedDict2);
    }

    /**
     * Tests that hashCode() returns the same value for two identical nested dicts.
     */
    @Test
    @Tag("0.5")
    @Order(13)
    void testHashCodeEqual() {
        assertEquals(nestedDict1.hashCode(), nestedDict2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two dicts containing different elements.
     */
    @Test
    @Tag("0.5")
    @Order(13)
    void testHashCodeNotEqual() {
        assertNotEquals(nestedDict1.hashCode(), nonEmptyDict1.hashCode());
    }

    /**
     * Helper function for setting up nestedVal2.
     *
     * @return the value for nestedVal
     */
    private static Map<PyBoolObj, PyDictObj<PyFloatObj, PyFloatObj>> getNestedVal() {
        Map<PyFloatObj, PyFloatObj> nestedVal1 =
            Map.of(new PyFloatObj(1.0), new PyFloatObj(1.0),
                new PyFloatObj(2.0), new PyFloatObj(4.0));
        Map<PyFloatObj, PyFloatObj> nestedVal2 =
            Map.of(new PyFloatObj(1.0), new PyFloatObj(-1.0));
        return Map.of(new PyBoolObj(true), new PyDictObj<>(nestedVal1),
            new PyBoolObj(false), new PyDictObj<>(nestedVal2));
    }
}