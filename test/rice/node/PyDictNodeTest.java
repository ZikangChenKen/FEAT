package test.rice.node;

import main.rice.node.PyBoolNode;
import main.rice.node.PyDictNode;
import main.rice.node.PyFloatNode;
import main.rice.node.PyIntNode;
import main.rice.obj.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases for the PyDictNode class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyDictNodeTest extends APyNodeTest {

    /**
     * A PyDictNode whose exhaustive domain includes only one option: the empty dict. Its
     * random domain includes only one option of size one.
     */
    private static PyDictNode<PyBoolObj, PyBoolObj> emptyOnly;
    private static PyBoolNode emptyLeftChild;
    private static PyBoolNode emptyRightChild;

    /**
     * A PyDictNode whose exhaustive domain includes only one option of size one. Its
     * random domain includes only the empty dict.
     */
    private static PyDictNode<PyBoolObj, PyBoolObj> oneLenOne;

    /**
     * A PyDictNode whose exhaustive domain includes two options of size one. Its random
     * domain includes four options of size two.
     */
    private static PyDictNode<PyBoolObj, PyBoolObj> twoLenOne;
    private static Set<PyDictObj<PyBoolObj, PyBoolObj>> twoLenOneExpected;

    /**
     * A PyDictNode whose domains include four options of size two. Its random domain
     * includes two options of size two.
     */
    private static PyDictNode<PyBoolObj, PyBoolObj> fourLenTwo;
    private static Set<PyDictObj<PyBoolObj, PyBoolObj>> fourLenTwoExpected;

    /**
     * A PyDictNode whose domains include many options of size two.
     */
    private static PyDictNode<PyIntObj, PyIntObj> manyLenTwo;
    private static Set<PyDictObj<PyIntObj, PyIntObj>> manyLenTwoExpected;

    /**
     * Two PyDictNode whose domains include multiple sizes. This first one includes
     * contiguous size ranges; the second uses size ranges that have gaps.
     */
    private static PyDictNode<PyIntObj, PyBoolObj> lensZeroToOne;
    private static PyDictNode<PyIntObj, PyBoolObj> lensZeroToFour;

    /**
     * A nested PyDictNode.
     */
    private static PyDictNode<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>> nested;
    private static Set<PyDictObj<PyFloatObj, PyFloatObj>> nestedInnerExpected;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        setUpOneOption();
        setUpTwoLenOne();
        setUpFourLenTwo();
        setUpManyLenTwo();
        setUpVariedLens();
        setUpNested();
    }

    /**
     * Tests that getExDomain() works.
     */
    @Test
    @Tag("0.1")
    @Order(1)
    void testGetExDomain() {
        assertEquals(Collections.singletonList(0), emptyOnly.getExDomain());
    }

    /**
     * Tests that getRanDomain() works.
     */
    @Test
    @Tag("0.1")
    @Order(2)
    void testGetRanDomain() {
        assertEquals(Collections.singletonList(1), emptyOnly.getRanDomain());
    }

    /**
     * Tests that getLeftChild() works.
     */
    @Test
    @Tag("0.1")
    @Order(3)
    void testGetLeftChild() {
        assertEquals(emptyLeftChild, emptyOnly.getLeftChild());
    }

    /**
     * Tests that getRightChild() works.
     */
    @Test
    @Tag("0.1")
    @Order(4)
    void testGetRightChild() {
        assertEquals(emptyRightChild, emptyOnly.getRightChild());
    }

    /**
     * Tests genExVals() in the case that the expected output contains a single element:
     * the empty dict.
     */
    @Test
    @Tag("0.1")
    @Order(5)
    void testGenExValsEmpty() {
        // Build the expected results: {}
        Set<PyDictObj<PyBoolObj, PyBoolObj>> expected =
                Collections.singleton(new PyDictObj<>(Collections.emptyMap()));

        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyBoolObj, PyBoolObj>> actual = emptyOnly.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains a single element: a
     * dict of size one.
     */
    @Test
    @Tag("0.2")
    @Order(6)
    void testGenExValsOneLenOne() {
        // Build the expected results: {true: true}
        Set<PyDictObj<PyBoolObj, PyBoolObj>> expected = Set.of(new PyDictObj<>(
                Map.of(new PyBoolObj(true), new PyBoolObj(true))));

        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyBoolObj, PyBoolObj>> actual = oneLenOne.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains two dicts of size
     * one.
     */
    @Test
    @Tag("0.2")
    @Order(7)
    void testGenExValsTwoLenOne() {
        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyBoolObj, PyBoolObj>> actual = twoLenOne.genExVals();
        assertEquals(twoLenOneExpected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains four dicts of size
     * two.
     */
    @Test
    @Tag("0.5")
    @Order(8)
    void testGenExValsFourLenTwo() {
        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyBoolObj, PyBoolObj>> actual = fourLenTwo.genExVals();
        assertEquals(fourLenTwoExpected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains many dicts of size
     * two.
     */
    @Test
    @Tag("1.0")
    @Order(9)
    void testGenExValsManyLenTwo() {
        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyIntObj, PyIntObj>> actual = manyLenTwo.genExVals();
        assertEquals(manyLenTwoExpected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains a variety of dicts
     * of sizes zero and one.
     */
    @Test
    @Tag("1.5")
    @Order(10)
    void testGenExValsMultLensContig() {
        // Build the expected results: create all possible (key, val) pairs
        List<Map<PyIntObj, PyBoolObj>> maps = new ArrayList<>();
        for (int i : new int[]{3, 4, 5}) {
            for (boolean b : new boolean[]{true, false}) {
                maps.add(Map.of(new PyIntObj(i), new PyBoolObj(b)));
            }
        }

        // Generate size 0 and 1 maps
        Set<PyDictObj<PyIntObj, PyBoolObj>> expected = new HashSet<>();
        expected.add(new PyDictObj<>(Collections.emptyMap()));
        for (Map<PyIntObj, PyBoolObj> map : maps) {
            expected.add(new PyDictObj<>(map));
        }

        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyIntObj, PyBoolObj>> actual = lensZeroToOne.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains a variety of dicts
     * of sizes zero and two (skipping one).
     */
    @Test
    @Tag("2.0")
    @Order(11)
    void testGenExValsMultLensNonContig() {
        // Build the expected results: create all possible (key, val) pairs
        List<Map<PyIntObj, PyBoolObj>> maps = new ArrayList<>();
        for (int i : new int[]{3, 4, 5}) {
            for (boolean b : new boolean[]{true, false}) {
                maps.add(Map.of(new PyIntObj(i), new PyBoolObj(b)));
            }
        }

        // Generate size 2 maps
        Set<PyDictObj<PyIntObj, PyBoolObj>> expected = buildTwoPairHelper(maps);

        // Add size 0 map
        expected.add(new PyDictObj<>(Collections.emptyMap()));

        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyIntObj, PyBoolObj>> actual = lensZeroToFour.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains nested dicts.
     */
    @Test
    @Tag("2.0")
    @Order(12)
    void testGenExValsNested() {
        // Build the expected results
        Set<PyDictObj<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>>> expected =
                new HashSet<>();

        // Outer options:
        // {1.1: *}
        // {2.2: *}
        for (double outerKey : new double[]{1.1, 2.2}) {
            for (PyDictObj<PyFloatObj, PyFloatObj> innerDict : nestedInnerExpected) {
                expected.add(new PyDictObj<>(
                        Map.of(new PyFloatObj(outerKey), innerDict)));
            }
        }

        // Generate the actual results and compare the actual and expected sets
        Set<PyDictObj<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>>> actual
                = nested.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genRandVal() in the case that there is only one option: the empty dict.
     */
    @Test
    @Tag("0.2")
    @Order(13)
    void testGenRandValEmpty() {
        // Build the expected results (only one option)
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> expected =
                Map.of(new PyDictObj<>(Collections.emptyMap()), 1.0);

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> actual =
                buildDistribution(oneLenOne, 100);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that there is only one option: a dict of size one.
     */
    @Test
    @Tag("0.2")
    @Order(14)
    void testGenRandValOneLenOne() {
        // Build the expected results (only one option)
        Map<PyBoolObj, PyBoolObj> val = Map.of(new PyBoolObj(true), new PyBoolObj(false));
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> expected =
                Map.of(new PyDictObj<>(val), 1.0);

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> actual =
                buildDistribution(emptyOnly, 100);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that there are two possible options of size one.
     */
    @Test
    @Tag("0.2")
    @Order(15)
    void testGenRandValTwoLenOne() {
        // Build the expected results
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> expected =
                convertExpExToRandEqual(twoLenOneExpected, 0.5);

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> actual =
                buildDistribution(twoLenOne, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are four possible options of size two.
     */
    @Test
    @Tag("0.5")
    @Order(16)
    void testGenRandValFourLenTwo() {
        // Build the expected results
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> expected =
                convertExpExToRandEqual(fourLenTwoExpected, 0.25);

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyBoolObj, PyBoolObj>, Double> actual =
                buildDistribution(fourLenTwo, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are many possible options of size two.
     */
    @Test
    @Tag("0.5")
    @Order(17)
    void testGenRandValManyLenTwo() {
        // Build the expected results
        Map<PyDictObj<PyIntObj, PyIntObj>, Double> expected =
                convertExpExToRandEqual(manyLenTwoExpected, (1.0 / 12.0));

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyIntObj, PyIntObj>, Double> actual =
                buildDistribution(manyLenTwo, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are a variety of options of sizes zero
     * and one.
     */
    @Test
    @Tag("1.5")
    @Order(18)
    void testGenRandValMultLensContig() {
        // Build the expected results
        Map<PyDictObj<PyIntObj, PyBoolObj>, Double> expected = new HashMap<>();

        // One possible dict of size 0
        expected.put(new PyDictObj<>(Collections.emptyMap()), (1.0 / 2.0));

        // Find all possible dicts of size 1
        for (int i = 6; i < 10; i++) {
            // Size one: only includes i
            for (boolean b : new boolean[]{true, false}) {
                expected.put(new PyDictObj<>(Map.of(
                        new PyIntObj(i), new PyBoolObj(b))), (1.0 / 16.0));
            }
        }

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyIntObj, PyBoolObj>, Double> actual =
                buildDistribution(lensZeroToOne, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are a variety of options of sizes one and
     * four (skipping zero, two, and three).
     */
    @Test
    @Tag("1.5")
    @Order(19)
    void testGenRandValMultLensNonContig() {
        // Build the expected results
        Map<PyDictObj<PyIntObj, PyBoolObj>, Double> expected = new HashMap<>();

        // Find all possible dicts containing a single (key, val) pair
        for (int i = 6; i < 10; i++) {
            for (boolean b : new boolean[]{true, false}) {
                expected.put(new PyDictObj<>(Map.of(
                        new PyIntObj(i), new PyBoolObj(b))), (1.0 / 16.0));
            }
        }

        // Find all possible dicts containing four (key, val) pairs
        boolean[] boolVals = new boolean[]{true, false};
        for (boolean b1 : boolVals) {
            for (boolean b2 : boolVals) {
                for (boolean b3 : boolVals) {
                    for (boolean b4 : boolVals) {
                        Map<PyIntObj, PyBoolObj> val = Map.of(
                                new PyIntObj(6), new PyBoolObj(b1),
                                new PyIntObj(7), new PyBoolObj(b2),
                                new PyIntObj(8), new PyBoolObj(b3),
                                new PyIntObj(9), new PyBoolObj(b4));
                        expected.put(new PyDictObj<>(val), (1.0 / 32.0));
                    }
                }
            }
        }

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyIntObj, PyBoolObj>, Double> actual =
                buildDistribution(lensZeroToFour, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that the expected output contains nested dicts.
     */
    @Test
    @Tag("1.5")
    @Order(20)
    void testGenRandValNested() {
        // Build the expected results
        Map<PyDictObj<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>>, Double> expected =
                new HashMap<>();

        // Find size one options
        List<Map<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>>> maps = new ArrayList<>();
        double[] keys = new double[]{-1.1, -2.2};
        for (double outerKey : keys) {
            for (PyDictObj<PyFloatObj, PyFloatObj> innerDict : nestedInnerExpected) {
                Map<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>> lenOneOption =
                        Map.of(new PyFloatObj(outerKey), innerDict);
                maps.add(lenOneOption);
            }
        }

        // Create size two options
        Set<PyDictObj<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>>> expectedLenTwo =
                buildTwoPairHelper(maps);

        // Compute probabilities
        for (PyDictObj<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>> lenTwoOption :
                expectedLenTwo) {
            expected.put(lenTwoOption, 1.0 / 16.0);
        }

        // Generate the actual results and compare the actual and expected distributions
        Map<PyDictObj<PyFloatObj, PyDictObj<PyFloatObj, PyFloatObj>>, Double> actual =
                buildDistribution(nested, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Sets up emptyOnly and oneLenOne.
     */
    private static void setUpOneOption() {
        emptyLeftChild = new PyBoolNode();
        emptyLeftChild.setExDomain(Collections.singletonList(1));
        emptyLeftChild.setRanDomain(Collections.singletonList(1));

        emptyRightChild = new PyBoolNode();
        emptyRightChild.setExDomain(Collections.singletonList(0));
        emptyRightChild.setRanDomain(Collections.singletonList(0));

        // Swap the random domains on emptyOnly and oneLenOne to ensure that the correct
        // domain is being used for generation
        emptyOnly = new PyDictNode<>(emptyLeftChild, emptyRightChild);
        emptyOnly.setExDomain(Collections.singletonList(0));
        emptyOnly.setRanDomain(Collections.singletonList(1));

        // Create generator of one option of size one
        PyBoolNode oneLenOneKey = new PyBoolNode();
        oneLenOneKey.setExDomain(Collections.singletonList(1));
        oneLenOneKey.setRanDomain(Collections.singletonList(1));

        oneLenOne = new PyDictNode<>(oneLenOneKey, oneLenOneKey);
        oneLenOne.setExDomain(Collections.singletonList(1));
        oneLenOne.setRanDomain(Collections.singletonList(0));
    }

    /**
     * Sets up twoLenOne.
     */
    private static void setUpTwoLenOne() {
        // Create generator of two options of size one
        PyBoolNode twoLenOneKey = new PyBoolNode();
        twoLenOneKey.setExDomain(Collections.singletonList(1));
        twoLenOneKey.setRanDomain(Collections.singletonList(1));

        PyBoolNode twoLenOneVal = new PyBoolNode();
        twoLenOneVal.setExDomain(Arrays.asList(1, 0));
        twoLenOneVal.setRanDomain(Arrays.asList(0, 1));

        twoLenOne = new PyDictNode<>(twoLenOneKey, twoLenOneVal);
        twoLenOne.setExDomain(Collections.singletonList(1));
        twoLenOne.setRanDomain(Collections.singletonList(1));

        // Build the expected results: {true: true}, {true: false}
        twoLenOneExpected = new HashSet<>();
        for (boolean b : new boolean[]{true, false}) {
            twoLenOneExpected.add(new PyDictObj<>(
                    Map.of(new PyBoolObj(true), new PyBoolObj(b))));
        }
    }

    /**
     * Sets up fourLenTwo.
     */
    private static void setUpFourLenTwo() {
        // Create generator of four options of size two
        PyBoolNode fourLenTwoKey = new PyBoolNode();
        fourLenTwoKey.setExDomain(Arrays.asList(1, 0));
        fourLenTwoKey.setRanDomain(Arrays.asList(0, 1));

        fourLenTwo = new PyDictNode<>(fourLenTwoKey, fourLenTwoKey);
        fourLenTwo.setExDomain(Collections.singletonList(2));
        fourLenTwo.setRanDomain(Collections.singletonList(2));

        // Build the expected results:
        // {true: true, false: true}, {true: true, false: false},
        // {true: false, false: true}, {true: false, false: false},
        List<Map<PyBoolObj, PyBoolObj>> maps = new ArrayList<>();
        for (boolean b1 : new boolean[]{true, false}) {
            for (boolean b2 : new boolean[]{true, false}) {
                maps.add(Map.of(new PyBoolObj(b1), new PyBoolObj(b2)));
            }
        }
        fourLenTwoExpected = buildTwoPairHelper(maps);
    }

    /**
     * Sets up manyLenTwo.
     */
    private static void setUpManyLenTwo() {
        // Create generator of many options of size two
        PyIntNode manyLenTwoKey = new PyIntNode();
        manyLenTwoKey.setExDomain(Arrays.asList(1, 2, 0));
        manyLenTwoKey.setRanDomain(Arrays.asList(1, 2, 0));
        PyIntNode manyLenTwoVal = new PyIntNode();
        manyLenTwoVal.setExDomain(Arrays.asList(4, 3));
        manyLenTwoVal.setRanDomain(Arrays.asList(3, 4));

        manyLenTwo = new PyDictNode<>(manyLenTwoKey, manyLenTwoVal);
        manyLenTwo.setExDomain(Collections.singletonList(2));
        manyLenTwo.setRanDomain(Collections.singletonList(2));

        // Build the expected results:
        // {0: 3, 1: 3}, {0: 3, 1: 4}, {0: 4, 1: 3}, {0: 4, 1: 4},
        // {0: 3, 2: 3}, {0: 3, 2: 4}, {0: 4, 2: 3}, {0: 4, 2: 4},
        // {1: 3, 2: 3}, {1: 3, 2: 4}, {1: 4, 2: 3}, {1: 4, 2: 4}
        List<Map<PyIntObj, PyIntObj>> maps2 = new ArrayList<>();
        for (int i : new int[]{0, 1, 2}) {
            for (int j : new int[]{3, 4}) {
                maps2.add(Map.of(new PyIntObj(i), new PyIntObj(j)));
            }
        }
        manyLenTwoExpected = buildTwoPairHelper(maps2);
    }

    /**
     * Sets up lensZeroToOne and lensZeroToFour.
     */
    private static void setUpVariedLens() {
        // Create generators of dicts of varying sizes
        PyIntNode lenZeroToFourKey = new PyIntNode();
        lenZeroToFourKey.setExDomain(Arrays.asList(5, 4, 3));
        lenZeroToFourKey.setRanDomain(Arrays.asList(6, 7, 8, 9));

        PyBoolNode lenZeroToFourVal = new PyBoolNode();
        lenZeroToFourVal.setExDomain(Arrays.asList(0, 1));
        lenZeroToFourVal.setRanDomain(Arrays.asList(0, 1));

        // Contiguous size ranges
        lensZeroToOne = new PyDictNode<>(lenZeroToFourKey, lenZeroToFourVal);
        lensZeroToOne.setExDomain(Arrays.asList(0, 1));
        lensZeroToOne.setRanDomain(Arrays.asList(0, 1));

        // Non-contiguous size ranges
        lensZeroToFour = new PyDictNode<>(lenZeroToFourKey, lenZeroToFourVal);
        lensZeroToFour.setExDomain(Arrays.asList(0, 2));
        lensZeroToFour.setRanDomain(Arrays.asList(1, 4));
    }

    /**
     * Sets up nested.
     */
    private static void setUpNested() {
        // Create a generator for inner dicts, which will be values in the outer dict
        // Create a generator that will be used for both the keys and the vals in the
        // inner dict
        PyFloatNode innerKey = new PyFloatNode();
        innerKey.setExDomain(Arrays.asList(3.7, 7.3));
        innerKey.setRanDomain(Arrays.asList(3.7, 7.3));

        // Create a generator for the inner dicts (vals in the outer dict)
        PyDictNode<PyFloatObj, PyFloatObj> outerVal = new PyDictNode<>(
                innerKey, innerKey);
        outerVal.setExDomain(Collections.singletonList(2));
        outerVal.setRanDomain(Collections.singletonList(2));

        // Create a generator for the keys in the outer dict
        PyFloatNode outerKey = new PyFloatNode();
        outerKey.setExDomain(Arrays.asList(1.1, 2.2));
        outerKey.setRanDomain(Arrays.asList(-1.1, -2.2));

        nested = new PyDictNode<>(outerKey, outerVal);
        nested.setExDomain(Collections.singletonList(1));
        nested.setRanDomain(Collections.singletonList(2));

        // Build the expected inner results:
        // Inner options:
        // {3.7: 3.7, 7.3: 3.7}
        // {3.7: 3.7, 7.3: 7.3}
        // {3.7: 7.3, 7.3: 3.7}
        // {3.7: 7.3, 7.3: 7.3}
        double[] keys = new double[]{3.7, 7.3};
        List<Map<PyFloatObj, PyFloatObj>> maps = new ArrayList<>();
        for (double i : keys) {
            for (double j : keys) {
                maps.add(Map.of(new PyFloatObj(i), new PyFloatObj(j)));
            }
        }
        nestedInnerExpected = buildTwoPairHelper(maps);
    }

    /**
     * Helper function for generating two-pair dictionaries.
     *
     * @param maps      all possible (key, val) pairs
     * @param <KeyType> type of keys in the dictionaries being built
     * @param <ValType> type of values in the dictionaries being built
     * @return all possible combinations of two (key, val) pairs (with each pair having a
     * unique key)
     */
    private static <KeyType extends APyObj, ValType extends APyObj> Set<PyDictObj<KeyType,
            ValType>> buildTwoPairHelper(List<Map<KeyType, ValType>> maps) {
        Set<PyDictObj<KeyType, ValType>> expected = new HashSet<>();

        // Compare each pair of possible (key, val) pairs and find all combinations of
        // two pairs that have unique keys
        for (Map<KeyType, ValType> map1 : maps) {
            for (Map<KeyType, ValType> map2 : maps) {
                if (map1.keySet().toArray()[0].equals(map2.keySet().toArray()[0])) {
                    // Same keys -> will give us a map of size one -> skip
                    continue;
                }
                Map<KeyType, ValType> lenTwoMap = new HashMap<>();
                lenTwoMap.putAll(map1);
                lenTwoMap.putAll(map2);
                expected.add(new PyDictObj<>(lenTwoMap));
            }
        }

        return expected;
    }
}