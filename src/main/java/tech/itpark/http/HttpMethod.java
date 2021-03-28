package tech.itpark.http;

public enum HttpMethod {
    GET(false),
    POST(true);

    private boolean isRequestBodyAllowed;

    public boolean isRequestBodyAllowed() {
        return isRequestBodyAllowed;
    }

    HttpMethod(boolean isRequestBodyAllowed) {
        this.isRequestBodyAllowed = isRequestBodyAllowed;
    }
}
