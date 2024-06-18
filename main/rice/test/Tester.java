package main.rice.test;

import main.rice.obj.APyObj;
import java.io.*;
import java.util.*;

/**
 * A class for running a test suite. Encapsulates the ability to run the test suite on a
 * reference implementation to generate the expected results, the ability to create a
 * "wrapper" file for comparing actual results to these expected results, and the ability
 * to run the test suite on a set of files and identify which test cases each file fails
 * on.
 */
public class Tester {

    /**
     * The name of the function under test.
     */
    private final String funcName;

    /**
     * The absolute path to the file containing the reference solution (known to be
     * correct).
     */
    private final String solutionPath;

    /**
     * The absolute path to the directory containing the student implementations.
     */
    private final String implDirPath;

    /**
     * The set of test cases to be executed on the reference solution.
     */
    private final List<TestCase> tests;

    /**
     * Constructor for a Tester, which initializes all of the fields using the given
     * inputs.
     *
     * @param funcName     the name of the function under test
     * @param solutionPath the absolute path to the file containing the reference
     *                     implementation
     * @param implDirPath  the absolute path to the directory containing the student
     *                     implementations
     * @param tests        the list of test cases to be executed
     */
    public Tester(String funcName, String solutionPath, String implDirPath,
                  List<TestCase> tests) {
        this.funcName = funcName;
        this.implDirPath = implDirPath;
        this.solutionPath = solutionPath;
        this.tests = tests;
    }

    /**
     * Computes the expected results by running each test case on the solution file.
     * Stores the results in a list (which is returned) and also creates a .py file
     * containing an equivalent list of the results.
     *
     * @return a list where the i-th element is the result (a string) of running the i-th
     * test case on the reference solution
     * @throws IOException if the path to the solution is invalid
     * @throws InterruptedException if the process is interrupted
     */
    public List<String> computeExpectedResults() throws IOException, InterruptedException {
        // Write an appropriate footer to the solution file to make it executable from
        // the command-line, if the footer doesn't exist already
        this.appendToSolution();

        // Run each test case on the solution file and gather the results in a map
        List<String> results = new ArrayList<>();
        for (int i = 0; i < this.tests.size(); i++) {
            List<String> args = this.getExpTestArgs(i);
            String result = this.runTestHelper(args);
            results.add(result);
        }

        // Write the expected results to a .py file, so that they can be accessed via
        // the wrapper. These cached results allow us to only run the solution once per
        // test rather than having to run it once per test per buggy implementation.
        this.outputExpectedResults(results);

        // Return the results
        return results;
    }

    /**
     * Runs all tests on all files in the directory of buggy implementations, comparing
     * the outputs to the pre-generated expected results and returning the results in the
     * form of a TestResults object.
     *
     * @return the results of testing
     * @throws IOException if the path to the directory of buggy implementations is
     *                     invalid
     * @throws InterruptedException if the process is interrupted
     */
    public TestResults runTests() throws IOException, InterruptedException {
        // Create the wrapper file
        this.createWrapperFile();

        // Initialize the outputs
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < this.tests.size(); i++) {
            caseToFiles.add(new HashSet<>());
        }
        Set<Integer> wrongSet = new HashSet<>();

        // Get the list of all files in the input directory
        var dir = new File(this.implDirPath);
        String[] filenames = dir.list();
        if (filenames == null) {
            throw new IOException("Error: implDir is empty");
        }
        Arrays.sort(filenames);

        // If implDirPath didn't actually point to a directory, files would be null
        int trueIndex = 0;

        // Test each individual file using all tests in the base test set
        for (String filename : filenames) {
            if (!filename.endsWith(".py") || filename.equals("wrapper.py")
                    || filename.equals("expected.py")) {
                continue;
            }

            // Run each test case on this file, keeping track of which ones caught
            // errors
            HashSet<Integer> caughtBy = new HashSet<>();
            for (int testIndex = 0; testIndex < this.tests.size(); testIndex++) {
                List<String> args = this.getTestArgs(testIndex, filename);
                String result = this.runTestHelper(args);
                if (!result.equals("True")) {
                    caseToFiles.get(testIndex).add(trueIndex);
                    caughtBy.add(testIndex);
                }
            }

            // Add to wrongSet if applicable
            if (caughtBy.size() > 0) {
                wrongSet.add(trueIndex);
            }
            trueIndex++;
        }

        // Clean up the pycache that was created
        this.deletePyCache();

        // Return the results
        return new TestResults(this.tests, caseToFiles, wrongSet);
    }

    /**
     * Builds the list of command-line arguments for executing the solution in order to
     * get the expected results on a single test case.
     *
     * @param testIndex the index of the test case to be run
     * @return the command-line args for running the given test case through the solution
     */
    private List<String> getExpTestArgs(int testIndex) {
        List<String> args = new ArrayList<>();

        // The solution must be a python3 file
        args.add("python3");
        args.add(this.solutionPath);

        // Add each argument as a string; the footer will take care of converting these to
        // Python objects before invoking the function under test
        TestCase test = this.tests.get(testIndex);
        for (APyObj arg : test.getArgs()) {
            args.add(arg.toString());
        }
        return args;
    }

    /**
     * Builds the list of command-line arguments for executing a buggy implementation in
     * order to get the actual results on a single test case.
     *
     * @param testIndex the index of the test case to be run
     * @param filename  the name of the implementation being tested
     * @return the command-line args for running the given test case through the solution
     */
    private List<String> getTestArgs(int testIndex, String filename) {
        List<String> args = new ArrayList<>();

        // The implementation must be a python3 file
        args.add("python3");

        // Directly invoking the wrapper, which will dynamically load the file under test
        args.add(this.implDirPath + "/wrapper.py");

        // Need to include the index of the test case so that we can look up the expected
        // results to determine whether the test passes or fails
        args.add(String.valueOf(testIndex));

        // Also need to know which file we're testing and which function to invoke within
        // the file under test
        args.add(filename);
        args.add(this.funcName);

        // Add each argument as a string; the footer will take care of converting these to
        // Python objects before invoking the function under test
        TestCase test = this.tests.get(testIndex);
        for (APyObj arg : test.getArgs()) {
            args.add(arg.toString());
        }
        return args;
    }

    /**
     * A helper function for runTest and runExpTest which runs a Python process (using a
     * list of arguments, as output by getTestArgs or getExpTestArgs) and reads its
     * output.
     *
     * @param args the arguments for the process to be created
     * @return the result of reading from the process
     * @throws IOException if the file to run or its output cannot be accessed
     * @throws InterruptedException if the process is interrupted
     */
    private String runTestHelper(List<String> args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(args);
        Process process = pb.start();

        // Redirect the output of the process to here
        var sb = new StringBuilder();
        var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Wait until the process has exited
        process.waitFor();

        // Read the output of the process, the last line of which should be the result
        String line;
        String prevLine = "";
        while ((line = reader.readLine()) != null) {
            prevLine = line;
        }
        reader.close();

        // Return the result
        return prevLine;
    }

    /**
     * Creates a wrapper file that imports the expected results, reads the command-line
     * args, dynamically imports the buggy implementation, generates the actual results
     * for a single test case, compares the returned value to the expected value, and then
     * returns a boolean value (True if test passes, False otherwise).
     *
     * @throws IOException if the wrapper file cannot be created
     */
    private void createWrapperFile() throws IOException {
        StringBuilder sb = new StringBuilder();

        // Import the expected results, plus the other modules we'll need
        sb.append("import sys\nfrom importlib import import_module\nfrom expected " +
                "import results\n\n");

        // Function for comparing the buggy implementation's results to the
        // pre-determined expected results
        sb.append("def test_buggy_impl(case_num, impl_name, fname, args):\n");
        sb.append("    mod_name = impl_name[:-3]\n");
        sb.append("    mod = import_module(mod_name)\n");
        sb.append("    func = getattr(mod, fname)\n");
        sb.append("    actual = func(*args)\n");
        sb.append("    expected = results[case_num]\n");
        sb.append("    return (actual == expected)\n\n");

        // Footer to make the function executable from the command line
        sb.append("if __name__ == \"__main__\":\n");
        sb.append("    case_num = int(sys.argv[1])\n");
        sb.append("    impl_name = sys.argv[2]\n");
        sb.append("    fname = sys.argv[3]\n");
        sb.append("    args = sys.argv[4:]\n");
        sb.append("    args = [eval(arg) for arg in args]\n");
        sb.append("    print (test_buggy_impl(case_num, impl_name, fname, args))");
        String wrapperContents = sb.toString();

        // Create the Python wrapper file including the above code
        FileWriter writer = new FileWriter(this.implDirPath + "/wrapper.py");
        writer.write(wrapperContents);
        writer.close();
    }

    /**
     * Writes a footer to the solution file which converts the command-line args from
     * strings into Python objects of the appropriate type, calls the function under test
     * with arguments, and prints the result.
     *
     * @throws IOException if the solution file cannot be accessed
     */
    private void appendToSolution() throws IOException {
        // Read the contents of the solution
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(this.solutionPath));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String contents = sb.toString();
        reader.close();

        // Generate the footer, which converts the command-line args from strings into
        // Python objects of the appropriate types, calls the function under test with
        // these arguments, and prints the result
        sb = new StringBuilder();
        sb.append("import sys\n\n");
        sb.append("if __name__ == \"__main__\":\n");
        sb.append("    args = sys.argv[1:]\n");
        sb.append("    new_args = [eval(arg) for arg in args]\n");
        sb.append("    print (repr(").append(this.funcName).append("(*new_args)))");
        String textToAdd = sb.toString();

        if (!contents.contains("import sys")) {
            // If no footer is present, append it to the file
            FileWriter writer = new FileWriter(this.solutionPath, true);
            writer.write("\n");
            writer.write(textToAdd);
            writer.close();
        } else {
            // If a footer is present, overwrite it
            String keepContents = contents.split("import sys")[0];
            FileWriter writer = new FileWriter(this.solutionPath);
            writer.write(keepContents);
            writer.write(textToAdd);
            writer.close();
        }
    }

    /**
     * Outputs the expected results (given) to the file expected.py in the form of a
     * Python list.
     *
     * @param results the per-case list of expected results
     * @throws IOException if the results file cannot be created or written to
     */
    private void outputExpectedResults(List<String> results) throws IOException {
        // Convert input results to Python list
        String contents = "results = " + results.toString();

        // Output results to expected.py, within the implementation directory
        FileWriter writer = new FileWriter(this.implDirPath + "/expected.py");
        writer.write(contents);
        writer.close();
    }

    /**
     * Helper function for deleting all cached Python files, so that an old cached version
     * of expected.pyc doesn't accidentally get invoked.
     *
     * @throws IOException if the path to the pycache is invalid or a deletion operation
     *                     fails
     */
    private void deletePyCache() throws IOException {
        // Get the list of all files in the pycache
        File pyCacheDir = new File(this.implDirPath + "/__pycache__/");
        String[] filepaths = pyCacheDir.list();

        if (filepaths != null) {
            for (String filepath : filepaths) {
                // Only delete .pyc files
                if (filepath.contains(".pyc")) {
                    File cachedFile =
                            new File(this.implDirPath + "/__pycache__/" + filepath);
                    if (!cachedFile.delete()) {
                        throw new IOException("could not delete cached file " + filepath);
                    }
                }
            }
        }
    }
}