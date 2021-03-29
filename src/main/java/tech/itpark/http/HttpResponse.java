package tech.itpark.http;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final HttpVersion httpVersion = HttpVersion.HTTP11;
    private final Map<String, String> headers = new HashMap<>();

    private HttpStatus httpStatus;
    private byte[] body;

    public HttpResponse(HttpStatus httpStatus, byte[] body) {
        this.httpStatus = httpStatus;
        this.body = body;

        headers.put("Content-Length", body == null ? "0" : String.valueOf(body.length));
        headers.put("Connection", "close");
        headers.put("Content-Type", "text/plain");
    }

    public HttpResponse() {
        headers.put("Content-Length", "0");
        headers.put("Content-Type", "text/plain");
        headers.put("Connection", "close");
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setBody(byte[] body) {
        this.body = body;
        headers.put("Content-Length", body == null ? "0" : String.valueOf(body.length));
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] getAsBytes() {
        String requestLine = httpVersion.getProtocol() + " " + httpStatus.getValue() + " " + httpStatus.getReasonPhrase() + "\r\n";
        final String contentLength = headers.get("Content-Length");
        final String contentLengthAsString = "0".equals(contentLength) ? "" : "Content-Length: " + contentLength + "\r\n";

        String httpResponse =
                requestLine +
                        "Content-Type: " + headers.get("Content-Type") + "\r\n" +
                        contentLengthAsString +
                        "Connection: " + headers.get("Connection") + "\r\n" +
                        "\r\n";

        final int bodyLength = body == null ? 0 : body.length;
        byte[] result = new byte[httpResponse.getBytes().length + bodyLength];
        System.arraycopy(httpResponse.getBytes(), 0, result, 0, httpResponse.getBytes().length);

        if (body != null)
            System.arraycopy(body, 0, result, httpResponse.getBytes().length, bodyLength);

        return result;
    }
}
