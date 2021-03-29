package tech.itpark.http.exception;

public class MethodAlreadyRegistered extends HttpServerException {

    public MethodAlreadyRegistered(String message) {
        super(message);
    }

    public MethodAlreadyRegistered(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodAlreadyRegistered(Throwable cause) {
        super(cause);
    }
}
