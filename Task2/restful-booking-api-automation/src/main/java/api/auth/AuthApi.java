package main.java.api.auth;

import main.java.httpCore.BaseRestService;
import main.java.httpCore.HttpMethodType;
import main.java.httpCore.MimeType;
import main.java.utils.PropertyUtil;

public class AuthApi extends BaseRestService {

    /**
     * Prepare RequestSpecification for the Subclass API
     *
     * @apiNote provides API endpoint, API Content-Type Header value
     * and HTTP Request Method Type to super class
     */
    public AuthApi() {
        super(PropertyUtil.getPropertyValue("auth.api.endpoint"), MimeType.APPLICATION_JSON, HttpMethodType.POST);
    }
}
