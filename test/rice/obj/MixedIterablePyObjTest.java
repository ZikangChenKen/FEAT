package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases that exercise a variety of different subclasses of APyObj, represent nested
 * Python objects.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MixedIterablePyObjTest {

    /**
     * A list, tuple, and set that all have the same internal value (simpleVal).
     */
    private static PyListObj<PyBoolObj> simpleList;
    private static PyTupleObj<PyBoolObj> simpleTup;
    private static PySetObj<PyBoolObj> simpleSet;

    /**
     * Two identical lists of tuples.
     */
    private static PyListObj<PyTupleObj<PyStringObj>> listOfTups1;
    private static PyListObj<PyTupleObj<PyStringObj>> listOfTups2;

    /**
     * Two identical tuples of sets.
     */
    private static PyTupleObj<PySetObj<PyIntObj>> tupOfSets1;
    private static PyTupleObj<PySetObj<PyIntObj>> tupOfSets2;

    /**
     * Two identical sets of tuples.
     */
    private static PySetObj<PyTupleObj<PyBoolObj>> setOfTups1;
    private static PySetObj<PyTupleObj<PyBoolObj>> setOfTups2;

    /**
     * Two identical dicts mapping tuples to lists.
     */
    private static PyDictObj<PyTupleObj<PyFloatObj>, PyListObj<PyIntObj>>
        dictOfTupsToLists1;
    private static PyDictObj<PyTupleObj<PyFloatObj>, PyListObj<PyIntObj>>
        dictOfTupsToLists2;

    /**
     * Two identical deeply nested objects (list(list(dict(tuple(bool):
     * tuple(list(set(tuple(float)))))))).
     */
    private static PyListObj<PyListObj<PyDictObj<PyTupleObj<PyBoolObj>,
        PyTupleObj<PyListObj<PySetObj<PyTupleObj<PyFloatObj>>>>>>>
        deeplyNestedObj1;
    private static PyListObj<PyListObj<PyDictObj<PyTupleObj<PyBoolObj>,
        PyTupleObj<PyListObj<PySetObj<PyTupleObj<PyFloatObj>>>>>>>
        deeplyNestedObj2;
    private static List<PyListObj<PyDictObj<PyTupleObj<PyBoolObj>,
        PyTupleObj<PyListObj<PySetObj<PyTupleObj<PyFloatObj>>>>>>>
        deeplyNestedVal;

    /**
     * A PySetObj containing square brackets.
     */
    private static PySetObj<PyStringObj> squareBracketsSet;

    /**
     * A PyDictObj containing equals signs.
     */
    private static PyDictObj<PyStringObj, PyIntObj> equalSignDict;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        setUpSimple();
        setUpListsOfTuples();
        setUpTupsOfSets();
        setUpSetsOfTuples();
        setUpDictsOfTuplesToLists();
        setUpDeeplyNested();
        squareBracketsSet = new PySetObj<>(Set.of(new PyStringObj("[]")));
        equalSignDict = new PyDictObj<>(Map.of(new PyStringObj("="), new PyIntObj(1)));
    }

    /**
     * Tests getValue() on a deeply nested dictionary.
     */
    @Test
    @Tag("0.2")
    @Order(1)
    void testGetValueDeeplyNested() {
        assertEquals(deeplyNestedVal, deeplyNestedObj2.getValue());
    }

    /**
     * Tests toString() on a list of tuples.
     */
    @Test
    @Tag("0.5")
    @Order(2)
    void testToStringList() {
        assertEquals("[('a', 'b'), ('c', 'd'), ('e', 'f')]", listOfTups1.toString());
    }

    /**
     * Tests toString() on a tuple of sets.
     */
    @Test
    @Tag("0.5")
    @Order(3)
    void testToStringTup() {
        // Since sets are unordered, there are multiple valid (equivalent) options for
        // toString()
        String[] tup1Options = new String[]{"({1, 2, 4}, set(), {3})",
            "({1, 4, 2}, set(), {3})", "({2, 1, 4}, set(), {3})",
            "({2, 4, 1}, set(), {3})", "({4, 1, 2}, set(), {3})",
            "({4, 2, 1}, set(), {3})"};
        assertTrue(Arrays.asList(tup1Options).contains(tupOfSets1.toString()));
    }

    /**
     * Tests toString() on a set of tuples.
     */
    @Test
    @Tag("0.5")
    @Order(4)
    void testToStringSet() {
        // Since sets are unordered, there are multiple valid (equivalent) options for
        // toString()
        String[] set1Options = new String[]{"{(True, True, False, True), (True,)}",
            "{(True,), (True, True, False, True)}"};
        assertTrue(Arrays.asList(set1Options).contains(setOfTups1.toString()));
    }

    /**
     * Tests toString() on a set containing an element containing square brackets.
     */
    @Test
    @Tag("1.0")
    @Order(5)
    void testToStringSquareBracketsSet() {
        assertEquals("{\'[]\'}", squareBracketsSet.toString());
    }
    /**
     * Tests toString() on a dict mapping tuples to lists.
     */
    @Test
    @Tag("1.0")
    @Order(6)
    void testToStringDict() {
        // Since dicts are unordered, there are multiple valid (equivalent) options for
        // toString()
        String[] dict1Options = new String[]{
            "{(4.1, 4.5, 4.8, 5.1, 5.4): [17, 20, 23, 26, 29], (1.0, 2.0, 3.0, 4.0): "
                + "[2, 3, 4], (0.0,): [0, 0]}",
            "{(4.1, 4.5, 4.8, 5.1, 5.4): [17, 20, 23, 26, 29], (0.0,): [0, 0], (1.0, 2"
                + ".0, 3.0, 4.0): [2, 3, 4]}",
            "{(1.0, 2.0, 3.0, 4.0): [2, 3, 4], (4.1, 4.5, 4.8, 5.1, 5.4): [17, 20, 23, "
                + "26, 29], (0.0,): [0, 0]}",
            "{(1.0, 2.0, 3.0, 4.0): [2, 3, 4], (0.0,): [0, 0], (4.1, 4.5, 4.8, 5.1, 5"
                + ".4): [17, 20, 23, 26, 29]}",
            "{(0.0,): [0, 0], (1.0, 2.0, 3.0, 4.0): [2, 3, 4], (4.1, 4.5, 4.8, 5.1, 5"
                + ".4): [17, 20, 23, 26, 29]}",
            "{(0.0,): [0, 0], (4.1, 4.5, 4.8, 5.1, 5.4): [17, 20, 23, 26, 29], (1.0, 2"
                + ".0, 3.0, 4.0): [2, 3, 4]}"};
        assertTrue(Arrays.asList(dict1Options).contains(dictOfTupsToLists1.toString()));
    }

    /**
     * Tests toString() on a dict containing an element containing an equals sign.
     */
    @Test
    @Tag("1.0")
    @Order(7)
    void testToStringSquareBrackets() {
        assertEquals("{\'=': 1}", equalSignDict.toString());
    }

    /**
     * Tests toString() on a deeply nested object.
     */
    @Test
    @Tag("1.0")
    @Order(8)
    void testToStringDeeplyNested() {
        assertEquals("[[{(True, False): ([{(17.65, -9.1)}],)}]]",
            deeplyNestedObj1.toString());
    }

    /**
     * Tests equals() on two identical lists of tuples.
     */
    @Test
    @Tag("0.2")
    @Order(9)
    void testEqualsList() {
        assertEquals(listOfTups1, listOfTups2);
    }

    /**
     * Tests equals() on two identical tuples of sets.
     */
    @Test
    @Tag("0.2")
    @Order(10)
    void testEqualsTup() {
        assertEquals(tupOfSets1, tupOfSets2);
    }

    /**
     * Tests equals() on two identical sets of tuples.
     */
    @Test
    @Tag("0.2")
    @Order(11)
    void testEqualsSet() {
        assertEquals(setOfTups1, setOfTups2);
    }

    /**
     * Tests equals() on two identical dicts mapping tuples to lists.
     */
    @Test
    @Tag("0.2")
    @Order(12)
    void testEqualsDict() {
        assertEquals(dictOfTupsToLists1, dictOfTupsToLists2);
    }

    /**
     * Tests equals() on two identical deeply nested objects.
     */
    @Test
    @Tag("0.5")
    @Order(13)
    void testEqualsDeeplyNested() {
        assertEquals(deeplyNestedObj1, deeplyNestedObj2);
    }

    /**
     * Tests that equals() returns false for a list and tuple with the same underlying
     * representation.
     */
    @Test
    @Tag("0.6")
    @Order(14)
    void testNotEqualListTup() {
        assertNotEquals(simpleList, simpleTup);
    }

    /**
     * Tests that equals() returns false for a tuple and list with the same underlying
     * representation.
     */
    @Test
    @Tag("0.6")
    @Order(15)
    void testNotEqualTupList() {
        assertNotEquals(simpleTup, simpleList);
    }

    /**
     * Tests that equals() returns false for a list and set with the same elements.
     */
    @Test
    @Tag("0.2")
    @Order(16)
    void testNotEqualListSet() {
        assertNotEquals(simpleList, simpleSet);
    }

    /**
     * Tests that equals() returns false for a set and list with the same elements.
     */
    @Test
    @Tag("0.2")
    @Order(17)
    void testNotEqualSetList() {
        assertNotEquals(simpleSet, simpleList);
    }

    /**
     * Tests that equals() returns false for a tuple and set with the same elements.
     */
    @Test
    @Tag("0.2")
    @Order(18)
    void testNotEqualTupSet() {
        assertNotEquals(simpleTup, simpleSet);
    }

    /**
     * Tests that equals() returns false for a set and tuple with the same elements.
     */
    @Test
    @Tag("0.2")
    @Order(19)
    void testNotEqualSetTup() {
        assertNotEquals(simpleSet, simpleTup);
    }

    /**
     * Tests that hashCode() returns the same value for two identical deeply nested
     * objects.
     */
    @Test
    @Tag("0.5")
    @Order(20)
    void testHashCodeDeeplyNested() {
        assertEquals(deeplyNestedObj1.hashCode(), deeplyNestedObj2.hashCode());
    }

    /**
     * Sets up simpleList, simpleSet, and simpleTup.
     */
    private static void setUpSimple() {
        List<PyBoolObj> simpleVal = new ArrayList<>();
        simpleVal.add(new PyBoolObj(true));
        simpleVal.add(new PyBoolObj(false));
        simpleList = new PyListObj<>(simpleVal);
        simpleTup = new PyTupleObj<>(simpleVal);
        simpleSet = new PySetObj<>(new HashSet<>(simpleVal));
    }

    /**
     * Sets up listOfTups1 and listOfTups2.
     */
    private static void setUpListsOfTuples() {
        // Set up the inner tuples
        List<PyStringObj> listSubval1 =
            Arrays.asList(new PyStringObj("a"), new PyStringObj("b"));
        PyTupleObj<PyStringObj> listSubtup1 = new PyTupleObj<>(listSubval1);
        List<PyStringObj> listSubval2 =
            Arrays.asList(new PyStringObj("c"), new PyStringObj("d"));
        PyTupleObj<PyStringObj> listSubtup2 = new PyTupleObj<>(listSubval2);
        List<PyStringObj> listSubval3 =
            Arrays.asList(new PyStringObj("e"), new PyStringObj("f"));
        PyTupleObj<PyStringObj> listSubtup3 = new PyTupleObj<>(listSubval3);

        // Store them all within a list
        List<PyTupleObj<PyStringObj>> listOfTupsVal =
            Arrays.asList(listSubtup1, listSubtup2, listSubtup3);
        listOfTups1 = new PyListObj<>(listOfTupsVal);

        // Do the exact same thing again to get two deeply unique objects
        listSubval1 = Arrays.asList(new PyStringObj("a"), new PyStringObj("b"));
        listSubtup1 = new PyTupleObj<>(listSubval1);
        listSubval2 = Arrays.asList(new PyStringObj("c"), new PyStringObj("d"));
        listSubtup2 = new PyTupleObj<>(listSubval2);
        listSubval3 = Arrays.asList(new PyStringObj("e"), new PyStringObj("f"));
        listSubtup3 = new PyTupleObj<>(listSubval3);

        listOfTupsVal = Arrays.asList(listSubtup1, listSubtup2, listSubtup3);
        listOfTups2 = new PyListObj<>(listOfTupsVal);
    }

    /**
     * Sets up tupOfSets1 and tupOfSets2.
     */
    private static void setUpTupsOfSets() {
        // Make some tuples of sets of ints...
        Set<PyIntObj> tupSubval1 =
            Set.of(new PyIntObj(1), new PyIntObj(2), new PyIntObj(4));
        PySetObj<PyIntObj> tupSubset1 = new PySetObj<>(tupSubval1);
        Set<PyIntObj> tupSubval2 = new HashSet<>();
        PySetObj<PyIntObj> tupSubset2 = new PySetObj<>(tupSubval2);
        Set<PyIntObj> tupSubval3 = Set.of(new PyIntObj(3));
        PySetObj<PyIntObj> tupSubset3 = new PySetObj<>(tupSubval3);

        List<PySetObj<PyIntObj>> tupOfSetsVal =
            Arrays.asList(tupSubset1, tupSubset2, tupSubset3);
        tupOfSets1 = new PyTupleObj<>(tupOfSetsVal);

        // Do the exact same thing again to get two deeply unique objects
        tupSubval1 = Set.of(new PyIntObj(1), new PyIntObj(2), new PyIntObj(4));
        tupSubset1 = new PySetObj<>(tupSubval1);
        tupSubval2 = new HashSet<>();
        tupSubset2 = new PySetObj<>(tupSubval2);
        tupSubval3 = Set.of(new PyIntObj(3));
        tupSubset3 = new PySetObj<>(tupSubval3);

        tupOfSets2 = new PyTupleObj<>(Arrays.asList(tupSubset1, tupSubset2, tupSubset3));
    }

    /**
     * Sets of setOfTups1 and setOfTups2.
     */
    private static void setUpSetsOfTuples() {
        // Make some sets of tuples of bools...
        List<PyBoolObj> setSubval1 = Arrays
            .asList(new PyBoolObj(true), new PyBoolObj(true), new PyBoolObj(false),
                new PyBoolObj(true));
        List<PyBoolObj> setSubval2 = Collections.singletonList(new PyBoolObj(true));

        List<PyTupleObj<PyBoolObj>> setOfTupsVal =
            Arrays.asList(new PyTupleObj<>(setSubval1), new PyTupleObj<>(setSubval2));
        setOfTups1 = new PySetObj<>(new HashSet<>(setOfTupsVal));

        // Do the exact same thing again to get two deeply unique objects
        setSubval1 = Arrays
            .asList(new PyBoolObj(true), new PyBoolObj(true), new PyBoolObj(false),
                new PyBoolObj(true));
        setSubval2 = Collections.singletonList(new PyBoolObj(true));

        setOfTups2 = new PySetObj<>(
            Set.of(new PyTupleObj<>(setSubval1), new PyTupleObj<>(setSubval2)));
    }

    /**
     * Sets up dictOfTupsToLists1 and dictOfTupsToLists2.
     */
    private static void setUpDictsOfTuplesToLists() {
        // Create a dict mapping tuples to lists
        // Create the first (key, val) pair
        List<PyFloatObj> dictKey1 = new ArrayList<>();
        List<PyIntObj> dictVal1 = new ArrayList<>();
        for (int i = 17; i < 30; i += 3) {
            dictKey1.add(new PyFloatObj(Math.round(Math.sqrt(i) * 10) / 10.0));
            dictVal1.add(new PyIntObj(i));
        }

        // Create the second (key, val) pair
        List<PyFloatObj> dictKey2 = new ArrayList<>();
        List<PyIntObj> dictVal2 = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            dictKey2.add(new PyFloatObj((double) i));
            dictVal2.add(new PyIntObj(i + 1));
        }
        dictKey2.add(new PyFloatObj(4.0));

        // Create the third (key, val) pair
        List<PyFloatObj> dictKey3 = Collections.singletonList(new PyFloatObj(0.0));
        List<PyIntObj> dictVal3 = Arrays.asList(new PyIntObj(0), new PyIntObj(0));

        // Place all (key, val) pairs in a dict object
        Map<PyTupleObj<PyFloatObj>, PyListObj<PyIntObj>> dictOfTupsToListsVal =
            Map.of(new PyTupleObj<>(dictKey1), new PyListObj<>(dictVal1),
                new PyTupleObj<>(dictKey2), new PyListObj<>(dictVal2),
                new PyTupleObj<>(dictKey3), new PyListObj<>(dictVal3));
        dictOfTupsToLists1 = new PyDictObj<>(dictOfTupsToListsVal);

        // Do the exact same thing again to get two deeply unique objects
        dictKey1 = new ArrayList<>();
        dictVal1 = new ArrayList<>();
        for (int i = 17; i < 30; i += 3) {
            dictKey1.add(new PyFloatObj(Math.round(Math.sqrt(i) * 10) / 10.0));
            dictVal1.add(new PyIntObj(i));
        }

        dictKey2 = new ArrayList<>();
        dictVal2 = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            dictKey2.add(new PyFloatObj((double) i));
            dictVal2.add(new PyIntObj(i + 1));
        }
        dictKey2.add(new PyFloatObj(4.0));

        dictKey3 = Collections.singletonList(new PyFloatObj(0.0));
        dictVal3 = Arrays.asList(new PyIntObj(0), new PyIntObj(0));

        dictOfTupsToListsVal =
            Map.of(new PyTupleObj<>(dictKey1), new PyListObj<>(dictVal1),
                new PyTupleObj<>(dictKey2), new PyListObj<>(dictVal2),
                new PyTupleObj<>(dictKey3), new PyListObj<>(dictVal3));
        dictOfTupsToLists2 = new PyDictObj<>(dictOfTupsToListsVal);
    }

    /**
     * Sets up deeplyNestedObj1 and deeplyNestedObj2.
     */
    private static void setUpDeeplyNested() {
        // Create a deeply-nested structure
        // Make the key for the inner dictionary
        List<PyBoolObj> key1 = Arrays.asList(new PyBoolObj(true), new PyBoolObj(false));

        // Make the val for the inner dictionary
        List<PyFloatObj> val1thirdLayer =
            Arrays.asList(new PyFloatObj(17.65), new PyFloatObj(-9.1));
        List<PyTupleObj<PyFloatObj>> val1secondLayer =
            Collections.singletonList(new PyTupleObj<>(val1thirdLayer));
        List<PySetObj<PyTupleObj<PyFloatObj>>> val1firstLayer =
            Collections.singletonList(new PySetObj<>(new HashSet<>(val1secondLayer)));
        List<PyListObj<PySetObj<PyTupleObj<PyFloatObj>>>> val1 =
            Collections.singletonList(new PyListObj<>(val1firstLayer));

        // Nest the dictionary in a layer of lists
        Map<PyTupleObj<PyBoolObj>,
            PyTupleObj<PyListObj<PySetObj<PyTupleObj<PyFloatObj>>>>> secondLayer =
            Map.of(new PyTupleObj<>(key1), new PyTupleObj<>(val1));
        List<PyDictObj<PyTupleObj<PyBoolObj>,
            PyTupleObj<PyListObj<PySetObj<PyTupleObj<PyFloatObj>>>>>> firstLayer =
            Collections.singletonList(new PyDictObj<>(secondLayer));
        deeplyNestedVal = Collections.singletonList(new PyListObj<>(firstLayer));
        deeplyNestedObj1 = new PyListObj<>(deeplyNestedVal);

        // Do the exact same thing again to get two deeply unique objects
        key1 = Arrays.asList(new PyBoolObj(true), new PyBoolObj(false));
        val1thirdLayer = Arrays.asList(new PyFloatObj(17.65), new PyFloatObj(-9.1));
        val1secondLayer = Collections.singletonList(new PyTupleObj<>(val1thirdLayer));
        val1firstLayer =
            Collections.singletonList(new PySetObj<>(new HashSet<>(val1secondLayer)));
        val1 = Collections.singletonList(new PyListObj<>(val1firstLayer));

        secondLayer = Map.of(new PyTupleObj<>(key1), new PyTupleObj<>(val1));
        firstLayer = Collections.singletonList(new PyDictObj<>(secondLayer));
        deeplyNestedVal = Collections.singletonList(new PyListObj<>(firstLayer));
        deeplyNestedObj2 = new PyListObj<>(deeplyNestedVal);
    }
}