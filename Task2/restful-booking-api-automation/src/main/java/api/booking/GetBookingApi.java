package main.java.api.booking;

import main.java.httpCore.BaseRestService;
import main.java.utils.PropertyUtil;

import static main.java.httpCore.HttpMethodType.GET;
import static main.java.httpCore.MimeType.APPLICATION_JSON;

public class GetBookingApi extends BaseRestService {
    /**
     * Prepare RequestSpecification for the Subclass API
     *
     * @apiNote provides API endpoint, API Content-Type Header value
     * and HTTP Request Method Type to super class
     */
    public GetBookingApi(int bookingId) {
        super(String.format("%s/%d", PropertyUtil.getPropertyValue("booking.api.endpoint"), bookingId), APPLICATION_JSON, GET);
    }
}
