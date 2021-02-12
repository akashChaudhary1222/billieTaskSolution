package main.java.constants;

public enum HttpStatusLines {

    HTTP_200("HTTP/1.1 200 OK"), HTTP_201("HTTP/1.1 201 Created"), HTTP_404("HTTP/1.1 404 Not Found");

    private String statusLine;

    HttpStatusLines(String statusLine) {
        this.statusLine = statusLine;
    }

    public String getStatusLine() {
        return statusLine;
    }
}
