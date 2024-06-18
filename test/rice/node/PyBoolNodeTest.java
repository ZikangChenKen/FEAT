package test.rice.node;

import main.rice.node.PyBoolNode;
import main.rice.obj.PyBoolObj;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyBoolNode class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyBoolNodeTest extends APyNodeTest {

    /**
     * A bool node whose domains include both True and False.
     */
    private static PyBoolNode both;

    /**
     * A bool node who has disjoint exhaustive and random domains.
     */
    private static PyBoolNode oppositeDomains;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Set up a bool node whose domains include both True and False
        both = new PyBoolNode();
        List<Integer> exDomain = List.of(0, 1);
        both.setExDomain(exDomain);
        both.setRanDomain(exDomain);

        // Set up a bool node who has disjoint exhaustive and random domains
        oppositeDomains = new PyBoolNode();
        oppositeDomains.setExDomain(Collections.singletonList(1));
        oppositeDomains.setRanDomain(Collections.singletonList(0));
    }

    /**
     * Tests that getExDomain() works.
     */
    @Test
    @Tag("0.2")
    @Order(1)
    void testGetExDomain() {
        assertEquals(Arrays.asList(0, 1), both.getExDomain());
    }

    /**
     * Tests that getRanDomain() works.
     */
    @Test
    @Tag("0.2")
    @Order(2)
    void testGetRanDomain() {
        assertEquals(Arrays.asList(0, 1), both.getRanDomain());
    }

    /**
     * Tests that getLeftChild() returns null.
     */
    @Test
    @Tag("0.3")
    @Order(3)
    void testGetLeftChild() {
        assertNull(both.getLeftChild());
    }

    /**
     * Tests that getRightChild() returns null.
     */
    @Test
    @Tag("0.3")
    @Order(4)
    void testGetRightChild() {
        assertNull(both.getRightChild());
    }

    /**
     * Tests genExVals() in the case that the exhaustive domain is {True, False}.
     */
    @Test
    @Tag("1.0")
    @Order(5)
    void testGenExValsBoth() {
        Set<PyBoolObj> actual = both.genExVals();
        Set<PyBoolObj> expected = Set.of(new PyBoolObj(true),
            new PyBoolObj(false));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the exhaustive domain is different from the
     * random domain (to make sure that the correct domain is used).
     */
    @Test
    @Tag("0.5")
    @Order(6)
    void testGenExValsOpposite() {
        Set<PyBoolObj> actual = oppositeDomains.genExVals();
        Set<PyBoolObj> expected = Set.of(new PyBoolObj(true));
        assertEquals(expected, actual);
    }

    /**
     * Tests genRandVal() in the case that the random domain is {True, False}.
     */
    @Test
    @Tag("1.0")
    @Order(7)
    void testGenRandValBoth() {
        Map<PyBoolObj, Double> actual = buildDistribution(both, 100000);
        Map<PyBoolObj, Double> expected = Map.of(new PyBoolObj(true), 0.5,
            new PyBoolObj(false), 0.5);
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that the exhaustive domain is different from the
     * random domain (to make sure that the correct domain is used).
     */
    @Test
    @Tag("0.5")
    @Order(8)
    void testGenRandValOpposite() {
        Map<PyBoolObj, Double> actual = buildDistribution(oppositeDomains, 100);
        Map<PyBoolObj, Double> expected = Map.of(new PyBoolObj(false), 1.0,
            new PyBoolObj(true), 0.0);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }
}