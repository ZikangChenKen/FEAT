package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyListObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyListObjTest {

    /**
     * A list of PyListObjs.
     */
    protected static List<PyListObj<PyBoolObj>> nestedVal;

    /**
     * An empty PyListObj designed to contain floats.
     */
    private static PyListObj<PyFloatObj> emptyFloatList;

    /**
     * An empty PyListObj designed to contain lists of ints.
     */
    private static PyListObj<PyListObj<PyIntObj>> emptyNestedList;

    /**
     * Two identical non-empty PyListObjs containing floats.
     */
    private static PyListObj<PyFloatObj> nonEmptyFloatList;
    private static PyListObj<PyFloatObj> nonEmptyFloatList2;

    /**
     * A non-empty PyListObj containing floats whose values are different from the other
     * two nonEmptyFloatLists.
     */
    private static PyListObj<PyFloatObj> nonEmptyFloatList3;

    /**
     * A non-empty PyListObj containing ints.
     */
    private static PyListObj<PyIntObj> nonEmptyIntList;

    /**
     * A PyListObj containing a subset of the elements in nonEmptyIntList.
     */
    private static PyListObj<PyIntObj> nonEmptyIntSubsetList;

    /**
     * Two identical nested PyListObjs.
     */
    private static PyListObj<PyListObj<PyBoolObj>> nestedList;
    private static PyListObj<PyListObj<PyBoolObj>> nestedList2;

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
        nonEmptyFloatVal.add(new PyFloatObj(1.5));
        nonEmptyFloatVal.add(new PyFloatObj(-17.0));
        nonEmptyFloatVal.add(new PyFloatObj(2.0));
        nonEmptyFloatVal.add(new PyFloatObj(4.888));
        List<PyFloatObj> nonEmptyFloatVal2 = List.of(new PyFloatObj(1.5), new PyFloatObj(-17.0),
                new PyFloatObj(2.0), new PyFloatObj(4.888));
        List<PyFloatObj> nonEmptyFloatVal3 = List.of(new PyFloatObj(-17.0),
                new PyFloatObj(1.5), new PyFloatObj(2.0), new PyFloatObj(4.888));

        // Create two non-empty lists of ints, one of which is a subset of the other
        List<PyIntObj> nonEmptyIntVal = List.of(new PyIntObj(1), new PyIntObj(2));
        List<PyIntObj> nonEmptyIntSubsetVal = Collections.singletonList(new PyIntObj(1));

        // Create two empty lists of different types
        emptyFloatList = new PyListObj<>(emptyFloatVal);
        emptyNestedList = new PyListObj<>(Collections.emptyList());

        // Create two identical non-empty lists of floats, and a third distinct
        // non-empty List of floats.
        nonEmptyFloatList = new PyListObj<>(nonEmptyFloatVal);
        nonEmptyFloatList2 = new PyListObj<>(nonEmptyFloatVal2);
        nonEmptyFloatList3 = new PyListObj<>(nonEmptyFloatVal3);

        // Create two non-empty lists of ints, one of which is a subset of the other
        nonEmptyIntList = new PyListObj<>(nonEmptyIntVal);
        nonEmptyIntSubsetList = new PyListObj<>(nonEmptyIntSubsetVal);

        // Create two identical nested lists
        nestedVal = getNestedVal();
        List<PyListObj<PyBoolObj>> nestedVal2 = getNestedVal();
        nestedList = new PyListObj<>(nestedVal);
        nestedList2 = new PyListObj<>(nestedVal2);
    }

    /**
     * Tests getValue() on a non-empty nested list.
     */
    @Test
    @Tag("0.3")
    @Order(1)
    void testGetValueNested() {
        assertEquals(nestedVal, nestedList2.getValue());
    }

    /**
     * Tests toString() on an empty list.
     */
    @Test
    @Tag("0.3")
    @Order(2)
    void testToStringEmpty() {
        assertEquals("[]", emptyFloatList.toString());
    }

    /**
     * Tests toString() on a list containing a single element.
     */
    @Test
    @Tag("0.3")
    @Order(3)
    void testToStringSimpleSingle() {
        assertEquals("[1]", nonEmptyIntSubsetList.toString());
    }

    /**
     * Tests toString() on a list containing multiple elements.
     */
    @Test
    @Tag("0.3")
    @Order(4)
    void testToStringSimpleMultiple() {
        assertEquals("[1.5, -17.0, 2.0, 4.888]", nonEmptyFloatList.toString());
    }

    /**
     * Tests toString() on an empty nested list.
     */
    @Test
    @Tag("0.3")
    @Order(5)
    void testToStringNestedEmpty() {
        assertEquals("[]", emptyNestedList.toString());
    }

    /**
     * Tests toString() on a non-empty nested list.
     */
    @Test
    @Tag("0.5")
    @Order(6)
    void testToStringNestedMultiple() {
        assertEquals("[[True, True], [True, False], [False, False, True, False]]",
            nestedList.toString());
    }

    /**
     * Tests equals() on two empty lists of different declared inner types (should still
     * be considered equal).
     */
    @Test
    @Tag("0.5")
    @Order(7)
    void testEqualsEmptyDifferent() {
        assertEquals(emptyFloatList, emptyNestedList);
    }

    /**
     * Tests equals() on two non-empty lists containing identical elements (but whose
     * underlying lists are unique objects).
     */
    @Test
    @Tag("0.3")
    @Order(8)
    void testEqualsSimple() {
        assertEquals(nonEmptyFloatList, nonEmptyFloatList2);
    }

    /**
     * Tests equals() on two non-empty lists containing different elements.
     */
    @Test
    @Tag("0.2")
    @Order(9)
    void testNotEqual() {
        assertNotEquals(nonEmptyFloatList3, nonEmptyFloatList);
    }

    /**
     * Tests equals() on two non-empty lists containing different elements, where one is a
     * subset of the other.
     */
    @Test
    @Tag("0.5")
    @Order(10)
    void testNotEqualSubset1() {
        assertNotEquals(nonEmptyIntSubsetList, nonEmptyIntList);
    }

    /**
     * Tests equals() on two non-empty lists containing different elements, where one is a
     * subset of the other (in the opposite direction of testNotEqualSubset1).
     */
    @Test
    @Tag("0.5")
    @Order(11)
    void testNotEqualSubset2() {
        assertNotEquals(nonEmptyIntList, nonEmptyIntSubsetList);
    }

    /**
     * Tests that a PyListObj is not considered to be equivalent to a non-APyObj.
     */
    @Test
    @Tag("0.4")
    @Order(12)
    void testNotEqualNonAPyObj() {
        assertNotEquals(nestedList, nestedVal);
    }

    /**
     * Tests equals() on two non-empty nested lists.
     */
    @Test
    @Tag("1.0")
    @Order(13)
    void testEqualsNested() {
        assertEquals(nestedList, nestedList2);
    }

    /**
     * Tests that hashCode() returns the same value for two nested lists containing
     * identical elements.
     */
    @Test
    @Tag("0.3")
    @Order(14)
    void testHashCodeEqual() {
        assertEquals(nestedList.hashCode(), nestedList2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two lists containing different elements.
     */
    @Test
    @Tag("0.3")
    @Order(13)
    void testHashCodeNotEqual() {
        assertNotEquals(nestedList.hashCode(), nonEmptyFloatList.hashCode());
    }

    /**
     * Sets up and returns value that will be used for nestedVal and nestedVal2.
     *
     * @return value for nestedVal and nestedVal2
     */
    private static List<PyListObj<PyBoolObj>> getNestedVal() {
        List<PyBoolObj> nestedSubval1 = List.of(new PyBoolObj(true),
                new PyBoolObj(true));
        List<PyBoolObj> nestedSubval2 = List.of(new PyBoolObj(true),
                new PyBoolObj(false));
        List<PyBoolObj> nestedSubval3 = List.of(new PyBoolObj(false),
                new PyBoolObj(false), new PyBoolObj(true), new PyBoolObj(false));
        return List.of(new PyListObj<>(nestedSubval1),
                new PyListObj<>(nestedSubval2), new PyListObj<>(nestedSubval3));
    }
}