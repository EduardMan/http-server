package tech.itpark.http.enums;

public enum HttpHeadersSeparator {
    CRLF((byte)'\r', (byte)'\n');

    private byte[] chars;

    public byte[] getChars() {
        return chars;
    }

    HttpHeadersSeparator(byte ...chars) {
        this.chars = chars;
    }
}