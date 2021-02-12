package main.java.api.booking;

import main.java.httpCore.BaseRestService;
import main.java.utils.PropertyUtil;

import static main.java.httpCore.HttpMethodType.PUT;
import static main.java.httpCore.MimeType.APPLICATION_JSON;

public class UpdateBookingApi extends BaseRestService {
    public UpdateBookingApi(int bookingId) {
        super(String.format("%s/%d", PropertyUtil.getPropertyValue("booking.api.endpoint"), bookingId), APPLICATION_JSON, PUT);
    }
}
