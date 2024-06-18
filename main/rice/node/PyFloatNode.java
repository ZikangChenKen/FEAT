package main.rice.node;

import main.rice.obj.PyFloatObj;

import java.util.HashSet;
import java.util.Set;

/**
 * A representation of a leaf node for generating PyFloatObjs.
 */
public class PyFloatNode extends APyNode<PyFloatObj> {

    /**
     * Generates all valid PyFloatObjs within the exhaustive domain.
     *
     * @return a set of PyFloatObjs comprising the exhaustive domain
     */
    @Override
    public Set<PyFloatObj> genExVals() {
        Set<PyFloatObj> vals = new HashSet<>();
        for (Number value : this.exDomain) {
            vals.add(new PyFloatObj(value.doubleValue()));
        }
        return vals;
    }

    /**
     * Generates a single valid PyFloatObj within the random domain.
     *
     * @return a single PyFloatObj selected from the random domain
     */
    @Override
    public PyFloatObj genRandVal() {
        return new PyFloatObj(this.ranDomainChoice().doubleValue());
    }
}