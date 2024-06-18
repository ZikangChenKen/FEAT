package test.rice.node;

import main.rice.node.*;
import main.rice.obj.PyBoolObj;
import main.rice.obj.PyFloatObj;
import main.rice.obj.PyIntObj;
import main.rice.obj.PySetObj;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PySetNode class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PySetNodeTest extends APyNodeTest {

    /**
     * A PySetNode whose exhaustive domain includes only one option: the empty set. Its
     * random domain includes only one option of length two.
     */
    private static PySetNode<PyBoolObj> emptyOnly;
    private static PyBoolNode emptyLeftChild;

    /**
     * A PySetNode whose exhaustive domain include only one option of length one. Its
     * random domain includes two options of length one.
     */
    private static PySetNode<PyBoolObj> oneLenOne;

    /**
     * A PySetNode whose exhaustive domains include two options of length one. Its random
     * domain includes one option of length one.
     */
    private static PySetNode<PyBoolObj> twoLenOne;

    /**
     * A PySetNode whose exhaustive domain includes one option of length two. Its random
     * domain consists of the empty list.
     */
    private static PySetNode<PyBoolObj> oneLenTwo;

    /**
     * A PySetNode whose domains include many options of length two.
     */
    private static PySetNode<PyIntObj> threeLenTwo;

    /**
     * A PySetNode whose domains include multiple lengths, from zero to two (contiguous).
     */
    private static PySetNode<PyFloatObj> lensZeroToTwo;

    /**
     * A PySetNode whose domains include multiple lengths, from zero to three.
     */
    private static PySetNode<PyFloatObj> lensZeroToThree;

    /**
     * A nested PySetNode.
     */
    private static PySetNode<PySetObj<PyIntObj>> nestedInts;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        setUpEmpty();
        setUpLenOne();
        setUpLenTwo();
        setUpMixedLen();
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
        assertEquals(Collections.singletonList(2), emptyOnly.getRanDomain());
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
        assertNull(emptyOnly.getRightChild());
    }

    /**
     * Tests genExVals() in the case that the expected output contains a single element:
     * the empty set.
     */
    @Test
    @Tag("0.1")
    @Order(5)
    void testGenExValsEmpty() {
        Set<PySetObj<PyBoolObj>> expected = Set.of(
                new PySetObj<>(Collections.emptySet()));
        Set<PySetObj<PyBoolObj>> actual = emptyOnly.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains a single element: a
     * set of length one.
     */
    @Test
    @Tag("0.2")
    @Order(6)
    void testGenExValsOneLenOne() {
        Set<PySetObj<PyBoolObj>> expected = Set.of(new PySetObj<>(
                Collections.singleton(new PyBoolObj(true))));
        Set<PySetObj<PyBoolObj>> actual = oneLenOne.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains two sets of length
     * one.
     */
    @Test
    @Tag("0.5")
    @Order(7)
    void testGenExValsTwoLenOne() {
        Set<PySetObj<PyBoolObj>> expected = Set.of(
                new PySetObj<>(Collections.singleton(new PyBoolObj(true))),
                new PySetObj<>(Collections.singleton(new PyBoolObj(false))));
        Set<PySetObj<PyBoolObj>> actual = twoLenOne.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains one set of length
     * two.
     */
    @Test
    @Tag("0.5")
    @Order(8)
    void testGenExValsOneLenTwo() {
        Set<PySetObj<PyBoolObj>> expected = Set.of(new PySetObj<>(
                Set.of(new PyBoolObj(true), new PyBoolObj(false))));
        Set<PySetObj<PyBoolObj>> actual = oneLenTwo.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains three sets of
     * length two.
     */
    @Test
    @Tag("0.5")
    @Order(9)
    void testGenExValsThreeLenTwo() {
        // Build the expected set
        Set<PySetObj<PyIntObj>> expected = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {
                expected.add(new PySetObj<>(Set.of(new PyIntObj(i), new PyIntObj(j))));
            }
        }

        // Generate the actual set and compare the actual and expected sets
        Set<PySetObj<PyIntObj>> actual = threeLenTwo.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains sets of lengths
     * zero, one, and two.
     */
    @Test
    @Tag("1.5")
    @Order(10)
    void testGenExValsLenMultLensContig() {
        // Build the expected set
        Set<PySetObj<PyFloatObj>> expected = mixedLenHelper(Set.of(0, 1, 2));

        // Generate the actual set and compare the actual and expected sets
        Set<PySetObj<PyFloatObj>> actual = lensZeroToTwo.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains sets of lengths
     * zero and two.
     */
    @Test
    @Tag("1.5")
    @Order(11)
    void testGenExValsMultLensNonContig() {
        // Build the expected set
        Set<PySetObj<PyFloatObj>> expected = mixedLenHelper(Set.of(0, 2));

        // Generate the actual set and compare the actual and expected sets
        Set<PySetObj<PyFloatObj>> actual = lensZeroToThree.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains nested sets.
     */
    @Test
    @Tag("1.5")
    @Order(12)
    void testGenExValsNested() {
        // Build the expected set
        Set<PySetObj<PySetObj<PyIntObj>>> expected = new HashSet<>();
        expected.add(new PySetObj<>(Collections.emptySet()));

        // Build the set of potential inner sets
        Set<PySetObj<PyIntObj>> innerVals = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            // Add length one sets
            innerVals.add(new PySetObj<>(Collections.singleton(new PyIntObj(i))));
            for (int j = i + 1; j < 3; j++) {
                // Add length two sets
                innerVals.add(new PySetObj<>(Set.of(new PyIntObj(i),
                        new PyIntObj(j))));
            }
        }

        // Combine the inner sets to make nested sets
        for (PySetObj<PyIntObj> val : innerVals) {
            for (PySetObj<PyIntObj> val2 : innerVals) {
                if (val == val2) {
                    // No duplicates allowed!
                    continue;
                }
                expected.add(new PySetObj<>(Set.of(val, val2)));
            }
        }

        // Generate the actual set and compare the actual and expected sets
        Set<PySetObj<PySetObj<PyIntObj>>> actual = nestedInts.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genRandVal() in the case that there is only one possible option: the empty
     * set.
     */
    @Test
    @Tag("0.5")
    @Order(13)
    void testGenRandValEmpty() {
        // Build the expected results
        Map<PySetObj<PyBoolObj>, Double> expected = Map.of(
                new PySetObj<>(new HashSet<>()), 1.0);

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PyBoolObj>, Double> actual = buildDistribution(oneLenTwo, 100);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that there is only one possible option: the empty
     * set.
     */
    @Test
    @Tag("0.2")
    @Order(14)
    void testGenRandValOneLenOne() {
        // Build the expected results
        Map<PySetObj<PyBoolObj>, Double> expected = Map.of(
                new PySetObj<>(Collections.singleton(new PyBoolObj(true))), 1.0);

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PyBoolObj>, Double> actual = buildDistribution(twoLenOne, 100);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that there is only one possible option: a set of
     * length one.
     */
    @Test
    @Tag("0.2")
    @Order(15)
    void testGenRandValTwoLenOne() {
        // Build the expected results
        Map<PySetObj<PyBoolObj>, Double> expected = Map.of(
                new PySetObj<>(Collections.singleton(new PyBoolObj(true))), 0.5,
                new PySetObj<>(Collections.singleton(new PyBoolObj(false))), 0.5);

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PyBoolObj>, Double> actual =
                buildDistribution(oneLenOne, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there is one possible options of length two.
     */
    @Test
    @Tag("0.2")
    @Order(16)
    void testGenRandValOneLenTwo() {
        // Build the expected results
        Map<PySetObj<PyBoolObj>, Double> expected = Map.of(
                new PySetObj<>(Set.of(new PyBoolObj(true), new PyBoolObj(false))), 1.0);

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PyBoolObj>, Double> actual = buildDistribution(emptyOnly, 100);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that there are three possible options of length
     * two.
     */
    @Test
    @Tag("0.2")
    @Order(17)
    void testGenRandValThreeLenTwo() {
        // Build the expected results
        Map<PySetObj<PyIntObj>, Double> expected = new HashMap<>();
        for (int i = 0; i > -3; i--) {
            for (int j = i - 1; j > -3; j--) {
                expected.put(new PySetObj<>(Set.of(new PyIntObj(i), new PyIntObj(j))),
                        (1.0 / 3.0));
            }
        }

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PyIntObj>, Double> actual =
                buildDistribution(threeLenTwo, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are many possible options of lengths
     * zero, one, and two.
     */
    @Test
    @Tag("1.0")
    @Order(18)
    void testGenRandValMultLensContig() {
        // Build the expected results
        Map<PySetObj<PyFloatObj>, Double> expected = new HashMap<>();
        double[] vals = new double[]{6.0, 8.0, 10.0};
        expected.put(new PySetObj<>(Collections.emptySet()), (1.0 / 3.0));
        for (int i = 0; i < vals.length; i++) {
            expected.put(new PySetObj<>(Set.of(new PyFloatObj(vals[i]))), (1.0 / 9.0));
            for (int j = i + 1; j < vals.length; j++) {
                expected.put(new PySetObj<>(Set.of(new PyFloatObj(vals[i]),
                        new PyFloatObj(vals[j]))), (1.0 / 9.0));
            }
        }

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PyFloatObj>, Double> actual =
                buildDistribution(lensZeroToTwo, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are many possible options of lengths one
     * and three.
     */
    @Test
    @Tag("1.0")
    @Order(19)
    void testGenRandValMultLensNonContig() {
        // Build the expected results
        Map<PySetObj<PyFloatObj>, Double> expected = new HashMap<>();
        double[] vals = new double[]{6.0, 8.0, 10.0};
        for (double val : vals) {
            expected.put(new PySetObj<>(Set.of(new PyFloatObj(val))), (1.0 / 6.0));
        }
        expected.put(new PySetObj<>(Set.of(new PyFloatObj(6.0),
                new PyFloatObj(8.0), new PyFloatObj(10.0))), (1.0 / 2.0));

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PyFloatObj>, Double> actual =
                buildDistribution(lensZeroToThree, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are many possible options that are
     * nested.
     */
    @Test
    @Tag("1.5")
    @Order(20)
    void testGenRandValNested() {
        // Build the expected results
        Map<PySetObj<PySetObj<PyIntObj>>, Double> expected = new HashMap<>();

        // 1. Build inner vals
        Set<PySetObj<PyIntObj>> innerVals = new HashSet<>();
        // Create sets of size three
        for (int i = 3; i < 7; i++) {
            for (int j = i + 1; j < 7; j++) {
                for (int k = j + 1; k < 7; k++) {
                    innerVals.add(new PySetObj<>(
                            Set.of(new PyIntObj(i), new PyIntObj(j), new PyIntObj(k))));
                }
            }
        }
        // Add set of size four
        innerVals.add(new PySetObj<>(Set.of(new PyIntObj(3), new PyIntObj(4),
                new PyIntObj(5), new PyIntObj(6))));

        // 2. For each inner val, add perm of {just that}, {everything but that}
        for (PySetObj<PyIntObj> val : innerVals) {
            double prob1;
            double prob2;
            if (val.getValue().size() == 3) {
                prob1 = (1.0 / 16.0);
                prob2 = (0.12267);
            } else {
                prob1 = (1.0 / 4.0);
                prob2 = (0.007);
            }
            expected.put(new PySetObj<>(Collections.singleton(val)), prob1);
            Set<PySetObj<PyIntObj>> allBut = new HashSet<>(innerVals);
            allBut.remove(val);
            PySetObj<PySetObj<PyIntObj>> allButVal = new PySetObj<>(allBut);
            expected.put(allButVal, prob2);
        }

        // Run a bunch of trials to get the actual distribution and compare the actual
        // and expected distributions
        Map<PySetObj<PySetObj<PyIntObj>>, Double> actual =
                buildDistribution(nestedInts, 100000);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Sets up emptyOnly.
     */
    private static void setUpEmpty() {
        emptyLeftChild = new PyBoolNode();
        emptyLeftChild.setExDomain(Arrays.asList(0, 1));
        emptyLeftChild.setRanDomain(Arrays.asList(0, 1));
        emptyOnly = new PySetNode<>(emptyLeftChild);
        emptyOnly.setExDomain(Collections.singletonList(0));
        emptyOnly.setRanDomain(Collections.singletonList(2));
    }

    /**
     * Sets up oneLenOne and twoLenOne.
     */
    private static void setUpLenOne() {
        PyBoolNode oneLenOneChild = new PyBoolNode();
        oneLenOneChild.setExDomain(Collections.singletonList(1));
        oneLenOneChild.setRanDomain(Arrays.asList(0, 1));
        oneLenOne = new PySetNode<>(oneLenOneChild);
        oneLenOne.setExDomain(Collections.singletonList(1));
        oneLenOne.setRanDomain(Collections.singletonList(1));

        // Swap the random domains to test that the right domain is being used
        PyBoolNode twoLenOneChild = new PyBoolNode();
        twoLenOneChild.setExDomain(Arrays.asList(0, 1));
        twoLenOneChild.setRanDomain(Collections.singletonList(1));
        twoLenOne = new PySetNode<>(twoLenOneChild);
        twoLenOne.setExDomain(Collections.singletonList(1));
        twoLenOne.setRanDomain(Collections.singletonList(1));
    }

    /**
     * Sets up oneLenTwo and threeLenTwo.
     */
    private static void setUpLenTwo() {
        PyBoolNode oneLenTwoChild = new PyBoolNode();
        oneLenTwoChild.setExDomain(Arrays.asList(0, 1));
        oneLenTwoChild.setRanDomain(Arrays.asList(0, 1));
        oneLenTwo = new PySetNode<>(oneLenTwoChild);
        oneLenTwo.setExDomain(Collections.singletonList(2));
        oneLenTwo.setRanDomain(Collections.singletonList(0));

        PyIntNode threeLenTwoChild = new PyIntNode();
        threeLenTwoChild.setExDomain(Arrays.asList(0, 1, 2));
        threeLenTwoChild.setRanDomain(Arrays.asList(0, -1, -2));
        threeLenTwo = new PySetNode<>(threeLenTwoChild);
        threeLenTwo.setExDomain(Collections.singletonList(2));
        threeLenTwo.setRanDomain(Collections.singletonList(2));
    }

    /**
     * Sets up lensZeroToTwo and lensZeroToThree.
     */
    private static void setUpMixedLen() {
        PyFloatNode child = new PyFloatNode();
        child.setExDomain(Arrays.asList(3, 5, 7));
        child.setRanDomain(Arrays.asList(6, 8, 10));

        lensZeroToTwo = new PySetNode<>(child);
        lensZeroToTwo.setExDomain(Arrays.asList(0, 2, 1));
        lensZeroToTwo.setRanDomain(Arrays.asList(0, 1, 2));

        lensZeroToThree = new PySetNode<>(child);
        lensZeroToThree.setExDomain(Arrays.asList(2, 0));
        lensZeroToThree.setRanDomain(Arrays.asList(1, 3));
    }

    /**
     * Sets up nestedInts.
     */
    private static void setUpNested() {
        PyIntNode both = new PyIntNode();
        both.setExDomain(Arrays.asList(0, 1, 2));
        both.setRanDomain(Arrays.asList(3, 4, 5, 6));

        APyNode<PySetObj<PyIntObj>> inner = new PySetNode<>(both);
        inner.setExDomain(Arrays.asList(1, 2));
        inner.setRanDomain(Arrays.asList(3, 4));

        nestedInts = new PySetNode<>(inner);
        nestedInts.setExDomain(Arrays.asList(0, 2));
        nestedInts.setRanDomain(Arrays.asList(1, 4));
    }

    /**
     * Helper for generating the expected sets for exhaustive generation for lensZeroToTwo
     * and lensZeroToThree.
     *
     * @param sizes the set of sizes to include in the generated results
     * @return all possible sets of the specified sizes
     */
    private static Set<PySetObj<PyFloatObj>> mixedLenHelper(Set<Integer> sizes) {
        // Build the expected set
        Set<PySetObj<PyFloatObj>> expected = new HashSet<>();
        if (sizes.contains(0)) {
            // Include the empty set
            expected.add(new PySetObj<>(Collections.emptySet()));
        }

        double[] vals = new double[]{3.0, 5.0, 7.0};
        for (int i = 0; i < vals.length; i++) {
            if (sizes.contains(1)) {
                // Include one element sets
                expected.add(new PySetObj<>(Set.of(new PyFloatObj(vals[i]))));
            }
            if (sizes.contains(2)) {
                // Include two element sets
                for (int j = i + 1; j < vals.length; j++) {
                    expected.add(new PySetObj<>(
                            Set.of(new PyFloatObj(vals[i]), new PyFloatObj(vals[j]))));
                }
            }
        }
        if (sizes.contains(3)) {
            // Include three element sets
            expected.add(new PySetObj<>(Set.of(new PyFloatObj(6.0),
                    new PyFloatObj(8.0), new PyFloatObj(10.0))));
        }
        return expected;
    }
}