package tech.itpark.http.exception;

public class HttpServerException extends RuntimeException {
    public HttpServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpServerException(String message) {
        super(message);
    }

    public HttpServerException(Throwable cause) {
        super(cause);
    }
}
