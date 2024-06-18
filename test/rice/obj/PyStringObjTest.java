package test.rice.obj;

import main.rice.obj.PyCharObj;
import main.rice.obj.PyStringObj;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyStringObj class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyStringObjTest {

    /**
     * An empty PyStringObj.
     */
    private static PyStringObj emptyStr;

    /**
     * Two identical PyStringObjs comprised of a single character.
     */
    private static PyStringObj singleCharStr;
    private static PyStringObj singleCharStr2;

    /**
     * Two identical PyStringObjs comprised of multiple characters.
     */
    private static PyStringObj multiCharStr;
    private static PyStringObj multiCharStr2;
    private static List<PyCharObj> multiCharVal;

    /**
     * A PyStringObj containing special characters.
     */
    private static PyStringObj specialCharStr;

    /**
     * Sets up all static fields for use in test cases.
     */
    @BeforeAll
    static void setUp() {
        // Set up emptyCharStr
        emptyStr = new PyStringObj(Collections.emptyList());

        // Set up singleCharStr using constructor that takes a String
        singleCharStr = new PyStringObj("a");

        List<PyCharObj> singleCharVal2 = Collections.singletonList(new PyCharObj('a'));
        singleCharStr2 = new PyStringObj(singleCharVal2);

        // Set up multiCharStr
        multiCharVal = Arrays.asList(new PyCharObj('a'), new PyCharObj('b'),
            new PyCharObj('c'));
        multiCharStr = new PyStringObj(multiCharVal);
        multiCharStr2 = new PyStringObj("abc");

        // Set up specialCharStr
        String string5 = "* HeL10 w0r1d !";
        List<PyCharObj> specialCharVal = new ArrayList<>();
        for (int idx = 0; idx < string5.length(); idx++) {
            specialCharVal.add(new PyCharObj(string5.charAt(idx)));
        }
        specialCharStr = new PyStringObj(specialCharVal);
    }

    /**
     * Tests getValue() on a multi-character string.
     */
    @Test
    @Tag("0.5")
    @Order(1)
    void testGetValue() {
        assertEquals(new ArrayList<>(multiCharVal), multiCharStr.getValue());
    }

    /**
     * Tests toString() on the empty string.
     */
    @Test
    @Tag("0.5")
    @Order(2)
    void testToStringEmpty() {
        assertEquals("''", emptyStr.toString());
    }

    /**
     * Tests toString() on a single character string.
     */
    @Test
    @Tag("0.5")
    @Order(3)
    void testToStringSingle() {
        assertEquals("'a'", singleCharStr.toString());
    }

    /**
     * Tests toString() on a multi-character string.
     */
    @Test
    @Tag("0.5")
    @Order(4)
    void testToStringMultiple() {
        assertEquals("'abc'", multiCharStr2.toString());
    }

    /**
     * Tests toString() on a multi-character string containing special characters.
     */
    @Test
    @Tag("0.5")
    @Order(5)
    void testToStringSpecialChars() {
        assertEquals("'* HeL10 w0r1d !'", specialCharStr.toString());
    }

    /**
     * Tests equals() on two single-character strings that don't share references to the
     * same underlying list.
     */
    @Test
    @Tag("0.5")
    @Order(6)
    void testEqualsSingle() {
        assertEquals(singleCharStr, singleCharStr2);
    }

    /**
     * Tests equals() on two multi-character strings that don't share references to the
     * same underlying list.
     */
    @Test
    @Tag("0.5")
    @Order(7)
    void testEqualsMultiple() {
        assertEquals(multiCharStr2, multiCharStr);
    }

    /**
     * Tests equals() on two strings that are not equal.
     */
    @Test
    @Tag("0.5")
    @Order(8)
    void testNotEqual() {
        assertNotEquals(singleCharStr, specialCharStr);
    }

    /**
     * Tests that equals() returns false for two strings whose underlying character lists
     * are not equivalent, even when one is a subset of the other.
     */
    @Test
    @Tag("0.3")
    @Order(9)
    void testNotEqualSubset1() {
        assertNotEquals(singleCharStr, multiCharStr);
    }

    /**
     * Tests that equals() returns false for two strings whose underlying character lists
     * are not equivalent, even when one is a subset of the other (in the opposite
     * direction of testEqualsSubset1).
     */
    @Test
    @Tag("0.3")
    @Order(10)
    void testNotEqualSubset2() {
        assertNotEquals(multiCharStr, singleCharStr);
    }

    /**
     * Tests that a PyCharObj is not considered to be equivalent to a non-APyObj.
     */
    @Test
    @Tag("0.4")
    @Order(11)
    void testNotEqualNonAPyObj() {
        assertNotEquals(multiCharStr2, "abc");
    }

    /**
     * Tests that hashCode() returns the same value for two multi-character strings that
     * have equivalent underlying character lists.
     */
    @Test
    @Tag("0.3")
    @Order(12)
    void testHashCodeEqual() {
        assertEquals(multiCharStr.hashCode(), multiCharStr2.hashCode());
    }

    /**
     * Tests that hashCode() returns different values for two strings comprised of different characters.
     */
    @Test
    @Tag("0.2")
    @Order(13)
    void testHashCodeNotEqual() {
        assertNotEquals(multiCharStr.hashCode(), specialCharStr.hashCode());
    }
}