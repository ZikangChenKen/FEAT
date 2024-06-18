package test.rice.test;

import main.rice.obj.*;
import main.rice.test.TestCase;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the TestCase class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestCaseTest {

    /**
     * Two identical test cases with no arguments.
     */
    private static TestCase noArgs;
    private static TestCase noArgs2;

    /**
     * Two identical test cases with one simple (non-compound) argument, as well as their
     * internal representation.
     */
    private static TestCase oneArgSimple;
    private static TestCase oneArgSimple2;
    private static List<APyObj> oneArgSimpleVal;

    /**
     * Two identical test cases with one nested (compound) argument, as well as their
     * internal representation.
     */
    private static TestCase oneArgNested;
    private static TestCase oneArgNested2;
    private static List<APyObj> oneArgNestedVal;

    /**
     * Two identical test cases with multiple simple (non-compound) arguments, as well as
     * their internal representation.
     */
    private static TestCase multipleArgsSimple;
    private static TestCase multipleArgsSimple2;
    private static List<APyObj> multipleArgsSimpleVal;

    /**
     * Two identical test cases with multiple nested (compound) argument, as well as their
     * internal representation.
     */
    private static TestCase multipleArgsNested;
    private static TestCase multipleArgsNested2;
    private static List<APyObj> multipleArgsNestedVal;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        setUpOneArgSimpleVal();
        setUpOneArgNestedVal();
        setUpMultipleArgsSimpleVal();
        setUpMultipleArgsNestedVal();
    }

    /**
     * Tests the getArgs() method on a test case with no arguments.
     */
    @Test
    @Tag("0.1")
    @Order(1)
    void testGetArgsNoArgs() {
        assertEquals(new ArrayList<>(), noArgs.getArgs());
    }

    /**
     * Tests the getArgs() method on a test case with one simple arg.
     */
    @Test
    @Tag("0.1")
    @Order(2)
    void testGetArgsOneArgSimple() {
        assertEquals(new ArrayList<>(oneArgSimpleVal), oneArgSimple.getArgs());
    }

    /**
     * Tests the getArgs() method on a test case with one nested arg.
     */
    @Test
    @Tag("0.1")
    @Order(3)
    void testGetArgsOneArgNested() {
        assertEquals(new ArrayList<>(oneArgNestedVal), oneArgNested.getArgs());
    }

    /**
     * Tests the getArgs() method on a test case with multiple simple args.
     */
    @Test
    @Tag("0.1")
    @Order(4)
    void testGetArgsMultipleArgsSimple() {
        assertEquals(new ArrayList<>(multipleArgsSimpleVal),
                multipleArgsSimple.getArgs());
    }

    /**
     * Tests the getArgs() method on a test case with multiple nested args.
     */
    @Test
    @Tag("0.1")
    @Order(5)
    void testGetArgsMultipleArgsNested() {
        assertEquals(new ArrayList<>(multipleArgsNestedVal),
                multipleArgsNested.getArgs());
    }

    /**
     * Tests the toString() method on a test case with no arguments.
     */
    @Test
    @Tag("0.1")
    @Order(6)
    void testToStringNoArgs() {
        assertEquals("[]", noArgs.toString());
    }

    /**
     * Tests the toString() method on a test case with one simple arg.
     */
    @Test
    @Tag("0.1")
    @Order(7)
    void testToStringOneArgSimple() {
        assertEquals("[17]", oneArgSimple.toString());
    }

    /**
     * Tests the toString() method on a test case with one nested arg.
     */
    @Test
    @Tag("0.1")
    @Order(8)
    void testToStringOneArgNested() {
        assertEquals("[[('hello ', 'world!'), (':)',)]]", oneArgNested.toString());
    }

    /**
     * Tests the toString() method on a test case with multiple simple args.
     */
    @Test
    @Tag("0.1")
    @Order(9)
    void testToStringMultipleArgsSimple() {
        assertEquals("[17, True, -183.381]", multipleArgsSimple.toString());
    }

    /**
     * Tests the toString() method on a test case with multiple nested args.
     */
    @Test
    @Tag("0.1")
    @Order(10)
    void testToStringMultipleArgsNested() {
        // There are many possible options for the output, since sets are unordered
        String[] options = new String[]{
                "[[('hello ', 'world!'), (':)',)], {0.05: {1, -1}}, {(True, False,"
                        + " True), (False,), (False, False)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {1, -1}}, {(True, False,"
                        + " True), (False, False), (False,)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {1, -1}}, {(False,), "
                        + "(True, False, True), (False, False)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {1, -1}}, {(False,), "
                        + "(False, False), (True, False, True)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {1, -1}}, {(False, "
                        + "False), (True, False, True), (False,)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {1, -1}}, {(False, "
                        + "False), (False,), (True, False, True)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {-1, 1}}, {(True, False,"
                        + " True), (False,), (False, False)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {-1, 1}}, {(True, False,"
                        + " True), (False, False), (False,)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {-1, 1}}, {(False,), "
                        + "(True, False, True), (False, False)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {-1, 1}}, {(False,), "
                        + "(False, False), (True, False, True)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {-1, 1}}, {(False, "
                        + "False), (True, False, True), (False,)}]",
                "[[('hello ', 'world!'), (':)',)], {0.05: {-1, 1}}, {(False, "
                        + "False), (False,), (True, False, True)}]"
        };

        assertTrue(List.of(options).contains(multipleArgsNested.toString()));
    }

    /**
     * Tests the equals() method on two identical test cases with no args.
     */
    @Test
    @Tag("0.2")
    @Order(11)
    void testEqualsNoArgs() {
        assertEquals(noArgs, noArgs2);
    }

    /**
     * Tests the equals() method on two identical test cases with one simple arg.
     */
    @Test
    @Tag("0.2")
    @Order(12)
    void testEqualsOneArgSimple() {
        assertEquals(oneArgSimple, oneArgSimple2);
    }

    /**
     * Tests the equals() method on two identical test cases with one nested arg.
     */
    @Test
    @Tag("0.2")
    @Order(13)
    void testEqualsOneArgNested() {
        assertEquals(oneArgNested, oneArgNested2);
    }

    /**
     * Tests the equals() method on two identical test cases with multiple simple args.
     */
    @Test
    @Tag("0.2")
    @Order(14)
    void testEqualsMultipleArgsSimple() {
        assertEquals(multipleArgsSimple, multipleArgsSimple2);
    }

    /**
     * Tests the equals() method on two identical test cases with multiple nested args.
     */
    @Test
    @Tag("0.2")
    @Order(15)
    void testEqualsMultipleArgsNested() {
        assertEquals(multipleArgsNested, multipleArgsNested2);
    }

    /**
     * Tests that two test cases with different args are not considered equal by the
     * equals() method.
     */
    @Test
    @Tag("0.2")
    @Order(16)
    void testNotEqual() {
        assertNotEquals(oneArgSimple, oneArgNested);
    }

    /**
     * Tests that two test cases where one's args are a subset of the other's are not
     * considered equal by the equals() method (of the test case whose args are a
     * superset).
     */
    @Test
    @Tag("0.2")
    @Order(17)
    void testNotEqualSubset1() {
        assertNotEquals(multipleArgsSimple, oneArgSimple);
    }

    /**
     * Tests that two test cases where one's args are a subset of the other's are not
     * considered equal by the equals() method (of the test case whose args are a
     * supbset).
     */
    @Test
    @Tag("0.2")
    @Order(18)
    void testNotEqualSubset2() {
        assertNotEquals(oneArgSimple, multipleArgsSimple);
    }

    /**
     * Tests the hashCode() method on two identical test cases with no args.
     */
    @Test
    @Tag("0.2")
    @Order(19)
    void testHashCodeNoArgs() {
        assertEquals(noArgs.hashCode(), noArgs2.hashCode());
    }

    /**
     * Tests the hashCode() method on two identical test cases with one simple arg.
     */
    @Test
    @Tag("0.2")
    @Order(20)
    void testHashCodeOneArgSimple() {
        assertEquals(oneArgSimple.hashCode(), oneArgSimple2.hashCode());
    }

    /**
     * Tests the hashCode() method on two identical test cases with one nested arg.
     */
    @Test
    @Tag("0.2")
    @Order(21)
    void testHashCodeOneArgNested() {
        assertEquals(oneArgNested.hashCode(), oneArgNested2.hashCode());
    }

    /**
     * Tests the hashCode() method on two identical test cases with multiple simple args.
     */
    @Test
    @Tag("0.2")
    @Order(22)
    void testHashCodeMultipleArgsSimple() {
        assertEquals(multipleArgsSimple.hashCode(), multipleArgsSimple2.hashCode());
    }

    /**
     * Tests the hashCode() method on two identical test cases with multiple nested args.
     */
    @Test
    @Tag("0.2")
    @Order(23)
    void testHashCodeMultipleArgsNested() {
        assertEquals(multipleArgsNested.hashCode(), multipleArgsNested2.hashCode());
    }

    /**
     * Tests that two test cases with different args do not return the same hash code.
     */
    @Test
    @Tag("0.2")
    @Order(24)
    void testHashCodeNotEqual() {
        assertNotEquals(oneArgSimple.hashCode(), oneArgNested.hashCode());
    }

    /**
     * Tests that two test cases with different args, where one's args are a subset of the
     * other's, do not return the same hash code.
     */
    @Test
    @Tag("0.2")
    @Order(25)
    void testHashCodeNotEqualSubset() {
        assertNotEquals(oneArgSimple.hashCode(), multipleArgsSimple.hashCode());
    }

    /**
     * Set up oneArgSimple, oneArgSimple2, and oneArgSimpleVal for use in the test cases.
     */
    private static void setUpOneArgSimpleVal() {
        noArgs = new TestCase(new ArrayList<>());
        noArgs2 = new TestCase(new ArrayList<>());

        oneArgSimpleVal = List.of(new APyObj[]{new PyIntObj(17)});
        oneArgSimple = new TestCase(oneArgSimpleVal);
        oneArgSimple2 = new TestCase(new ArrayList<>(oneArgSimpleVal));
    }

    /**
     * Set up oneArgNested, oneArgNested2, and oneArgNestedVal for use in the test cases.
     */
    private static void setUpOneArgNestedVal() {
        // Create several strings
        PyStringObj nestedArgInnermost1 = new PyStringObj("hello ");
        PyStringObj nestedArgInnermost2 = new PyStringObj("world!");
        PyStringObj nestedArgInnermost3 = new PyStringObj(":)");

        // Create two tuples containing the above strings
        PyTupleObj<PyStringObj> nestedArgMiddle1 =
                new PyTupleObj<>(List.of(nestedArgInnermost1, nestedArgInnermost2));
        PyTupleObj<PyStringObj> nestedArgMiddle2 =
                new PyTupleObj<>(Collections.singletonList(nestedArgInnermost3));

        // Create a list containing the above tuples
        PyListObj<PyTupleObj<PyStringObj>>
                nestedArg1 =
                new PyListObj<>(List.of(nestedArgMiddle1, nestedArgMiddle2));
        oneArgNestedVal = List.of(new APyObj[]{nestedArg1});
        oneArgNested = new TestCase(oneArgNestedVal);
    }

    /**
     * Set up multipleArgsSimple, multipleArgsSimple2, and multipleArgsSimpleVal for use
     * in the test cases.
     */
    private static void setUpMultipleArgsSimpleVal() {
        multipleArgsSimpleVal = Arrays
                .asList(new PyIntObj(17), new PyBoolObj(true), new PyFloatObj(-183.381));
        multipleArgsSimple = new TestCase(multipleArgsSimpleVal);
        multipleArgsSimple2 = new TestCase(new ArrayList<>(multipleArgsSimpleVal));
    }

    /**
     * Set up multipleArgsNested, multipleArgsNested2, and multipleArgsNestedVal for use
     * in the test cases.
     */
    private static void setUpMultipleArgsNestedVal() {
        // Create the first argument: a list of tuples of strings
        // Create several strings
        PyStringObj nestedArgInnermost1 = new PyStringObj("hello ");
        PyStringObj nestedArgInnermost2 = new PyStringObj("world!");
        PyStringObj nestedArgInnermost3 = new PyStringObj(":)");

        // Create two tuples containing the above strings
        PyTupleObj<PyStringObj> nestedArgMiddle1 =
                new PyTupleObj<>(List.of(nestedArgInnermost1, nestedArgInnermost2));
        PyTupleObj<PyStringObj> nestedArgMiddle2 =
                new PyTupleObj<>(Collections.singletonList(nestedArgInnermost3));

        // Create a list containing the above tuples
        PyListObj<PyTupleObj<PyStringObj>>
                nestedArg1 =
                new PyListObj<>(List.of(nestedArgMiddle1, nestedArgMiddle2));
        oneArgNested2 = new TestCase(List.of(new APyObj[]{nestedArg1}));

        // Create the second argument: a dictionary mapping a float to a set of ints
        PyDictObj<PyFloatObj, PySetObj<PyIntObj>> nestedArg2 =
                new PyDictObj<>(Map.of(new PyFloatObj(0.05),
                        new PySetObj<>(Set.of(new PyIntObj(1), new PyIntObj(-1)))));

        // Create the third argument: a set of tuples of booleans
        // Create two booleans
        PyBoolObj pyFalse = new PyBoolObj(false);
        PyBoolObj pyTrue = new PyBoolObj(true);

        // Create several tuples containing the above booleans
        PyTupleObj<PyBoolObj>
                nestedArg3Middle1 = new PyTupleObj<>(List.of(pyTrue, pyFalse, pyTrue));
        PyTupleObj<PyBoolObj> nestedArg3Middle2 =
                new PyTupleObj<>(Collections.singletonList(pyFalse));
        PyTupleObj<PyBoolObj>
                nestedArg3Middle3 = new PyTupleObj<>(List.of(pyFalse, pyFalse));

        // Create a set containing the above tuples
        PySetObj<PyTupleObj<PyBoolObj>>
                nestedArg3 = new PySetObj<>(
                Set.of(nestedArg3Middle1, nestedArg3Middle2, nestedArg3Middle3));

        // Wrap all args together into a test case
        multipleArgsNestedVal = List.of(nestedArg1, nestedArg2, nestedArg3);
        multipleArgsNested = new TestCase(multipleArgsNestedVal);
        multipleArgsNested2 = new TestCase(new ArrayList<>(multipleArgsNestedVal));
    }
}