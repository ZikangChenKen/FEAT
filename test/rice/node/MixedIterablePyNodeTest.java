package test.rice.node;

import main.rice.node.PyDictNode;
import main.rice.node.PyListNode;
import main.rice.node.PyTupleNode;
import main.rice.obj.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases of objects with deeply nested types.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MixedIterablePyNodeTest extends AIterablePyNodeTest {

    /**
     * A deeply nested PyDictObj (keys and values are themselves nested).
     */
    private static PyDictNode<PyTupleObj<PyListObj<PyBoolObj>>, PyListObj<PyFloatObj>>
            deeplyNested;
    private static Set<PyDictObj<PyTupleObj<PyListObj<PyBoolObj>>,
            PyListObj<PyFloatObj>>> deeplyNestedExpected;
    private static Map<PyDictObj<PyTupleObj<PyListObj<PyBoolObj>>, PyListObj<PyFloatObj>>,
            Double> deeplyNestedExpectedRand;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        // Set up some AIterablePyNodes
        AIterablePyNodeTest.setUp();

        // Create the deeply nested node, re-using nodes from the AIterablePyNode tests
        // for the keys and values
        PyTupleNode<PyListObj<PyBoolObj>> keyNode =
                convertListNodeToTup(nestedBools);
        keyNode.setRanDomain(Collections.singletonList(1));
        PyListNode<PyFloatObj> valNode = lensZeroToThree;
        valNode.setRanDomain(Collections.singletonList(1));
        deeplyNested = new PyDictNode<>(keyNode, valNode);
        deeplyNested.setExDomain(Arrays.asList(0, 1, 2));
        deeplyNested.setRanDomain(Arrays.asList(1, 2));

        // Get the expected values for the key node
        Set<PyTupleObj<PyListObj<PyBoolObj>>> expectedKeysEx =
                convertListObjsToTuples(expectedNested);

        // Remove everything that's not length one
        Set<PyTupleObj<PyListObj<PyBoolObj>>> expectedKeysRand =
                convertMapOfListObjs(expectedRandNested).keySet();
        Set<PyTupleObj<PyListObj<PyBoolObj>>> notLenOne = new HashSet<>();
        for (PyTupleObj<PyListObj<PyBoolObj>> tup : expectedKeysRand) {
            if (tup.getValue().size() != 1) {
                notLenOne.add(tup);
            }
        }
        expectedKeysRand.removeAll(notLenOne);

        // Get the expected values for the val node
        Set<PyListObj<PyFloatObj>> expectedValsEx = expectedLenZeroToThree;
        Set<PyListObj<PyFloatObj>> expectedValsRand = expectedRandLenZeroToThree.keySet();

        // Remove everything that's not length one
        Set<PyListObj<PyFloatObj>> notLenOneVals = new HashSet<>();
        for (PyListObj<PyFloatObj> list : expectedValsRand) {
            if (list.getValue().size() != 1) {
                notLenOneVals.add(list);
            }
        }
        expectedValsRand.removeAll(notLenOneVals);

        // Generate the expected exhaustive values for the nested dictionary by combining
        // the expected values from the key and val nodes
        deeplyNestedExpected = new HashSet<>();
        deeplyNestedExpected.add(new PyDictObj<>(Collections.emptyMap()));

        // Generate all options of length one and two
        for (PyTupleObj<PyListObj<PyBoolObj>> key : expectedKeysEx) {
            for (PyListObj<PyFloatObj> val : expectedValsEx) {
                // Add options of length one
                deeplyNestedExpected.add(new PyDictObj<>(Map.of(key, val)));

                // Add options of length two
                for (PyTupleObj<PyListObj<PyBoolObj>> key2 : expectedKeysEx) {
                    for (PyListObj<PyFloatObj> val2 : expectedValsEx) {
                        if (!key.equals(key2)) {
                            deeplyNestedExpected.add(new PyDictObj<>(Map.of(key, val,
                                    key2, val2)));
                        }
                    }
                }
            }
        }

        // Generate the expected random values for the nested dictionary by combining
        // the expected values from the key and val nodes
        deeplyNestedExpectedRand = new HashMap<>();

        // Generate all options of length one and two
        for (PyTupleObj<PyListObj<PyBoolObj>> key : expectedKeysRand) {
            for (PyListObj<PyFloatObj> val : expectedValsRand) {
                // Add options of length one; equal probability of each possible
                // combination, of which there are four
                deeplyNestedExpectedRand.put(new PyDictObj<>(Map.of(key, val)),
                        (1.0 / 8.0));

                // Add options of length two; 50% chance of selecting length one + equal
                // probability of each possible combination, of which there are four
                for (PyTupleObj<PyListObj<PyBoolObj>> key2 : expectedKeysRand) {
                    for (PyListObj<PyFloatObj> val2 : expectedValsRand) {
                        if (!key.equals(key2)) {
                            deeplyNestedExpectedRand.put(new PyDictObj<>(Map.of(key, val,
                                    key2, val2)), (1.0 / 8.0));
                        }
                    }
                }

            }
        }
    }

    /**
     * Tests genExVals() on a deeply nested dictionary with many possible options.
     */
    @Test
    @Tag("1.5")
    @Order(1)
    void testGenExValsDeeplyNested() {
        // Generate the exhaustive set
        Set<PyDictObj<PyTupleObj<PyListObj<PyBoolObj>>, PyListObj<PyFloatObj>>> actual =
                deeplyNested.genExVals();

        // Compare the actual and expected results
        assertEquals(deeplyNestedExpected, actual);
    }

    /**
     * Tests genRandVal() on a deeply nested dictionary with many possible options.
     */
    @Test
    @Tag("1.5")
    @Order(2)
    void testGenRandValDeeplyNested() {
        // Run a bunch of trials to get the actual distribution
        Map<PyDictObj<PyTupleObj<PyListObj<PyBoolObj>>, PyListObj<PyFloatObj>>, Double>
                actual = buildDistribution(deeplyNested, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(deeplyNestedExpectedRand,
                actual, 0.01));
    }
}