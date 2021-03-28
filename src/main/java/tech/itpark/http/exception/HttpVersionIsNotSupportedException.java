package tech.itpark.http.exception;

public class HttpVersionIsNotSupportedException extends HttpServerException {

    public HttpVersionIsNotSupportedException(String message) {
        super(message);
    }

    public HttpVersionIsNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpVersionIsNotSupportedException(Throwable cause) {
        super(cause);
    }
}
