package test.rice.parse;

import main.rice.node.*;
import main.rice.parse.ConfigFile;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for the ConfigFile class.
 */
class ConfigFileTest {

    /**
     * Tests the getFuncName() method of the ConfigFile class.
     */
    @Test
    void testGetFuncName() {
        assertEquals("test", new ConfigFile("test", null, 0).getFuncName());
    }

    /**
     * Tests the getNumRand() method of the ConfigFile class.
     */
    @Test
    void testGetNumRand() {
        assertEquals(17, new ConfigFile(null, null, 17).getNumRand());
    }

    /**
     * Tests the getNodes() method of the ConfigFile class.
     */
    @Test
    void testGetNodes() {
        // First node: a boolean
        PyBoolNode node1 = new PyBoolNode();
        node1.setExDomain(List.of(0, 1));
        node1.setRanDomain(List.of(0, 1));

        // Second node: an integer
        PyIntNode node2 = new PyIntNode();
        node2.setExDomain(List.of(0, 1, 2));
        node2.setRanDomain(List.of(3, 4, 5));

        List<APyNode<?>> nodes = List.of(node1, node2);
        assertEquals(new ArrayList<>(nodes), new ConfigFile(null, nodes, 0).getNodes());
    }
}