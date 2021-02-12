package main.java.model;

import io.restassured.response.Response;
import main.java.dto.request.BookingRequest;

public class StepTestContext {

    public String authToken;
    public Response currentEventRestAssuredResponse;
    public Response previousEventRestAssuredResponse;
    public int bookingId;
    public BookingRequest bookingRequest;
}
