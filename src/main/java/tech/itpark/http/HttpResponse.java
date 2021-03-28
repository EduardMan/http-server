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

    public byte[] getAsBytes() {
        String requestLine = httpVersion.getProtocol() + " " + httpStatus.getValue() + " " + httpStatus.getReasonPhrase() + "\r\n";

        String httpResponse =
                requestLine +
                        "Content-Type: " + headers.get("Content-Type") + "\r\n" +
                        "Content-Length: " + headers.get("Content-Length") + "\r\n" +
                        "Connection: " + headers.get("Connection") + "\r\n" +
                        "\r\n";

        byte[] result = new byte[httpResponse.getBytes().length + body.length];
        System.arraycopy(httpResponse.getBytes(), 0, result, 0, httpResponse.getBytes().length);
        System.arraycopy(body, 0, result, httpResponse.getBytes().length, body.length);

        return result;
    }
}
