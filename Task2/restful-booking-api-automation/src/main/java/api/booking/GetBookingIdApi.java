package main.java.api.booking;

import main.java.httpCore.BaseRestService;
import main.java.utils.PropertyUtil;

import static main.java.httpCore.HttpMethodType.GET;
import static main.java.httpCore.MimeType.APPLICATION_JSON;

public class GetBookingIdApi extends BaseRestService {
    public GetBookingIdApi(String firstName, String lastName) {
        super(PropertyUtil.getPropertyValue("booking.api.endpoint"), APPLICATION_JSON, GET);
        super.addUrlParameter("firstname", firstName);
        super.addUrlParameter("lastname", lastName);
    }
}
