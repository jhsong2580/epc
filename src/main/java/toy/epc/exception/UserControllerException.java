package toy.epc.exception;

public class UserControllerException extends RuntimeException {

    public UserControllerException() {
        super();
    }

    public UserControllerException(String message) {
        super(message);
    }
}
