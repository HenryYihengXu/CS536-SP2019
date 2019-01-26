
public class WrongArgumentException extends Exception {

    private String message;

    public WrongArgumentException(String message) {
        this.message = message;
    }
}