package test.rice.test;

import main.rice.obj.PyBoolObj;
import main.rice.obj.PyIntObj;
import main.rice.obj.PyStringObj;
import main.rice.test.TestCase;
import main.rice.test.TestResults;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test cases for the TestResults class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestResultsTest {

    /**
     * A list of test cases, to be shared across the different tests.
     */
    private static List<TestCase> testCases;

    /**
     * A TestResults object for the case where there are no tests.
     */
    private static TestResults noTests;

    /**
     * A TestResults object (and its caseToFiles list) for the case where all files pass
     * all tests.
     */
    private static TestResults allPass;
    private static List<Set<Integer>> allFilesPass;

    /**
     * A TestResults object (and its caseToFiles list) for the case where some files fail
     * some tests.
     */
    private static TestResults someFail;
    private static List<Set<Integer>> someFilesFail;
    private static Set<Integer> wrongSet;

    /**
     * Sets up all of the static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Set up a trivial TestResults for the situation where there are no tests
        noTests = new TestResults(List.of(), List.of(), Set.of());

        // Set up 10 test cases, to be shared by allPass and someFail
        testCases = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testCases.add(new TestCase(List.of(new PyBoolObj(true), new PyIntObj(i),
                    new PyStringObj(String.valueOf((char) i)))));
        }

        // Create a caseToFiles list for the case where all files are correct
        allFilesPass = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // Each case failed no files
            allFilesPass.add(new HashSet<>());
        }
        allPass = new TestResults(testCases, allFilesPass, Set.of());

        // Create a wrongSet and caseToFiles list for the case where some files are wrong
        wrongSet = new HashSet<>();
        someFilesFail = new ArrayList<>();
        Set<Integer> failedSet;
        for (int i = 0; i < 10; i++) {
            // Let odd numbered files be wrong
            failedSet = new HashSet<>();
            if (i % 2 != 0) {
                wrongSet.add(i);
            } else {
                // Let even case i catch odd-numbered files of index >= i
                for (int j = i + 1; j < 10; j += 2) {
                    failedSet.add(j);
                }
            }
            someFilesFail.add(failedSet);
        }
        someFail = new TestResults(testCases, someFilesFail, wrongSet);
    }

    /**
     * Tests getTestCase() using a valid index.
     */
    @Test
    @Tag("0.2")
    @Order(1)
    void testGetTestCaseValid() {
        assertEquals(testCases.get(3), allPass.getTestCase(3));
    }

    /**
     * Tests getTestCase() using an index of 0.
     */
    @Test
    @Tag("0.5")
    @Order(2)
    void testGetTestCaseZero() {
        assertEquals(testCases.get(0), allPass.getTestCase(0));
    }

    /**
     * Tests getTestCase() using a negative index; should return null.
     */
    @Test
    @Tag("0.4")
    @Order(3)
    void testGetTestCaseOutOfBoundsNeg() {
        assertNull(someFail.getTestCase(-1));
    }

    /**
     * Tests getTestCase() using an index that's out of bounds; should return null.
     */
    @Test
    @Tag("0.4")
    @Order(4)
    void testGetTestCaseOutOfBoundsPos() {
        assertNull(someFail.getTestCase(10));
    }

    /**
     * Tests getWrongSet() when the wrong set is empty.
     */
    @Test
    @Tag("0.1")
    @Order(5)
    void testGetWrongSetEmpty() {
        assertEquals(new HashSet<>(), allPass.getWrongSet());
    }

    /**
     * Tests getWrongSet() when the wrong set is non-empty.
     */
    @Test
    @Tag("0.1")
    @Order(6)
    void testGetWrongSetNonEmpty() {
        assertEquals(new HashSet<>(wrongSet), someFail.getWrongSet());
    }

    /**
     * Tests getCaseToFiles() when there are no tests.
     */
    @Test
    @Tag("0.1")
    @Order(7)
    void testGetCaseToFilesEmpty() {
        assertEquals(List.of(), noTests.getCaseToFiles());
    }

    /**
     * Tests getCaseToFiles() when all files pass all tests.
     */
    @Test
    @Tag("0.1")
    @Order(8)
    void testGetCaseToFilesAllPass() {
        assertEquals(new ArrayList<>(allFilesPass), allPass.getCaseToFiles());
    }

    /**
     * Tests getCaseToFiles() when some files fail some tests.
     */
    @Test
    @Tag("0.1")
    @Order(9)
    void testGetCaseToFilesNonEmpty() {
        assertEquals(new ArrayList<>(someFilesFail), someFail.getCaseToFiles());
    }
}