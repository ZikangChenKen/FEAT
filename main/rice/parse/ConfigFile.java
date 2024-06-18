package main.rice.parse;

// TODO: implement the ConfigFile class here

import main.rice.node.*;

import java.util.*;

/**
 * This class is designed to bundle together three pieces of data that will be extracted from
 * the config file during the parsing process.
 */
public class ConfigFile{

    /**
     * field for the name of the function under test
     */
    protected String funcName;

    /**
     * field for a List of PyNodes which will be used to generate TestCases for the function under test
     */
    protected List<APyNode<?>> nodes;

    /**
     * field for the number of random test cases to be generated
     */
    protected int numRand;

    /**
     * Constructor for a ConfigFile object, which takes in three pieces of data.
     * @param funcName The name of the function under test
     * @param nodes A List of PyNodes that will be used to generate TestCases for the function under test
     * @param numRand The number of random test cases to be generated
     */
    public ConfigFile(String funcName, List<APyNode<?>> nodes, int numRand){
        this.funcName = funcName;
        this.nodes = nodes;
        this.numRand = numRand;
    }

    /**
     * Returns the name of the function under test.
     * @return the name of the function under test
     */
    public String getFuncName(){
        return this.funcName;
    }

    /**
     * Returns the List of PyNodes that will be used to generate TestCases for the function under test.
     * @return the List of PyNodes that will be used to generate TestCases for the function under test
     */
    public List<APyNode<?>> getNodes(){
        return this.nodes;
    }

    /**
     * Returns the number of random test cases to be generated.
     * @return the number of random test cases to be generated
     */
    public int getNumRand(){
        return this.numRand;
    }
}