package tech.itpark.http.exception;

public class HttpMethodIsNotSupportedException extends HttpServerException {

    public HttpMethodIsNotSupportedException(String message) {
        super(message);
    }

    public HttpMethodIsNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpMethodIsNotSupportedException(Throwable cause) {
        super(cause);
    }
}
