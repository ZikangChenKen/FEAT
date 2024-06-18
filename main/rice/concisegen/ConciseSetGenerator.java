package main.rice.concisegen;

import main.rice.test.TestCase;
import main.rice.test.TestResults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stateless class that is a wrapper for a greedy approximation of the set cover
 * algorithm. Running the hitting set algorithm on the results of testing yields a
 * "concise" (approximately minimal) set of test cases that still catches every known
 * (based on the base test set) incorrect implementation.
 */
public class ConciseSetGenerator {

    /**
     * A greedy approximation of the set cover algorithm. Given a set of incorrect
     * implementations (S), a set of test cases (B), and list "mapping" each test case
     * (index i) to the set of implementations (Si) that it caught -- all contained within
     * results --, finds an approximately minimal subset of B (M) such that all of the
     * incorrect implementations are caught by at least one element in M.
     *
     * @param results the results of running all tests in B on all files in S
     * @return M, a set of test cases that is an approximately minimal set covering
     */
    public static Set<TestCase> setCover(TestResults results) {

        // Initialize the structure to return
        HashSet<TestCase> hittingSet = new HashSet<>();

        // We'll be mutating both caseToFiles and wrongSet, so make copies
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (Set<Integer> set : results.getCaseToFiles()) {
            caseToFiles.add(new HashSet<>(set));
        }
        Set<Integer> wrongSet = new HashSet<>(results.getWrongSet());

        // Select tests until we've "covered" every program that was initially in the
        // wrong set
        while (wrongSet.size() > 0) {
            int maxFilesCaught = 0;
            int caseOfMaxFiles = -1;

            // Find the test case that covers the most heretofore-uncovered files
            for (int caseIndex = 0; caseIndex < caseToFiles.size(); caseIndex++) {
                Set<Integer> filesCaught = caseToFiles.get(caseIndex);
                if (filesCaught.size() > maxFilesCaught) {
                    maxFilesCaught = filesCaught.size();
                    caseOfMaxFiles = caseIndex;
                }
            }

            // Remove all covered files from the wrongSet
            HashSet<Integer> coveredFiles =
                new HashSet<>(caseToFiles.get(caseOfMaxFiles));
            wrongSet.removeAll(coveredFiles);

            // Update the caseToFiles mapping such that the set of files caught by each
            // test no longer includes the newly-covered files
            for (Set<Integer> filesCaught : caseToFiles) {
                filesCaught.removeAll(coveredFiles);
            }

            // Add the selected test case to the hitting set
            hittingSet.add(results.getTestCase(caseOfMaxFiles));
        }

        // Return the hitting set
        return hittingSet;
    }
}