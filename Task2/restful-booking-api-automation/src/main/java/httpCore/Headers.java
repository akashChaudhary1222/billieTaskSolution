package main.java.httpCore;

public enum Headers {

    COOKIE("Cookie");

    private String name;

    Headers(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
