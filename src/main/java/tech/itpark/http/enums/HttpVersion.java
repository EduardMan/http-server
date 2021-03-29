package tech.itpark.http.enums;

public enum HttpVersion {
    HTTP11("HTTP/1.1");

    private String protocol;

    HttpVersion(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public static HttpVersion fineByProtocol(String protocol) {
        for (HttpVersion httpVersion :
                values()) {
            if (httpVersion.protocol.equals(protocol)) {
                return httpVersion;
            }
        }

        throw new IllegalArgumentException("No enum constant " + protocol);
    }
}