package test.rice.node;

import main.rice.node.PyStringNode;
import main.rice.obj.PyStringObj;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyStringNode class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyStringNodeTest extends APyNodeTest {

    /**
     * A PyStringNode whose exhaustive domain includes only one option: the empty string.
     * Its random domain includes only one option of length one.
     */
    private static PyStringNode emptyOnly;

    /**
     * A PyStringNode whose exhaustive domain include only one option of length one. Its
     * random domain includes only the empty string.
     */
    private static PyStringNode oneLenOne;

    /**
     * A PyStringNode whose exhaustive domains include two options of length one. Its
     * random domain includes four options of length two.
     */
    private static PyStringNode twoLenOne;

    /**
     * A PyStringNode whose domains include four options of length two. Its random domain
     * includes two options of length two.
     */
    private static PyStringNode fourLenTwo;

    /**
     * A PyStringNode whose domains include many options of length two.
     */
    private static PyStringNode manyLenTwo;

    /**
     * A PyStringNode whose domains include multiple lengths, from zero to three.
     */
    private static PyStringNode lensZeroToThree;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Swap the random domains on emptyOnly and oneLenOne to ensure that the correct
        // domain is being used for generation
        emptyOnly = new PyStringNode("A");
        emptyOnly.setExDomain(Collections.singletonList(0));
        emptyOnly.setRanDomain(Collections.singletonList(1));

        oneLenOne = new PyStringNode("A");
        oneLenOne.setExDomain(Collections.singletonList(1));
        oneLenOne.setRanDomain(Collections.singletonList(0));

        // Swap the random domains on twoLenOne and fourLenTwo to ensure that the correct
        // domain is being used for generation
        twoLenOne = new PyStringNode("!$");
        twoLenOne.setExDomain(Collections.singletonList(1));
        twoLenOne.setRanDomain(Collections.singletonList(2));

        fourLenTwo = new PyStringNode("!$");
        fourLenTwo.setExDomain(Collections.singletonList(2));
        fourLenTwo.setRanDomain(Collections.singletonList(1));

        manyLenTwo = new PyStringNode("abcde");
        manyLenTwo.setExDomain(Collections.singletonList(2));
        manyLenTwo.setRanDomain(Collections.singletonList(2));

        lensZeroToThree = new PyStringNode("1234");
        lensZeroToThree.setExDomain(Arrays.asList(0, 2));
        lensZeroToThree.setRanDomain(Arrays.asList(2, 3));
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
        assertNull(emptyOnly.getLeftChild());
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
     * Tests genExVals() in the case that the exhaustive domain only includes the empty
     * string.
     */
    @Test
    @Tag("0.1")
    @Order(5)
    void testGenExValsEmpty() {
        Set<PyStringObj> actual = emptyOnly.genExVals();
        Set<PyStringObj> expected = Set.of(new PyStringObj(""));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the exhaustive domain only includes one string
     * of length one.
     */
    @Test
    @Tag("0.2")
    @Order(6)
    void testGenExValsOneLenOne() {
        Set<PyStringObj> actual = oneLenOne.genExVals();
        Set<PyStringObj> expected = Set.of(new PyStringObj("A"));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the exhaustive domain includes two strings of
     * length one.
     */
    @Test
    @Tag("0.3")
    @Order(7)
    void testGenExValsTwoLenOne() {
        Set<PyStringObj> actual = twoLenOne.genExVals();
        Set<PyStringObj> expected =
                Set.of(new PyStringObj("!"), new PyStringObj("$"));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the exhaustive domain includes four strings of
     * length two.
     */
    @Test
    @Tag("0.5")
    @Order(8)
    void testGenExValsFourLenTwo() {
        Set<PyStringObj> actual = fourLenTwo.genExVals();
        Set<PyStringObj> expected =
                Set.of(new PyStringObj("!!"), new PyStringObj("!$"), new PyStringObj("$!"),
                        new PyStringObj("$$"));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the exhaustive domain includes many strings of
     * lengths two.
     */
    @Test
    @Tag("1.0")
    @Order(9)
    void testGenExValsManyLenTwo() {
        Set<PyStringObj> actual = manyLenTwo.genExVals();
        Set<PyStringObj> expected = new HashSet<>();

        // Manually generate all possible combinations of two letters in "abcde"
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expected.add(
                        new PyStringObj(
                                String.valueOf("abcde".charAt(i)) + "abcde".charAt(j)));
            }
        }
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the exhaustive domain includes multiple strings
     * of lengths zero to two.
     */
    @Test
    @Tag("1.5")
    @Order(10)
    void testGenExValsLenMultLens() {
        Set<PyStringObj> actual = lensZeroToThree.genExVals();
        Set<PyStringObj> expected = new HashSet<>();

        // Manually generate all possible combinations of zero and two characters in
        // "1234"
        expected.add(new PyStringObj(""));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expected.add(new PyStringObj(
                        String.valueOf("1234".charAt(i)) + "1234".charAt(j)));
            }
        }
        assertEquals(expected, actual);
    }

    /**
     * Tests genRandVal() in the case that the random domain only includes the empty
     * string.
     */
    @Test
    @Tag("0.5")
    @Order(11)
    void testGenRandValEmpty() {
        // Only one possible outcome -> no margin of error
        Map<PyStringObj, Double> expected =
                Map.of(new PyStringObj(""), 1.0);

        // Run a bunch of trials to get the actual distribution
        Map<PyStringObj, Double> actual = buildDistribution(oneLenOne, 100);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that the random domain only includes one string of
     * length one.
     */
    @Test
    @Tag("0.2")
    @Order(12)
    void testGenRandValOneLenOne() {
        // Only one possible outcome -> no margin of error
        Map<PyStringObj, Double> expected =
                Map.of(new PyStringObj("A"), 1.0);

        // Run a bunch of trials to get the actual distribution
        Map<PyStringObj, Double> actual = buildDistribution(emptyOnly, 100);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that the random domain includes two strings of
     * length two.
     */
    @Test
    @Tag("0.3")
    @Order(13)
    void testGenRandValTwoLenOne() {
        // Equal probability of each outcome
        Map<PyStringObj, Double> expected =
                Map.of(new PyStringObj("!"), 0.5, new PyStringObj("$"), 0.5);

        // Run a bunch of trials to get the actual distribution
        Map<PyStringObj, Double> actual = buildDistribution(fourLenTwo, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that the random domain includes four strings of
     * length two.
     */
    @Test
    @Tag("1.0")
    @Order(14)
    void testGenRandValFourLenTwo() {
        // Equal probability of each outcome
        Map<PyStringObj, Double> expected =
                Map.of(new PyStringObj("!!"), 0.25, new PyStringObj("!$"), 0.25,
                        new PyStringObj("$!"), 0.25, new PyStringObj("$$"), 0.25);

        // Run a bunch of trials to get the actual distribution
        Map<PyStringObj, Double> actual = buildDistribution(twoLenOne, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that the random domain includes many strings of
     * length two.
     */
    @Test
    @Tag("1.0")
    @Order(15)
    void testGenRandValManyLenTwo() {
        // Equal probability of each outcome
        Map<PyStringObj, Double> expected = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                expected.put(
                        new PyStringObj(
                                String.valueOf("abcde".charAt(i)) + "abcde".charAt(j)),
                        // 25 possible outcomes
                        (1.0 / 25.0));
            }
        }

        // Run a bunch of trials to get the actual distribution
        Map<PyStringObj, Double> actual = buildDistribution(manyLenTwo, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that the random domain includes multiple strings of
     * lengths two to three.
     */
    @Test
    @Tag("1.0")
    @Order(16)
    void testGenRandValMultLens() {
        // Equal probability of each *length*, but different numbers of possibilities
        // within each length.
        Map<PyStringObj, Double> expected = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // 50% chance of selecting length 2 divided by 16 options
                expected.put(
                        new PyStringObj(String.valueOf("1234".charAt(i)) + "1234".charAt(j)),
                        (1.0 / 32.0));

                for (int k = 0; k < 4; k++) {
                    // 50% chance of selecting length 3 divided by 64 options
                    expected.put(new PyStringObj(
                            String.valueOf("1234".charAt(i)) + "1234".charAt(j) + "1234"
                                    .charAt(k)), (1.0 / 128.0));
                }
            }
        }

        // Run a bunch of trials to get the actual distribution
        Map<PyStringObj, Double> actual = buildDistribution(
                lensZeroToThree, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.005));
    }
}