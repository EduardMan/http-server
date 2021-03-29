package tech.itpark.http;

import tech.itpark.http.enums.HttpHeadersSeparator;
import tech.itpark.http.enums.HttpMethod;
import tech.itpark.http.enums.HttpVersion;
import tech.itpark.http.exception.HttpMethodIsNotSupportedException;
import tech.itpark.http.exception.HttpVersionIsNotSupportedException;
import tech.itpark.http.guava.Bytes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private String requestUrl;
    private HttpVersion httpVersion;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public HttpRequest(BufferedInputStream in) throws IOException {
        final byte[] rawRequest = new byte[4096];
        in.read(rawRequest);

        final int requestLineEndIndex = readRequestLine(rawRequest);
        final int headersEndIndex = readHeaders(rawRequest, requestLineEndIndex);

        if (method.isRequestBodyAllowed()) {
            body = readRequestBody(rawRequest, headersEndIndex);
        }
    }

    private byte[] readRequestBody(byte[] rawRequest, int headersEndIndex) {

        final int requestBodyContentLength = Integer.parseInt(headers.get("Content-Length"));

        final byte[] requestBodyBytes = new byte[requestBodyContentLength];

        int j = 0;
        for (int i = headersEndIndex + HttpHeadersSeparator.CRLF.getChars().length; j < requestBodyContentLength; i++) {
            requestBodyBytes[j] = rawRequest[i];
            j++;
        }

        return requestBodyBytes;
    }

    private int readHeaders(byte[] rawRequest, int requestLineEndIndex) {
        int headerBeginningIndex = requestLineEndIndex + HttpHeadersSeparator.CRLF.getChars().length;
        int headerEndIndex = headerBeginningIndex;

        while (headerBeginningIndex != -1) {
            headerEndIndex = Bytes.indexOf(rawRequest, HttpHeadersSeparator.CRLF.getChars(), headerBeginningIndex, 0);
            final String headerField = new String(rawRequest, headerBeginningIndex, headerEndIndex - headerBeginningIndex).trim();

            if (headerField.isEmpty()) break;

            final String[] headerKeyValue = headerField.split(":");
            headers.put(headerKeyValue[0], headerKeyValue[1].trim());
            headerBeginningIndex = headerEndIndex + 1;
        }

        return headerEndIndex;
    }

    private int readRequestLine(byte[] rawRequest) {
        int requestLineEndIndex = Bytes.indexOf(rawRequest, HttpHeadersSeparator.CRLF.getChars(), 0, 0);

        final String[] requestLine = new String(rawRequest, 0, requestLineEndIndex)
                .trim()
                .split(" ");

        method = getHttpMethodTypeFromRequestLine(requestLine);
        requestUrl = requestLine[1];
        httpVersion = getHttpVersion(requestLine);

        return requestLineEndIndex;
    }

    private HttpMethod getHttpMethodTypeFromRequestLine(String[] requestLine) {
        try {
            return HttpMethod.valueOf(requestLine[0]);
        } catch (IllegalArgumentException e) {
            throw new HttpMethodIsNotSupportedException("Http protocol: " + "\"" + requestLine[0] + "\"" + " is not supported", e);
        }
    }

    private HttpVersion getHttpVersion(String[] requestLine) {
        try {
            return HttpVersion.fineByProtocol(requestLine[2]);
        } catch (IllegalArgumentException e) {
            throw new HttpVersionIsNotSupportedException("Http version: " + "\"" + requestLine[2] + "\"" + " is not supported", e);
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}