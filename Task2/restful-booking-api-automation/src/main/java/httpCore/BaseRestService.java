package main.java.httpCore;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.java.exception.InvalidMethodTypeException;
import main.java.utils.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;


/**
 * Allows you to prepare the request for RestAssured. Implements HTTP connections for different method types and returns RestAssured Response.
 */
public abstract class BaseRestService {
    protected final RequestSpecification request;
    private final HttpMethodType methodType;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * Prepare RequestSpecification for the Subclass API
     *
     * @param uri        API endpoint
     * @param mimeType   API Content-Type Header value
     * @param methodType HTTP Request Method Type
     */
    public BaseRestService(String uri, MimeType mimeType, HttpMethodType methodType) {
        this.request = given();
        this.request.baseUri(PropertyUtil.getPropertyValue("base.url"));
        if (StringUtils.isNoneBlank(uri))
            this.request.basePath(uri);
        this.request.header("Content-Type", mimeType.getValue());
        this.methodType = methodType;
        this.request.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    /**
     * Allows you to specify URL Params to send along with URL.
     *
     * @param key   Param name
     * @param value Param value
     */
    public void addUrlParameter(String key, String value) {
        this.request.queryParam(key, value);
    }

    /**
     * Allows you to specify Path Params.
     *
     * @param key   Path param name
     * @param value Path param value
     */
    public void addPathParameter(String key, Object value) {
        this.request.pathParam(key, value);
    }

    /**
     * Allows you to specify Request Headers.
     *
     * @param key   Header name
     * @param value header value
     */
    public void addHeader(String key, String value) {
        this.request.header(key, value);
    }

    /**
     * Allows you to specify Cookie.
     *
     * @param cookie cookie value
     */
    public void setCookie(String cookie) {
        this.request.cookie(cookie);
    }

    /**
     * Allows you to set request body as String.
     *
     * @param requestBody to send as body
     */
    public void setRequestBody(String requestBody) {
        this.request.body(requestBody);
    }

    /**
     * This will send the request to the Request URI.
     */
    public Response sendRequest() {
        logger.info("Sending request: ");
        return send(this.methodType);
    }

    /**
     * Performs HTTP requests to the configured path for supported HTTP method types.
     *
     * @param methodType HTTP request method type
     * @throws InvalidMethodTypeException if Method type is not found in HttpMethodType
     */
    private Response send(HttpMethodType methodType) {
        switch (methodType) {
            case GET -> {
                return this.request.get();
            }
            case POST -> {
                return this.request.when().post();
            }
            case DELETE -> {
                return this.request.when().delete();
            }
            case PUT -> {
                return this.request.when().put();
            }
            default -> throw new InvalidMethodTypeException(methodType + " MethodType is not specified.");
        }
    }
}
