package main.rice.node;

import main.rice.obj.PyBoolObj;

import java.util.HashSet;
import java.util.Set;

/**
 * A representation of a leaf node for generating PyBoolObjs. Assumes that the domains
 * will only include 0 and/or 1.
 */
public class PyBoolNode extends APyNode<PyBoolObj> {

    /**
     * Generates all valid PyBoolObjs within the exhaustive domain. Interprets 0 (in
     * the exhaustive domain) as False and 1 as True.
     *
     * @return a set of PyBoolObjs comprising the exhaustive domain
     */
    @Override
    public Set<PyBoolObj> genExVals() {
        Set<PyBoolObj> vals = new HashSet<>();
        for (Number value : this.exDomain) {
            vals.add(new PyBoolObj((value.intValue() > 0)));
        }
        return vals;
    }

    /**
     * Generates a single valid PyBoolObj within the random domain.
     *
     * @return a single PyBoolObj selected from the random domain
     */
    @Override
    public PyBoolObj genRandVal() {
        return new PyBoolObj(this.ranDomainChoice().intValue() > 0);
    }
}