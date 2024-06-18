package main.rice.parse;

// TODO: implement the ConfigFileParser class here

import main.rice.node.*;
import org.json.*;

import java.io.*;
import java.util.ArrayList;
import java.util.*;

/**
 * this class represents the config file parser
 */
public class ConfigFileParser{

    /**
     * Reads and returns the contents of the file located at the input filepath; throws an
     * IOException if the file does not exist or cannot be read.
     * @param filepath the input filepath
     * @return the contents of the file located at the input filepath
     * @throws IOException
     */
    public String readFile(String filepath) throws IOException {
        File file = new File(filepath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;

        // read the contents of the file
        while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Parses the input string, which should be the contents of a JSON file formatted according to
     * the config file specifications.
     * @param contents the input contents of a JSON file
     * @return result of parsing input contents
     * @throws InvalidConfigException
     */
    public ConfigFile parse(String contents) throws InvalidConfigException{

        // parse the input string
        JSONObject content;
        String fname;
        JSONArray types;
        JSONArray exhaustiveD;
        JSONArray randomD;
        int numRand;

        // initialize the content and throw exception if needed
        try{
            content = new JSONObject(contents);
            fname = (String)content.get("fname");
            types = (JSONArray) content.get("types");
            exhaustiveD = (JSONArray) content.get("exhaustive domain");
            randomD = (JSONArray) content.get("random domain");
            numRand = (int) content.get("num random");
        } catch (Exception e){
            throw new InvalidConfigException("invalid file");
        }

        // check numRand
        if (numRand < 0){
            throw new InvalidConfigException("Invalid numRand");
        }

        // check length
        if(exhaustiveD.length() != types.length() || exhaustiveD.length() != randomD.length() ||
                types.length()!= randomD.length()){
            throw new InvalidConfigException("invalid file");
        }

        List<APyNode<?>> nodes = new ArrayList<>();
        for (int i=0; i< types.length();i++){
            nodes.add(parseTypeElem((String)types.get(i), (String)exhaustiveD.get(i), (String)randomD.get(i)));
        }
        return new ConfigFile(fname, nodes, numRand);
    }

    /**
     * parse the type into a PyNode.
     * @param elem the input string to be parsed
     * @param exDomain the input string represents the exhaustive domain
     * @param ranDomain the input string represents the random domain
     * @return a PyNodes that the input types represent
     */
    protected APyNode<?> parseTypeElem(String elem, String exDomain, String ranDomain) throws InvalidConfigException{
        elem.strip();
        exDomain.strip();
        ranDomain.strip();
        int idx = elem.indexOf("(");
        APyNode<?> node = null;

        // simple type
        if (idx == -1){

            // int type
            if (elem.equals("int")){
                node = new PyIntNode();
                this.parseDomain(exDomain,ranDomain,node);
            }

            // float type
            else if (elem.equals("float")){
                node = new PyFloatNode();
                this.parseDomain(exDomain, ranDomain, node);
            }

            // bool type
            else if (elem.equals("bool")){
                node = new PyBoolNode();
                this.parseDomain(exDomain,ranDomain,node);
            }

            else{
                throw new InvalidConfigException("invalid type");
            }
        }

        // string type
        else if (elem.substring(0, idx).strip().equals("str")){
            node = new PyStringNode(elem.substring(idx+1).strip());
            this.parseDomain(exDomain,ranDomain,node);
        }

        // dict type
        else if (elem.substring(0, idx).strip().equals("dict")){
            int idx2 = elem.indexOf(":");
            int exIdx1 = exDomain.indexOf("(");
            int exIdx2 = exDomain.indexOf(":");
            int ranIdx1 = ranDomain.indexOf("(");
            int ranIdx2 = ranDomain.indexOf(":");

            if (idx2 == -1 || exIdx1 == -1 || exIdx2 == -1 || exIdx1 > exIdx2){
                throw new InvalidConfigException("invalid exhaustive type");
            }
            else if (ranIdx1 == -1 || ranIdx2 == -1 || ranIdx1 > ranIdx2){
                throw new InvalidConfigException("invalid random type");
            }

            else{
                String leftElem = elem.substring(idx+1, idx2).strip();
                String leftEx = exDomain.substring(exIdx1+1, exIdx2).strip();
                String leftRan = ranDomain.substring(ranIdx1+1, ranIdx2).strip();
                APyNode<?> leftChild = parseTypeElem(leftElem, leftEx, leftRan);
                String rightElem = elem.substring(idx2+1).strip();
                String rightEx = exDomain.substring(exIdx2+1).strip();
                String rightRan = ranDomain.substring(ranIdx2+1).strip();
                APyNode<?> rightChild = parseTypeElem(rightElem, rightEx, rightRan);
                node = new PyDictNode<>(leftChild, rightChild);
                this.parseDomain(exDomain.substring(0, exIdx1).strip(), ranDomain.substring(0,ranIdx1).strip(), node);
            }

        }

        // iterable type
        else{
            // list type
            int exIdx = exDomain.indexOf("(");
            int ranIdx = ranDomain.indexOf("(");
            if (exIdx == -1||ranIdx==-1){
                throw new InvalidConfigException("invalid input");
            }
            if (elem.substring(0, idx).strip().equals("list")){
                APyNode<?> leftChild = parseTypeElem(elem.substring(idx+1).strip(),
                        exDomain.substring(exIdx+1).strip(), ranDomain.substring(ranIdx+1).strip());
                node = new PyListNode<>(leftChild);
                this.parseDomain(exDomain.substring(0,exIdx).strip(), ranDomain.substring(0,ranIdx).strip(), node);
            }

            // tuple type
            else if (elem.substring(0, idx).strip().equals("tuple")){
                APyNode<?> leftChild = parseTypeElem(elem.substring(idx+1).strip(),
                        exDomain.substring(exIdx+1).strip(), ranDomain.substring(ranIdx+1).strip());
                node = new PyTupleNode<>(leftChild);
                this.parseDomain(exDomain.substring(0,exIdx).strip(), ranDomain.substring(0,ranIdx).strip(), node);
            }

            // set type
            else if (elem.substring(0, idx).strip().equals("set")){
                APyNode<?> leftChild = parseTypeElem(elem.substring(idx+1).strip(),
                        exDomain.substring(exIdx+1).strip(), ranDomain.substring(ranIdx+1).strip());
                node = new PySetNode<>(leftChild);
                this.parseDomain(exDomain.substring(0,exIdx).strip(), ranDomain.substring(0,ranIdx).strip(), node);
            }

            else{
                throw new InvalidConfigException("invalid type");
            }
        }
        return node;
    }

    /**
     * parse the domain to set for the node.
     * @param exDomain the input exDomain string to be parsed
     * @param ranDomain the input ranDomain string to be parsed
     * @param node the input node to be set domain
     */
    protected void parseDomain(String exDomain, String ranDomain, APyNode<?> node) throws InvalidConfigException{
        exDomain.strip();
        ranDomain.strip();
        int idxEx = exDomain.indexOf("~");
        int idxRan = ranDomain.indexOf("~");

        // range representation
        if (idxEx > -1){
            int lower = this.parseInt(exDomain.substring(0,idxEx).strip());
            int upper = this.parseInt(exDomain.substring(idxEx+1).strip());
            // check upper > lower
            if (upper < lower){
                throw new InvalidConfigException("upper<lower");
            }

            // if AIterableNode
            if (node instanceof AIterablePyNode<?,?> || node instanceof PyDictNode<?,?>){
                if (lower<0){
                    throw new InvalidConfigException("negative length");
                }
            }
            node.setExDomain(this.parseDomainRange(lower,upper,node));
        }
        // list representation
        else{
            node.setExDomain(this.parseDomainList(exDomain,node));
        }

        // range representation
        if (idxRan > -1){
            int lower = this.parseInt(ranDomain.substring(0,idxRan).strip());
            int upper = this.parseInt(ranDomain.substring(idxRan+1).strip());
            // check upper > lower
            if (upper < lower){
                throw new InvalidConfigException("upper<lower");
            }

            // if AIterableNode
            if (node instanceof AIterablePyNode<?,?> || node instanceof PyDictNode<?,?>){
                if (lower<0){
                    throw new InvalidConfigException("negative length");
                }
            }
            node.setRanDomain(this.parseDomainRange(lower,upper,node));
        }
        // list representation
        else{
            node.setRanDomain(this.parseDomainList(ranDomain,node));
        }
    }

    /**
     * parse the domain to set for the node.
     * @param domain the input exDomain string to be parsed
     * @param node the input node
     * @return a list of number representing the domain
     */
    protected List<Number> parseDomainList(String domain, APyNode<?> node) throws InvalidConfigException{
        List<Number> result = new ArrayList<>();
        int idx1 = domain.indexOf("[");
        int idx2 = domain.indexOf("]");
        domain.strip();

        // check the brackets
        if (idx1 != 0 || idx2 != domain.length()-1){
            throw new InvalidConfigException("invalid domain");
        }

        String[] elems = domain.substring(1,domain.length()-1).strip().split(",");

        // PyBoolNode
        if (node instanceof PyBoolNode){
            for (String elem: elems){
                if (this.parseInt(elem) != 0 && this.parseInt(elem) != 1){
                    throw new InvalidConfigException("invalid input");
                }
                else{
                    result.add(this.parseInt(elem));
                }
            }
        }

        // PyIntNode
        else if (node instanceof PyIntNode){
            for (String elem:elems){
                result.add(this.parseInt(elem));
            }
        }

        //PyFloatNode
        else if (node instanceof PyFloatNode){
            for (String elem:elems){
                result.add(this.parseDouble(elem));
            }
        }

        // Iterable Node
        else if (node instanceof AIterablePyNode<?,?> || node instanceof PyDictNode<?,?>){
            for (String elem:elems){
                int num = this.parseInt(elem);
                if(num<0){
                    throw new InvalidConfigException("invalid input");
                }
                else{
                    result.add(num);
                }
            }
        }
        else{
            throw new InvalidConfigException("invalid input");
        }
        Set<Number> target = new HashSet<>(result);
        List<Number> last = new ArrayList<>(target);
        return last;
    }

    /**
     * parse the domain to set for the node.
     * @param lower the lower bounds of the range
     * @param upper the upper bounds of the range
     * @param node the input node
     * @return a list of number representing the domain
     */
    protected List<Number> parseDomainRange(int lower, int upper, APyNode<?> node) throws InvalidConfigException{
        List<Number> result = new ArrayList<>();

        // float
        if (node instanceof PyFloatNode){
            for (double i=lower; i<=upper; i++){
                result.add(i);
            }
        }

        // all other domain whose output should be integer
        else{
            for (int i=lower; i<= upper; i++){
                if (node instanceof PyBoolNode && (i!=0 && i!= 1)){
                        throw new InvalidConfigException("invalid domain");

                }
                result.add(i);
            }
        }
        return result;
    }

    /**
     * parse the input string to integer.
     * @param parse the input string
     * @return the integer
     * @throws InvalidConfigException
     */
    protected int parseInt(String parse) throws InvalidConfigException{
        try{
            return Integer.parseInt(parse.strip());
        }
        catch (Exception e){
            throw new InvalidConfigException("invalid int");
        }
    }

    /**
     * parse the input string to double.
     * @param parse the input string
     * @return the double
     * @throws InvalidConfigException
     */
    protected double parseDouble(String parse) throws InvalidConfigException{
        try{
            return Double.parseDouble(parse.strip());
        }
        catch (Exception e){
            throw new InvalidConfigException("invalid double");
        }
    }
}












