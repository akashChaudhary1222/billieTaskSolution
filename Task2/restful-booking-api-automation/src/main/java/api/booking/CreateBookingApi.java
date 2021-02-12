package main.java.api.booking;

import main.java.httpCore.BaseRestService;
import main.java.utils.PropertyUtil;

import static main.java.httpCore.HttpMethodType.POST;
import static main.java.httpCore.MimeType.APPLICATION_JSON;

/**
 * API class for Booking Api.
 */
public class CreateBookingApi extends BaseRestService {
    /**
     * Prepare RequestSpecification for the Subclass API
     *
     * @apiNote provides API endpoint, API Content-Type Header value
     * and HTTP Request Method Type to super class
     */
    public CreateBookingApi() {
        super(PropertyUtil.getPropertyValue("booking.api.endpoint"), APPLICATION_JSON, POST);
    }
}
