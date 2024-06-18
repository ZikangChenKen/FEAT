package test.rice.node;

import main.rice.node.PyIntNode;
import main.rice.obj.PyIntObj;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyIntNode class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyIntNodeTest extends APyNodeTest {

    /**
     * A PyIntNode whose domains include a single positive integer (using different
     * integers for the exhaustive and random domains).
     */
    static PyIntNode singlePos;

    /**
     * A PyIntNode whose domains include a single negative integer (using different
     * integers for the exhaustive and random domains).
     */
    static PyIntNode singleNeg;

    /**
     * A PyIntNode whose domains include multiple integer values (using different integers
     * for the exhaustive and random domains).
     */
    static PyIntNode multipleVals;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        singlePos = new PyIntNode();
        List<Integer> exDomain = Collections.singletonList(17);
        List<Integer> ranDomain = Collections.singletonList(203);
        singlePos.setExDomain(exDomain);
        singlePos.setRanDomain(ranDomain);

        singleNeg = new PyIntNode();
        exDomain = Collections.singletonList(-3);
        ranDomain = Collections.singletonList(-18);
        singleNeg.setExDomain(exDomain);
        singleNeg.setRanDomain(ranDomain);

        multipleVals = new PyIntNode();
        exDomain = Arrays.asList(-2, -1, 0, 1, 2);
        ranDomain = Arrays.asList(-5, -4, -3, 3, 4, 5);
        multipleVals.setExDomain(exDomain);
        multipleVals.setRanDomain(ranDomain);
    }

    /**
     * Tests that getExDomain() works.
     */
    @Test
    @Tag("0.3")
    @Order(1)
    void testGetExDomain() {
        assertEquals(Collections.singletonList(17), singlePos.getExDomain());
    }

    /**
     * Tests that getRanDomain() works.
     */
    @Test
    @Tag("0.3")
    @Order(2)
    void testGetRanDomain() {
        assertEquals(Collections.singletonList(203), singlePos.getRanDomain());
    }

    /**
     * Tests that getLeftChild() returns null.
     */
    @Test
    @Tag("0.3")
    @Order(3)
    void testGetLeftChild() {
        assertNull(singlePos.getLeftChild());
    }

    /**
     * Tests that getRightChild() returns null.
     */
    @Test
    @Tag("0.3")
    @Order(4)
    void testGetRightChild() {
        assertNull(singlePos.getRightChild());
    }

    /**
     * Tests genExVals() in the case that the domain consists of a single positive
     * integer.
     */
    @Test
    @Tag("0.2")
    @Order(5)
    void testGenExValsPos() {
        Set<PyIntObj> actual = singlePos.genExVals();
        Set<PyIntObj> expected = Set.of(new PyIntObj(17));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the domain consists of a single negative
     * integer.
     */
    @Test
    @Tag("0.2")
    @Order(6)
    void testGenExValsNeg() {
        Set<PyIntObj> actual = singleNeg.genExVals();
        Set<PyIntObj> expected = Set.of(new PyIntObj(-3));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the domain consists of multiple integers.
     */
    @Test
    @Tag("1.0")
    @Order(7)
    void testGenExValsMultiple() {
        Set<PyIntObj> actual = multipleVals.genExVals();
        Set<PyIntObj> expected = new HashSet<>();
        for (int i = -2; i < 3; i++) {
            expected.add(new PyIntObj(i));
        }
        assertEquals(expected, actual);
    }

    /**
     * Tests genRandVal() in the case that the domain consists of a single positive
     * integer.
     */
    @Test
    @Tag("0.2")
    @Order(8)
    void testGenRandValPos() {
        Map<PyIntObj, Double> actual = buildDistribution(singlePos, 100);
        Map<PyIntObj, Double> expected = Map.of(new PyIntObj(203), 1.0);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that the domain consists of a single negative
     * integer.
     */
    @Test
    @Tag("0.2")
    @Order(9)
    void testGenRandValNeg() {
        Map<PyIntObj, Double> actual =
            buildDistribution(singleNeg, 100);
        Map<PyIntObj, Double> expected = Map.of(new PyIntObj(-18), 1.0);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that the domain consists of multiple integers.
     */
    @Test
    @Tag("1.0")
    @Order(10)
    void testGenRandValMultiple() {
        Map<PyIntObj, Double> actual =
            buildDistribution(multipleVals, 10000);
        Map<PyIntObj, Double> expected = new HashMap<>();
        for (int i : new int[]{-5, -4, -3, 3, 4, 5}) {
            expected.put(new PyIntObj(i), (1.0 / 6.0));
        }
        assertTrue(compareDistribution(expected, actual, 0.01));
    }
}