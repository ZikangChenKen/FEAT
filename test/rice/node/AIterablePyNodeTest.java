package test.rice.node;

import main.rice.node.*;
import main.rice.obj.*;
import org.junit.jupiter.api.BeforeAll;

import java.util.*;

/**
 * Helper functions for testing PyListNode and PyTupleNode.
 */
public abstract class AIterablePyNodeTest extends APyNodeTest {

    /**
     * A PyListNode whose exhaustive domain includes only one option: the empty list. Its
     * random domain includes only one option of length one.
     */
    protected static PyListNode<PyBoolObj> emptyOnly;
    protected static PyBoolNode emptyLeftChild;

    /**
     * A PyListNode whose exhaustive domain includes only one option of length one. Its
     * random domain includes only the empty list.
     */
    protected static PyListNode<PyBoolObj> oneLenOne;

    /**
     * A PyListNode whose exhaustive domain includes two options of length one. Its random
     * domain includes four options of length two.
     */
    protected static PyListNode<PyBoolObj> twoLenOne;

    /**
     * A PyListNode whose exhaustive domain includes four options of length two. Its
     * random domain includes two options of length two.
     */
    protected static PyListNode<PyBoolObj> fourLenTwo;
    protected static Set<PyListObj<PyBoolObj>> expectedFourLenTwo;

    /**
     * A PyListNode whose domains include many options of length two.
     */
    protected static PyListNode<PyIntObj> manyLenTwo;
    protected static Set<PyListObj<PyIntObj>> expectedManyLenTwo;

    /**
     * A PyListNode whose domains include lists of lengths zero and one.
     */
    protected static PyListNode<PyFloatObj> lensZeroToOne;
    protected static Set<PyListObj<PyFloatObj>> expectedLenZeroToOne;
    protected static Map<PyListObj<PyFloatObj>, Double> expectedRandLenZeroToOne;

    /**
     * A PyListNode whose domains include multiple (non-contiguous) lengths, from zero to
     * three.
     */
    protected static PyListNode<PyFloatObj> lensZeroToThree;
    protected static Set<PyListObj<PyFloatObj>> expectedLenZeroToThree;
    protected static Map<PyListObj<PyFloatObj>, Double> expectedRandLenZeroToThree;

    /**
     * A nested PyListNode.
     */
    protected static PyListNode<PyListObj<PyBoolObj>> nestedBools;
    protected static Set<PyListObj<PyListObj<PyBoolObj>>> expectedNested;
    protected static Map<PyListObj<PyListObj<PyBoolObj>>, Double> expectedRandNested;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        setUpOneOption();
        setUpFewOptions();
        setUpManyLenTwo();
        setUpLensZeroToOne();
        setUpLensZeroToThree();
        setUpNested();
    }

    /**
     * Converts the input PyListNode into a PyTupleNode. Only affects the outer node.
     *
     * @param node the PyListNode to be converted
     * @return a PyTupleNode that is the root node of a tree that is otherwise identical
     * to the tree rooted in the input node
     * @param <InnerType> the type of inner element in the lists/tuples
     */
    protected static <InnerType extends APyObj> PyTupleNode<InnerType> convertListNodeToTup(
            PyListNode<InnerType> node) {
        APyNode<InnerType> leftChild = node.getLeftChild();

        // Convert input node, keeping domains the same, and return
        PyTupleNode<InnerType> newNode = new PyTupleNode<>(leftChild);
        newNode.setExDomain(node.getExDomain());
        newNode.setRanDomain(node.getRanDomain());
        return newNode;
    }

    /**
     * Recursively converts the input PyListNode into a PyTupleNode. Only works for tuples
     * nested two layers deep.
     *
     * @param node the PyListNode that is the root node of a list generator to be
     *             recursively converted
     * @return a PyTupleNode that is the root node of a tree that is structurally
     * identical to the tree rooted in the input node, but with PyTupleNodes instead of
     * PyListNodes
     * @param <InnerType> the type of inner element in the lists/tuples
     */
    protected static <InnerType extends APyObj> PyTupleNode<PyTupleObj<InnerType>> convertNestedListNodeToTup(
            PyListNode<PyListObj<InnerType>> node) {
        APyNode<PyListObj<InnerType>> leftChild = node.getLeftChild();

        // Convert input node, keeping domains the same, and return
        PyTupleNode<PyTupleObj<InnerType>> newNode = new PyTupleNode<>(
                convertListNodeToTup((PyListNode<InnerType>) leftChild));
        newNode.setExDomain(node.getExDomain());
        newNode.setRanDomain(node.getRanDomain());
        return newNode;
    }

    /**
     * Converts each PyListObj in the input map's key set into a PyTupleObj.
     *
     * @param map the map of PyListObjs to be converted
     * @return a map of PyTupleObjs whose keys are tuples that have identical contents to
     * the lists in the input key set
     * @param <InnerType> the type of inner element in the lists/tuples
     */
    protected static <InnerType extends APyObj> Map<PyTupleObj<InnerType>, Double> convertMapOfListObjs(
            Map<PyListObj<InnerType>, Double> map) {
        Map<PyTupleObj<InnerType>, Double> newMap = new HashMap<>();
        for (Map.Entry<PyListObj<InnerType>, Double> entry : map.entrySet()) {
            newMap.put(new PyTupleObj<>(new ArrayList<>(entry.getKey().getValue())), entry.getValue());
        }
        return newMap;
    }

    /**
     * Converts each nested PyListObj in the input map's key set into a PyTupleObj.
     *
     * @param map the map of PyListObjs to be converted
     * @return a map of PyTupleObjs whose keys are tuples containing tuples that are
     * structurally identical to the lists in the input set
     * @param <InnerType> the type of inner element in the lists/tuples
     */
    protected static <InnerType extends APyObj> Map<PyTupleObj<PyTupleObj<InnerType>>,
            Double> convertMapOfNestedListObjs(Map<PyListObj<PyListObj<InnerType>>, Double>
                                                       map) {
        Map<PyTupleObj<PyTupleObj<InnerType>>, Double> newMap = new HashMap<>();
        for (Map.Entry<PyListObj<PyListObj<InnerType>>, Double> entry : map.entrySet()) {
            newMap.put(convertNestedListObjsHelper(entry.getKey()), entry.getValue());
        }
        return newMap;
    }

    /**
     * Converts each PyListObj in the input set into a PyTupleObj.
     *
     * @param set the set of PyListObjs to be converted
     * @return a set of PyTupleObjs containing tuples that have identical contents to the
     * lists in the input set
     * @param <InnerType> the type of inner element in the lists/tuples
     */
    protected static <InnerType extends APyObj> Set<PyTupleObj<InnerType>> convertListObjsToTuples(
            Collection<PyListObj<InnerType>> set) {
        Set<PyTupleObj<InnerType>> newSet = new HashSet<>();
        for (PyListObj<InnerType> listObj : set) {
            newSet.add(new PyTupleObj<>(new ArrayList<>(listObj.getValue())));
        }
        return newSet;
    }

    /**
     * Converts each nested PyListObj in the input set into a nested PyTupleObj.
     *
     * @param set the set of PyListObjs to be converted
     * @return a set of PyTupleObjs containing tuples that are structurally identical to
     * the lists in the input set
     * @param <InnerType> the type of inner element in the lists/tuples
     */
    protected static <InnerType extends APyObj> Set<PyTupleObj<PyTupleObj<InnerType>>> convertNestedListObjsToTuples(
            Set<PyListObj<PyListObj<InnerType>>> set) {
        Set<PyTupleObj<PyTupleObj<InnerType>>> newSet = new HashSet<>();
        for (PyListObj<PyListObj<InnerType>> listObj : set) {
            newSet.add(convertNestedListObjsHelper(listObj));
        }
        return newSet;
    }

    /**
     * Converts the input PyListObj and each of its sub-elements into a PyTupleObj.
     *
     * @param obj the outer PyListObj to be converted
     * @return a PyTupleObj that is structurally identical to the input list, but with
     * PyTupleObjs instead of PyListObjs
     * @param <InnerType> the type of inner element in the lists/tuples
     */
    protected static <InnerType extends APyObj> PyTupleObj<PyTupleObj<InnerType>> convertNestedListObjsHelper(
            PyListObj<PyListObj<InnerType>> obj) {

        // Recursively convert sub-elements
        List<PyTupleObj<InnerType>> newValue = new ArrayList<>();
        for (PyListObj<InnerType> elem : obj.getValue()) {
            newValue.add(new PyTupleObj<>(new ArrayList<>(elem.getValue())));
        }
        // Convert input obj and return
        return new PyTupleObj<>(newValue);
    }

    /**
     * Sets up emptyOnly and oneLenOne.
     */
    private static void setUpOneOption() {
        // Create generators of lists of length 0-1 (one option each)
        emptyLeftChild = new PyBoolNode();
        emptyLeftChild.setExDomain(Collections.singletonList(1));
        emptyLeftChild.setRanDomain(Collections.singletonList(1));

        // Swap the random domains on emptyOnly and oneLenOne to ensure that the correct
        // domain is being used for generation
        emptyOnly = new PyListNode<>(emptyLeftChild);
        emptyOnly.setExDomain(Collections.singletonList(0));
        emptyOnly.setRanDomain(Collections.singletonList(1));

        PyBoolNode oneLenOneChild = new PyBoolNode();
        oneLenOneChild.setExDomain(Collections.singletonList(1));
        oneLenOneChild.setRanDomain(Collections.singletonList(1));

        oneLenOne = new PyListNode<>(oneLenOneChild);
        oneLenOne.setExDomain(Collections.singletonList(1));
        oneLenOne.setRanDomain(Collections.singletonList(0));
    }

    /**
     * Sets up twoLenOne and fourLenTwo.
     */
    private static void setUpFewOptions() {
        // Create generators of lists of lengths 1-2 (multiple options each)
        PyBoolNode twoLenOneChild = new PyBoolNode();
        twoLenOneChild.setExDomain(Arrays.asList(0, 1));
        twoLenOneChild.setRanDomain(Arrays.asList(0, 1));

        // Swap the random domains on twoLenOne and fourLenTwo to ensure that the correct
        // domain is being used for generation
        twoLenOne = new PyListNode<>(twoLenOneChild);
        twoLenOne.setExDomain(Collections.singletonList(1));
        twoLenOne.setRanDomain(Collections.singletonList(2));

        PyBoolNode fourLenTwoChild = new PyBoolNode();
        fourLenTwoChild.setExDomain(Arrays.asList(0, 1));
        fourLenTwoChild.setRanDomain(Arrays.asList(0, 1));

        fourLenTwo = new PyListNode<>(fourLenTwoChild);
        fourLenTwo.setExDomain(Collections.singletonList(2));
        fourLenTwo.setRanDomain(Collections.singletonList(1));
        expectedFourLenTwo = genExpectedFourLenTwo();
    }

    /**
     * Generates the expected results of exhaustive generation for fourLenTwo.
     *
     * @return the expected results of exhaustive distribution for fourLenTwo
     */
    private static Set<PyListObj<PyBoolObj>> genExpectedFourLenTwo() {
        Set<PyListObj<PyBoolObj>> expected = new HashSet<>();
        for (boolean val1 : Arrays.asList(true, false)) {
            for (boolean val2 : Arrays.asList(true, false)) {
                List<PyBoolObj> val = Arrays.asList(new PyBoolObj(val1),
                        new PyBoolObj(val2));
                expected.add(new PyListObj<>(val));
            }
        }
        return expected;
    }

    /**
     * Sets up manyLenTwo.
     */
    private static void setUpManyLenTwo() {
        // Create generators of multiple (slightly) longer lists
        PyIntNode manyLenTwoChild = new PyIntNode();
        manyLenTwoChild.setExDomain(Arrays.asList(2, 0, 1));
        manyLenTwoChild.setRanDomain(Arrays.asList(2, 0, 1));

        manyLenTwo = new PyListNode<>(manyLenTwoChild);
        manyLenTwo.setExDomain(Collections.singletonList(2));
        manyLenTwo.setRanDomain(Collections.singletonList(2));
        expectedManyLenTwo = genExpectedManyLenTwo();
    }

    /**
     * Generates the expected results of exhaustive generation for manyLenTwo.
     *
     * @return the expected results of exhaustive distribution for manyLenTwo
     */
    private static Set<PyListObj<PyIntObj>> genExpectedManyLenTwo() {
        Set<PyListObj<PyIntObj>> expected = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                List<PyIntObj> val = Arrays.asList(new PyIntObj(i), new PyIntObj(j));
                expected.add(new PyListObj<>(val));
            }
        }
        return expected;
    }

    /**
     * Sets up lensZeroToOne.
     */
    private static void setUpLensZeroToOne() {
        PyFloatNode lenZeroToOneChild = new PyFloatNode();
        lenZeroToOneChild.setExDomain(Arrays.asList(3, 5, 4));
        lenZeroToOneChild.setRanDomain(Arrays.asList(6, 7));

        lensZeroToOne = new PyListNode<>(lenZeroToOneChild);
        lensZeroToOne.setExDomain(Arrays.asList(0, 1));
        lensZeroToOne.setRanDomain(Arrays.asList(0, 1));
        expectedLenZeroToOne = genExpectedLenZeroToOne();
        expectedRandLenZeroToOne = genExpectedRandLenZeroToOne();
    }

    /**
     * Generates the expected results of exhaustive generation for lensZeroToOne.
     *
     * @return the expected results of exhaustive generation for lensZeroToOne
     */
    private static Set<PyListObj<PyFloatObj>> genExpectedLenZeroToOne() {
        Set<PyListObj<PyFloatObj>> expected = new HashSet<>();
        expected.add(new PyListObj<>(Collections.emptyList()));

        // Create all length one options
        for (double i = 3; i < 6; i++) {
            List<PyFloatObj> val = Collections.singletonList(new PyFloatObj(i));
            expected.add(new PyListObj<>(val));
        }
        return expected;
    }

    /**
     * Generates the expected results of random generation for lensZeroToOne.
     *
     * @return the expected results of random generation for lensZeroToOne
     */
    private static Map<PyListObj<PyFloatObj>, Double> genExpectedRandLenZeroToOne() {
        Map<PyListObj<PyFloatObj>, Double> expected = new HashMap<>();
        expected.put(new PyListObj<>(Collections.emptyList()), (1.0 / 2.0));

        // Create all length one options
        for (double i = 6; i < 8; i++) {
            List<PyFloatObj> val = Collections.singletonList(new PyFloatObj(i));
            expected.put(new PyListObj<>(val), (1.0 / 4.0));
        }
        return expected;
    }

    /**
     * Sets up lensZeroToThree.
     */
    private static void setUpLensZeroToThree() {
        PyFloatNode lenZeroToThreeChild = new PyFloatNode();
        lenZeroToThreeChild.setExDomain(Arrays.asList(3, 5, 4));
        lenZeroToThreeChild.setRanDomain(Arrays.asList(6, 7));

        lensZeroToThree = new PyListNode<>(lenZeroToThreeChild);
        lensZeroToThree.setExDomain(Arrays.asList(0, 2));
        lensZeroToThree.setRanDomain(Arrays.asList(1, 3));
        expectedLenZeroToThree = genExpectedLenZeroToThree();
        expectedRandLenZeroToThree = genExpectedRandLenZeroToThree();
    }

    /**
     * Generates the expected results of exhaustive generation for lensZeroToThree.
     *
     * @return the expected results of exhaustive generation for lensZeroToThree
     */
    private static Set<PyListObj<PyFloatObj>> genExpectedLenZeroToThree() {
        // Build the expected set for exhaustive generation
        Set<PyListObj<PyFloatObj>> expected = new HashSet<>();
        expected.add(new PyListObj<>(Collections.emptyList()));

        for (double i = 3; i < 6; i++) {
            // Add length two options
            for (double j = 3; j < 6; j++) {
                expected.add(new PyListObj<>(Arrays.asList(new PyFloatObj(i),
                        new PyFloatObj(j))));
            }
        }
        return expected;
    }

    /**
     * Generates the expected results of random generation for lensZeroToThree.
     *
     * @return the expected results of random generation for lensZeroToThree
     */
    private static Map<PyListObj<PyFloatObj>, Double> genExpectedRandLenZeroToThree() {
        Map<PyListObj<PyFloatObj>, Double> expected = new HashMap<>();
        // Create all length one options
        for (double i = 6; i < 8; i++) {
            List<PyFloatObj> val = Collections.singletonList(new PyFloatObj(i));
            expected.put(new PyListObj<>(val), (1.0 / 4.0));
        }

        // Create all length three options
        for (double i = 6; i < 8; i++) {
            for (double j = 6; j < 8; j++) {
                for (double k = 6; k < 8; k++) {
                    List<PyFloatObj> val = Arrays.asList(new PyFloatObj(i),
                            new PyFloatObj(j), new PyFloatObj(k));
                    expected.put(new PyListObj<>(val), (1.0 / 16.0));
                }
            }
        }
        return expected;
    }

    /**
     * Sets up nestedBools.
     */
    private static void setUpNested() {
        // Create generators of nested lists
        PyBoolNode trueOnly = new PyBoolNode();
        trueOnly.setExDomain(Collections.singletonList(1));
        trueOnly.setRanDomain(Collections.singletonList(0));

        PyListNode<PyBoolObj> inner = new PyListNode<>(trueOnly);
        inner.setExDomain(Arrays.asList(1, 2));
        inner.setRanDomain(Arrays.asList(3, 4));

        nestedBools = new PyListNode<>(inner);
        nestedBools.setExDomain(Arrays.asList(2, 0));
        nestedBools.setRanDomain(Arrays.asList(1, 3));
        expectedNested = genExpectedNested();
        expectedRandNested = genExpectedRandNested();
    }

    /**
     * Generates the expected results of exhaustive generation for nestedBools.
     *
     * @return the expected results of exhaustive distribution for nestedBools
     */
    private static Set<PyListObj<PyListObj<PyBoolObj>>> genExpectedNested() {
        Set<PyListObj<PyListObj<PyBoolObj>>> expected = new HashSet<>();
        // Empty option
        expected.add(new PyListObj<>(Collections.emptyList()));

        // Two options for the inner iterables
        List<PyBoolObj> val1 = Collections.singletonList(new PyBoolObj(true));
        List<PyBoolObj> val2 = Arrays.asList(new PyBoolObj(true), new PyBoolObj(true));
        Set<List<PyBoolObj>> vals = Set.of(val1, val2);
        Set<PyListObj<PyBoolObj>> innerObjs = new HashSet<>();
        for (List<PyBoolObj> val : vals) {
            innerObjs.add(new PyListObj<>(val));
        }

        // Build all combinations of length two of the inner iterables
        for (PyListObj<PyBoolObj> innerObj : innerObjs) {
            for (PyListObj<PyBoolObj> innerObj2 : innerObjs) {
                List<PyListObj<PyBoolObj>> val = Arrays.asList(innerObj, innerObj2);
                expected.add(new PyListObj<>(val));
            }
        }
        return expected;
    }

    /**
     * Generates the expected results of random generation for nestedBools.
     *
     * @return the expected results of random generation for nestedBools
     */
    private static Map<PyListObj<PyListObj<PyBoolObj>>, Double> genExpectedRandNested() {
        Map<PyListObj<PyListObj<PyBoolObj>>, Double> expected = new HashMap<>();

        // Create two possible inner lists
        List<PyBoolObj> valLenThree = Arrays.asList(new PyBoolObj(false),
                new PyBoolObj(false), new PyBoolObj(false));
        List<PyBoolObj> valLenFour = Arrays.asList(new PyBoolObj(false),
                new PyBoolObj(false), new PyBoolObj(false), new PyBoolObj(false));

        PyListObj<PyBoolObj> lenThree;
        PyListObj<PyBoolObj> lenFour;

        // Create all length one combinations of the inner lists
        lenThree = new PyListObj<>(valLenThree);
        lenFour = new PyListObj<>(valLenFour);
        expected.put(new PyListObj<>(Collections.singletonList(lenThree)), (1.0 / 4.0));
        expected.put(new PyListObj<>(Collections.singletonList(lenFour)), (1.0 / 4.0));

        // Create all length three combinations of the inner lists
        for (PyListObj<PyBoolObj> listObj : Arrays.asList(lenThree, lenFour)) {
            for (PyListObj<PyBoolObj> listObj2 : Arrays.asList(lenThree, lenFour)) {
                for (PyListObj<PyBoolObj> listObj3 : Arrays.asList(lenThree, lenFour)) {
                    List<PyListObj<PyBoolObj>> val = Arrays.asList(listObj, listObj2,
                            listObj3);
                    expected.put(new PyListObj<>(val), (1.0 / 16.0));
                }
            }
        }
        return expected;
    }
}