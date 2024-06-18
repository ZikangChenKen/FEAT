package test.rice;

import main.rice.Main;
import main.rice.obj.*;
import main.rice.parse.InvalidConfigException;
import main.rice.test.TestCase;
import org.junit.jupiter.api.*;
import test.rice.node.APyNodeTest;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the Main class.
 */
public class MainTest {

    /**
     * The absolute path to this project directory, which we'll use to find the provided
     * pyfils.
     */
    private static final String userDir = System.getProperty("user.dir");

    /**
     * Tests the situation where all files are correct.
     */
    @Test
    void testAllFilesCorrect() {
        String[] args = buildArgs("func0", "func0simple", "f0multipleRight");
        Set<TestCase> expected = Collections.emptySet();
        mainTestHelper(args, expected);
    }

    /**
     * Tests the situation where the config file only specifies a single test case, and
     * that test catches the wrong programs.
     */
    @Test
    void testOnlyOneCase() {
        String[] args = buildArgs("func0", "func0oneTest", "f0multipleWrong");
        Set<TestCase> expected = Collections
                .singleton(new TestCase(Collections.singletonList(new PyIntObj(0))));
        mainTestHelper(args, expected);
    }

    /**
     * Tests the situation where the config file specifies multiple test cases, a subset
     * (of size > 1) of which is required to catch all wrong programs. Can be satisfied
     * only with the exhaustive set.
     */
    @Test
    void testMultipleCasesNonDeterministic() {
        String[] args = buildArgs("func0", "func0simple", "f0multipleMixed");

        // Should select one even-numbered test and one odd-numbered test
        Set<Set<TestCase>> expectedOptions = new HashSet<>();
        for (int i = 0; i < 10; i += 2) {
            for (int j = 1; j < 10; j += 2) {
                expectedOptions
                        .add(Set.of(new TestCase(Collections.singletonList(new PyIntObj(i))),
                                new TestCase(Collections.singletonList(new PyIntObj(j)))));
            }
        }
        mainTestMultipleOptionsHelper(args, expectedOptions);
    }

    /**
     * Tests the situation where the config file specifies multiple test cases, a subset *
     * (of size > 1) of which is required to catch all wrong programs. Requires that both
     * exhaustive and random sets be generated.
     */
    @Test
    void testMultipleCasesDeterministic() {
        String[] args = buildArgs("func0", "func0simple", "f0multipleMixedDeterministic");
        Set<TestCase> expected = Set.of(new TestCase(Collections.singletonList(
                new PyIntObj(2))), new TestCase(Collections.singletonList(new PyIntObj(7))));
        mainTestHelper(args, expected);
    }

    /**
     * Tests the situation where the config file specifies multiple test cases, a subset
     * (of size > 1) of which is required to catch all wrong programs. Requires that both
     * exhaustive and random sets be generated, and checks randomness.
     */
    @Test
    void testMultipleCasesRandomness() {
        String[] args = buildArgs("func0", "func0random", "f0multipleMixedReqRandom");
        Map<Set<TestCase>, Double> expectedOptions = new HashMap<>();

        // We need 1 and 3 plus whatever the random test was (an integer between 5 and
        // 9, inclusive on both bounds, with an equal chance of each occurring)
        for (int i = 5; i < 10; i++) {
            Set<TestCase> option = new HashSet<>();
            option.add(new TestCase(Collections.singletonList(new PyIntObj(1))));
            option.add(new TestCase(Collections.singletonList(new PyIntObj(3))));
            option.add(new TestCase(Collections.singletonList(new PyIntObj(i))));
            expectedOptions.put(option, 1.0 / 5.0);
        }

        // Run a bunch of trials and make sure the distribution of results is reasonable
        try {
            Map<Set<TestCase>, Double> actual = buildDistribution(args, 100);
            assertTrue(APyNodeTest.compareDistribution(expectedOptions, actual, 0.1));
        } catch (Exception e) {
            assert false;
        }
    }

    /**
     * Tests the situation where the config file specifies multiple test cases, a subset *
     * (of size > 1) of which is required to catch all wrong programs. Requires that both
     * exhaustive and random sets be generated. Tests a function that requires multiple
     * arguments.
     */
    @Test
    void testMultipleCasesComplex() {
        String[] args = buildArgs("func3", "func3simple", "f3multipleMixed");
        Set<Set<TestCase>> expectedOptions = new HashSet<>();

        PySetObj<PyIntObj> set1 = new PySetObj<>(Set.of());
        PySetObj<PyIntObj> set2 = new PySetObj<>(Set.of(new PyIntObj(1)));

        // Create two possible list(int)s
        PyListObj<PyIntObj> list1 =
                new PyListObj<>(Collections.singletonList(new PyIntObj(2)));
        PyListObj<PyIntObj> list2 =
                new PyListObj<>(Arrays.asList(new PyIntObj(2), new PyIntObj(2)));

        // Create two possible tuple(int)s
        PyTupleObj<PyIntObj> tup1 =
                new PyTupleObj<>(Collections.singletonList(new PyIntObj(3)));
        PyTupleObj<PyIntObj> tup2 =
                new PyTupleObj<>(Arrays.asList(new PyIntObj(3), new PyIntObj(3)));

        // Create four possible concise set results
        // Requires set(), [2], (3,) AND
        // {1} combined with any valid values for the list and tup
        for (PyListObj<?> list : new PyListObj[]{list1, list2}) {
            for (PyTupleObj<?> tup : new PyTupleObj[]{tup1, tup2}) {
                Set<TestCase> option = new HashSet<>();
                option.add(new TestCase(Arrays.asList(set1, list1, tup1)));
                option.add(new TestCase(Arrays.asList(set2, list, tup)));
                expectedOptions.add(option);
            }
        }
        mainTestMultipleOptionsHelper(args, expectedOptions);
    }

    /**
     * Helper function for building the array of args for Main.main() by adding absolute
     * path information.
     *
     * @param funcName   the name of the function under test
     * @param configName the name of the config file to be used
     * @param implDir    the name of the implementation directory, which should be found
     *                   in the test.rice.test.pyfiles directory
     * @return an array of args encapsulating the input information
     */
    private static String[] buildArgs(String funcName, String configName, String implDir) {
        // Build config file path
        String configFilePath = userDir + "/src/test/rice/parse/config/"
                + configName + ".json";

        // Build implDir path
        String implDirPath = userDir + "/src/test/rice/test/pyfiles/" + implDir;

        // Build solution path
        String solutionPath = userDir + "/src/test/rice/test/pyfiles/sols/" + funcName
                + "sol.py";
        return new String[]{configFilePath, implDirPath, solutionPath};
    }

    /**
     * Helper function for running a test of Main.generateTests(); returns the actual
     * results.
     *
     * @param args args to pass to Main.generateTests()
     * @return actual results of running Main.generateTests()
     */
    private static Set<TestCase> runMain(String[] args) {
        Set<TestCase> actual = null;
        try {
            // Build concise test set
            actual = Main.generateTests(args);
        } catch (Exception e) {
            // Check for exceptions
            e.printStackTrace();
            fail();
        }
        // Return concise test set
        return actual;
    }

    /**
     * Helper function for running a test of Main.main() with the input args
     * and comparing the actual to expected output.
     *
     * @param args     args to pass to Main.main();
     * @param expected expected result (concise test set)
     */
    private static void mainTestHelper(String[] args, Set<TestCase> expected) {
        Set<TestCase> actual = runMain(args);
        assertEquals(expected, actual);
    }

    /**
     * Helper function for running a test of Main.generateTests() with the input args
     * and comparing the actual to expected results when there are multiple possible
     * correct outcomes.
     *
     * @param args     args to pass to Main.generateTests();
     * @param expected set of acceptable expected results
     */
    private static void mainTestMultipleOptionsHelper(String[] args,
                                               Set<Set<TestCase>> expected) {
        Set<TestCase> actual = runMain(args);
        assert expected.contains(actual);
    }

    /**
     * Helper function for testing Main.generateTests() that runs the input number of
     * trials and builds a distribution of the results.
     *
     * @param args the arguments to run Main.generateTests() with
     * @param numTrials the number of trials to be run
     * @return the distribution of results, in the form of a mapping of each generated
     * test set to its relative frequency of generation
     */
    private static Map<Set<TestCase>, Double> buildDistribution(String[] args, int numTrials)
            throws IOException, InvalidConfigException, InterruptedException {

        // Build raw frequency counts
        Map<Set<TestCase>, Double> actual = new HashMap<>();
        for (int i = 0; i < numTrials; i++) {
            Set<TestCase> trialResult = Main.generateTests(args);
            if (!actual.containsKey(trialResult)) {
                actual.put(trialResult, 1.0);
            } else {
                actual.put(trialResult, actual.get(trialResult) + 1);
            }
        }

        // Normalize frequency counts
        actual.replaceAll((k, v) -> v / numTrials);

        // Return the distribution
        return actual;
    }
}