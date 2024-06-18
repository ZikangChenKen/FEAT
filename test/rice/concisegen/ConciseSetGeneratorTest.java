package test.rice.concisegen;

import main.rice.concisegen.ConciseSetGenerator;
import main.rice.obj.APyObj;
import main.rice.obj.PyFloatObj;
import main.rice.obj.PyIntObj;
import main.rice.obj.PyStringObj;
import main.rice.test.TestCase;
import main.rice.test.TestResults;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for the ConciseSetGenerator class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConciseSetGeneratorTest {

    /**
     * Tests that setCover() does not mutate the input allCases.
     */
    @Test
    @Tag("1.0")
    @Order(1)
    void testDoesNotMutateAllCases() {
        List<TestCase> allCases = generateIntegerCases(10);
        Set<Integer> wrongSet = generateAllWrong(10);
        List<Set<Integer>> caseToFiles = generateSimpleCaseToFile(10);
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        ConciseSetGenerator.setCover(input);
        assertEquals(generateIntegerCases(10), allCases);
    }

    /**
     * Tests that setCover() does not mutate the input caseToFiles.
     */
    @Test
    @Tag("1.0")
    @Order(2)
    void testDoesNotMutateCaseToFiles() {
        List<TestCase> allCases = generateIntegerCases(10);
        Set<Integer> wrongSet = generateAllWrong(10);
        List<Set<Integer>> caseToFiles = generateSimpleCaseToFile(10);
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        ConciseSetGenerator.setCover(input);
        assertEquals(generateSimpleCaseToFile(10), caseToFiles);
    }

    /**
     * Tests that setCover() does not mutate the input wrongSet.
     */
    @Test
    @Tag("2.0")
    @Order(3)
    void testDoesNotMutateWrongSet() {
        List<TestCase> allCases = generateIntegerCases(10);
        Set<Integer> wrongSet = generateAllWrong(10);
        List<Set<Integer>> caseToFiles = generateSimpleCaseToFile(10);
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        ConciseSetGenerator.setCover(input);
        assertEquals(generateAllWrong(10), wrongSet);
    }

    /**
     * Tests setCover() in the case that all tests are needed to get coverage.
     */
    @Test
    @Tag("1.0")
    @Order(4)
    void testNeedAll() {
        // Generate ten integer test cases
        List<TestCase> allCases = generateIntegerCases(10);

        // Construct a situation where all cases in allCases are needed and check results
        this.testNeedAllHelper(allCases);
    }

    /**
     * Tests setCover() in the case that all tests are needed to get coverage, using
     * non-integer objects as the test cases to ensure that indices (not test case values)
     * are being used.
     */
    @Test
    @Tag("2.0")
    @Order(5)
    void testNotIntegersNeedAll() {
        // Generate ten string test cases
        List<TestCase> allCases = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<APyObj> elem =
                Collections.singletonList(new PyStringObj(String.valueOf((char) i)));
            allCases.add(new TestCase(elem));
        }

        // Construct a situation where all cases in allCases are needed and check results
        this.testNeedAllHelper(allCases);
    }

    /**
     * Tests setCover() in the case that only one test case is needed (i.e., all wrong
     * files failed on the given test) when all files are wrong.
     */
    @Test
    @Tag("1.0")
    @Order(6)
    void testNeedOneAllWrong() {
        // Every file is wrong
        Set<Integer> wrongSet = generateAllWrong(10);

        // Create ten integer test cases
        List<TestCase> allCases = generateIntegerCases(10);

        // Tests 0-8 each catch two files
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            caseToFiles.add(Set.of(i, i + 1));
        }

        // The last test case catches all files (and thus is the only that should be
        // chosen by setCover())
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            set.add(i);
        }
        caseToFiles.add(set);

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        List<APyObj> elem = Collections.singletonList(new PyIntObj(9));
        Set<TestCase> expected = Set.of(new TestCase(elem));
        assertEquals(expected, actual);
    }

    /**
     * Tests setCover() in the case that only one test case is needed (i.e., all wrong
     * files failed on the given test) when not all files are wrong.
     */
    @Test
    @Tag("1.0")
    @Order(7)
    void testNeedOneSomeWrong() {
        // Odd-numbered files are wrong
        Set<Integer> wrongSet = Set.of(1, 3, 5, 7, 9);

        // Create ten integer test cases
        List<TestCase> allCases = generateIntegerCases(10);

        // Tests 0-8 each catch one file
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (i % 2 == 0) {
                // Only odd files are wrong, so shift even tests' failed files by one
                caseToFiles.add(Set.of(i + 1));
            } else {
                caseToFiles.add(Set.of(i));
            }
        }

        // The last test case catches all wrong files (and thus is the only that should be
        // chosen by setCover())
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 != 0) {
                set.add(i);
            }
        }
        caseToFiles.add(set);

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        List<APyObj> elem = Collections.singletonList(new PyIntObj(9));
        Set<TestCase> expected = Set.of(new TestCase(elem));
        assertEquals(expected, actual);
    }

    /**
     * Tests setCover() in the case that each file fails one different test, but not all
     * tests are needed (number of tests exceeds number of files).
     */
    @Test
    @Tag("1.0")
    @Order(8)
    void testEachFileFailsOneTest() {
        // Every file is wrong
        Set<Integer> wrongSet = generateAllWrong(5);

        // Create ten integer test cases
        List<TestCase> allCases = generateIntegerCases(10);

        // Tests 0-8 each catch one file
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            if (i % 2 == 0) {
                // Even test catch the file at i/2
                caseToFiles.add(Set.of(i / 2));
            } else {
                // Odd tests don't catch anything
                caseToFiles.add(Set.of());
            }
        }

        // Expect even-numbered tests to be selected
        Set<TestCase> expected = new HashSet<>();
        for (int i = 0; i <= 9; i += 2) {
            List<APyObj> elem = Collections.singletonList(new PyIntObj(i));
            expected.add(new TestCase(elem));
        }

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        assertEquals(expected, actual);
    }

    /**
     * Tests that setCover() does the right thing when there is overlap between tests,
     * i.e. multiple tests cover the same files.
     */
    @Test
    @Tag("1.0")
    @Order(9)
    void testOverlap() {
        List<TestCase> allCases = generateIntegerCases(8);

        // Generate expected results, tailored to what will happen in testOverlapHelper
        Set<TestCase> expected = new HashSet<>();
        for (int i = 0; i < 8; i += 3) {
            expected.add(new TestCase(Collections.singletonList(new PyIntObj(i))));
        }

        testOverlapHelper(allCases, expected);
    }

    /**
     * Tests that setCover() does the right thing when there is overlap between tests,
     * i.e. multiple tests cover the same files. Uses non-integer test cases to make sure
     * indices are being used rather than test case values.
     */
    @Test
    @Tag("2.0")
    @Order(10)
    void testNotIntegersOverlap() {
        // Generate eight unconventional cases
        List<TestCase> allCases = new ArrayList<>();
        for (int i = 17; i < 25; i++) {
            List<APyObj> elem = new ArrayList<>();
            elem.add(new PyStringObj(String.valueOf((char) i)));
            elem.add(new PyFloatObj(i + 0.5));
            allCases.add(new TestCase(elem));
        }

        // Generate expected results, tailored to what will happen in testOverlapHelper
        Set<TestCase> expected = new HashSet<>();
        for (int i = 0; i < 8; i += 3) {
            List<APyObj> elem = new ArrayList<>();
            elem.add(new PyStringObj(String.valueOf((char) (17 + i))));
            elem.add(new PyFloatObj(17 + i + 0.5));
            expected.add(new TestCase(elem));
        }

        testOverlapHelper(allCases, expected);
    }

    /**
     * Tests that setCover() does the "right" thing (executes the greedy algorithm
     * discussed in class) even when that is sub-optimal.
     */
    @Test
    @Tag("2.0")
    @Order(11)
    void testGreedyNotOptimal() {
        // Ten files; each one is wrong
        Set<Integer> wrongSet = generateAllWrong(10);

        // Create six integer test cases
        List<TestCase> allCases = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            allCases.add(new TestCase(Collections.singletonList(new PyIntObj(i))));
        }

        // The i-th case catches file i and file (i + 5)
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            caseToFiles.add(Set.of(i, i + 5));
        }

        // The 5th case catches files 0-4, so it will get picked first. However,
        // cases 0-4 will be needed to catch files 5-9, and would have collectively
        // also caught files 0-4, so the 5th case is actually unnecessary (and therefore
        // the greedy algorithm is sub-optimal).
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            set.add(i);
        }
        caseToFiles.add(set);

        // Greedy algorithm will require *every* test case
        Set<TestCase> expected = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            expected.add(new TestCase(Collections.singletonList(new PyIntObj(i))));
        }

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        assertEquals(expected, actual);
    }

    /**
     * Helper function which generates an allCases list containing integers from 0 to
     * numTests - 1, inclusive.
     *
     * @param numTests the number of tests to generate
     * @return a list of test cases, each consisting of a single integer
     */
    private static List<TestCase> generateIntegerCases(int numTests) {
        List<TestCase> allCases = new ArrayList<>();
        for (int i = 0; i < numTests; i++) {
            List<APyObj> elem = Collections.singletonList(new PyIntObj(i));
            allCases.add(new TestCase(elem));
        }
        return allCases;
    }

    /**
     * Helper function which generates a wrongSet containing all numbers from 0 to
     * numFiles.
     *
     * @param numFiles the number of files that were tested
     * @return the set of each integer from 0 to numFiles - 1, inclusive
     */
    private static Set<Integer> generateAllWrong(int numFiles) {
        Set<Integer> wrongSet = new HashSet<>();
        for (int i = 0; i < numFiles; i++) {
            wrongSet.add(i);
        }
        return wrongSet;
    }

    /**
     * Helper function which generates a simple caseToFile list mapping the i-th test case
     * to the set containing the i-th file.
     *
     * @param numTests number of test cases
     * @return simple caseToFile list that was generated
     */
    private static List<Set<Integer>> generateSimpleCaseToFile(int numTests) {
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < numTests; i++) {
            Set<Integer> set = new HashSet<>();
            set.add(i);
            caseToFiles.add(set);
        }
        return caseToFiles;
    }

    /**
     * Helper function for testing the case where all tests in allCases are needed for
     * coverage.
     *
     * @param allCases the test cases
     */
    private static void testNeedAllHelper(List<TestCase> allCases) {
        // Every file is wrong
        Set<Integer> wrongSet = generateAllWrong(10);

        // Generate expected results: need every test case
        Set<TestCase> expected = new HashSet<>(allCases);

        // Ten files: each file fails a different test
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 9; i > -1; i--) {
            caseToFiles.add(Set.of(i));
        }

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        assertEquals(expected, actual);
    }

    /**
     * Helper function for testing the case where there is overlap between the test cases
     * (one case catches a subset of the files caught by another case).
     *
     * @param allCases the test cases
     * @param expected the expected results
     */
    private static void testOverlapHelper(List<TestCase> allCases, Set<TestCase> expected) {
        // Generate caseToFiles list that looks somewhat random, but is carefully
        // constructed to include sets of varying size and moderate overlap between sets.
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Set<Integer> set = new HashSet<>();
            set.add(i);

            if (i % 2 == 0) {
                set.add(i + 1);
                set.add(i * 2);
            }
            if (i % 3 == 0) {
                set.add(i + 1);
                set.add(i + 2);
                set.add(i * 3);
            }
            caseToFiles.add(set);
        }
        caseToFiles.get(2).add(1);
        caseToFiles.get(3).add(13);
        caseToFiles.get(6).add(4);

        // Make sure the wrongSet includes everything that appeared in caseToFiles above
        Set<Integer> wrongSet = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            wrongSet.add(i);
            if (i % 2 == 0) {
                wrongSet.add(i * 2);
            }
            if (i % 3 == 0) {
                wrongSet.add(i * 3);
            }
        }
        wrongSet.add(13);

        // Compute actual results and compare to expected
        TestResults input = new TestResults(allCases, caseToFiles, wrongSet);
        Set<TestCase> actual = ConciseSetGenerator.setCover(input);
        assertEquals(expected, actual);
    }
}
