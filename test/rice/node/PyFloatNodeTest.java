package test.rice.node;

import main.rice.node.PyFloatNode;
import main.rice.obj.PyFloatObj;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyFloatNode class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyFloatNodeTest extends APyNodeTest {

    /**
     * A PyFloatNode whose domains include a single positive floating-point value (using
     * different integers for the exhaustive and random domains).
     */
    static PyFloatNode singlePos;

    /**
     * A PyFloatNode whose domains include a single positive floating-point value (using
     * different integers for the exhaustive and random domains).
     */
    static PyFloatNode singleNeg;

    /**
     * A PyFloatNode whose domains include multiple integer values (using different
     * integers for the exhaustive and random domains).
     */
    static PyFloatNode multipleVals;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        singlePos = new PyFloatNode();
        List<Double> exDomain = Collections.singletonList(17.654321);
        List<Double> ranDomain = Collections.singletonList(203.123456);
        singlePos.setExDomain(exDomain);
        singlePos.setRanDomain(ranDomain);

        singleNeg = new PyFloatNode();
        exDomain = Collections.singletonList(-3.3);
        ranDomain = Collections.singletonList(-18.18);
        singleNeg.setExDomain(exDomain);
        singleNeg.setRanDomain(ranDomain);

        multipleVals = new PyFloatNode();
        exDomain = Arrays.asList(-2.0, -1.0, 0.0, 1.0, 2.0);
        ranDomain = Arrays.asList(-5.0, -4.0, -3.0, 3.0, 4.0, 5.0);
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
        List<? extends Number> exDomain = singlePos.getExDomain();
        assert (exDomain.size() == 1 && exDomain.get(0).equals(17.654321));
    }

    /**
     * Tests that getRanDomain() works.
     */
    @Test
    @Tag("0.3")
    @Order(2)
    void testGetRanDomain() {
        List<? extends Number> ranDomain = singlePos.getRanDomain();
        assert (ranDomain.size() == 1 && ranDomain.get(0).equals(203.123456));
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
     * Tests genExVals() in the case that the domain consists of a single positive float.
     */
    @Test
    @Tag("0.2")
    @Order(5)
    void testGenExValsPos() {
        Set<PyFloatObj> actual = singlePos.genExVals();
        Set<PyFloatObj> expected = Set.of(new PyFloatObj(17.654321));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the domain consists of a single negative float.
     */
    @Test
    @Tag("0.2")
    @Order(6)
    void testGenExValsNeg() {
        Set<PyFloatObj> actual = singleNeg.genExVals();
        Set<PyFloatObj> expected = Set.of(new PyFloatObj(-3.3));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the domain consists of multiple floats.
     */
    @Test
    @Tag("1.0")
    @Order(7)
    void testGenExValsMultiple() {
        Set<PyFloatObj> actual = multipleVals.genExVals();
        Set<PyFloatObj> expected = new HashSet<>();
        for (double i = -2; i < 3; i++) {
            expected.add(new PyFloatObj(i));
        }
        assertEquals(expected, actual);
    }

    /**
     * Tests genRandVal() in the case that the domain consists of a single positive
     * float.
     */
    @Test
    @Tag("0.2")
    @Order(8)
    void testGenRandValPos() {
        Map<PyFloatObj, Double> actual =
            buildDistribution(singlePos, 100);
        Map<PyFloatObj, Double> expected = Map.of(new PyFloatObj(203.123456), 1.0);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that the domain consists of a single negative
     * float.
     */
    @Test
    @Tag("0.2")
    @Order(9)
    void testGenRandValNeg() {
        Map<PyFloatObj, Double> actual =
            buildDistribution(singleNeg, 100);
        Map<PyFloatObj, Double> expected = Map.of(new PyFloatObj(-18.18), 1.0);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that the domain consists of multiple floats.
     */
    @Test
    @Tag("1.0")
    @Order(10)
    void testGenRandValMultiple() {
        Map<PyFloatObj, Double> actual =
            buildDistribution(multipleVals, 100000);
        Map<PyFloatObj, Double> expected = new HashMap<>();
        for (double i : new double[]{-5.0, -4.0, -3.0, 3.0, 4.0, 5.0}) {
            expected.put(new PyFloatObj(i), (1.0 / 6.0));
        }
        assertTrue(compareDistribution(expected, actual, 0.01));
    }
}