package main.rice.parse;

/**
 * An exception class for indicating that the config file being parsed was malformed.
 */
public class InvalidConfigException extends Exception {

    /**
     * Constructor for an InvalidConfigException; takes as its input a single String
     * containing more details about the error that occurred.
     *
     * @param msg the error message
     */
    public InvalidConfigException(String msg) {
        super(msg);
    }
}