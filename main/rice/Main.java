package main.rice;

// TODO: implement the Main class here

import main.rice.basegen.BaseSetGenerator;
import main.rice.concisegen.ConciseSetGenerator;
import main.rice.node.APyNode;
import main.rice.parse.*;
import main.rice.test.*;

import java.io.*;
import java.util.*;

/**
 * the Main class of FEAT
 */
public class Main{
    /**
     * Using these arguments, main() should delegate to generateTests() (described below) in order to
     * compute the concise test set.
     * @param args input a String[] that should contain three arguments
     * @throws IOException
     * @throws InvalidConfigException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InvalidConfigException, InterruptedException{
        Set<TestCase> tests = Main.generateTests(args);
        System.out.print(tests);
    }

    /**
     * It should utilize the components that you built in homeworks 1-6 in order to perform end-to-end
     * test case generation, returning the concise test set.
     * @param args the exact same array of arguments
     * @return the concise test set
     * @throws IOException
     * @throws InvalidConfigException
     * @throws InterruptedException
     */
    public static Set<TestCase> generateTests(String[] args) throws IOException, InvalidConfigException,
            InterruptedException{
        // extract the path from args
        String configPath = args[0];
        String buggyPath = args[1];
        String referPath = args[2];

        ConfigFileParser parser = new ConfigFileParser();
        String content = parser.readFile(configPath);
        ConfigFile file = parser.parse(content);

        //get nodes and numRand from file
        List<APyNode<?>> nodes = file.getNodes();
        String funcName = file.getFuncName();
        int numRand = file.getNumRand();
        BaseSetGenerator generator = new BaseSetGenerator(nodes, numRand);
        List<TestCase> testCases = generator.genBaseSet();
        Tester tester = new Tester(funcName, referPath, buggyPath, testCases);
        tester.computeExpectedResults();
        TestResults testResults = tester.runTests();
        return ConciseSetGenerator.setCover(testResults);
    }

}