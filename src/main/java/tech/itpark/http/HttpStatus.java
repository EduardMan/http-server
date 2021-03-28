package tech.itpark.http;

public enum HttpStatus {
    OK(200, "OK"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int value;

    private final String reasonPhrase;

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    HttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }
}
