/**
 * WrongArgumentException
 *
 * Exception for adding declarations with null idName, null Sym,
 * or both null idName and null Sym.
 *
 *  @author Henry Xu
 */

public class WrongArgumentException extends Exception {

    /** The exception message . */
    private String message;

    /** Constructor of WrongArgumentException.
     *  Construct it with given exception message.
     *  @param message exception message to be assigned. */
    public WrongArgumentException(String message) {
        this.message = message;
    }

    /** Get the exception message of this WrongArgumentException. */
    @Override
    public String getMessage() {
        return message;
    }
}
