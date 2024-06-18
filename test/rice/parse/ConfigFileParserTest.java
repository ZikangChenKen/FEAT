package test.rice.parse;

import main.rice.node.*;
import main.rice.obj.*;
import main.rice.parse.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the ConfigFileParser class.
 */
class ConfigFileParserTest {

    /**
     * A ConfigFileParser to be used for testing.
     */
    private static final ConfigFileParser parser = new ConfigFileParser();

    /**
     * Config file text and expected results of parsing for a function with one boolean
     * parameter.
     */
    private static String oneBoolConfig;
    private static final List<APyNode<?>> oneBoolNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one integer
     * parameter.
     */
    private static String oneIntConfig;
    private static final List<APyNode<?>> oneIntNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one integer
     * parameter, where the exhaustive domain is expressed using an explicit list.
     */
    private static String oneIntExplicitConfig;
    private static final List<APyNode<?>> oneIntExplicitNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one float
     * parameter.
     */
    private static String oneFloatConfig;
    private static final List<APyNode<?>> oneFloatNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one float
     * parameter, where the exhaustive domain is expressed using an explicit list.
     */
    private static String oneFloatExplicitConfig;
    private static final List<APyNode<?>> oneFloatExplicitNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one string
     * parameter.
     */
    private static String oneStringConfig;
    private static final List<APyNode<?>> oneStringNodes = new ArrayList<>();

    /**
     * Config file text (two versions -- one using an explicit domain and one using a
     * range --) and expected results of parsing for a function with one list parameter.
     */
    private static String oneListConfig;
    private static String oneListExplicitConfig;
    private static final List<APyNode<?>> oneListNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one tuple
     * parameter.
     */
    private static String oneTupConfig;
    private static final List<APyNode<?>> oneTupNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one set
     * parameter.
     */
    private static String oneSetConfig;
    private static final List<APyNode<?>> oneSetNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with one dict
     * parameter.
     */
    private static String oneDictConfig;
    private static final List<APyNode<?>> oneDictNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with multiple
     * simple parameters.
     */
    private static String multipleSimpleConfig;
    private static List<APyNode<?>> multipleSimpleNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with multiple
     * nested parameters.
     */
    private static String multipleNestedConfig;
    private static List<APyNode<?>> multipleNestedNodes = new ArrayList<>();

    /**
     * Config file text and expected results of parsing for a function with a single
     * parameter involving nested dictionaries.
     */
    private static String nestedDictConfig;
    private static List<APyNode<?>> nestedDictNodes;

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        setUpOneBool();
        setUpOneInt();
        setUpOneIntExplicit();
        setUpOneFloat();
        setUpOneFloatExplicit();
        setUpOneString();
        setUpOneList();
        setUpOneTup();
        setUpOneSet();
        setUpOneDict();
        setUpMultipleSimple();
        setUpMultipleNested();
        setUpNestedDict();
    }

    /** COARSE-GRAINED INVALIDITY CHECKS (at the level of parsing the JSON) **/
    /**
     * Tests case where the config file's contents do not form a valid JSON object.
     */
    @Test
    void testConfigNotJSON() {
        String config = "abc";
        invalidConfigHelper(config);
    }

    /**
     * Tests case where the fname key is missing; should throw an InvalidConfigException.
     */
    @Test
    void testMissingFname() {
        String config = "{\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": 4"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the types key is missing; should throw an InvalidConfigException.
     */
    @Test
    void testMissingTypes() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": 4"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the exDomain key is missing; should throw an
     * InvalidConfigException.
     */
    @Test
    void testMissingExDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": 4"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the ranDomain key is missing; should throw an
     * InvalidConfigException.
     */
    @Test
    void testMissingRanDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"num random\": 15"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the numRand key is missing; should throw an
     * InvalidConfigException.
     */
    @Test
    void testMissingNumRand() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the fname key is not a string; should throw an
     * InvalidConfigException.
     */
    @Test
    void testFnameNotString() {
        String config = "{\n\t\"fname\": 1"
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": 15"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the types key is not an array; should throw an
     * InvalidConfigException.
     */
    @Test
    void testTypesNotArray() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": \"int\""
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": 6"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the exDomain key is not an array; should throw an
     * InvalidConfigException.
     */
    @Test
    void testExDomainNotArray() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": \"0~1\""
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": 6"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the ranDomain key is not an array; should throw an
     * InvalidConfigException.
     */
    @Test
    void testRanDomainNotArray() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": \"3~5\""
                + ",\n\t\"num random\": 30"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where the numRand key is not an integer; should throw an
     * InvalidConfigException.
     */
    @Test
    void testNumRandNotInt() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": \"30\""
                + "\n}";

        invalidConfigHelper(config);
    }

    /** FINER-GRAINED INVALIDITY CHECKS **/
    /**
     * Tests case where there's a spurious parenthesis in types; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousParenTypes() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list((int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 1000"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious space in types; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousSpaceTypes() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"li st(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 1000"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious parenthesis in exDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousParenExDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2((0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 3"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious space in exDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousSpaceExDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~1 0\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 3"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious parenthesis in ranDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousParenRanDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10((0~100\"]"
                + ",\n\t\"num random\": 19"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious space in ranDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousSpaceRanDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~1 00\"]"
                + ",\n\t\"num random\": 19"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious parenthesis at the end of types; should
     * throw an InvalidConfigException.
     */
    @Test
    void testSpuriousParenEndTypes() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int( \"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 100"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious parenthesis at the end of exDomain; should
     * throw an InvalidConfigException.
     */
    @Test
    void testSpuriousParenEndExDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10( \"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 100"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious parenthesis at the end of ranDomain; should
     * throw an InvalidConfigException.
     */
    @Test
    void testSpuriousParenEndRanDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100( \"]"
                + ",\n\t\"num random\": 1"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a missing parenthesis in types; should throw an
     * InvalidConfigException.
     */
    @Test
    void testMissingParenTypes() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 1"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a missing parenthesis in exDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testMissingParenExDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2 0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100\"]"
                + ",\n\t\"num random\": 19"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a missing parenthesis in ranDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testMissingParenRanDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"list(int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10\"]"
                + ",\n\t\"random domain\": [\"0~10 0~100\"]"
                + ",\n\t\"num random\": 19"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious colon in types; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousColonTypes() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"dict(int::int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10:0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100:0~100\"]"
                + ",\n\t\"num random\": 100"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious colon in exDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousColonExDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"dict(int:int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10::0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100:0~100\"]"
                + ",\n\t\"num random\": 100"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious colon in ranDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousColonRanDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"dict(int:int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10:0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100::0~100\"]"
                + ",\n\t\"num random\": 3"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious colon at the end of types; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousColonEndTypes() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"dict(int:int:\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10:0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100:0~100\"]"
                + ",\n\t\"num random\": 1000"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious colon at the end of exDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousColonEndExDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"dict(int:int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10:0~10:\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100:0~100\"]"
                + ",\n\t\"num random\": 30"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where there's a spurious colon at the end of ranDomain; should throw an
     * InvalidConfigException.
     */
    @Test
    void testSpuriousColonEndRanDomain() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"dict(int:int\"]"
                + ",\n\t\"exhaustive domain\": [\"0~2(0~10:0~10\"]"
                + ",\n\t\"random domain\": [\"0~10(0~100:0~100:\"]"
                + ",\n\t\"num random\": 6"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where an explicit list of values for an int contains a decimal value;
     * should throw an InvalidConfigException.
     */
    @Test
    void testIntValDecimal() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"int\"]"
                + ",\n\t\"exhaustive domain\": [\"[0, 1, 2.3]\"]"
                + ",\n\t\"random domain\": [\"0~1\"]"
                + ",\n\t\"num random\": 4"
                + "\n}";

        invalidConfigHelper(config);
    }

    /**
     * Tests case where an invalid type is found in types; should throw
     * an InvalidConfigException.
     */
    @Test
    void testUnexpectedType() {
        String config = "{\n\t\"fname\": \"broken\""
                + ",\n\t\"types\": [\"integer\"]"
                + ",\n\t\"exhaustive domain\": [\"0~1\"]"
                + ",\n\t\"random domain\": [\"3~5\"]"
                + ",\n\t\"num random\": 15"
                + "\n}";

        invalidConfigHelper(config);
    }

    /** TESTS OF VALID CONFIG FILES **/
    /**
     * Tests that the ConfigFileParser correctly parses numRandom.
     */
    @Test
    void testParseNumRandom() throws InvalidConfigException {
        assertEquals(4, parser.parse(oneIntConfig).getNumRand());
    }

    /**
     * Tests that the ConfigFileParser correctly parses fname.
     */
    @Test
    void testParseFname() throws InvalidConfigException {
        assertEquals("oneBool", parser.parse(oneBoolConfig).getFuncName());
    }

    /**
     * Tests that the ConfigFileParser correctly parses boolean types.
     */
    @Test
    void testParseTypesOneBool() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneBoolConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneBoolNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses integer types.
     */
    @Test
    void testParseTypesOneInt() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneIntConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneIntNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses float types.
     */
    @Test
    void testParseTypesOneFloat() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneFloatConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneFloatNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses string types.
     */
    @Test
    void testParseTypesOneString() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneStringConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneStringNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses list types.
     */
    @Test
    void testParseTypesOneList() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneListConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneListNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses tuple types.
     */
    @Test
    void testParseTypesOneTup() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneTupConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneTupNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses set types.
     */
    @Test
    void testParseTypesOneSet() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneSetConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneSetNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses dict types.
     */
    @Test
    void testParseTypesOneDict() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneDictConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), oneDictNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the types of multiple simple
     * nodes.
     */
    @Test
    void testParseTypesMultipleSimple() throws InvalidConfigException {
        ConfigFile retval = parser.parse(multipleSimpleConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), multipleSimpleNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the types of multiple nested
     * nodes.
     */
    @Test
    void testParseTypesMultipleNested() throws InvalidConfigException {
        ConfigFile retval = parser.parse(multipleNestedConfig);
        assertTrue(correctTreeStructure(retval.getNodes(), multipleNestedNodes));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of a boolean
     * node.
     */
    @Test
    void testParseExDomainOneBool() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneBoolConfig);
        assertTrue(correctDomains(retval.getNodes(), oneBoolNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of an
     * integer node.
     */
    @Test
    void testParseExDomainOneInt() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneIntConfig);
        assertTrue(correctDomains(retval.getNodes(), oneIntNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses an explicit exhaustive domain
     * (list of discrete values).
     */
    @Test
    void testParseExDomainOneIntExplicitVals() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneIntExplicitConfig);
        assertTrue(correctDomains(retval.getNodes(), oneIntExplicitNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of a float
     * node.
     */
    @Test
    void testParseExDomainOneFloat() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneFloatConfig);
        assertTrue(correctDomains(retval.getNodes(), oneFloatNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses an explicit exhaustive domain
     * (list of discrete values) for a float.
     */
    @Test
    void testParseExDomainOneFloatExplicitVals() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneFloatExplicitConfig);
        assertTrue(correctDomains(retval.getNodes(), oneFloatExplicitNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of a string
     * node.
     */
    @Test
    void testParseExDomainOneString() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneStringConfig);
        assertTrue(correctDomains(retval.getNodes(), oneStringNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of a list
     * node.
     */
    @Test
    void testParseExDomainOneList() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneListConfig);
        assertTrue(correctDomains(retval.getNodes(), oneListNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of a tuple
     * node.
     */
    @Test
    void testParseExDomainOneTup() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneTupConfig);
        assertTrue(correctDomains(retval.getNodes(), oneTupNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of a set
     * node.
     */
    @Test
    void testParseExDomainOneSet() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneSetConfig);
        assertTrue(correctDomains(retval.getNodes(), oneSetNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domain of a dict
     * node.
     */
    @Test
    void testParseExDomainOneDict() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneDictConfig);
        assertTrue(correctDomains(retval.getNodes(), oneDictNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domains of multiple
     * simple nodes.
     */
    @Test
    void testParseExDomainMultipleSimple() throws InvalidConfigException {
        ConfigFile retval = parser.parse(multipleSimpleConfig);
        assertTrue(correctDomains(retval.getNodes(), multipleSimpleNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the exhaustive domains of multiple
     * nested nodes.
     */
    @Test
    void testParseExDomainMultipleNested() throws InvalidConfigException {
        ConfigFile retval = parser.parse(multipleNestedConfig);
        assertTrue(correctDomains(retval.getNodes(), multipleNestedNodes, true));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of a bool node.
     */
    @Test
    void testParseRanDomainOneBool() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneBoolConfig);
        assertTrue(correctDomains(retval.getNodes(), oneBoolNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of an int node.
     */
    @Test
    void testParseRanDomainOneInt() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneIntConfig);
        assertTrue(correctDomains(retval.getNodes(), oneIntNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of a float
     * node.
     */
    @Test
    void testParseRanDomainOneFloat() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneFloatConfig);
        assertTrue(correctDomains(retval.getNodes(), oneFloatNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of a string
     * node.
     */
    @Test
    void testParseRanDomainOneString() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneStringConfig);
        assertTrue(correctDomains(retval.getNodes(), oneStringNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of a list node.
     */
    @Test
    void testParseRanDomainOneList() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneListConfig);
        assertTrue(correctDomains(retval.getNodes(), oneListNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses an explicit random domain (list of
     * discrete values).
     */
    @Test
    void testParseRanDomainOneListExplicitVals() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneListExplicitConfig);
        assertTrue(correctDomains(retval.getNodes(), oneListNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of a tuple
     * node.
     */
    @Test
    void testParseRanDomainOneTup() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneTupConfig);
        assertTrue(correctDomains(retval.getNodes(), oneTupNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of a set node.
     */
    @Test
    void testParseRanDomainOneSet() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneSetConfig);
        assertTrue(correctDomains(retval.getNodes(), oneSetNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domain of a dict node.
     */
    @Test
    void testParseRanDomainOneDict() throws InvalidConfigException {
        ConfigFile retval = parser.parse(oneDictConfig);
        assertTrue(correctDomains(retval.getNodes(), oneDictNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domains of multiple
     * simple nodes.
     */
    @Test
    void testParseRanDomainMultipleSimple() throws InvalidConfigException {
        ConfigFile retval = parser.parse(multipleSimpleConfig);
        assertTrue(correctDomains(retval.getNodes(), multipleSimpleNodes, false));
    }

    /**
     * Tests that the ConfigFileParser correctly parses the random domains of multiple
     * nested nodes.
     */
    @Test
    void testParseRanDomainMultipleNested() throws InvalidConfigException {
        ConfigFile retval = parser.parse(multipleNestedConfig);
        assertTrue(correctDomains(retval.getNodes(), multipleNestedNodes, false));
    }

    @Test
    void testParseExDomainNestedDicts() throws InvalidConfigException {
        ConfigFile retval = parser.parse(nestedDictConfig);
        assertTrue(correctDomains(retval.getNodes(), nestedDictNodes, true));
    }

    /**
     * Helper function for building a ConfigFile object.
     *
     * @param fname     name of the function under test
     * @param types     parameter types of the function under test
     * @param exDomain  exhaustive domains for the function under test
     * @param ranDomain random domains for the function under test
     * @param numRand   number of random tests to be generated
     * @return a ConfigFile encapsulated the input information
     */
    private static String buildConfigText(String fname, String types, String exDomain,
                                          String ranDomain, String numRand) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\t\"fname\": \"");
        sb.append(fname);
        sb.append("\",\n\t\"types\": ");
        sb.append(types);
        sb.append(",\n\t\"exhaustive domain\": ");
        sb.append(exDomain);
        sb.append(",\n\t\"random domain\": ");
        sb.append(ranDomain);
        sb.append(",\n\t\"num random\": ");
        sb.append(numRand);
        sb.append("\n}");
        return sb.toString();
    }

    /**
     * Sets up oneBoolConfig and oneBoolNodes.
     */
    private static void setUpOneBool() {
        // Build config file text
        oneBoolConfig = buildConfigText("oneBool", "[\"bool\"]",
                "[\"0~1\"]", "[\"0~1\"]", "1");

        // Build expected node
        PyBoolNode oneBoolNode = new PyBoolNode();
        List<Integer> exDomain = List.of(0, 1);
        oneBoolNode.setExDomain(exDomain);
        oneBoolNode.setRanDomain(exDomain);
        oneBoolNodes.add(oneBoolNode);
    }

    /**
     * Sets up oneIntConfig and oneIntNodes.
     */
    private static void setUpOneInt() {
        // Build config file text
        oneIntConfig = buildConfigText("oneInt", "[\"int\"]",
                "[\"-2~2\"]", "[\"-5~5\"]", "4");

        // Build expected node
        PyIntNode oneIntNode = new PyIntNode();
        oneIntNode.setExDomain(List.of(-2, -1, 0, 1, 2));
        oneIntNode.setRanDomain(List.of(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5));
        oneIntNodes.add(oneIntNode);
    }

    /**
     * Sets up oneIntExplicitConfig and oneIntExplicitNodes.
     */
    private static void setUpOneIntExplicit() {
        // Build config file text
        oneIntExplicitConfig = buildConfigText("oneInt", "[\"int\"]",
                "[\"[-2, -1, 1, 2]\"]", "[\"[-5, -4, -3, 3, 4, 5]\"]", "4");

        // Build expected node
        PyIntNode oneIntExplicitNode = new PyIntNode();
        oneIntExplicitNode.setExDomain(List.of(-2, -1, 1, 2));
        oneIntExplicitNode.setRanDomain(List.of(-5, -4, -3, 3, 4, 5));
        oneIntExplicitNodes.add(oneIntExplicitNode);
    }

    /**
     * Sets up oneFloatConfig and oneFloatNodes.
     */
    private static void setUpOneFloat() {
        // Build config file text
        oneFloatConfig = buildConfigText("oneFloat", "[\"float\"]",
                "[\"-2~2\"]", "[\"-5~5\"]", "4");

        // Build expected node
        PyFloatNode oneFloatNode = new PyFloatNode();
        oneFloatNode.setExDomain(List.of(-2.0, -1.0, 0.0, 1.0, 2.0));
        oneFloatNode.setRanDomain(List.of(
                -5.0, -4.0, -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0));
        oneFloatNodes.add(oneFloatNode);
    }

    /**
     * Sets up oneFloatExplicitConfig and oneFloatExplicitNodes.
     */
    private static void setUpOneFloatExplicit() {
        // Build config file text
        oneFloatExplicitConfig = buildConfigText("oneFloat", "[\"float\"]",
                "[\"[-1.3, 2.2, -10.0]\"]", "[\"[-2.3, 1.2, -9.0]\"]", "2");

        // Build expected node
        PyFloatNode oneFloatNode = new PyFloatNode();
        oneFloatNode.setExDomain(List.of(-1.3, 2.2, -10.0));
        oneFloatNode.setRanDomain(List.of(-2.3, 1.2, -9.0));
        oneFloatExplicitNodes.add(oneFloatNode);
    }

    /**
     * Sets up oneString and oneStringNodes.
     */
    private static void setUpOneString() {
        // Build config file text
        oneStringConfig = buildConfigText("oneString", "[\"str (!bC4\"]",
                "[\"1~3\"]", "[\"4~10\"]", "15");

        // Build expected node
        PyStringNode oneStringNode = new PyStringNode("!bC4");
        oneStringNode.setExDomain(List.of(1, 2, 3));
        oneStringNode.setRanDomain(List.of(4, 5, 6, 7, 8, 9, 10));
        oneStringNodes.add(oneStringNode);
    }

    /**
     * Sets up oneListConfig, oneListExplicitConfig, and oneListNodes.
     */
    private static void setUpOneList() {
        // Build config file text
        oneListConfig = buildConfigText("oneList", "[\"list (bool\"]",
                "[\"0~2 (0~1\"]", "[\"3~5 (0~1\"]", "6");

        // Build version of config file text that uses explicit domains
        oneListExplicitConfig = buildConfigText("oneListExplicit", "[\"list (bool\"]",
                "[\"[0, 1, 2] ([0, 1]\"]", "[\"[3, 4, 5] ([0, 1]\"]", "6");

        // Build inner boolean node
        PyBoolNode oneBoolNode = new PyBoolNode();
        List<Integer> exDomain = List.of(0, 1);
        oneBoolNode.setExDomain(exDomain);
        oneBoolNode.setRanDomain(exDomain);

        // Build outer list node
        PyListNode<PyBoolObj> oneListNode = new PyListNode<>(oneBoolNode);
        oneListNode.setExDomain(List.of(0, 1, 2));
        oneListNode.setRanDomain(List.of(3, 4, 5));
        oneListNodes.add(oneListNode);
    }

    /**
     * Sets up oneTup and oneTupNodes.
     */
    private static void setUpOneTup() {
        // Build config file text
        oneTupConfig = buildConfigText("oneTup", "[\"tuple(int\"]",
                "[\"0~2 (1~10\"]", "[\"0~10 (-10~20\"]", "30");

        // Build inner integer node
        PyIntNode innerIntNode = new PyIntNode();
        innerIntNode.setExDomain(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        innerIntNode.setRanDomain(List.of(-10, -9, -8, -7, -6, -5, -4, -3, -2,
                -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20));

        // Build outer tuple node
        PyTupleNode<PyIntObj> oneTupleNode = new PyTupleNode<>(innerIntNode);
        oneTupleNode.setExDomain(List.of(0, 1, 2));
        oneTupleNode.setRanDomain(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        oneTupNodes.add(oneTupleNode);
    }

    /**
     * Sets up oneSetConfig and oneSetNodes.
     */
    private static void setUpOneSet() {
        // Build config file text
        oneSetConfig = buildConfigText("oneSet", "[\"set  (float\"]",
                "[\"1~3 (7~8\"]", "[\"1~4 (12~750\"]", "1000");
        PyFloatNode innerFloatNode = new PyFloatNode();
        List<Double> domain = new ArrayList<>();
        for (int i = 12; i < 751; i++) {
            domain.add((double) i);
        }

        // Build inner float node
        innerFloatNode.setExDomain(List.of(7.0, 8.0));
        innerFloatNode.setRanDomain(domain);

        // Build outer set node
        PySetNode<PyFloatObj> oneSetNode = new PySetNode<>(innerFloatNode);
        oneSetNode.setExDomain(List.of(1, 2, 3));
        oneSetNode.setRanDomain(List.of(1, 2, 3, 4));
        oneSetNodes.add(oneSetNode);
    }

    /**
     * Sets up oneDictConfig and oneDictNodes.
     */
    private static void setUpOneDict() {
        // Build config file text
        oneDictConfig = buildConfigText("oneDict", "[\"dict (bool : int\"]",
                "[\"0~1 (0~0 : -7~8\"]", "[\"1~2 (0~1 : -70~80\"]", "3");

        // Build bool key node
        PyBoolNode keyNode = new PyBoolNode();
        keyNode.setExDomain(Collections.singletonList(0));
        keyNode.setRanDomain(List.of(0, 1));

        // Build int val node
        PyIntNode valNode = new PyIntNode();
        List<Integer> domain = new ArrayList<>();
        for (int i = -7; i < 9; i++) {
            domain.add(i);
        }
        valNode.setExDomain(domain);
        domain = new ArrayList<>();
        for (int i = -70; i < 81; i++) {
            domain.add(i);
        }
        valNode.setRanDomain(domain);

        // Build outer dict node
        PyDictNode<PyBoolObj, PyIntObj> oneDictNode = new PyDictNode<>(keyNode, valNode);
        oneDictNode.setExDomain(List.of(0, 1));
        oneDictNode.setRanDomain(List.of(1, 2));
        oneDictNodes.add(oneDictNode);
    }

    /**
     * Sets up multipleSimpleConfig and multipleSimpleNodes.
     */
    private static void setUpMultipleSimple() {
        // Build config file text
        multipleSimpleConfig = buildConfigText("oneMultipleSimple",
                "[\"float\", \"int\", \"bool\", \"int\"]",
                "[\"0~1\", \"2~3\", \"0~0\", \"2~3\"]",
                "[\"-1~1\", \"-2~2\", \"0~1\", \"-2~2\"]", "19");

        // Build first node: a bool
        PyBoolNode boolNode = new PyBoolNode();
        boolNode.setExDomain(Collections.singletonList(0));
        boolNode.setRanDomain(List.of(0, 1));

        // Build second node: a float
        PyFloatNode floatNode = new PyFloatNode();
        floatNode.setExDomain(List.of(0.0, 1.0));
        floatNode.setRanDomain(List.of(-1.0, 0.0, 1.0));

        // Build third node: an int
        PyIntNode intNode = new PyIntNode();
        intNode.setExDomain(List.of(2, 3));
        intNode.setRanDomain(List.of(-2, -1, 0, 1, 2));

        // Wrap nodes together in a single list
        multipleSimpleNodes = List.of(floatNode, intNode, boolNode, intNode);
    }

    /**
     * Sets up multipleNestedConfig and multipleNestedNodes.
     */
    private static void setUpMultipleNested() {
        // Build config file text
        multipleNestedConfig = buildConfigText("oneMultipleNested",
                "[\"dict(tuple   (bool:set(int\", \"list ( list ( list ( str (qWeRtY  "
                        + " \", \" set(tuple(float\"]",
                "[\"0~1(1~2   (0~1:2~3(3~4\", \"4~5 ( 5~6 ( 6~7 ( 7~8   \", \" 8~9"
                        + "(9~10(10~11\"]",
                "[\"0~2(1~3   (0~0:2~4(3~5\", \"4~6 ( 5~7 ( 6~8 ( 7~9   \", \" 8~10"
                        + "(9~11(10~12\"]",
                "100");

        // Build first node: dict(tuple(bool): set(int))
        // Build key node: a tuple(bool)
        PyBoolNode innerKeyNode = new PyBoolNode();
        innerKeyNode.setExDomain(List.of(0, 1));
        innerKeyNode.setRanDomain(Collections.singletonList(0));
        PyTupleNode<PyBoolObj> keyNode2 = new PyTupleNode<>(innerKeyNode);
        keyNode2.setExDomain(List.of(1, 2));
        keyNode2.setRanDomain(List.of(1, 2, 3));

        // Build val node: a set(int)
        PyIntNode innerValNode = new PyIntNode();
        innerValNode.setExDomain(List.of(3, 4));
        innerValNode.setRanDomain(List.of(3, 4, 5));
        PySetNode<PyIntObj> valNode2 = new PySetNode<>(innerValNode);
        valNode2.setExDomain(List.of(2, 3));
        valNode2.setRanDomain(List.of(2, 3, 4));

        // Build outer dict node
        PyDictNode<PyTupleObj<PyBoolObj>, PySetObj<PyIntObj>> dictNode =
                new PyDictNode<>(keyNode2, valNode2);
        dictNode.setExDomain(List.of(0, 1));
        dictNode.setRanDomain(List.of(0, 1, 2));

        // Build second node: list(list(list(str)))
        // Build fourth (innermost) layer of nesting: str
        PyStringNode innerStringNode = new PyStringNode("qWeRtY");
        innerStringNode.setExDomain(List.of(7, 8));
        innerStringNode.setRanDomain(List.of(7, 8, 9));

        // Build third layer of nesting: list(str)
        PyListNode<PyStringObj> list3Node =
                new PyListNode<>(innerStringNode);
        list3Node.setExDomain(List.of(6, 7));
        list3Node.setRanDomain(List.of(6, 7, 8));

        // Build second layer of nesting: list(list(str))
        PyListNode<PyListObj<PyStringObj>> list2Node =
                new PyListNode<>(list3Node);
        list2Node.setExDomain(List.of(5, 6));
        list2Node.setRanDomain(List.of(5, 6, 7));

        // Build outer list node (list(list(list(str))))
        PyListNode<PyListObj<PyListObj<PyStringObj>>>
                outerListNode = new PyListNode<>(list2Node);
        outerListNode.setExDomain(List.of(4, 5));
        outerListNode.setRanDomain(List.of(4, 5, 6));

        // Build third node: set(tuple(float))
        // Build third (innermost) layer of nesting: float
        PyFloatNode innerFloatNode = new PyFloatNode();
        innerFloatNode.setExDomain(List.of(10.0, 11.0));
        innerFloatNode.setRanDomain(List.of(10.0, 11.0, 12.0));

        // Build second layer of nesting: tuple(float)
        PyTupleNode<PyFloatObj> innerTupleNode = new PyTupleNode<>(innerFloatNode);
        innerTupleNode.setExDomain(List.of(9, 10));
        innerTupleNode.setRanDomain(List.of(9, 10, 11));

        // Build outer set node (set(tuple(float)))
        PySetNode<PyTupleObj<PyFloatObj>> setNode = new PySetNode<>(innerTupleNode);
        setNode.setExDomain(List.of(8, 9));
        setNode.setRanDomain(List.of(8, 9, 10));

        // Wrap nodes together in a single list
        multipleNestedNodes = List.of(dictNode, outerListNode, setNode);
    }

    /**
     * Sets up nestedDictConfig and nestedDictNodes.
     */
    private static void setUpNestedDict() {
        nestedDictConfig = "{\n\t\"fname\": \"nested\""
                + ",\n\t\"types\": [\"dict(int:dict(tuple(int:dict(bool:int\"]"
                + ",\n\t\"exhaustive domain\": [\"1~2(3~4:5~6(7~8(9~10:11~12(0~1:13~14\"]"
                + ",\n\t\"random domain\": [\"1~2(1~2:1~2(1~2(1~2:1~2(0~1:1~2\"]"
                + ",\n\t\"num random\": 1"
                + "\n}";

        nestedDictNodes = new ArrayList<>();

        // Create the key and value nodes within the innermost (third) dict node
        PyIntNode lastVal = new PyIntNode();
        lastVal.setExDomain(List.of(13, 14));
        PyBoolNode lastKey = new PyBoolNode();
        lastKey.setExDomain(List.of(0, 1));

        // Create the innermost (third) dict node
        PyDictNode<PyBoolObj, PyIntObj> lastDict = new PyDictNode<>(lastKey, lastVal);
        lastDict.setExDomain(List.of(11, 12));

        // Create the key node for the middle (second) dict node
        PyIntNode secondLastKeyInner = new PyIntNode();
        secondLastKeyInner.setExDomain(List.of(9, 10));
        PyTupleNode<PyIntObj> secondLastKey = new PyTupleNode<>(secondLastKeyInner);
        secondLastKey.setExDomain(List.of(7, 8));

        // Create the middle (second) dict node
        PyDictNode<PyTupleObj<PyIntObj>, PyDictObj<PyBoolObj, PyIntObj>> secondLastDict =
                new PyDictNode<>(secondLastKey, lastDict);
        secondLastDict.setExDomain(List.of(5, 6));

        // Create the overarching PyDictNode
        PyIntNode firstKey = new PyIntNode();
        firstKey.setExDomain(List.of(3, 4));
        PyDictNode<PyIntObj,
                PyDictObj<PyTupleObj<PyIntObj>, PyDictObj<PyBoolObj, PyIntObj>>> outerDict =
                new PyDictNode<>(firstKey, secondLastDict);
        outerDict.setExDomain(List.of(1, 2));
        nestedDictNodes.add(outerDict);
    }

    /**
     * Returns true if exactly one of {o1, o2} is null; false otherwise.
     *
     * @param o1 an arbitrary object
     * @param o2 another arbitrary object
     * @return true if exactly one of {o1, o2} is null
     */
    private static boolean exactlyOneNull(Object o1, Object o2) {
        return (o1 == null && o2 != null) || (o1 != null && o2 == null);
    }

    /**
     * Helper function for recursively checking that a single node from the actual result
     * has the correct tree structure (identical to the corresponding node of the expected
     * result).
     *
     * @param actual   actual result of parsing
     * @param expected expected result of parsing
     * @return true if actual and expected have the same structure; false otherwise
     */
    private static boolean correctTreeStructureHelper(APyNode<?> actual,
                                               APyNode<?> expected) {
        // Compare the types of the roots of actual and expected
        if (actual.getClass() != expected.getClass()) {
            return false;
        }

        // Make sure actual and expected have the same number of children
        if (exactlyOneNull(actual.getLeftChild(), expected.getLeftChild()) ||
                exactlyOneNull(actual.getRightChild(), expected.getRightChild())) {
            return false;
        }

        // Recursively check the structure of the left subtree
        if (actual.getLeftChild() != null && (!correctTreeStructureHelper(
                actual.getLeftChild(), expected.getLeftChild()))) {
            return false;
        }

        // Recursively check the structure of the right subtree
        return actual.getRightChild() == null || (correctTreeStructureHelper(
                actual.getRightChild(), expected.getRightChild()));
    }

    /**
     * Helper function for recursively checking each node of the actual result of parsing
     * to ensure that they all have the correct structure (identical to that of the
     * expected result).
     *
     * @param actual   actual result of parsing
     * @param expected expected result of parsing
     * @return true if actual and expected have the same structure; false otherwise
     */
    private static boolean correctTreeStructure(List<APyNode<?>> actual,
                                         List<APyNode<?>> expected) {
        // Make sure there are the expected number of nodes
        if (actual.size() != expected.size()) {
            return false;
        }

        // Make sure each node is of the correct structure
        for (int i = 0; i < actual.size(); i++) {
            if (!correctTreeStructureHelper(actual.get(i), expected.get(i))) {
                return false;
            }
        }

        // No issues!
        return true;
    }

    /**
     * Helper function for checking that two domains are equal. Doesn't care about order
     * or type (as long as it's a Number).
     *
     * @param domain1 first domain being compared
     * @param domain2 second domain being compared
     * @return true if domain1 and domain2 are equivalent; false otherwise
     */
    private static boolean domainEquals(List<? extends Number> domain1, List<? extends Number> domain2) {
        return domain1.size() == domain2.size() &&
                new HashSet<>(domain1).equals(new HashSet<>(domain2));
    }

    /**
     * Helper function for recursively checking that a single node from the actual result
     * has the correct domain (identical to the corresponding domain of the expected
     * result).
     *
     * @param actual   actual result of parsing
     * @param expected expected result of parsing
     * @param exDomain true if we're checking the exDomains; false if we're checking the
     *                 ranDomains
     * @return true if the actual and expected domains are equivalent; false otherwise
     */
    private static boolean correctDomainsHelper(APyNode<?> actual,
                                         APyNode<?> expected, boolean exDomain) {

        // Compare the domains of the roots of actual and expected
        if (exDomain && !domainEquals(actual.getExDomain(), expected.getExDomain())) {
            return false;
        } else if (!exDomain && !domainEquals(actual.getRanDomain(), (expected.getRanDomain()))) {
            return false;
        }

        // Make sure actual and expected have the same number of children
        if (exactlyOneNull(actual.getLeftChild(), expected.getLeftChild()) ||
                exactlyOneNull(actual.getRightChild(), expected.getRightChild())) {
            return false;
        }

        // Recursively check the domains of the left subtree
        if (actual.getLeftChild() != null && (!correctDomainsHelper(actual.getLeftChild(),
                expected.getLeftChild(), exDomain))) {
            return false;
        }

        // Recursively check the domains of the right subtree
        return actual.getRightChild() == null || (correctDomainsHelper(
                actual.getRightChild(), expected.getRightChild(), exDomain));
    }

    /**
     * Helper function for recursively checking each node of the actual result of parsing
     * to ensure that they all have the correct domains (equivalent to that of the
     * expected result).
     *
     * @param actual   actual result of parsing
     * @param expected expected result of parsing
     * @param exDomain true if we're checking the exDomains; false if we're checking the
     *                 ranDomains
     * @return true if actual and expected have equivalent domains; false otherwise
     */
    private static boolean correctDomains(List<APyNode<?>> actual,
                                   List<APyNode<?>> expected, boolean exDomain) {
        if (actual.size() != expected.size()) {
            return false;
        }

        for (int i = 0; i < actual.size(); i++) {
            if (!correctDomainsHelper(actual.get(i), expected.get(i), exDomain)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper function for testing malformed inputs; wraps the call to parse in a
     * try-catch block and only passes if an InvalidConfigException is thrown.
     *
     * @param contents the contents of the config file to be parsed
     */
    private static void invalidConfigHelper(String contents) {
        try {
            parser.parse(contents);
        } catch (InvalidConfigException e) {
            return;
        }
        fail();
    }
}
