package main.java.httpCore;

public enum MimeType {
    URL_ENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json"),
    APPLICATION_HAL_JSON("application/hal+json"),
    MULTIPART_FORM("multipart/form-data"),
    NO_CONTENT("noContent"),
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_XML("application/xml");

    private String value;

    MimeType(String name) {
        this.value = name;
    }

    public String getValue() {
        return this.value;
    }

}
