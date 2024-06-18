package test.rice.basegen;

import main.rice.basegen.BaseSetGenerator;
import main.rice.node.*;
import main.rice.obj.*;
import main.rice.test.TestCase;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the BaseSetGenerator class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BaseSetGeneratorTest {

    /**
     * Nodes for generating one argument with one possible option.
     */
    protected static List<APyNode<?>> oneArgOneOption;
    protected static Set<TestCase> oneArgOneOptionExVals;
    protected static Set<TestCase> oneArgOneOptionRandVals;

    /**
     * Nodes for generating one simple (non-compound) argument with multiple possible
     * options.
     */
    protected static List<APyNode<?>> oneArgSimple;
    protected static Set<TestCase> oneArgSimpleExVals;
    protected static Set<TestCase> oneArgSimpleRandVals;

    /**
     * Nodes for generating one nested argument with multiple possible options.
     */
    protected static List<APyNode<?>> oneArgNested;
    protected static Set<TestCase> oneArgNestedExVals;
    protected static Set<TestCase> oneArgNestedRandVals;

    /**
     * Nodes for generating multiple arguments with one option each.
     */
    protected static List<APyNode<?>> multipleArgsOneOption;
    protected static Set<TestCase> multipleArgsOneOptionExVals;
    protected static Set<TestCase> multipleArgsOneOptionRandVals;

    /**
     * Nodes for generating multiple simple arguments with multiple options each.
     */
    protected static List<APyNode<?>> multipleArgsSimple;
    protected static Set<TestCase> multipleArgsSimpleExVals;
    protected static Set<TestCase> multipleArgsSimpleRandVals;

    /**
     * Nodes for generating multiple nested arguments with multiple options each.
     */
    protected static List<APyNode<?>> multipleArgsNested;
    protected static Set<TestCase> multipleArgsNestedExVals;
    protected static Set<TestCase> multipleArgsNestedRandVals;

    /**
     * Nodes for generating multiple arguments where there are only two options.
     */
    protected static List<APyNode<?>> multipleArgsTwoOptions;
    protected static Set<TestCase> multipleArgsTwoOptionsExVals;
    protected static Set<TestCase> multipleArgsTwoOptionsRandVals;

    /**
     * Nodes for generating one argument, with overlap between the two domains.
     */
    protected static List<APyNode<?>> oneArgSimpleOverlap;
    protected static Set<TestCase> oneArgSimpleOverlapExVals;
    protected static Set<TestCase> oneArgSimpleOverlapRandVals;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        setUpOneArgOneOption();
        setUpOneArgSimple();
        setUpOneArgNested();
        setUpOneArgSimpleOverlap();
        setUpMultipleArgsOneOption();
        setUpMultipleArgsTwoOptions();
        setUpMultipleArgsSimple();
        setUpMultipleArgsNested();
    }

    /**
     * Tests exhaustive generation when there is only one argument with one option.
     */
    @Test
    @Tag("0.2")
    @Order(0)
    void testExOneArgOneOption() {
        BaseSetGenerator generator = new BaseSetGenerator(oneArgOneOption, 0);
        assertEquals(new HashSet<>(oneArgOneOptionExVals),
                new HashSet<>(generator.genExTests()));
    }

    /**
     * Tests exhaustive generation when there is only one simple argument (with multiple
     * options).
     */
    @Test
    @Tag("0.2")
    @Order(1)
    void testExOneArgSimple() {
        BaseSetGenerator generator = new BaseSetGenerator(oneArgSimple, 0);
        assertEquals(new HashSet<>(oneArgSimpleExVals),
                new HashSet<>(generator.genExTests()));
    }

    /**
     * Tests exhaustive generation when there is one nested argument (with multiple
     * options).
     */
    @Test
    @Tag("0.2")
    @Order(2)
    void testExOneArgNested() {
        BaseSetGenerator generator = new BaseSetGenerator(oneArgNested, 0);
        assertEquals(new HashSet<>(oneArgNestedExVals),
                new HashSet<>(generator.genExTests()));
    }

    /**
     * Tests exhaustive generation when there are multiple arguments, each with only one
     * option.
     */
    @Test
    @Tag("0.5")
    @Order(3)
    void testExMultipleArgsOneOptionEach() {
        BaseSetGenerator generator = new BaseSetGenerator(multipleArgsOneOption, 0);
        assertEquals(new HashSet<>(multipleArgsOneOptionExVals),
                new HashSet<>(generator.genExTests()));
    }

    /**
     * Tests exhaustive generation when there are multiple simple arguments (with multiple
     * options).
     */
    @Test
    @Tag("1.0")
    @Order(4)
    void testExMultipleArgsSimple() {
        BaseSetGenerator generator = new BaseSetGenerator(multipleArgsSimple, 0);
        assertEquals(new HashSet<>(multipleArgsSimpleExVals),
                new HashSet<>(generator.genExTests()));
    }

    /**
     * Tests exhaustive generation when there are multiple nested arguments (with multiple
     * options).
     */
    @Test
    @Tag("1.0")
    @Order(5)
    void testExMultipleArgsNested() {
        BaseSetGenerator generator = new BaseSetGenerator(multipleArgsNested, 0);
        assertEquals(new HashSet<>(multipleArgsNestedExVals),
                new HashSet<>(generator.genExTests()));
    }

    /**
     * Tests the case where no tests are requested.
     */
    @Test
    @Tag("0.2")
    @Order(6)
    void testRandZeroTests() {
        BaseSetGenerator generator = new BaseSetGenerator(oneArgSimple, 0);
        assertEquals(0, generator.genRandTests().size());
    }

    /**
     * Tests that the requested number of tests is returned when it is non-zero. Does not
     * look at the actual values returned.
     */
    @Test
    @Tag("0.3")
    @Order(7)
    void testRandNumTestsNonZero() {
        BaseSetGenerator generator = new BaseSetGenerator(oneArgSimple, 3);
        assertEquals(3, generator.genRandTests().size());
    }

    /**
     * Tests the case where one test is requested and there is only one possible option,
     * using a single simple arg.
     */
    @Test
    @Tag("0.2")
    @Order(8)
    void testRandOneArgOneOption() {
        BaseSetGenerator generator = new BaseSetGenerator(oneArgOneOption, 1);
        assertEquals(oneArgOneOptionRandVals, generator.genRandTests());
    }

    /**
     * Tests the case where exactly the number of possible tests is requested, for
     * numTests > 1. Uses a single simple arg.
     */
    @Test
    @Tag("0.2")
    @Order(9)
    void testRandOneArgSimpleAll() {
        assertTrue(runTrialsRand(oneArgSimple, oneArgSimpleRandVals, 8, 100, false));
    }


    /**
     * Tests the case where a small number of tests are requested out of a larger random
     * domain; returned tests should be a subset of the valid tests. Uses a single simple
     * arg.
     */
    @Test
    @Tag("0.5")
    @Order(10)
    void testRandOneArgSimpleSubset() {
        assertTrue(runTrialsRand(oneArgSimple, oneArgSimpleRandVals, 4, 100, true));
    }

    /**
     * Tests the case where a small number of tests are requested out of a larger random
     * domain, using a single nested arg.
     */
    @Test
    @Tag("0.5")
    @Order(11)
    void testRandOneArgNested() {
        assertTrue(runTrialsRand(oneArgNested, oneArgNestedRandVals, 5, 100, true));
    }

    /**
     * Tests the case where one test is requested and there is only one possible option,
     * using multiple args.
     */
    @Test
    @Tag("0.5")
    @Order(12)
    void testRandMultipleArgsOneOptionEach() {
        BaseSetGenerator generator = new BaseSetGenerator(multipleArgsOneOption, 1);
        assertEquals(multipleArgsOneOptionRandVals, generator.genRandTests());
    }

    /**
     * Tests the case where a small number of tests are requested out of a larger random
     * domain; returned tests should be a subset of the valid tests. Uses multiple simple
     * args.
     */
    @Test
    @Tag("1.0")
    @Order(13)
    void testRandMultipleArgsSimple() {
        assertTrue(
                runTrialsRand(multipleArgsSimple, multipleArgsSimpleRandVals, 3, 100, true));
    }

    /**
     * Tests the case where a small number of tests are requested out of a larger random
     * domain; returned tests should be a subset of the valid tests. Uses multiple nested
     * args.
     */
    @Test
    @Tag("1.0")
    @Order(14)
    void testRandMultipleArgsNested() {
        assertTrue(
                runTrialsRand(multipleArgsNested, multipleArgsNestedRandVals, 2, 100, true));
    }

    /**
     * Tests the case where there's only one possible outcome for random generation,
     * and there's no overlap between random and exhaustive. Uses multiple simple args.
     */
    @Test
    @Tag("1.5")
    @Order(15)
    void testFullNoOverlapDeterministic() {
        assertTrue(
                runTrialsBoth(multipleArgsTwoOptions, multipleArgsTwoOptionsExVals,
                        multipleArgsTwoOptionsRandVals, 2, 100, false));
    }

    /**
     * Tests the case where a small number of tests are requested out of a larger random
     * domain, but there's still no overlap between the exhaustive and random domains.
     */
    @Test
    @Tag("1.5")
    @Order(16)
    void testFullNoOverlap() {
        assertTrue(runTrialsBoth(multipleArgsSimple, multipleArgsSimpleExVals,
                multipleArgsSimpleRandVals, 3, 100, true));
    }

    /**
     * Tests the case where a small number of tests are requested out of a larger random
     * domain, and there's overlap between the exhaustive and random domains.
     */
    @Test
    @Tag("2.0")
    @Order(17)
    void testFullSomeOverlap() {
        assertTrue(runTrialsBoth(oneArgSimpleOverlap, oneArgSimpleOverlapExVals,
                oneArgSimpleOverlapRandVals, 3, 100, true));
    }

    /**
     * Sets up oneArgOneOption, oneArgOneOptionExVals, and oneArgOneOptionRandVals.
     */
    private static void setUpOneArgOneOption() {
        PyIntNode oneOptionNode = new PyIntNode();
        oneOptionNode.setExDomain(Collections.singletonList(17));
        oneOptionNode.setRanDomain(Collections.singletonList(-17));

        // Populate the the expected results of exhaustive generation and the
        // exhaustive set of values from the random domain
        oneArgOneOptionExVals = Set.of(new TestCase(
                Collections.singletonList(new PyIntObj(17))));
        oneArgOneOptionRandVals = Set.of(new TestCase(
                Collections.singletonList(new PyIntObj(-17))));

        oneArgOneOption = Collections.singletonList(oneOptionNode);
    }

    /**
     * Sets up oneArgSimple, oneArgSimpleExVals, and oneArgSimpleRandVals.
     */
    private static void setUpOneArgSimple() {
        List<Integer> exDomain = new ArrayList<>();
        List<Integer> ranDomain = new ArrayList<>();
        oneArgSimpleExVals = new HashSet<>();
        oneArgSimpleRandVals = new HashSet<>();

        // Populate the domains, the expected results of exhaustive generation, and the
        // exhaustive set of values from the random domain
        for (int i : new int[]{-7, -5, -3, -1, 1, 3, 5, 7}) {
            // Make the two domains different
            exDomain.add(i);
            ranDomain.add(i + 1);
            oneArgSimpleExVals
                    .add(new TestCase(Collections.singletonList(new PyIntObj(i))));
            oneArgSimpleRandVals
                    .add(new TestCase(Collections.singletonList(new PyIntObj(i + 1))));
        }

        // Create the PyIntNode with the newly-created domains
        PyIntNode intNode = new PyIntNode();
        intNode.setExDomain(exDomain);
        intNode.setRanDomain(ranDomain);
        oneArgSimple = Collections.singletonList(intNode);
    }

    /**
     * Sets up oneArgNested, oneArgNestedExVals, and oneArgNestedRandVals.
     */
    private static void setUpOneArgNested() {
        List<Integer> intExDomain = new ArrayList<>();
        List<Integer> intRanDomain = new ArrayList<>();
        oneArgNestedExVals = new HashSet<>();
        oneArgNestedRandVals = new HashSet<>();

        // Include the empty list as a possible value
        oneArgNestedExVals.add(new TestCase(
                Collections.singletonList(new PyListObj<PyIntObj>(new ArrayList<>()))));
        oneArgNestedRandVals.add(new TestCase(
                Collections.singletonList(new PyListObj<PyIntObj>(new ArrayList<>()))));

        // Populate the domains, the expected results of exhaustive generation, and the
        // exhaustive set of values from the random domain, including lists of length
        // one and two
        for (int i : new int[]{1, 2, 4}) {
            // Make the two domains different
            intExDomain.add(i);
            intRanDomain.add(i * 2);

            // Include lists of length one
            oneArgNestedExVals.add(new TestCase(Collections.singletonList(
                    new PyListObj<>(Collections.singletonList(new PyIntObj(i))))));
            oneArgNestedRandVals.add(new TestCase(Collections.singletonList(
                    new PyListObj<>(Collections.singletonList(new PyIntObj(i * 2))))));

            // Include lists of length two
            for (int j : new int[]{1, 2, 4}) {
                List<PyIntObj> list = Arrays.asList(new PyIntObj(i), new PyIntObj(j));
                PyListObj<PyIntObj> listObj = new PyListObj<>(list);
                oneArgNestedExVals.add(new TestCase(Collections.singletonList(listObj)));

                list = Arrays.asList(new PyIntObj(i * 2), new PyIntObj(j * 2));
                listObj = new PyListObj<>(list);
                oneArgNestedRandVals
                        .add(new TestCase(Collections.singletonList(listObj)));
            }
        }

        // Create the PyIntNode and PyListNodes
        PyIntNode intNode = new PyIntNode();
        intNode.setExDomain(intExDomain);
        intNode.setRanDomain(intRanDomain);

        PyListNode<PyIntObj> listNode = new PyListNode<>(intNode);
        listNode.setExDomain(Arrays.asList(0, 1, 2));
        listNode.setRanDomain(Arrays.asList(0, 1, 2));
        oneArgNested = Collections.singletonList(listNode);
    }

    /**
     * Sets up oneArgSimpleOverlap, oneArgSimpleOverlapExVals, and
     * oneArgSimpleOverlapRandVals.
     */
    private static void setUpOneArgSimpleOverlap() {
        List<Integer> exDomain = new ArrayList<>();
        List<Integer> ranDomain = new ArrayList<>();
        oneArgSimpleOverlapExVals = new HashSet<>();
        oneArgSimpleOverlapRandVals = new HashSet<>();

        // Populate the domains, the expected results of exhaustive generation, and the
        // exhaustive set of values from the random domain
        for (int i : new int[]{-7, -5, -3, -1, 1, 3, 5, 7}) {
            // Make the two domains different
            exDomain.add(i);
            ranDomain.add(i);
            ranDomain.add(i + 1);
            oneArgSimpleOverlapExVals
                    .add(new TestCase(Collections.singletonList(new PyIntObj(i))));
            oneArgSimpleOverlapRandVals
                    .add(new TestCase(Collections.singletonList(new PyIntObj(i))));
            oneArgSimpleOverlapRandVals
                    .add(new TestCase(Collections.singletonList(new PyIntObj(i + 1))));
        }

        // Create the PyIntNode with the newly-created domains
        PyIntNode intNode = new PyIntNode();
        intNode.setExDomain(exDomain);
        intNode.setRanDomain(ranDomain);
        oneArgSimpleOverlap = Collections.singletonList(intNode);
    }

    /**
     * Sets up multipleArgsOneOption, multipleArgsOneOptionExVals, and
     * multipleArgsOneOptionRandVals.
     */
    private static void setUpMultipleArgsOneOption() {
        // First arg: an integer
        PyIntNode node1 = new PyIntNode();
        node1.setExDomain(Collections.singletonList(17));
        node1.setRanDomain(Collections.singletonList(19));

        // Second arg: a tuple(int)
        PyIntNode node2Inner = new PyIntNode();
        node2Inner.setExDomain(Collections.singletonList(3));
        node2Inner.setRanDomain(Collections.singletonList(5));
        PyTupleNode<PyIntObj> node2 = new PyTupleNode<>(node2Inner);
        node2.setExDomain(new ArrayList<>(Collections.singletonList(2)));
        node2.setRanDomain(new ArrayList<>(Collections.singletonList(4)));

        // Wrap both args into a list
        multipleArgsOneOption = Arrays.asList(node1, node2);

        // Populate the expected result of exhaustive generation
        List<APyObj> exArgs = Arrays.asList(new PyIntObj(17), new PyTupleObj<>(
                Arrays.asList(new PyIntObj(3), new PyIntObj(3))));
        multipleArgsOneOptionExVals = Set.of(new TestCase(exArgs));

        // Populate the exhaustive set of values from the random domain
        List<APyObj> randArgs = Arrays.asList(new PyIntObj(19), new PyTupleObj<>(
                Arrays.asList(new PyIntObj(5), new PyIntObj(5), new PyIntObj(5),
                        new PyIntObj(5))));
        multipleArgsOneOptionRandVals = Set.of(new TestCase(randArgs));
    }

    /**
     * Sets up multipleArgsTwoOptions, multipleArgsTwoOptionsExVals, and
     * multipleArgsTwoOptionsRandVals.
     */
    private static void setUpMultipleArgsTwoOptions() {
        // First arg: an integer
        PyIntNode node1 = new PyIntNode();
        node1.setExDomain(Collections.singletonList(17));
        node1.setRanDomain(Collections.singletonList(19));

        // Second arg: a tuple(int)
        PyIntNode node2Inner = new PyIntNode();
        node2Inner.setExDomain(Collections.singletonList(3));
        node2Inner.setRanDomain(Collections.singletonList(5));
        PyTupleNode<PyIntObj> node2 = new PyTupleNode<>(node2Inner);
        node2.setExDomain(new ArrayList<>(List.of(2, 3)));
        node2.setRanDomain(new ArrayList<>(List.of(3, 4)));

        // Wrap both args into a list
        multipleArgsTwoOptions = Arrays.asList(node1, node2);

        // Populate the expected result of exhaustive generation
        multipleArgsTwoOptionsExVals = new HashSet<>();
        multipleArgsTwoOptionsExVals.add(new TestCase(List.of(new PyIntObj(17),
                new PyTupleObj<>(Arrays.asList(new PyIntObj(3), new PyIntObj(3))))));
        multipleArgsTwoOptionsExVals.add(new TestCase(List.of(new PyIntObj(17),
                new PyTupleObj<>(Arrays.asList(new PyIntObj(3), new PyIntObj(3),
                        new PyIntObj(3))))));

        // Populate the exhaustive set of values from the random domain
        multipleArgsTwoOptionsRandVals = new HashSet<>();
        multipleArgsTwoOptionsRandVals.add(new TestCase(List.of(new PyIntObj(19),
                new PyTupleObj<>(Arrays.asList(new PyIntObj(5), new PyIntObj(5),
                        new PyIntObj(5))))));
        multipleArgsTwoOptionsRandVals.add(new TestCase(List.of(new PyIntObj(19),
                new PyTupleObj<>(Arrays.asList(new PyIntObj(5), new PyIntObj(5),
                        new PyIntObj(5), new PyIntObj(5))))));
    }

    /**
     * Sets up multipleArgsSimple, and multipleArgsSimpleExVals, and
     * multipleArgsSimpleRandVals.
     */
    private static void setUpMultipleArgsSimple() {
        // First arg: an integer
        PyIntNode firstArgSimple = new PyIntNode();
        firstArgSimple.setExDomain(Arrays.asList(888, 88, 8));
        firstArgSimple.setRanDomain(Arrays.asList(7777, 777, 77, 7));

        // Second arg: a float
        PyFloatNode secondArgSimple = new PyFloatNode();
        secondArgSimple.setExDomain(Arrays.asList(8.88, 8.8, 8.0));
        secondArgSimple.setRanDomain(Arrays.asList(7.77, 7.7, 7.0));

        // Third arg: a bool
        PyBoolNode thirdArgSimple = new PyBoolNode();
        thirdArgSimple.setExDomain(Arrays.asList(0, 1));
        thirdArgSimple.setRanDomain(Arrays.asList(0, 1));

        // Wrap all args into a list
        multipleArgsSimple =
                Arrays.asList(firstArgSimple, secondArgSimple, thirdArgSimple);

        // Populate the expected result of exhaustive generation
        multipleArgsSimpleExVals = new HashSet<>();
        for (Number i : firstArgSimple.getExDomain()) {
            for (Number j : secondArgSimple.getExDomain()) {
                for (boolean b : new boolean[]{true, false}) {
                    multipleArgsSimpleExVals.add(new TestCase(Arrays.asList(
                            new PyIntObj(i.intValue()), new PyFloatObj(j.doubleValue()),
                            new PyBoolObj(b))));
                }
            }
        }

        // Populate the exhaustive set of values from the random domain
        multipleArgsSimpleRandVals = new HashSet<>();
        for (Number i : firstArgSimple.getRanDomain()) {
            for (Number j : secondArgSimple.getRanDomain()) {
                for (boolean b : new boolean[]{true, false}) {
                    multipleArgsSimpleRandVals.add(new TestCase(Arrays.asList(
                            new PyIntObj(i.intValue()), new PyFloatObj(j.doubleValue()),
                            new PyBoolObj(b))));
                }
            }
        }
    }

    /**
     * Sets up multipleArgsNested, multipleArgsNestedExVals, and
     * multipleArgsNestedRandVals.
     */
    private static void setUpMultipleArgsNested() {
        // First arg: a string
        PyStringNode firstArgNested = new PyStringNode("rjs7");
        firstArgNested.setExDomain(Collections.singletonList(1));
        firstArgNested.setRanDomain(Collections.singletonList(2));

        // Populate the expected results of exhaustive generation and the exhaustive set
        // set of values from the random domain for this particular argument
        List<PyStringObj> firstArgExOptions = new ArrayList<>();
        List<PyStringObj> firstArgRandOptions = new ArrayList<>();
        for (Character c : "rjs7".toCharArray()) {
            firstArgExOptions.add(new PyStringObj(c.toString()));
            for (Character c2 : "rjs7".toCharArray()) {
                firstArgRandOptions.add(new PyStringObj(c.toString() + c2.toString()));
            }
        }

        // Second arg: a set(bool)
        PyBoolNode secondArgInner = new PyBoolNode();
        secondArgInner.setExDomain(Arrays.asList(0, 1));
        secondArgInner.setRanDomain(Arrays.asList(0, 1));
        PySetNode<PyBoolObj> secondArgNested = new PySetNode<>(secondArgInner);
        secondArgNested.setExDomain(Arrays.asList(0, 1));
        secondArgNested.setRanDomain(Arrays.asList(1, 2));

        // Populate the expected results of exhaustive generation and the exhaustive set
        // set of values from the random domain for this particular argument
        List<APyObj> secondArgExOptions = new ArrayList<>();
        List<APyObj> secondArgRandOptions = new ArrayList<>();
        secondArgExOptions.add(new PySetObj<>(Set.of()));
        secondArgExOptions.add(new PySetObj<>(Set.of(new PyBoolObj(true))));
        secondArgExOptions.add(new PySetObj<>(Set.of(new PyBoolObj(false))));
        secondArgRandOptions.add(new PySetObj<>(Set.of(new PyBoolObj(true))));
        secondArgRandOptions.add(new PySetObj<>(Set.of(new PyBoolObj(false))));
        secondArgRandOptions
                .add(new PySetObj<>(Set.of(new PyBoolObj(true), new PyBoolObj(false))));

        // Third arg: a dict(int: bool)
        // Set up the key node
        PyIntNode keyNode = new PyIntNode();
        keyNode.setExDomain(Arrays.asList(10, 20, 30));
        keyNode.setRanDomain(Arrays.asList(100, 200, 300, 400));

        // Set up the value node
        PyBoolNode valNode = new PyBoolNode();
        valNode.setExDomain(Collections.singletonList(0));
        valNode.setRanDomain(Collections.singletonList(1));

        // Set up the dictionary itself
        PyDictNode<PyIntObj, PyBoolObj> thirdArgNested =
                new PyDictNode<>(keyNode, valNode);
        thirdArgNested.setExDomain(Arrays.asList(1, 3));
        thirdArgNested.setRanDomain(Arrays.asList(2, 4));

        // Populate the expected results of exhaustive generation and the exhaustive set
        // set of values from the random domain for this particular argument
        List<APyObj> thirdArgExOptions = new ArrayList<>();
        List<APyObj> thirdArgRandOptions = new ArrayList<>();

        // Exhaustive options:
        Map<PyIntObj, PyBoolObj> map = new HashMap<>();
        for (int i = 10; i < 40; i += 10) {
            map.put(new PyIntObj(i), new PyBoolObj(false));
            // Add each length one option
            thirdArgExOptions
                    .add(new PyDictObj<>(Map.of(new PyIntObj(i), new PyBoolObj(false))));
        }
        // Add the length three option
        thirdArgExOptions.add(new PyDictObj<>(map));

        // Random options:
        map = new HashMap<>();
        for (int i = 100; i < 500; i += 100) {
            map.put(new PyIntObj(i), new PyBoolObj(true));
            for (int j = i + 100; j < 500; j += 100) {
                // Add each length two option
                thirdArgRandOptions.add(new PyDictObj<>(
                        Map.of(new PyIntObj(i), new PyBoolObj(true), new PyIntObj(j),
                                new PyBoolObj(true))));
            }
        }
        // Add the length four option
        thirdArgRandOptions.add(new PyDictObj<>(map));

        // Generate all combinations of exhaustive values for all three arguments
        multipleArgsNestedExVals = new HashSet<>();
        for (APyObj o1 : firstArgExOptions) {
            for (APyObj o2 : secondArgExOptions) {
                for (APyObj o3 : thirdArgExOptions) {
                    multipleArgsNestedExVals.add(new TestCase(Arrays.asList(o1, o2, o3)));
                }
            }
        }

        // Generate all combinations of random values for all three arguments
        multipleArgsNestedRandVals = new HashSet<>();
        for (APyObj o1 : firstArgRandOptions) {
            for (APyObj o2 : secondArgRandOptions) {
                for (APyObj o3 : thirdArgRandOptions) {
                    multipleArgsNestedRandVals
                            .add(new TestCase(Arrays.asList(o1, o2, o3)));
                }
            }
        }

        multipleArgsNested =
                Arrays.asList(firstArgNested, secondArgNested, thirdArgNested);
    }

    /**
     * Helper function for running random generation a large number of times, checking
     * each generated set for both validity and uniqueness.
     *
     * @param nodes          the nodes to be used for generation
     * @param validTests     the expected tests to be generated
     * @param numTests       the number of random tests to be generated
     * @param numTrials      the number of trials to run
     * @param checkNonDeterm true if we need to check for non-determinism; false otherwise
     * @return true if every trial produced a valid, duplicate-free set of tests; false
     * otherwise
     */
    private static boolean runTrialsRand(List<APyNode<?>> nodes, Set<TestCase> validTests,
                                  int numTests, int numTrials, boolean checkNonDeterm) {

        // Run random generation a bunch of times, checking the results of each trial for
        // validity
        Set<Set<TestCase>> results = new HashSet<>();
        for (int i = 0; i < numTrials; i++) {
            Set<TestCase> tests = new BaseSetGenerator(nodes, numTests).genRandTests();
            results.add(tests);

            // Check for validity of each individual test and size of the overall set
            if (!validTests.containsAll(tests)) {
                return false;
            } else if (tests.size() != numTests) {
                return false;
            }
        }

        // Check for non-determinism; don't need to check the full distribution,
        // because that was checked in homeworks 4-5
        if (checkNonDeterm && results.size() < 3) {
            return false;
        }

        // All trials succeeded
        return true;
    }

    /**
     * Helper function for running random generation a large number of times, checking
     * each generated set for both validity and uniqueness.
     *
     * @param nodes          the nodes to be used for generation
     * @param expExTests     the expected exhaustive tests to be generated
     * @param validRandTests the valid random tests to be generated
     * @param numTests       the number of random tests to be generated
     * @param numTrials      the number of trials to run
     * @param checkNonDeterm true if we need to check for non-determinism; false otherwise
     * @return true if every trial produced a valid, duplicate-free set of tests; false
     * otherwise
     */
    private static boolean runTrialsBoth(List<APyNode<?>> nodes, Set<TestCase> expExTests,
                                  Set<TestCase> validRandTests, int numTests, int numTrials,
                                  boolean checkNonDeterm) {

        // Run random generation a bunch of times, checking the results of each trial for
        // validity
        Set<Set<TestCase>> results = new HashSet<>();
        for (int i = 0; i < numTrials; i++) {
            BaseSetGenerator generator = new BaseSetGenerator(nodes, numTests);
            List<TestCase> tests = generator.genBaseSet();

            // Make sure there are no duplicates
            int ogSize = tests.size();
            Set<TestCase> dedupedTests = new HashSet<>(tests);
            if (dedupedTests.size() != ogSize) {
                return false;
            }
            results.add(new HashSet<>(dedupedTests));

            // Extract the exhaustive set and make sure everything is there
            dedupedTests.removeAll(expExTests);
            if (dedupedTests.size() != ogSize - expExTests.size()) {
                return false;
            }

            // Check for validity of each individual random test and size of the random
            // set
            if (!validRandTests.containsAll(dedupedTests)) {
                return false;
            } else if (dedupedTests.size() != numTests) {
                return false;
            }
        }

        // Check for non-determinism; don't need to check the full distribution,
        // because that was checked in homeworks 4-5
        if (checkNonDeterm && results.size() < 3) {
            return false;
        }

        // All trials succeeded
        return true;
    }
}