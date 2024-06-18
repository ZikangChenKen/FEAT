package test.rice.obj;

import main.rice.obj.PyIntObj;
import org.junit.jupiter.api.*;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyIntObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyIntObjTest {

    /**
     * A PyIntObj representing the Python integer value 1.
     */
    private static final PyIntObj posInt = new PyIntObj(1);

    /**
     * A PyIntObjs representing the Python integer value -1.
     */
    private static final PyIntObj negInt = new PyIntObj(-1);

    /**
     * Two distinct PyIntObjs representing the Python integer value 123456789.
     */
    private static final PyIntObj largeInt = new PyIntObj(123456789);
    private static final PyIntObj largeInt2 = new PyIntObj(123456789);

    /**
     * A random integer (between 0 and Integer.MAX_VALUE), and a PyIntObj representing
     * that value.
     */
    private static final int randVal = new Random().nextInt(Integer.MAX_VALUE);
    private static final PyIntObj randInt = new PyIntObj(randVal);

    /**
     * Tests that getValue() returns the expected result for a PyIntObj constructed with a
     * positive integer value.
     */
    @Test
    @Tag("0.2")
    @Order(0)
    void testGetValuePositive() {
        assertEquals(1, posInt.getValue());
    }

    /**
     * Tests that getValue() returns the expected result for a PyIntObj constructed with a
     * negative integer value.
     */
    @Test
    @Tag("0.2")
    @Order(1)
    void testGetValueNegative() {
        assertEquals(-1, negInt.getValue());
    }

    /**
     * Tests that getValue() returns the expected result for a PyIntObj constructed with a
     * random integer value.
     */
    @Test
    @Tag("0.2")
    @Order(2)
    void testGetValueRandom() {
        assertEquals(randVal, randInt.getValue());
    }

    /**
     * Tests that toString() returns the expected result for a PyIntObj constructed with a
     * positive integer value.
     */
    @Test
    @Tag("0.2")
    @Order(3)
    void testToStringPositive() {
        assertEquals("1", posInt.toString());
    }

    /**
     * Tests that toString() returns the expected result for a PyIntObj constructed with a
     * negative integer value.
     */
    @Test
    @Tag("0.2")
    @Order(4)
    void testToStringNegative() {
        assertEquals("-1", negInt.toString());
    }

    /**
     * Tests that toString() returns the expected result for a PyIntObj constructed with a
     * large positive integer value.
     */
    @Test
    @Tag("0.2")
    @Order(5)
    void testToStringLarge() {
        assertEquals("123456789", largeInt.toString());
    }

    /**
     * Tests that two distinct PyIntObjs constructed with the same integer value are
     * considered equivalent by equals().
     */
    @Test
    @Tag("1.0")
    @Order(6)
    void testEqualsSimple() {
        assertEquals(largeInt, largeInt2);
    }

    /**
     * Tests that two distinct PyIntObjs constructed with different values are not
     * considered equivalent by equals().
     */
    @Test
    @Tag("0.5")
    @Order(7)
    void testNotEqual() {
        assertNotEquals(posInt, largeInt);
    }

    /**
     * Tests that a PyIntObj is not considered to be equivalent to a non-APyObj.
     */
    @Test
    @Tag("1.0")
    @Order(8)
    void testNotEqualNonAPyObj() {
        assertNotEquals(posInt, 1);
    }

    /**
     * Tests that two distinct PyIntObjs constructed with the same integer value return
     * the same value for hashCode().
     */
    @Test
    @Tag("0.3")
    @Order(9)
    void testHashCodeSimple() {
        assertEquals(largeInt.hashCode(), largeInt2.hashCode());
    }

    /**
     * Tests that two distinct PyIntObjs constructed with the different values return
     * different values for hashCode().
     */
    @Test
    @Tag("0.5")
    @Order(10)
    void testHashCodeNotEqual() {
        assertNotEquals(posInt.hashCode(), largeInt.hashCode());
    }
}