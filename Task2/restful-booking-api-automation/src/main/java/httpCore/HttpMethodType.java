package main.java.httpCore;

public enum HttpMethodType {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String name;

    HttpMethodType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
