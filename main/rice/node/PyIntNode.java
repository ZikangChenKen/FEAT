package main.rice.node;

import main.rice.obj.PyIntObj;

import java.util.HashSet;
import java.util.Set;

/**
 * A representation of a leaf node for generating PyIntObjs.
 */
public class PyIntNode extends APyNode<PyIntObj> {

    /**
     * Generates all valid PyIntObjs within the exhaustive domain.
     *
     * @return a set of PyIntObjs comprising the exhaustive domain
     */
    @Override
    public Set<PyIntObj> genExVals() {
        Set<PyIntObj> vals = new HashSet<>();
        for (Number value : this.exDomain) {
            vals.add(new PyIntObj(value.intValue()));
        }
        return vals;
    }

    /**
     * Generates a single valid PyIntObj within the random domain.
     *
     * @return a single PyIntObj selected from the random domain
     */
    @Override
    public PyIntObj genRandVal() {
        return new PyIntObj(this.ranDomainChoice().intValue());
    }
}