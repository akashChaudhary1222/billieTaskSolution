package main.java.model;

import io.restassured.response.Response;
import main.java.dto.request.BookingRequest;
import main.java.dto.response.BookingResponse;

public class TestContext {
    private BookingRequest bookingRequestClassObject;
    private Response createUpdateRestAssuredResponse;
    private Response getApiRestAssuredResponse;
    private Response deleteApiRestAssuredResponse;
    private Response currentEventRestAssuredResponse;
    private BookingResponse bookingResponse;
    private String bookingRequestJson;
    private boolean assertWithUpdate;


    public Response getCurrentEventRestAssuredResponse() {
        return currentEventRestAssuredResponse;
    }

    public void setCurrentEventRestAssuredResponse(Response currentEventRestAssuredResponse) {
        this.currentEventRestAssuredResponse = currentEventRestAssuredResponse;
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

    public String getBookingRequestJson() {
        return bookingRequestJson;
    }

    public void setBookingRequestJson(String bookingRequestJson) {
        this.bookingRequestJson = bookingRequestJson;
    }

}
