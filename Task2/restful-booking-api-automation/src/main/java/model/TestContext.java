package main.java.model;

import io.restassured.response.Response;
import main.java.dto.request.BookingRequest;
import main.java.dto.response.BookingResponse;

public class TestContext {
    private BookingRequest bookingRequestClassObject;
    private Response createUpdateRestAssuredResponse;
    private Response getApiRestAssuredResponse;
    private Response deleteApiRestAssuredResponse;
    private Response commonRestAssuredResponse;
    private BookingResponse bookingResponse;
    private String bookingRequest;
    private boolean assertWithUpdate;


    public Response getCommonRestAssuredResponse() {
        return commonRestAssuredResponse;
    }

    public void setCommonRestAssuredResponse(Response commonRestAssuredResponse) {
        this.commonRestAssuredResponse = commonRestAssuredResponse;
    }


    public Response getDeleteApiRestAssuredResponse() {
        return deleteApiRestAssuredResponse;
    }

    public void setDeleteApiRestAssuredResponse(Response deleteApiRestAssuredResponse) {
        this.deleteApiRestAssuredResponse = deleteApiRestAssuredResponse;
    }


    public Response getGetApiRestAssuredResponse() {
        return getApiRestAssuredResponse;
    }

    public void setGetApiRestAssuredResponse(Response getApiRestAssuredResponse) {
        this.getApiRestAssuredResponse = getApiRestAssuredResponse;
    }


    public boolean isAssertWithUpdate() {
        return assertWithUpdate;
    }

    public void setAssertWithUpdate(boolean assertWithUpdate) {
        this.assertWithUpdate = assertWithUpdate;
    }


    public BookingRequest getBookingRequestClassObject() {
        return bookingRequestClassObject;
    }

    public void setBookingRequestClassObject(BookingRequest bookingRequestClassObject) {
        this.bookingRequestClassObject = bookingRequestClassObject;
    }


    public BookingResponse getBookingResponse() {
        return bookingResponse;
    }

    public void setBookingResponse(BookingResponse bookingResponse) {
        this.bookingResponse = bookingResponse;
    }


    public Response getCreateUpdateRestAssuredResponse() {
        return createUpdateRestAssuredResponse;
    }

    public void setCreateUpdateRestAssuredResponse(Response createUpdateRestAssuredResponse) {
        this.createUpdateRestAssuredResponse = createUpdateRestAssuredResponse;
    }

    public String getBookingRequest() {
        return bookingRequest;
    }

    public void setBookingRequest(String bookingRequest) {
        this.bookingRequest = bookingRequest;
    }

}
